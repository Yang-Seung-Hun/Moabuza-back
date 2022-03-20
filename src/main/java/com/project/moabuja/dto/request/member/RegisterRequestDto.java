package com.project.moabuja.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    private Long kakaoId;
    private String email;
}
