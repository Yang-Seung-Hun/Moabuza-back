package com.project.moabuja.dto.request.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class NicknameValidationRequestDto {
    @NotNull
    private String nickname;
}
