package com.project.moabuja.util;

import com.project.moabuja.dto.TokenDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;

@Getter
@NoArgsConstructor
public class CustomResponseEntity {
    private HttpStatus code;
    private String message;
    private Object data;

    @Builder
    public CustomResponseEntity( HttpStatus code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseEntity responseAccessRefresh(TokenDto dto){
        // 헤더에 access, refresh 토큰 담아서 주기
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.add("AccessToken", dto.getAccess());
        headers.add("RefreshToken", dto.getRefresh());

        CustomResponseEntity response = CustomResponseEntity.builder()
                .code(this.code)
                .message(this.message)
                .data(this.data)
                .build();
        return new ResponseEntity(response, headers, this.code);
    }

    public ResponseEntity responseAll(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        CustomResponseEntity response = CustomResponseEntity.builder()
                .code(this.code)
                .message(this.message)
                .data(this.data)
                .build();
        return new ResponseEntity(response, headers, this.code);
    }
}
