package com.project.moabuja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.KakaoUserInfoDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import com.project.moabuja.dto.request.member.NicknameValidationRequestDto;
import com.project.moabuja.dto.request.member.RegToLoginDto;
import com.project.moabuja.dto.response.member.HomeResponseDto;
import com.project.moabuja.model.LogoutResponse;
import com.project.moabuja.model.NicknameValidResponse;
import com.project.moabuja.model.UpdateInfoResponse;
import com.project.moabuja.util.CustomResponseEntity;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

    ResponseEntity<HomeResponseDto> getHomeInfo(Member current);

    ResponseEntity<CustomResponseEntity> kakaoLogin(String code) throws JsonProcessingException;

    String getAccessToken(String code) throws JsonProcessingException;

    KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException;

    RegToLoginDto register(KakaoUserInfoDto dto);

    ResponseEntity<NicknameValidResponse> nicknameValid(NicknameValidationRequestDto nicknameValidationRequestDto);

    ResponseEntity<UpdateInfoResponse> updateMemberInfo(MemberUpdateRequestDto dto, String email);

    ResponseEntity<CustomResponseEntity> reissue(HttpServletRequest request);

    ResponseEntity<LogoutResponse> logout(HttpServletRequest request);

}
