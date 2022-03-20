package com.project.moabuja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.KakaoUserInfoDto;
import com.project.moabuja.dto.TokenDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.NicknameValidationRequestDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.dto.response.member.ReissueDto;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.repository.RecordRepository;
import com.project.moabuja.security.filter.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService{
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;
    private final RecordRepository recordRepository;

    @Override
    public TokenDto kakaoLogin(String code) throws JsonProcessingException {
        String accessToken = getAccessToken(code);
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);
        TokenDto dto = TokenDto.builder()
                .nickname(register(kakaoUserInfoDto))
                .access(jwtTokenProvider.createAccessToken(kakaoUserInfoDto.getEmail()))
                .refresh(jwtTokenProvider.createRefreshToken(kakaoUserInfoDto.getEmail()))
                .build();

        redisTemplate.opsForValue()
                .set("RT:" + kakaoUserInfoDto.getEmail(), dto.getRefresh(), jwtTokenProvider.getExpiration(dto.getRefresh()), TimeUnit.MILLISECONDS);

        return dto;
    }

    @Override
    public String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "f367d5c13479608400bba9be2af87fc6");
        body.add("redirect_uri", "http://localhost:3000/user/kakao/callback");
//        body.add("redirect_uri", "https://moabuza.com/user/kakao/callback");
        body.add("code", code);
        body.add("client_secret", "X8m672khDWbTiYJlRBNwNGtH8K3k7HVE");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    @Override
    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long kakaoId = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KakaoUserInfoDto(kakaoId, email);
    }

    @Transactional
    @Override
    public String register(KakaoUserInfoDto dto) {
        Member member = new Member();

        // 기존회원이 아니면 회원가입 완료
        if(!memberRepository.existsByEmail(dto.getEmail())){
            String password = String.valueOf(UUID.randomUUID());
            memberRepository.save(member.fromDto(dto, password));
            return null;
        }
        Optional<Member> byEmail = Optional
                .ofNullable(memberRepository.findByEmails(dto.getEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음"));

        String nickname = byEmail.get().getNickname();

        // 기존 회원이면 그냥 로그인완료 메세지
        return nickname;
    }

    @Transactional
    @Override
    public ResponseEntity nicknameValid(NicknameValidationRequestDto nicknameValidationRequestDto) {
        String nickname = nicknameValidationRequestDto.getNickname();
        if(memberRepository.existsByNickname(nickname)){
            return ResponseEntity.badRequest().body("이미 사용 중인 닉네임입니다.");
        }
        return ResponseEntity.ok().body("닉네임 사용 가능");
    }

    @Transactional
    @Override
    public ResponseEntity updateMemberInfo(MemberUpdateRequestDto dto, String email) {
        Member byEmail = memberRepository.findByEmail(email);
        if(byEmail == null){
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
        Member updateInfo = byEmail.updateInfo(dto);
        return ResponseEntity.ok().body("캐릭터, 닉네임 설정 완료");
    }

    @Override
    public ReissueDto reissue(HttpServletRequest request) {
        String access = request.getHeader("A-AUTH-TOKEN").substring(7);
        String refresh = request.getHeader("R-AUTH-TOKEN").substring(7);

        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refresh)) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(access);
        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // 4. Refresh Token 일치하는지 검사
        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new RuntimeException("잘못된 요청");
        }
        if(!refreshToken.equals(refresh)) {
            throw new IllegalArgumentException("refresh 일치하지 않음");
        }
        // 5. 새로운 토큰 생성
        ReissueDto dto = ReissueDto.builder()
                .refresh(jwtTokenProvider.createRefreshToken(authentication.getName()))
                .access(jwtTokenProvider.createAccessToken(authentication.getName()))
                .build();
        // 6. 저장소 정보 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), dto.getRefresh(),
                        jwtTokenProvider.getExpiration(dto.getRefresh()), TimeUnit.MILLISECONDS);
        // 토큰 발급
        return dto;
    }

    @Override
    public ResponseEntity logout(HttpServletRequest request) {
        String access = request.getHeader("A-AUTH-TOKEN").substring(7);
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(access)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청");
        }
        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(access);

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(access);
        redisTemplate.opsForValue()
                .set(access, "logout", expiration, TimeUnit.MILLISECONDS);
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    @Transactional
    @Override
    public HomeResponseDto getHomeInfo(Member current) {
        Optional<Member> currentUserTmp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTmp.get();

        Hero hero = currentUser.getHero();

        int groupCurrentAmount = 0;
        int groupNeedAmount = 0;
        int groupAmount = 0;
        int groupPercent = 0;
        String groupName = null;

        int challengeCurrentAmount = 0;
        int challengeNeedAmount = 0;
        int challengeAmount = 0;
        int challengePercent = 0;
        String challengeName = null;

        int totalAmount = 0;
        int wallet = 0;

        GroupGoal groupGoal = currentUser.getGroupGoal();
        if (groupGoal != null && groupGoal.isAcceptedGroup()) {

            List<Member> members = groupGoal.getMembers();
            int currentGroupAmount = 0;
            for (Member member : members) {
                List<Record> memberGroupRecord = recordRepository.findRecordsByRecordTypeAndMember(RecordType.group, member);
                for (Record record : memberGroupRecord) {
                    groupCurrentAmount += record.getRecordAmount();
                }
            }
            groupNeedAmount = groupGoal.getGroupGoalAmount() - groupCurrentAmount;
            groupPercent = (int) (((double) groupCurrentAmount / (double) (groupGoal.getGroupGoalAmount())) * 100);
            groupName = groupGoal.getGroupGoalName();
        }

        ChallengeGoal challengeGoal = currentUser.getChallengeGoal();
        if (challengeGoal != null && challengeGoal.isAcceptedChallenge()) {

            List<Record> challengeRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, currentUser);
            for (Record challengeRecord : challengeRecords) {
                challengeCurrentAmount += challengeRecord.getRecordAmount();
            }
            challengeNeedAmount = challengeGoal.getChallengeGoalAmount() - challengeCurrentAmount;
            challengePercent = (int) (((double) challengeCurrentAmount / (double) (challengeGoal.getChallengeGoalAmount())) * 100);
            challengeName = challengeGoal.getChallengeGoalName();
        }

        //hero랑 heroLevel(레벨은 기준 정해야 함)
        //순자산(지갑+저금통), 지갑 계산
        int groupUserWallet = 0;//해당 user가 group에 넣은 돈
        List<Record> groupUserRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.group, currentUser);
        for (Record groupUserRecord : groupUserRecords) {
            groupUserWallet += groupUserRecord.getRecordAmount();
        }

        List<Record> recordsByMember = recordRepository.findRecordsByMember(currentUser);
        for (Record record : recordsByMember) {
            if (record.getRecordType() == RecordType.expense) {
                wallet -= record.getRecordAmount();
            }
            if (record.getRecordType() == RecordType.income) {
                wallet += record.getRecordAmount();
            }
            if (record.getRecordType() == RecordType.challenge && record.getRecordAmount() > 0) {
                wallet -= record.getRecordAmount();
            }
            if (record.getRecordType() == RecordType.group && record.getRecordAmount() > 0) {
                wallet -= record.getRecordAmount();
            }
        }
        totalAmount = wallet + challengeCurrentAmount + groupUserWallet;

        return new HomeResponseDto(groupCurrentAmount, groupNeedAmount, groupAmount, groupPercent, groupName,
                challengeCurrentAmount, challengeNeedAmount, challengeAmount, challengePercent, challengeName,
                hero, 0, totalAmount, wallet);//heroLevel 일단 0으로
    }
}
