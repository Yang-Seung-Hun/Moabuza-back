package com.project.moabuja.controller;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.security.userdetails.UserDetailsImpl;
import com.project.moabuja.service.RecordService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @ApiOperation(value = "레코드 생성")
    @PostMapping("/money/addRecord/post")
    public ResponseEntity<RecordResponseDto> addRecord(@RequestBody RecordRequestDto recordRequestDto,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return recordService.save(recordRequestDto, currentUser);
    }

    @ApiOperation(value = "하루부자 내역 불러오기")
    @PostMapping("/money/dayList")
    public ResponseEntity<DayListResponseDto> getDay(@RequestBody DayListRequestDto dayListRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return recordService.getDayList(dayListRequestDto,currentUser);
    }

    @ApiOperation(value = "레코드 삭제")
    @DeleteMapping("/money/dayList/delete/{id}")
    public ResponseEntity<String> DeleteDay(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member currentUser = userDetails.getMember();
        return recordService.deleteRecord(id, currentUser);
    }

}
