package com.project.moabuja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.moabuja.domain.alarm.Alarm;
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
import com.project.moabuja.dto.request.member.RegToLoginDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.dto.response.member.ReissueDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.repository.RecordRepository;
import com.project.moabuja.security.filter.JwtTokenProvider;
import com.project.moabuja.util.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
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

import static com.project.moabuja.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService{
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;
    private final RecordRepository recordRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public ResponseEntity<CustomResponseEntity> kakaoLogin(String code) throws JsonProcessingException {
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(getAccessToken(code));
        log.info("서비스 확인 ----------------------");
        RegToLoginDto regToLoginDto = register(kakaoUserInfoDto);
        String access = jwtTokenProvider.createAccessToken(regToLoginDto.getPassword());
        String refresh = jwtTokenProvider.createRefreshToken(regToLoginDto.getPassword());

        System.out.println("로그인 시 발급되는 토큰 리프래쉬 : " + refresh);
        System.out.println("로그인 시 발급되는 토큰 어세스: " + access);

        TokenDto dto = TokenDto.builder()
                .nickname(regToLoginDto.getNickname())
                .access(access)
                .refresh(refresh)
                .build();

        redisTemplate.opsForValue()
                .set("RT:" + kakaoUserInfoDto.getEmail(), dto.getRefresh(), jwtTokenProvider.getExpiration(dto.getRefresh()), TimeUnit.MILLISECONDS);

         CustomResponseEntity response = CustomResponseEntity.builder()
                .code(HttpStatus.OK)
                .message("카카오 로그인 콜백 메서드부분")
                .data(dto)
                .build();
         return response.responseAll();
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
    public RegToLoginDto register(KakaoUserInfoDto dto) {
        Member member = new Member();
        RegToLoginDto regToLoginDto = new RegToLoginDto();

        // 기존회원이 아니면 회원가입 완료
        if(!memberRepository.existsByEmail(dto.getEmail())){
            String password = UUID.randomUUID().toString().substring(0,8);
            memberRepository.save(member.fromDto(dto, password));
            regToLoginDto.setPassword(password);
            regToLoginDto.setNickname(null);
            return regToLoginDto;
        }

        Optional<Member> byEmail = Optional
                .ofNullable(memberRepository.findByEmails(dto.getEmail()))
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));


        regToLoginDto.setNickname(byEmail.get().getNickname());
        regToLoginDto.setPassword(byEmail.get().getPassword());
        return regToLoginDto;
    }

    @Transactional
    @Override
    public ResponseEntity<String> nicknameValid(NicknameValidationRequestDto nicknameValidationRequestDto) {
        String nickname = nicknameValidationRequestDto.getNickname();
        if(memberRepository.existsByNickname(nickname)){
            return ResponseEntity.ok().body("사용 중인 닉네임입니다.");
        }
        return ResponseEntity.ok().body("닉네임 사용 가능");
    }

    @Transactional
    @Override
    public ResponseEntity<String> updateMemberInfo(MemberUpdateRequestDto dto, String email) {
        Member byEmail = memberRepository.findByEmail(email);
        if(byEmail == null){
            throw new ErrorException(MEMBER_NOT_FOUND);
        }
        byEmail.updateInfo(dto);
        return ResponseEntity.ok().body("캐릭터, 닉네임 설정 완료");
    }

    @Override
    public ResponseEntity<CustomResponseEntity> reissue(HttpServletRequest request) {
        String access = request.getHeader("A-AUTH-TOKEN").substring(7);
        String refresh = request.getHeader("R-AUTH-TOKEN").substring(7);

        if (!jwtTokenProvider.validateToken(refresh)) {
            throw new ErrorException(REFRESH_NOT_VALID);
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(refresh);
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());

        System.out.println("아이디 : " + authentication.getName());

        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new ErrorException(REFRESH_NOT_EXIST);
        }
        if(!refreshToken.equals(refresh)) {
            throw new ErrorException(REFRESH_NOT_MATCH);
        }

        ReissueDto dto = ReissueDto.builder()
                .refresh(jwtTokenProvider.createRefreshToken(authentication.getName()))
                .access(jwtTokenProvider.createAccessToken(authentication.getName()))
                .build();

        System.out.println("다시 만든 리프래쉬 : " + dto.getRefresh());

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), dto.getRefresh(),
                        jwtTokenProvider.getExpiration(dto.getRefresh()), TimeUnit.MILLISECONDS);

        CustomResponseEntity response = CustomResponseEntity.builder()
                .data(dto)
                .message("Redis 저장 성공")
                .code(HttpStatus.OK)
                .build();

        return response.responseAll();
    }

    @Override
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String access = request.getHeader("A-AUTH-TOKEN").substring(7);
        if (!jwtTokenProvider.validateToken(access)) {
            throw new ErrorException(ACCESS_NOT_VALID);
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(access);

        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

        Long expiration = jwtTokenProvider.getExpiration(access);
        redisTemplate.opsForValue()
                .set(access, "logout", expiration, TimeUnit.MILLISECONDS);
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    @Transactional
    @Override
    public ResponseEntity<HomeResponseDto> getHomeInfo(Member current) {
        Optional<Member> currentUserTmp = Optional
                .of(memberRepository.findById(current.getId()))
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

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
        if (groupGoal != null) { //  && groupGoal.isAcceptedGroup()

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
        if (challengeGoal != null) { //  && challengeGoal.isAcceptedChallenge()

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

        wallet = RecordServiceImpl.getWallet(currentUser, wallet, recordRepository);
        totalAmount = wallet + challengeCurrentAmount + groupUserWallet;

        List<Alarm> alarmList = alarmRepository.findAllByMember(currentUser);
        int alarmCount = alarmList.size();

        HomeResponseDto homeResponseDto = HomeResponseDto.builder()
                .groupCurrentAmount(groupCurrentAmount)
                .groupNeedAmount(groupNeedAmount)
                .groupAmount(groupAmount)
                .groupPercent(groupPercent)
                .groupName(groupName)
                .challengeCurrentAmount(challengeCurrentAmount)
                .challengeNeedAmount(challengeAmount)
                .challengePercent(challengePercent)
                .challengeName(challengeName)
                .hero(hero)
                .totalAmount(totalAmount)
                .wallet(wallet)
                .alarmCount(alarmCount)
                .build();

        return ResponseEntity.ok().body(homeResponseDto);
    }
}