package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.GoalType;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.GoalAlarmSaveDto;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.DayRecordResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.DoneGoalRepository;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.CHALLENGE;
import static com.project.moabuja.domain.alarm.AlarmType.GROUP;
import static com.project.moabuja.dto.ResponseMsg.RecordDelete;
import static com.project.moabuja.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;
    private final DoneGoalRepository doneGoalRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    @CacheEvict(key = "#currentMemberTemp.id", value = "homeData")
    @Override
    public ResponseEntity<RecordResponseDto> save(RecordRequestDto recordRequestDto, Member currentMemberTemp) {

        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        RecordResponseDto recordResponseDto = new RecordResponseDto(false);
        Record record = new Record(recordRequestDto, currentMember);

        int wallet = walletCheck(currentMember);

        if(recordRequestDto.getRecordType() == RecordType.income) {
            recordRepository.save(record);
        }

        else if(recordRequestDto.getRecordType() == RecordType.expense) {
            if (recordRequestDto.getRecordAmount() <= wallet) {
                recordRepository.save(record);
            } else { throw new ErrorException(MONEY_LESS_THAN_WALLET); }
        }

        //type이 challenge일때
        else if(recordRequestDto.getRecordType() == RecordType.challenge){

            //생성된 challenge있는지 확인
            Optional.of(Optional.ofNullable(currentMember.getChallengeGoal())).get().orElseThrow(() -> new ErrorException(CHALLENGE_GOAL_NOT_EXIST));

            if (recordRequestDto.getRecordAmount() <= wallet) {
                recordRepository.save(record);
            } else { throw new ErrorException(SAVINGS_LESS_THAN_WALLET); }

            List<Member> members = currentMember.getChallengeGoal().getMembers();

            sendRecordAlarm(members, CHALLENGE, currentMember.getChallengeGoal().getChallengeGoalName(), recordRequestDto.getRecordAmount(), currentMember.getNickname());

//            for (Member member : members) {
//                Alarm alarm = new Alarm(CHALLENGE, AlarmDetailType.record, currentMember.getChallengeGoal().getChallengeGoalName(),
//                        recordRequestDto.getRecordAmount(), null, currentMember.getNickname(), member);
//                alarmRepository.save(alarm);
//            }

            int goalAmount = currentMember.getChallengeGoal().getChallengeGoalAmount();
            int currentAmount = countCurrentChallenge(currentMember);

            //목표완료됐는지 확인
            if(currentAmount >= goalAmount){
                makeChallengeDoneGoal(recordRequestDto, currentMember, recordResponseDto, members, currentAmount);
            }
        }

        //group goal
        else if(recordRequestDto.getRecordType() == RecordType.group){

            //생성된 같이해부자 있는지 확인
            Optional.of(Optional.ofNullable(currentMember.getGroupGoal())).get().orElseThrow(() -> new ErrorException(GROUP_GOAL_NOT_EXIST));

            if (recordRequestDto.getRecordAmount() <= wallet) {
                recordRepository.save(record);
            } else { throw new ErrorException(SAVINGS_LESS_THAN_WALLET); }

            List<Member> members = currentMember.getGroupGoal().getMembers();

            sendRecordAlarm(members, GROUP, currentMember.getGroupGoal().getGroupGoalName(), recordRequestDto.getRecordAmount(), currentMember.getNickname());

//            for (Member member : members) {
//                Alarm alarm = new Alarm(GROUP, AlarmDetailType.record, currentMember.getGroupGoal().getGroupGoalName(),
//                        recordRequestDto.getRecordAmount(), null, currentMember.getNickname(), member);
//                alarmRepository.save(alarm);
//            }

            int goalAmount = currentMember.getGroupGoal().getGroupGoalAmount();
            GroupGoal groupGoal = currentMember.getGroupGoal();
            HashMap<Member, Integer> separateAmount = countSeparateCurrentGroup(groupGoal);
            int currentAmount = 0;
            for (Member member : separateAmount.keySet()) {
                currentAmount += separateAmount.get(member);
            }

            //완료로직
            if(currentAmount >= goalAmount){
                //완료 로직 => 각 사용자 저금통에서 각자 낸 만큼 빼주기(도전해부자랑 다름, 도전해부자는 다시 넣어주는것 까지 있음)
                makeGroupDoneGoal(recordRequestDto, currentMember, recordResponseDto, members, separateAmount);
            }
        }
        return new ResponseEntity<>(recordResponseDto, HttpStatus.OK);
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
            return DayRecordResponseDto.builder()
                    .id(record.getId())
                    .recordType(record.getRecordType())
                    .recordDate(record.getRecordDate())
                    .memos(record.getMemo())
                    .recordAmount(record.getRecordAmount())
                    .build();
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
        DayListResponseDto dayListResponseDto = DayListResponseDto.builder()
                .dayRecordList(dayRecordList)
                .dayIncomeAmount(dayIncomeAmount)
                .dayExpenseAmount(dayExpenseAmount)
                .dayChallengeAmount(dayChallengeAmount)
                .dayGroupAmount(dayGroupAmount)
                .build();
        return new ResponseEntity<>(dayListResponseDto, HttpStatus.OK);
    }

    @Override
    @CacheEvict(key = "#currentMember.id", value = "homeData")
    @Transactional
    public ResponseEntity<Msg> deleteRecord(Long id, Member currentMember) {

        Record selectRecord = Optional.of(recordRepository.findRecordById(id)).get().orElseThrow(() -> new ErrorException(RECORD_NOT_EXIST));
        Long selectId = selectRecord.getMember().getId();

        if (Objects.equals(currentMember.getId(), selectId)) {
            recordRepository.deleteRecordById(id);
            return new ResponseEntity<>(new Msg(RecordDelete.getMsg()), HttpStatus.OK);
        } throw new ErrorException(RECORD_MEMBER_NOT_MATCH);
    }

    public void sendRecordAlarm(List<Member> members,AlarmType alarmType, String goalName, int goalAmount,String friendNickname){
        for (Member member : members) {
            GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                    .alarmType(alarmType)
                    .alarmDetailType(AlarmDetailType.record)
                    .goalName(goalName)
                    .goalAmount(goalAmount)
                    .waitingGoalId(null)
                    .friendNickname(friendNickname)
                    .member(member)
                    .build();
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
        }
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

    private void makeChallengeDoneGoal(RecordRequestDto recordRequestDto, Member currentMember, RecordResponseDto recordResponseDto, List<Member> members, int currentAmount) {
        for (Member member : members) {
            GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                    .alarmType(CHALLENGE)
                    .alarmDetailType(AlarmDetailType.success)
                    .goalName(currentMember.getChallengeGoal().getChallengeGoalName())
                    .goalAmount(recordRequestDto.getRecordAmount())
                    .waitingGoalId(null)
                    .friendNickname(currentMember.getNickname())
                    .member(member)
                    .build();
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
        }

        //완료 로직
        RecordRequestDto dto1 = RecordRequestDto.builder()
                .recordType(RecordType.challenge)
                .recordDate(recordRequestDto.getRecordDate())
                .memos("도전해부자 완료!!")
                .recordAmount(-1 * currentAmount)
                .build();
        RecordRequestDto dto2 = RecordRequestDto.builder()
                .recordType(RecordType.income)
                .recordDate(recordRequestDto.getRecordDate())
                .memos("도전해부자 완료!!")
                .recordAmount(currentAmount)
                .build();
        Record minusRecord = new Record(dto1, currentMember);
        Record plusRecord = new Record(dto2, currentMember);
        recordRepository.save(minusRecord);
        recordRepository.save(plusRecord);

        //완료된 목표 저장
        DoneGoal doneGoal = DoneGoal.builder()
                .doneGoalName(currentMember.getChallengeGoal().getChallengeGoalName())
                .doneGoalAmount(currentMember.getChallengeGoal().getChallengeGoalAmount())
                .member(currentMember)
                .goalType(GoalType.CHALLENGE)
                .build();
        doneGoalRepository.save(doneGoal);
        currentMember.addDoneGoal(doneGoal);

        //완료된 목표 삭제
        currentMember.getChallengeGoal().removeMember(currentMember);

        recordResponseDto.changeIsComplete();
    }

    private void makeGroupDoneGoal(RecordRequestDto recordRequestDto, Member currentMember, RecordResponseDto recordResponseDto, List<Member> members, HashMap<Member, Integer> separateAmount) {
        for (Member member : members) {
            GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                    .alarmType(GROUP)
                    .alarmDetailType(AlarmDetailType.success)
                    .goalName(currentMember.getGroupGoal().getGroupGoalName())
                    .goalAmount(recordRequestDto.getRecordAmount())
                    .waitingGoalId(null)
                    .friendNickname(currentMember.getNickname())
                    .member(member)
                    .build();
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
        }

        for (Member member : separateAmount.keySet()) {
            RecordRequestDto dto = RecordRequestDto.builder()
                    .recordType(RecordType.group)
                    .recordDate(recordRequestDto.getRecordDate())
                    .memos("같이해부자 완료!!")
                    .recordAmount(-1* separateAmount.get(member))
                    .build();

            Record minusRecord = new Record(dto, member);
            recordRepository.save(minusRecord);

            DoneGoal doneGoal = DoneGoal.builder()
                    .doneGoalName(member.getGroupGoal().getGroupGoalName())
                    .doneGoalAmount(member.getGroupGoal().getGroupGoalAmount())
                    .member(member)
                    .goalType(GoalType.GROUP)
                    .build();

            doneGoalRepository.save(doneGoal);
            member.addDoneGoal(doneGoal);
            member.getGroupGoal().removeMember(member);
        }
        recordResponseDto.changeIsComplete();
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
            if (recordData.getRecordType() == RecordType.challenge && recordData.getRecordAmount() > 0 ) {
                wallet -= recordData.getRecordAmount();
            }
            if (recordData.getRecordType() == RecordType.group && recordData.getRecordAmount() > 0 ) {
                wallet -= recordData.getRecordAmount();
            }
        }
        return wallet;
    }
}
