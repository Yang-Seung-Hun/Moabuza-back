package com.project.moabuja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.KakaoUserInfoDto;
import com.project.moabuja.dto.TokenDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.NicknameValidationRequestDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.dto.response.member.ReissueDto;
import com.project.moabuja.util.CustomResponseEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

    ResponseEntity<CustomResponseEntity> kakaoLogin(String code) throws JsonProcessingException;

    String getAccessToken(String code) throws JsonProcessingException;

    KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException;

    String register(KakaoUserInfoDto dto);

    ResponseEntity<String> nicknameValid(NicknameValidationRequestDto nicknameValidationRequestDto);

    ResponseEntity<String> updateMemberInfo(MemberUpdateRequestDto dto, String email);

    ResponseEntity<CustomResponseEntity> reissue(HttpServletRequest request);

    ResponseEntity<String> logout(HttpServletRequest request);

    ResponseEntity<HomeResponseDto> getHomeInfo(Member current);

}
