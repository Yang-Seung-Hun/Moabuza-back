package com.project.moabuja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.TokenDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.NicknameValidationRequestDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.dto.response.member.ReissueDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.FCMServiceImpl;
import com.project.moabuja.service.MemberService;
import com.project.moabuja.util.CustomResponseEntity;
import com.project.moabuja.util.Validation;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FCMServiceImpl fcmService;
    private final Validation validation;

    @ApiOperation(value = "카카오 로그인 api")
    @GetMapping("/user/kakao/callback")
    public ResponseEntity kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        return memberService.kakaoLogin(code);
    }

    @ApiOperation(value = "닉네임, 캐릭터 선택")
    @PutMapping("/member/info")
    public ResponseEntity update(@Valid @RequestBody MemberUpdateRequestDto dto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        fcmService.register(dto.getNickname(), dto.getFcmToken());
        Member currentMember =  validation.memberValidation(userDetails.getMember());
        String email = currentMember.getEmail();

        return memberService.updateMemberInfo(dto, email);
    }

    @ApiOperation(value = "닉네임 중복체크")
    @PostMapping("/nickname/validation")
    public ResponseEntity nicknameValid(@Valid @RequestBody NicknameValidationRequestDto nicknameValidationRequestDto){
        return memberService.nicknameValid(nicknameValidationRequestDto);
    }

    @ApiOperation(value = "access 토큰 재발급")
    @GetMapping("/api/reissue")
    public ResponseEntity reissue(HttpServletRequest request){
        return memberService.reissue(request);
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        return memberService.logout(request);
    }

    @ApiOperation(value = "로그인 후 home 페이지")
    @GetMapping("/home")
    public ResponseEntity getHome(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        if (userDetails == null) {
//            return memberService.guestHome();
//        }
        Member currentUser = userDetails.getMember();
        return memberService.getHomeInfo(currentUser);
    }

}
