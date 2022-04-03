package com.project.moabuja.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
public class KakaoUserInfoDto {
    private Long kakaoId;
    private String email;
}
