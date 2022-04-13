package com.project.moabuja.dto.request.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class NicknameValidationRequestDto {
    @NotNull
    private String nickname;

    public NicknameValidationRequestDto(String nickname) {
        this.nickname = nickname;
    }
}
