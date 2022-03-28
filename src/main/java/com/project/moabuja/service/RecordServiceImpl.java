package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.GoalType;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.DayRecordResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.exception.exceptionClass.AlarmErrorException;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.exception.exceptionClass.RecordErrorException;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.DoneGoalRepository;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;
    private final DoneGoalRepository doneGoalRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    @Override
    public ResponseEntity<RecordResponseDto> save(RecordRequestDto recordRequestDto, Member currentMemberTemp) {

        if (memberRepository.findById(currentMemberTemp.getId()).isEmpty()) { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
        Member currentMember = memberRepository.findById(currentMemberTemp.getId()).get();

        RecordResponseDto recordResponseDto = new RecordResponseDto(false);
        Record record = new Record(recordRequestDto, currentMember);

        int wallet = walletCheck(currentMember);

        if(recordRequestDto.getRecordType() == RecordType.income) {
            recordRepository.save(record);
        }

        else if(recordRequestDto.getRecordType() == RecordType.expense) {
            if (recordRequestDto.getRecordAmount() <= wallet) {
                recordRepository.save(record);
            } else { throw new RecordErrorException("지갑보다 큰 돈을 사용할 수 없습니다."); }
        }

        //type이 challenge일때 목표완료됐는지 확인
        else if(recordRequestDto.getRecordType() == RecordType.challenge){
            if (recordRequestDto.getRecordAmount() <= wallet) {
                recordRepository.save(record);
            } else { throw new RecordErrorException("지갑보다 큰 돈을 저금하는 것은 불가능 합니다."); }

            List<Member> members = currentMember.getChallengeGoal().getMembers();
            for (Member member : members) {
                Alarm alarm = new Alarm(AlarmType.CHALLENGE, AlarmDetailType.record, currentMember.getChallengeGoal().getChallengeGoalName(),
                        recordRequestDto.getRecordAmount(), null, currentMember.getNickname(), member);
                alarmRepository.save(alarm);
            }

            int goalAmount = currentMember.getChallengeGoal().getChallengeGoalAmount();
            int currentAmount = countCurrentChallenge(currentMember);

            if(currentAmount >= goalAmount){

                for (Member member : members) {
                    Alarm alarm = new Alarm(AlarmType.CHALLENGE, AlarmDetailType.success, currentMember.getChallengeGoal().getChallengeGoalName(),
                            recordRequestDto.getRecordAmount(), null, currentMember.getNickname(), member);
                    alarmRepository.save(alarm);
                }

                //완료 로직
                RecordRequestDto dto1 = new RecordRequestDto(RecordType.challenge, recordRequestDto.getRecordDate(), "도전해부자 완료!!", -1 * currentAmount);
                RecordRequestDto dto2 = new RecordRequestDto(RecordType.income, recordRequestDto.getRecordDate(), "도전해부자 완료!!", currentAmount);
                Record minusRecord = new Record(dto1, currentMember);
                Record plusRecord = new Record(dto2, currentMember);
                recordRepository.save(minusRecord);
                recordRepository.save(plusRecord);

                //완료된 목표 저장
                DoneGoal doneGoal = new DoneGoal(currentMember.getChallengeGoal().getChallengeGoalName(), currentMember.getChallengeGoal().getChallengeGoalAmount(), currentMember, GoalType.CHALLENGE);
                doneGoalRepository.save(doneGoal);
                currentMember.addDoneGoal(doneGoal);

                //완료된 목표 삭제
                currentMember.getChallengeGoal().removeMember(currentMember);

                recordResponseDto.changeIsComplete();
            }
        }

        //group goal 완료 로직
        else if(recordRequestDto.getRecordType() == RecordType.group){
            if (recordRequestDto.getRecordAmount() <= wallet) {
                recordRepository.save(record);
            } else { throw new RecordErrorException("지갑보다 큰 돈을 저금하는 것은 불가능 합니다."); }

            List<Member> members = currentMember.getGroupGoal().getMembers();
            for (Member member : members) {
                Alarm alarm = new Alarm(AlarmType.GROUP, AlarmDetailType.record, currentMember.getGroupGoal().getGroupGoalName(),
                        recordRequestDto.getRecordAmount(), null, currentMember.getNickname(), member);
                alarmRepository.save(alarm);
            }

            int goalAmount = currentMember.getGroupGoal().getGroupGoalAmount();
            GroupGoal groupGoal = currentMember.getGroupGoal();
            HashMap<Member, Integer> separateAmount = countSeparateCurrentGroup(groupGoal);
            int currentAmount = 0;
            for (Member member : separateAmount.keySet()) {
                currentAmount += separateAmount.get(member);
            }

            if(currentAmount >= goalAmount){
                //완료 로직 => 각 사용자 저금통에서 각자 낸 만큼 빼주기(도전해부자랑 다름, 도전해부자는 다시 넣어주는것 까지 있음)

                for (Member member : members) {
                    Alarm alarm = new Alarm(AlarmType.GROUP, AlarmDetailType.success, currentMember.getGroupGoal().getGroupGoalName(),
                            recordRequestDto.getRecordAmount(), null, currentMember.getNickname(), member);
                    alarmRepository.save(alarm);
                }

                for (Member member : separateAmount.keySet()) {
                    RecordRequestDto dto = new RecordRequestDto(RecordType.group, recordRequestDto.getRecordDate(), "같이해부자 완료!!", -1*separateAmount.get(member));
                    Record minusRecord = new Record(dto, member);
                    recordRepository.save(minusRecord);

                    DoneGoal doneGoal = new DoneGoal(member.getGroupGoal().getGroupGoalName(),member.getGroupGoal().getGroupGoalAmount(),member, GoalType.GROUP);
                    doneGoalRepository.save(doneGoal);
                    member.addDoneGoal(doneGoal);
                    member.getGroupGoal().removeMember(member);
                }
                recordResponseDto.changeIsComplete();
            }
        }
        return ResponseEntity.ok().body(recordResponseDto);
    }

    @Override//wallet, totalAmount 보류
    public ResponseEntity<DayListResponseDto> getDayList(DayListRequestDto dayListRequestDto, Member currentUser) {

        int dayIncomeAmount = 0;
        int dayExpenseAmount = 0;
        int dayChallengeAmount = 0;
        int dayGroupAmount = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        List<Record> recordsByRecordDate = recordRepository.findRecordsByRecordDateAndMember(LocalDateTime.parse(dayListRequestDto.getRecordDate(),formatter),currentUser);

        List<DayRecordResponseDto> dayRecordList = recordsByRecordDate.stream().map(record -> {
            return new DayRecordResponseDto(record.getId(),record.getRecordType(), record.getRecordDate(), record.getMemo(), record.getRecordAmount());
        }).collect(Collectors.toList());

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
        DayListResponseDto dayListResponseDto = new DayListResponseDto(dayRecordList,dayIncomeAmount,dayExpenseAmount,dayChallengeAmount,dayGroupAmount);
        return ResponseEntity.ok().body(dayListResponseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteRecord(Long id, Member currentMember) {
        Optional<Record> selectRecord = recordRepository.findRecordById(id);
        Long selectId = selectRecord.get().getMember().getId();
        if (selectId.equals(currentMember.getId())) {
            recordRepository.deleteRecordById(id);
        } else {
            throw new IllegalArgumentException("게시물을 등록한 사용자가 아닙니다.");
        }
        return ResponseEntity.ok().body("내역 삭제 완료");
    }

    public int countCurrentChallenge(Member member){
        int currentAmount = 0;
        List<Record> challengeRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, member);
        for (Record challengeRecord : challengeRecords) {
            currentAmount += challengeRecord.getRecordAmount();
        }
        return currentAmount;
    }

    public HashMap<Member,Integer> countSeparateCurrentGroup(GroupGoal groupGoal){

        HashMap<Member,Integer> separateAmounts = new HashMap<>();

        List<Member> members = groupGoal.getMembers();
        for (Member member : members) {
            int tmpAmount = 0;
            List<Record> groupRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.group, member);
            for (Record groupRecord : groupRecords) {
                tmpAmount += groupRecord.getRecordAmount();
            }
            separateAmounts.put(member,tmpAmount);
        }
        return separateAmounts;
    }

    public int walletCheck(Member currentMember) {
        int wallet = 0;
        wallet = getWallet(currentMember, wallet, recordRepository);
        return wallet;
    }

    //memberServicImpl에서 중복 되는 부분이라 추출
    static int getWallet(Member currentMember, int wallet, RecordRepository recordRepository) {
        List<Record> recordsByMember = recordRepository.findRecordsByMember(currentMember);
        for (Record recordData : recordsByMember) {
            if (recordData.getRecordType() == RecordType.expense) {
                wallet -= recordData.getRecordAmount();
            }
            if (recordData.getRecordType() == RecordType.income) {
                wallet += recordData.getRecordAmount();
            }
            if (recordData.getRecordType() == RecordType.challenge && recordData.getRecordAmount() > 0) {
                wallet -= recordData.getRecordAmount();
            }
            if (recordData.getRecordType() == RecordType.group && recordData.getRecordAmount() > 0) {
                wallet -= recordData.getRecordAmount();
            }
        }
        return wallet;
    }
}
