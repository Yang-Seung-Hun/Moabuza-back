package com.project.moabuja.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class TokenDto {
    private String access;
    private String refresh;
    private String nickname;
}
