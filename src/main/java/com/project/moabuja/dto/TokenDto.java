package com.project.moabuja.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDto {
    private String access;
    private String refresh;
    private String nickname;
}
