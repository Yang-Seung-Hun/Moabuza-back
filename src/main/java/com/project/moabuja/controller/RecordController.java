package com.project.moabuja.controller;

import com.project.moabuja.domain.record.Record;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;


    @PostMapping("/money/addRecord/post")
    public RecordResponseDto addRecord(RecordRequestDto recordRequestDto){

        return null;
    }

}
