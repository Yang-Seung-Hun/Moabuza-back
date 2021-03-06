package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import org.springframework.http.ResponseEntity;

public interface RecordService {

    ResponseEntity<RecordResponseDto> save(RecordRequestDto recordRequestDto, Member currentMember);

    int walletCheck(Member currentMember);

    ResponseEntity<DayListResponseDto> getDayList(DayListRequestDto dayListRequestDto, Member currentMember);

    ResponseEntity<Msg> deleteRecord(Long id, Member currentMember);
}
