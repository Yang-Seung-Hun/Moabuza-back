package com.project.moabuja.service;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.DayRecordResponseDto;
import com.project.moabuja.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImp implements RecordService{

    private final RecordRepository recordRepository;

    @Transactional
    @Override
    public Record save(RecordRequestDto recordRequestDto, Member currentMember) {
        Record record = new Record(recordRequestDto, currentMember);
        recordRepository.save(record);
        return record;
    }

    @Override//wallet, totalAmount 보류
    public DayListResponseDto getDayList(DayListRequestDto dayListRequestDto, Member currentUser) {
        List<Record> recordsByRecordDate = recordRepository.findRecordsByRecordDateAndMember(dayListRequestDto.getRecordDate(),currentUser);

        List<DayRecordResponseDto> dayRecordList = recordsByRecordDate.stream().map(record -> {
            return new DayRecordResponseDto(record.getRecordType(), record.getRecordDate(), record.getMemo(), record.getRecordAmount());
        }).collect(Collectors.toList());

        int dayIncomeAmount = 0;
        int dayExpenseAmount = 0;
        int dayChallengeAmount = 0;
        int dayGroupAmount = 0;
        for (DayRecordResponseDto dayRecordResponseDto : dayRecordList) {
            if (dayRecordResponseDto.getRecordType() == RecordType.income){
                dayIncomeAmount += dayRecordResponseDto.getRecordAmount();
            }
            if (dayRecordResponseDto.getRecordType() == RecordType.expense){
                dayExpenseAmount += dayRecordResponseDto.getRecordAmount();
            }
            if (dayRecordResponseDto.getRecordType() == RecordType.challenge){
                dayChallengeAmount += dayRecordResponseDto.getRecordAmount();
            }
            if (dayRecordResponseDto.getRecordType() == RecordType.group){
                dayGroupAmount += dayRecordResponseDto.getRecordAmount();
            }
        }
        return new DayListResponseDto(dayRecordList,dayIncomeAmount,dayExpenseAmount,dayChallengeAmount,dayGroupAmount,0,0);
    }


}
