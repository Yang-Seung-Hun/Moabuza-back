package com.project.moabuja.dto.request.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class NicknameValidationRequestDto {
//    @Size(max = 8, message = "8자 이하 입력가능")
//    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣]*$")
    @NotNull
    private String nickname;
}
