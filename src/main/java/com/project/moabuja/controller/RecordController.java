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

import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    @GetMapping("/money/dayList")
    public DayListResponseDto getToday(@AuthenticationPrincipal UserDetailsImpl userDetails){

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = now.format(formatter);
        date = date + " 00:00:00.000";

        Member currentUser = userDetails.getMember();
        DayListRequestDto dayListRequestDto = new DayListRequestDto(date);
        return recordService.getDayList(dayListRequestDto,currentUser);
    }

    @DeleteMapping("/money/dayList/delete/{id}")
    public String DeleteDay(@PathVariable Long id){

        recordService.deleteRecord(id);

        return "삭제요 완료!!!";
    }

}
