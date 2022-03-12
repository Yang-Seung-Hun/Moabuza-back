package com.project.moabuja.dto.response.record;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecordResponseDto {
    private boolean isComplete;//group, challenge 완료됐는지 확인

    public RecordResponseDto(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public void changeIsComplete(){
        this.isComplete = true;
    }
}
