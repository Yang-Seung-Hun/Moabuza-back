package com.project.moabuja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.TokenDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.NicknameValidationRequestDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.dto.response.member.ReissueDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.MemberService;
import com.project.moabuja.util.CustomResponseEntity;
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

    // 카카오 로그인 api
    @GetMapping("/user/kakao/callback")
    public ResponseEntity kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        return memberService.kakaoLogin(code);
    }

    // 닉네임, 캐릭터 선택 api
    @PutMapping("/member/info")
    public ResponseEntity update(@Valid @RequestBody MemberUpdateRequestDto dto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        String email = userDetails.getUsername();
        return memberService.updateMemberInfo(dto, email);
    }

    // 닉네임 이름 중복체크  api
    @PostMapping("/nickname/validation")
    public ResponseEntity nicknameValid(@Valid @RequestBody NicknameValidationRequestDto nicknameValidationRequestDto){
        return memberService.nicknameValid(nicknameValidationRequestDto);
    }

    // access 토큰 만료 시 재발급 api : access, refresh 모두 재발급
    @GetMapping("/api/reissue")
    public ResponseEntity reissue(HttpServletRequest request){
        return memberService.reissue(request);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        return memberService.logout(request);
    }


    @GetMapping("/home")
    public HomeResponseDto getHome(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return memberService.getHomeInfo(currentUser);

    }
}
