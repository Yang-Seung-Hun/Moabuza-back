package com.project.moabuja.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
public class KakaoUserInfoDto {
    private Long kakaoId;
    @Nullable
    private String email;
}
