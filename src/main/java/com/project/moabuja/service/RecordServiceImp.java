package com.project.moabuja.service;

import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.DoneGoalType;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.DayRecordResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.repository.DoneGoalRepository;
import com.project.moabuja.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImp implements RecordService{

    private final RecordRepository recordRepository;
    private final DoneGoalRepository doneGoalRepository;

    @Transactional
    @Override
    public RecordResponseDto save(RecordRequestDto recordRequestDto, Member currentMember) {

        RecordResponseDto recordResponseDto = new RecordResponseDto(false);

        Record record = new Record(recordRequestDto, currentMember);
        recordRepository.save(record);

        //type이 challenge일때 목표완료됐는지 확인
        if(recordRequestDto.getRecordType() == RecordType.challenge){
            int goalAmount = currentMember.getChallengeGoal().getChallengeGoalAmount();
            int currentAmount = countCurrentChallenge(currentMember);

            if(currentAmount >= goalAmount){
                //완료 로직
                RecordRequestDto dto1 = new RecordRequestDto(RecordType.challenge, recordRequestDto.getRecordDate(), "도전해부자 완료!!", -1 * currentAmount);
                RecordRequestDto dto2 = new RecordRequestDto(RecordType.income, recordRequestDto.getRecordDate(), "도전해부자 완료!!", currentAmount);
                Record minusRecord = new Record(dto1, currentMember);
                Record plusRecord = new Record(dto2, currentMember);
                recordRepository.save(minusRecord);
                recordRepository.save(plusRecord);

                //완료된 목표 저장
                DoneGoal doneGoal = new DoneGoal(currentMember.getChallengeGoal().getChallengeGoalName(), currentMember.getChallengeGoal().getChallengeGoalAmount(), currentMember, DoneGoalType.CHALLENGE);
                doneGoalRepository.save(doneGoal);
                currentMember.addDoneGoal(doneGoal);

                //완료된 목표 삭제
                currentMember.getChallengeGoal().removeMember(currentMember);

                recordResponseDto.changeIsComplete();
            }
        }
        return recordResponseDto;
    }

    @Override//wallet, totalAmount 보류
    public DayListResponseDto getDayList(DayListRequestDto dayListRequestDto, Member currentUser) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        List<Record> recordsByRecordDate = recordRepository.findRecordsByRecordDateAndMember(LocalDateTime.parse(dayListRequestDto.getRecordDate(),formatter),currentUser);

        List<DayRecordResponseDto> dayRecordList = recordsByRecordDate.stream().map(record -> {
            return new DayRecordResponseDto(record.getId(),record.getRecordType(), record.getRecordDate(), record.getMemo(), record.getRecordAmount());
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

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        recordRepository.deleteRecordById(id);
    }

    public int countCurrentChallenge(Member member){
        int currentAmount = 0;
        List<Record> challengeRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, member);
        for (Record challengeRecord : challengeRecords) {
            currentAmount += challengeRecord.getRecordAmount();
        }
        return currentAmount;
    }
}
