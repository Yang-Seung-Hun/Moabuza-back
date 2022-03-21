package com.project.moabuja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.KakaoUserInfoDto;
import com.project.moabuja.dto.TokenDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.NicknameValidationRequestDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.dto.response.member.ReissueDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

    public ResponseEntity kakaoLogin(String code) throws JsonProcessingException;

    public String getAccessToken(String code) throws JsonProcessingException;

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException;

    public String register(KakaoUserInfoDto dto);

    public ResponseEntity nicknameValid(NicknameValidationRequestDto nicknameValidationRequestDto);

    public ResponseEntity updateMemberInfo(MemberUpdateRequestDto dto, String email);

    public ResponseEntity reissue(HttpServletRequest request);

    public ResponseEntity logout(HttpServletRequest request);

    public HomeResponseDto getHomeInfo(Member current);

}
