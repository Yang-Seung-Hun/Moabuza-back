package com.project.moabuja.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    private Long kakaoId;
    @Email
    private String email;
}
