package com.project.moabuja.dto.request.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateGroupRequestDto {
    private String createGroupName;
    private String createGroupAmount;
    private List<String> groupFiends = new ArrayList<>();
}
