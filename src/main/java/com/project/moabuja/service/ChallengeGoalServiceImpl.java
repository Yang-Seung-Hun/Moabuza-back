package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.*;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmSaveDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.request.goal.WaitingGoalSaveDto;
import com.project.moabuja.dto.response.goal.*;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.CHALLENGE;
import static com.project.moabuja.domain.alarm.AlarmType.GROUP;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class ChallengeGoalServiceImpl implements ChallengeGoalService{

    private final MemberRepository memberRepository;
    private final ChallengeGoalRepository challengeGoalRepository;
    private final RecordRepository recordRepository;
    private final FriendRepository friendRepository;
    private final AlarmRepository alarmRepository;
    private final WaitingGoalRepository waitingGoalRepository;
    private final MemberWaitingGoalRepository memberWaitingGoalRepository;

    @Transactional
    @Override
    public ResponseEntity<String> save(CreateChallengeRequestDto createChallengeRequestDto, Member currentTemp) {


        Optional<Member> currentMemberTemp = memberRepository.findById(currentTemp.getId());
        Member currentMember = null;
        if(currentMemberTemp.isPresent()){
            currentMember = currentMemberTemp.get();
        }else{
            throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다.");
        }

        ChallengeGoal challengeGoal = new ChallengeGoal(createChallengeRequestDto.getCreateChallengeName(), createChallengeRequestDto.getCreateChallengeAmount(), 0);

        if (Optional.ofNullable(createChallengeRequestDto.getChallengeFriends()).isEmpty()) {
            ChallengeGoal savedGoal = challengeGoalRepository.save(challengeGoal);
            savedGoal.addMember(currentMember);
            return ResponseEntity.ok().body("도전해부자 생성 완료");
        }

        for(String name :createChallengeRequestDto.getChallengeFriends()){
            Optional<Member> memberByNickname = memberRepository.findMemberByNickname(name);
            if (memberByNickname.isPresent()) {
                challengeGoal.addMember(memberByNickname.get());
            } else {
                throw new MemberNotFoundException("선택하신 친구 중 존재하지 않는 사용자가 있습니다.");
            }
        }

        //member랑 challengegoal 연관관계 맺음
        ChallengeGoal savedGoal = challengeGoalRepository.save(challengeGoal);
        savedGoal.addMember(currentMember);
        return ResponseEntity.ok().body("도전해부자 생성 완료");
    }

    @Override
    public ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member currentTemp) {

        //여기는 프록시 생명주기 문제 땜에 필요
        Optional<Member> currentMemberTemp = memberRepository.findById(currentTemp.getId());
        Member currentMember = currentMemberTemp.get();

        Optional<ChallengeGoal> challengeGoal = Optional.ofNullable(currentMember.getChallengeGoal());
        List<String> challengeDoneGoalNames = new ArrayList<>();
        for (DoneGoal doneGoal : currentMember.getDoneGaols()) {
            if (doneGoal.getGoalType() == GoalType.CHALLENGE) {
                challengeDoneGoalNames.add(doneGoal.getDoneGoalName());
            }
        }
        List<MemberWaitingGoal> memberWaitingGoals = currentMember.getMemberWaitingGoals();

        //challengeGoal 있을때
        if (challengeGoal.isPresent()) {
            String goalStatus = "goal";
            List<ChallengeMemberDto> challengeMembers = new ArrayList<>();
            for (Member user : challengeGoal.get().getMembers()) {
                int currentAmount = 0;
                List<Record> records = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, user);
                for (Record record : records) {
                    currentAmount += record.getRecordAmount();
                }
                int leftAmount = challengeGoal.get().getChallengeGoalAmount() - currentAmount;
                int percent = (int) (((double) currentAmount / (double) (challengeGoal.get().getChallengeGoalAmount())) * 100);
                challengeMembers.add(new ChallengeMemberDto(user.getNickname(), user.getHero(), leftAmount, percent));
            }

            List<Record> challengeRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, currentMember);
            List<ChallengeListDto> challengeLists = challengeRecords.stream().map(record -> {
                return new ChallengeListDto(record.getRecordDate(), record.getMemo(), record.getRecordAmount());
            }).collect(Collectors.toList());

            ChallengeResponseDto goalResponseDto = new ChallengeResponseDto(goalStatus, challengeMembers, challengeGoal.get().getChallengeGoalName(), challengeDoneGoalNames, challengeLists,null);

            return ResponseEntity.ok().body(goalResponseDto);
        } else {
            if (! memberWaitingGoals.isEmpty()) { //수락대기중
                String goalStatus = "waiting";

                List<WaitingGoalResponseDto> waitingGoals = new ArrayList<>();
                for (MemberWaitingGoal memberWaitingGoal : memberWaitingGoals) {
                    waitingGoals.add(new WaitingGoalResponseDto(memberWaitingGoal.getWaitingGoal().getId(),memberWaitingGoal.getWaitingGoal().getWaitingGoalName()));
                }

                ChallengeResponseDto waitingResponseDto = new ChallengeResponseDto(goalStatus, null, null, challengeDoneGoalNames, null, waitingGoals);

                return ResponseEntity.ok().body(waitingResponseDto);
            } else { //challengeGoal 없을때
                String goalStatus = "noGoal";
                ChallengeResponseDto noGoalResponseDto = new ChallengeResponseDto(goalStatus, null, null, challengeDoneGoalNames, null, null);

                return ResponseEntity.ok().body(noGoalResponseDto);
            }
        }
    }

    @Override
    public ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentMember) {

        List<Friend> friends = friendRepository.findFriendsByMember(currentMember);
        List<CreateChallengeMemberDto> challengeMembers = new ArrayList<>();

        if (friends.size() == 0){
            CreateChallengeResponseDto createChallengeResponseDto = new CreateChallengeResponseDto(challengeMembers);
            return ResponseEntity.ok().body(createChallengeResponseDto);
        }

        for(Friend friend : friends){
            //친구의 챌린지 골을 확인
            Optional<Member> friendById = memberRepository.findById(friend.getFriend().getId());
            Optional<ChallengeGoal> friendChallengeGoal = Optional.ofNullable(friendById.get().getChallengeGoal());

            //이미 진행중인 챌린지 있음
            if(friendChallengeGoal.isPresent()) {
                if (friendById.isPresent()) {
                    challengeMembers.add(new CreateChallengeMemberDto(friendById.get().getNickname(), false, friendById.get().getHero()));
                } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
            } else { // 진행 중인 챌린지 없음
                if (friendById.isPresent()){
                    challengeMembers.add(new CreateChallengeMemberDto(friendById.get().getNickname(),true,friendById.get().getHero()));
                } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
            }
        }

        CreateChallengeResponseDto challengeResponseDto = new CreateChallengeResponseDto(challengeMembers);
        return ResponseEntity.ok().body(challengeResponseDto);
    }


    @Transactional
    @Override
    public ResponseEntity<String> postChallenge(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto) {
        if (Optional.ofNullable(goalAlarmRequestDto.getFriendNickname()).isEmpty()) {
            CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto(goalAlarmRequestDto.getGoalName(), goalAlarmRequestDto.getGoalAmount(), null);
            save(createChallengeRequestDto, currentMember);

            return ResponseEntity.ok().body("해부자 요청 완료");
        }

        WaitingGoal waitingGoal = waitingGoalRepository.save(WaitingGoalSaveDto.toEntity(goalAlarmRequestDto.getGoalName(), goalAlarmRequestDto.getGoalAmount(), GoalType.CHALLENGE));
        memberWaitingGoalRepository.save(new MemberWaitingGoal(currentMember, waitingGoal, true));

        for (String friendNickname : goalAlarmRequestDto.getFriendNickname()) {
            Optional<Member> member = memberRepository.findMemberByNickname(friendNickname);
            if (member.isPresent()) {
                MemberWaitingGoal memberWaitingGoal = new MemberWaitingGoal(member.get(), waitingGoal, false);
                memberWaitingGoalRepository.save(memberWaitingGoal);

                alarmRepository.save(GoalAlarmSaveDto.goalToEntity(goalAlarmRequestDto, waitingGoal.getId(), CHALLENGE, AlarmDetailType.invite, currentMember.getNickname(), member.get()));
            } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
        }

        return ResponseEntity.ok().body("해부자 요청 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postChallengeAccept(Member currentMember, Long alarmId) {
        Alarm alarm = alarmRepository.findAlarmById(alarmId);
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        MemberWaitingGoal currentMemberWaitingGoal = memberWaitingGoalRepository.findMemberWaitingGoalByMemberAndWaitingGoal(currentMember, waitingGoal);
        currentMemberWaitingGoal.changeIsAcceptedGoal();

        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        // 전체 수락 전
        if (! checkAccepted(friends)) {
            List<String> friendList = new ArrayList<>();

            for (MemberWaitingGoal friend : friends) {
                if (friend.getMember() != currentMember) {
                    friendList.add(friend.getMember().getNickname());
                    alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.CHALLENGE, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                            waitingGoal.getId(), CHALLENGE, AlarmDetailType.accept, currentMember.getNickname(), friend.getMember()));
                }
            }
            alarmRepository.delete(alarm);
        }

        // 전체 수락 후 마지막 수락
        else if (checkAccepted(friends)) {
            List<String> friendList = new ArrayList<>();

            for (MemberWaitingGoal friend : friends) {
                if (friend.getMember() != currentMember) {
                    friendList.add(friend.getMember().getNickname());
                    alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.CHALLENGE, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                            waitingGoal.getId(), CHALLENGE, AlarmDetailType.create, currentMember.getNickname(), friend.getMember()));
                }
            }
            // ChallengeGoal 생성
            CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto(waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList);
            save(createChallengeRequestDto, currentMember);
            waitingGoalRepository.delete(waitingGoal);
            alarmRepository.delete(alarm);
        }

        return ResponseEntity.ok().body("해부자 수락 완료");
    }

    public boolean checkAccepted(List<MemberWaitingGoal> memberWaitingGoals) {
        boolean result = true;

        for (MemberWaitingGoal memberWaitingGoal : memberWaitingGoals) {
            if (! memberWaitingGoal.isAcceptedGoal()) {
                result = false;
                break; }}
        return result;
    }

    @Transactional
    @Override
    public ResponseEntity<String> postChallengeRefuse(Member currentMember, Long alarmId) {
        Alarm alarm = alarmRepository.findAlarmById(alarmId);
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        List<String> friendList = new ArrayList<>();

        for (MemberWaitingGoal friend : friends) {
            friendList.add(friend.getMember().getNickname());
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.CHALLENGE, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                    waitingGoal.getId(), CHALLENGE, AlarmDetailType.boom, currentMember.getNickname(), friend.getMember()));
        }
        alarmRepository.delete(alarm);
        waitingGoalRepository.delete(waitingGoal);

        return ResponseEntity.ok().body("해부자 거절 완료");
    }

    @Override
    @Transactional
    public ResponseEntity<String> exitChallenge(Member currentMemberTemp, Long id) {

        Optional<Member> currentMember = memberRepository.findById(currentMemberTemp.getId());

        List<Member> memberList = currentMember.get().getChallengeGoal().getMembers();

        ChallengeGoal challengeGoal = currentMember.get().getChallengeGoal();
        currentMember.get().changeGroupGoal(null);

        if (memberList.size() == 1) { challengeGoalRepository.delete(challengeGoal); }

        return ResponseEntity.ok().body("도전해부자 나가기 완료");
    }

    @Override
    @Transactional
    public ResponseEntity<String> exitWaitingChallenge(Member current, Long id) {

        Optional<Member> currentMemberTmp = memberRepository.findById(current.getId());
        Member currentMember = null;

        if(currentMemberTmp.isPresent()){
            currentMember = currentMemberTmp.get();
        } else{
            throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다.");
        }

        WaitingGoal waitingGoalById = waitingGoalRepository.findWaitingGoalById(id);
        waitingGoalRepository.delete(waitingGoalById);

        return ResponseEntity.ok().body("도전해부자 나가기 완료");
    }


}
