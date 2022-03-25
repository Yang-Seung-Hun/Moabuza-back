package com.project.moabuja.dto.request.member;

import com.project.moabuja.domain.member.Hero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Setter
public class MemberUpdateRequestDto {

    // fcm 토큰 받아오기
    private String fcmToken;

//    @Size(max = 8, message = "8자 이하 입력가능")
//    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣]*$")
    @NotNull
    private String nickname;

    @NotNull
    private Hero hero;
    
    
}
