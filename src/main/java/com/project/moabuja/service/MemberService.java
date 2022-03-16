package com.project.moabuja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.KakaoUserInfoDto;
import com.project.moabuja.dto.TokenDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.RegisterRequestDto;
import com.project.moabuja.repository.MemberRepository;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;

    @Transactional
    public TokenDto kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);
        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);

        RegisterRequestDto dto = RegisterRequestDto.builder()
                .kakaoId(kakaoUserInfoDto.getKakaoId())
                .email(kakaoUserInfoDto.getEmail())
                .build();

        // 회원가입, 로그인 처리
        register(dto);

        // access, refresh 둘다 만들어줌
        String access = jwtTokenProvider.createAccessToken(kakaoUserInfoDto.getEmail());
        String refresh = jwtTokenProvider.createRefreshToken(kakaoUserInfoDto.getEmail());

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + kakaoUserInfoDto.getEmail(), refresh, jwtTokenProvider.getExpiration(refresh), TimeUnit.MILLISECONDS);

        return TokenDto.builder()
                .access(access)
                .refresh(refresh)
                .build();
    }

    public String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "f367d5c13479608400bba9be2af87fc6");
        body.add("redirect_uri", "https://moabuza.com/callback");
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

    public ResponseEntity register(RegisterRequestDto dto){
        Member member = new Member();
        
        // 기존회원이 아니면 회원가입 완료
        if(!memberRepository.existsByEmail(dto.getEmail())){
            String password = String.valueOf(UUID.randomUUID());
            memberRepository.save(member.fromDto(dto, password));
            return new ResponseEntity("회원가입 완료", null, HttpStatus.OK);
            // 회원가입한 회원은 온보딩 화면을 보여주도록 한다.
        }
        // 기존 회원이면 그냥 로그인완료 메세지
        return new ResponseEntity("로그인 완료", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity nicknameValid(String nickname){
        if(memberRepository.existsByNickname(nickname)){
            return ResponseEntity.badRequest().body("중복된 닉네임 사용");
        }
        return ResponseEntity.ok().body("닉네임 사용 가능");
    }

    // todo : 회원이 회원이 캐릭터랑 닉네임 설정한 경우
    @Transactional
    public ResponseEntity updateMemberInfo(MemberUpdateRequestDto dto, String email){
        Member byEmail = memberRepository.findByEmail(email);
        if(byEmail == null){
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
        Member updateInfo = byEmail.updateInfo(dto);
        return ResponseEntity.ok().body("캐릭터, 닉네임 설정 완료");
    }

    @Transactional
    public TokenDto reissue(HttpServletRequest request) {
        String access = request.getHeader("A-AUTH-TOKEN").substring(7);
        String refresh = request.getHeader("R-AUTH-TOKEN").substring(7);

        System.out.println("어세스 토큰 : " + access);
        System.out.println("리프래쉬 토큰 : " + refresh);

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
        TokenDto tokenDto = TokenDto.builder()
                .refresh(jwtTokenProvider.createRefreshToken(authentication.getName()))
                .access(jwtTokenProvider.createAccessToken(authentication.getName()))
                .build();
        // 6. 저장소 정보 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefresh(),
                        jwtTokenProvider.getExpiration(tokenDto.getRefresh()), TimeUnit.MILLISECONDS);
        // 토큰 발급
        return tokenDto;
    }

    // 로그아웃 처리
    public ResponseEntity logout(HttpServletRequest request) {
        String access = request.getHeader("A-AUTH-TOKEN").substring(7);
        System.out.println("------------------access" + access);
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
}
