package com.project.moabuja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.NicknameValidationRequestDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.FCMServiceImpl;
import com.project.moabuja.service.MemberService;
import com.project.moabuja.util.CustomResponseEntity;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.project.moabuja.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final FCMServiceImpl fcmService;

    @ApiOperation(value = "로그인 후 home 페이지")
    @GetMapping("/home")
    public ResponseEntity<HomeResponseDto> getHome(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            throw new ErrorException(GEUST_TO_LOGIN);
        }
        return memberService.getHomeInfo(userDetails.getMember());
    }

    @ApiOperation(value = "카카오 로그인 api")
    @GetMapping("/user/kakao/callback")
    public ResponseEntity<CustomResponseEntity> kakaoLogin(@RequestParam String code) throws JsonProcessingException {

        log.info("------------ 컨트롤러 확인");
        return memberService.kakaoLogin(code);
    }

    @ApiOperation(value = "닉네임, 캐릭터 선택")
    @PutMapping("/member/info")
    public ResponseEntity<String> update(@Valid @RequestBody MemberUpdateRequestDto dto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        fcmService.register(dto.getNickname(), dto.getFcmToken());
        return memberService.updateMemberInfo(dto, userDetails.getMember().getEmail());
    }

    @ApiOperation(value = "닉네임 중복체크")
    @PostMapping("/member/validation")
    public ResponseEntity<String> nicknameValid(@Valid @RequestBody NicknameValidationRequestDto nicknameValidationRequestDto){
        return memberService.nicknameValid(nicknameValidationRequestDto);
    }

    @ApiOperation(value = "access 토큰 재발급")
    @GetMapping("/member/reissue")
    public ResponseEntity<CustomResponseEntity> reissue(HttpServletRequest request){
        return memberService.reissue(request);
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/member/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        return memberService.logout(request);
    }
}
