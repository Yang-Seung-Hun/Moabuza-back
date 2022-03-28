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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.moabuja.domain.alarm.AlarmType.CHALLENGE;
import static com.project.moabuja.domain.alarm.AlarmType.GROUP;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupGoalServiceImpl implements GroupGoalService{

    private final MemberRepository memberRepository;
    private final GroupGoalRepository groupGoalRepository;
    private final RecordRepository recordRepository;
    private final FriendRepository friendRepository;
    private final AlarmRepository alarmRepository;
    private final WaitingGoalRepository waitingGoalRepository;
    private final MemberWaitingGoalRepository memberWaitingGoalRepository;

    @Override
    @Transactional
    public ResponseEntity<String> save(CreateGroupRequestDto groupRequestDto, Member current) {

        Optional<Member> currentMemberTemp = memberRepository.findById(current.getId());
        Member currentMember = null;
        if(currentMemberTemp.isPresent()){
            currentMember = currentMemberTemp.get();
        }

        GroupGoal groupGoal = new GroupGoal(groupRequestDto.getCreateGroupName(), groupRequestDto.getCreateGroupAmount(), 0);

        //member랑 groupGoal 연관관계 맺음
        GroupGoal savedGoal = groupGoalRepository.save(groupGoal);

        Optional<GroupGoal> goal = groupGoalRepository.findById(savedGoal.getId());

        if(goal.isPresent()) {
            goal.get().addMember(currentMember);

            for (String name : groupRequestDto.getGroupFriends()) {
                Optional<Member> memberByNickname = memberRepository.findMemberByNickname(name);
                memberByNickname.ifPresent(member -> goal.get().addMember(member));
            }
        }
        return ResponseEntity.ok().body("같이해부자 생성 완료");
    }

    @Override
    public ResponseEntity<GroupResponseDto> getGroupInfo(Member current) {

        Optional<Member> currentMemberTemp = memberRepository.findById(current.getId());
        Member currentMember = null;
        if(currentMemberTemp.isPresent()){
            currentMember = currentMemberTemp.get();
        }

        Optional<GroupGoal> groupGoal = Optional.ofNullable(currentMember.getGroupGoal());
        List<String> groupDoneGoalNames = new ArrayList<>();
        for(DoneGoal doneGoal:currentMember.getDoneGaols()){
            if(doneGoal.getGoalType() == GoalType.GROUP){
                groupDoneGoalNames.add(doneGoal.getDoneGoalName());
            }
        }
        List<MemberWaitingGoal> memberWaitingGoals = currentMember.getMemberWaitingGoals();

        //GroupGoal 있을때
        if (groupGoal.isPresent()){
            String goalStatus = "goal";

            List<GroupMemberDto> groupMembers = new ArrayList<>();
            List<GroupListDto> groupList = new ArrayList<>();
            List<Member> members = groupGoal.get().getMembers();
            int currentAmount = 0;
            for (Member member : members) {
                groupMembers.add(new GroupMemberDto(member.getNickname(), member.getHero()));

                List<Record> memberGroupRecord = recordRepository.findRecordsByRecordTypeAndMember(RecordType.group, member);
                int tempAmount = 0;
                for (Record record : memberGroupRecord) {
                    groupList.add(new GroupListDto(record.getRecordDate(), member.getHero(), member.getNickname(),record.getMemo(),record.getRecordAmount()));
                    tempAmount = tempAmount+record.getRecordAmount();
                }
                currentAmount += tempAmount;
            }
            int leftAmount = groupGoal.get().getGroupGoalAmount() - currentAmount;
            int percent = (int) (((double) currentAmount / (double) (groupGoal.get().getGroupGoalAmount())) * 100);

            GroupResponseDto haveGoal = new GroupResponseDto(goalStatus,groupMembers,groupGoal.get().getGroupGoalName(),leftAmount,percent,groupDoneGoalNames,groupList,null);
            return ResponseEntity.ok().body(haveGoal);

        } else {

            if (!memberWaitingGoals.isEmpty()) { // 수락대기중
                String goalStatus = "waiting";

                List<WaitingGoalResponseDto> waitingGoals = new ArrayList<>();
                for (MemberWaitingGoal memberWaitingGoal : memberWaitingGoals) {
                    waitingGoals.add(new WaitingGoalResponseDto(memberWaitingGoal.getWaitingGoal().getId(),memberWaitingGoal.getWaitingGoal().getWaitingGoalName()));
                }

                GroupResponseDto waiting = new GroupResponseDto(goalStatus, null, null, 0, 0, groupDoneGoalNames, null, waitingGoals);

                return ResponseEntity.ok().body(waiting);
            } else { //challengeGoal 없을때
                String goalStatus = "noGoal";
                GroupResponseDto noGoal = new GroupResponseDto(goalStatus, null, null, 0, 0, groupDoneGoalNames, null, null);

                return ResponseEntity.ok().body(noGoal);
            }
        }
    }

    @Override
    public ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentMember) {

        List<Friend> friends = friendRepository.findFriendsByMember(currentMember);
        List<CreateGroupMemberDto> groupMembers = new ArrayList<>();

        if (friends.size() == 0){
            CreateGroupResponseDto createGroupResponseDto = new CreateGroupResponseDto(groupMembers);
            return ResponseEntity.ok().body(createGroupResponseDto);
        }

        for(Friend friend : friends){
            //친구의 그룹 골을 확인
            Optional<Member> friendById = memberRepository.findById(friend.getFriend().getId());
            Optional<GroupGoal> friendGroupGoal = Optional.ofNullable(friendById.get().getGroupGoal());

            //이미 진행중인 챌린지 있음
            if(friendGroupGoal.isPresent()){
                if (friendById.isPresent()){
                    groupMembers.add(new CreateGroupMemberDto(friendById.get().getNickname(),false,friendById.get().getHero()));
                } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
            } else{ //진행중인 챌린지 없고, 대기만 있음
                if(friendById.isPresent()){
                    groupMembers.add(new CreateGroupMemberDto(friendById.get().getNickname(),true,friendById.get().getHero()));
                } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
            }
        }

        CreateGroupResponseDto groupResponseDto = new CreateGroupResponseDto(groupMembers);
        return ResponseEntity.ok().body(groupResponseDto);
    }

    @Transactional
    @Override
    public ResponseEntity<String> postGroup(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto) {
        WaitingGoal waitingGoal = waitingGoalRepository.save(WaitingGoalSaveDto.toEntity(goalAlarmRequestDto.getGoalName(), goalAlarmRequestDto.getGoalAmount(), GoalType.GROUP));
        memberWaitingGoalRepository.save(new MemberWaitingGoal(currentMember, waitingGoal, true));

        for (String friendNickname : goalAlarmRequestDto.getFriendNickname()) {
            Optional<Member> member = memberRepository.findMemberByNickname(friendNickname);
            if (member.isPresent()) {
                MemberWaitingGoal memberWaitingGoal = new MemberWaitingGoal(member.get(), waitingGoal, false);
                memberWaitingGoalRepository.save(memberWaitingGoal);

                alarmRepository.save(GoalAlarmSaveDto.goalToEntity(goalAlarmRequestDto, waitingGoal.getId(), GROUP, AlarmDetailType.invite, currentMember.getNickname(), member.get()));
            } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
        }

        return ResponseEntity.ok().body("해부자 요청 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postGroupAccept(Member currentMember, Long alarmId) {
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
                    alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.GROUP, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                            waitingGoal.getId(), GROUP, AlarmDetailType.accept, currentMember.getNickname(), friend.getMember()));
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
                    alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.GROUP, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                            waitingGoal.getId(), GROUP, AlarmDetailType.create, currentMember.getNickname(), friend.getMember()));
                }
            }
            // GroupGoal 생성
            CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto(waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList);
            save(createGroupRequestDto, currentMember);
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
    public ResponseEntity<String> postGroupRefuse(Member currentMember, Long alarmId) {
        Alarm alarm = alarmRepository.findAlarmById(alarmId);
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        List<String> friendList = new ArrayList<>();

        for (MemberWaitingGoal friend : friends) {
            friendList.add(friend.getMember().getNickname());
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.GROUP, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                    waitingGoal.getId(), GROUP, AlarmDetailType.boom, currentMember.getNickname(), friend.getMember()));
        }
        alarmRepository.delete(alarm);
        waitingGoalRepository.delete(waitingGoal);

        return ResponseEntity.ok().body("해부자 거절 완료");
    }

    @Override
    @Transactional
    public ResponseEntity<String> exitGroup(Member currentMemberTemp, Long id) {

        Optional<Member> currentMember = memberRepository.findById(currentMemberTemp.getId());

        List<Member> memberList = currentMember.get().getGroupGoal().getMembers();
        if (memberList.size() <= 2) {
            GroupGoal groupGoal = currentMember.get().getGroupGoal();
            for (Member member : memberList) {
                member.changeGroupGoal(null);
            }
            groupGoalRepository.delete(groupGoal);
        } else {
            currentMember.get().changeGroupGoal(null);
        }

        return ResponseEntity.ok().body("도전해부자 취소 완료");
    }

    @Override
    @Transactional
    public ResponseEntity<String> exitWaitingGroup(Member current, Long id) {

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
