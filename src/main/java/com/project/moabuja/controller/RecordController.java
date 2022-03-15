package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;


    @PostMapping("/money/addRecord/post")
    public RecordResponseDto addRecord(@RequestBody  RecordRequestDto recordRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        return recordService.save(recordRequestDto, currentUser);
    }

    @PostMapping("/money/dayList")
    public DayListResponseDto getDay(@RequestBody DayListRequestDto dayListRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Member currentUser = userDetails.getMember();
        return recordService.getDayList(dayListRequestDto,currentUser);
    }

    @DeleteMapping("/money/dayList/delete/{id}")
    public String DeleteDay(@PathVariable Long id){

        recordService.deleteRecord(id);

        return "삭제요 완료!!!";
    }

}
