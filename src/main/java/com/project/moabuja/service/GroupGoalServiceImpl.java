package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.friend.FriendStatus;
import com.project.moabuja.domain.goal.*;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmSaveDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.request.goal.WaitingGoalSaveDto;
import com.project.moabuja.dto.response.goal.*;
import com.project.moabuja.exception.ErrorCode;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.moabuja.domain.alarm.AlarmType.GROUP;
import static com.project.moabuja.dto.ResponseMsg.*;
import static com.project.moabuja.exception.ErrorCode.*;

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
    private final FriendServiceImpl friendService;

    @Override
    @Transactional
    public ResponseEntity<Msg> save(CreateGroupRequestDto groupRequestDto, Member currentMemberTemp) {

        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        GroupGoal groupGoal = new GroupGoal(groupRequestDto.getCreateGroupName(), groupRequestDto.getCreateGroupAmount(), 0);

        //member랑 groupGoal 연관관계 맺음
        GroupGoal savedGoal = groupGoalRepository.save(groupGoal);

        GroupGoal goal = Optional
                .of(groupGoalRepository.findById(savedGoal.getId())).get()
                .orElseThrow(() -> new ErrorException(GOAL_NOT_EXIST));

        goal.addMember(currentMember);

        for (String name : groupRequestDto.getGroupFriends()) {
            Optional<Member> memberByNickname = Optional
                    .of(memberRepository.findMemberByNickname(name))
                    .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
            memberByNickname.ifPresent(goal::addMember);
        }
        return new ResponseEntity<>(new Msg(GroupCreate.getMsg()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GroupResponseDto> getGroupInfo(Member currentMemberTemp) {

        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

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
                    if(record.getCreatedAt().isAfter(groupGoal.get().getCreatedAt())){
                        groupList.add(GroupListDto.builder()
                                .groupDate(record.getRecordDate())
                                .hero(member.getHero())
                                .nickname(member.getNickname())
                                .groupMemo(record.getMemo())
                                .groupAmount(record.getRecordAmount())
                                .build());
                        tempAmount = tempAmount+record.getRecordAmount();
                    }
                }
                currentAmount += tempAmount;
            }
            int leftAmount = groupGoal.get().getGroupGoalAmount() - currentAmount;
            int percent = (int) (((double) currentAmount / (double) (groupGoal.get().getGroupGoalAmount())) * 100);

            GroupResponseDto haveGoal = GroupResponseDto.builder()
                    .goalStatus(goalStatus)
                    .groupMembers(groupMembers)
                    .groupName(groupGoal.get().getGroupGoalName())
                    .groupLeftAmount(leftAmount)
                    .groupNowPercent(percent)
                    .groupDoneGoals(groupDoneGoalNames)
                    .groupLists(groupList)
                    .waitingGoals(null)
                    .build();
            return new ResponseEntity<>(haveGoal, HttpStatus.OK);

        } else {
            List<MemberWaitingGoal> checkWaitingGoal = new ArrayList<>();
            for (MemberWaitingGoal memberWaitingGoal : memberWaitingGoals) {
                GoalType goalType = memberWaitingGoal.getWaitingGoal().getGoalType();
                if (goalType == GoalType.GROUP){ checkWaitingGoal.add(memberWaitingGoal); }
            }

            if (!checkWaitingGoal.isEmpty()) { // 수락대기중
                String goalStatus = "waiting";

                List<WaitingGoalResponseDto> waitingGoals = new ArrayList<>();
                for (MemberWaitingGoal memberWaitingGoal : checkWaitingGoal) {
                    waitingGoals.add(new WaitingGoalResponseDto(memberWaitingGoal.getWaitingGoal().getId(), memberWaitingGoal.getWaitingGoal().getWaitingGoalName()));
                }

                GroupResponseDto waiting = GroupResponseDto.builder()
                        .goalStatus(goalStatus)
                        .groupMembers(null)
                        .groupName(null)
                        .groupLeftAmount(0)
                        .groupNowPercent(0)
                        .groupDoneGoals(groupDoneGoalNames)
                        .groupLists(null)
                        .waitingGoals(waitingGoals)
                        .build();

                return new ResponseEntity<>(waiting, HttpStatus.OK);
            } else { //GroupGoal 없을때
                String goalStatus = "noGoal";
                GroupResponseDto noGoal = GroupResponseDto.builder()
                        .goalStatus(goalStatus)
                        .groupMembers(null)
                        .groupName(null)
                        .groupLeftAmount(0)
                        .groupNowPercent(0)
                        .groupDoneGoals(groupDoneGoalNames)
                        .groupLists(null)
                        .waitingGoals(null)
                        .build();

                return new ResponseEntity<>(noGoal, HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentMember) {

        List<Friend> friendsTemp = friendRepository.findFriendsByMember(currentMember);
        List<Friend> friends = new ArrayList<>();
        for (Friend friend : friendsTemp) {
            if (friendService.friendCheck(friend.getMember(), friend.getFriend()).equals(FriendStatus.FRIEND)) {
                friends.add(new Friend(friend.getMember(), friend.getFriend(), true));
            }
        }
        List<CreateGroupMemberDto> groupMembers = new ArrayList<>();

        if (friends.size() == 0){
            CreateGroupResponseDto createGroupResponseDto = new CreateGroupResponseDto(groupMembers);
            return new ResponseEntity<>(createGroupResponseDto, HttpStatus.OK);
        }

        for(Friend friend : friends){
            //친구의 그룹 골을 확인
            Member friendById = Optional
                    .of(memberRepository.findById(friend.getFriend().getId())).get()
                    .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
            Optional<GroupGoal> friendGroupGoal = Optional.ofNullable(friendById.getGroupGoal());

            //이미 진행중인 챌린지 있음
            if(friendGroupGoal.isPresent()){
                groupMembers.add(new CreateGroupMemberDto(friendById.getNickname(),false,friendById.getHero()));
            } else{ //진행중인 챌린지 없고, 대기만 있음
                groupMembers.add(new CreateGroupMemberDto(friendById.getNickname(),true,friendById.getHero()));
            }
        }

        CreateGroupResponseDto groupResponseDto = new CreateGroupResponseDto(groupMembers);
        return new ResponseEntity<>(groupResponseDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postGroup(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto) {
        WaitingGoal waitingGoal = waitingGoalRepository.save(WaitingGoalSaveDto.toEntity(goalAlarmRequestDto.getGoalName(), goalAlarmRequestDto.getGoalAmount(), GoalType.GROUP));
        inviteFriends(currentMember, goalAlarmRequestDto, waitingGoal, memberWaitingGoalRepository, memberRepository, alarmRepository, GROUP);

        return new ResponseEntity<>(new Msg(GroupPost.getMsg()), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postGroupAccept(Member currentMemberTemp, Long alarmId) {
        Member currentMember = Optional
                .of(memberRepository.findById(currentMemberTemp.getId())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        Alarm alarm = Optional
                .of(alarmRepository.findById(alarmId)).get()
                .orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
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
                    GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                            .alarmType(GROUP)
                            .alarmDetailType(AlarmDetailType.accept)
                            .goalName(waitingGoal.getWaitingGoalName())
                            .goalAmount(waitingGoal.getWaitingGoalAmount())
                            .waitingGoalId(waitingGoal.getId())
                            .friendNickname(currentMember.getNickname())
                            .member(friend.getMember())
                            .build();
                    alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
                }
            }
            alarmRepository.delete(alarm);
        }

        // 전체 수락 후 마지막 수락
        else if (checkAccepted(friends)) {
            List<String> friendList = new ArrayList<>();
            GoalAlarmSaveDto alarmSave = GoalAlarmSaveDto.builder()
                    .alarmType(GROUP)
                    .alarmDetailType(AlarmDetailType.create)
                    .goalName(waitingGoal.getWaitingGoalName())
                    .goalAmount(waitingGoal.getWaitingGoalAmount())
                    .waitingGoalId(waitingGoal.getId())
                    .friendNickname(currentMember.getNickname())
                    .member(currentMember)
                    .build();
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSave));

            for (MemberWaitingGoal friend : friends) {
                if (friend.getMember() != currentMember) {
                    friendList.add(friend.getMember().getNickname());
                    GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                            .alarmType(GROUP)
                            .alarmDetailType(AlarmDetailType.create)
                            .goalName(waitingGoal.getWaitingGoalName())
                            .goalAmount(waitingGoal.getWaitingGoalAmount())
                            .waitingGoalId(waitingGoal.getId())
                            .friendNickname(currentMember.getNickname())
                            .member(friend.getMember())
                            .build();
                    alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
                }
            }
            // GroupGoal 생성
            CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto(waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList);
            save(createGroupRequestDto, currentMember);
            waitingGoalRepository.delete(waitingGoal);

            alarmRepository.delete(alarm);
        }

        return new ResponseEntity<>(new Msg(GroupAccept.getMsg()), HttpStatus.OK);
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
    public ResponseEntity<Msg> postGroupRefuse(Member currentMember, Long alarmId) {
        Alarm alarm = Optional
                .of(alarmRepository.findById(alarmId)).get()
                .orElseThrow(() -> new ErrorException(ErrorCode.ALARM_NOT_EXIST));
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        List<String> friendList = new ArrayList<>();

        for (MemberWaitingGoal friend : friends) {
            friendList.add(friend.getMember().getNickname());
            GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                    .alarmType(GROUP)
                    .alarmDetailType(AlarmDetailType.boom)
                    .goalName(waitingGoal.getWaitingGoalName())
                    .goalAmount(waitingGoal.getWaitingGoalAmount())
                    .waitingGoalId(waitingGoal.getId())
                    .friendNickname(currentMember.getNickname())
                    .member(friend.getMember())
                    .build();
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
        }
        alarmRepository.delete(alarm);
        waitingGoalRepository.delete(waitingGoal);

        return new ResponseEntity<>(new Msg(GroupRefuse.getMsg()), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Msg> exitGroup(Member currentMemberTemp) {

        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        GroupGoal groupGoal = currentMember.getGroupGoal();

        List<Alarm> alarmList = alarmRepository.findAlarmsByGoalNameAndMember(groupGoal.getGroupGoalName(), currentMember);
        alarmRepository.deleteAll(alarmList);

        List<Member> memberList = currentMember.getGroupGoal().getMembers();
        if (memberList.size() == 2) {
            for (Member member : memberList) {
                member.changeGroupGoal(null);
            } groupGoalRepository.delete(groupGoal);
        } else { currentMember.changeGroupGoal(null); }

        return new ResponseEntity<>(new Msg(GroupExit.getMsg()), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Msg> exitWaitingGroup(Member currentMemberTemp, Long id) {
        exitWaitingGoal(currentMemberTemp, id, memberRepository, waitingGoalRepository, memberWaitingGoalRepository);

        return new ResponseEntity<>(new Msg(GroupExit.getMsg()), HttpStatus.OK);
    }

    //challengeGoalServiceImpl에서도 사용
    static void inviteFriends(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto, WaitingGoal waitingGoal, MemberWaitingGoalRepository memberWaitingGoalRepository, MemberRepository memberRepository, AlarmRepository alarmRepository, AlarmType alarmType) {
        memberWaitingGoalRepository.save(new MemberWaitingGoal(currentMember, waitingGoal, true));

        for (String friendNickname : goalAlarmRequestDto.getFriendNickname()) {
            Member member = Optional
                    .of(memberRepository.findMemberByNickname(friendNickname)).get()
                    .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
            MemberWaitingGoal memberWaitingGoal = new MemberWaitingGoal(member, waitingGoal, false);
            memberWaitingGoalRepository.save(memberWaitingGoal);
            GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                    .alarmType(alarmType)
                    .alarmDetailType(AlarmDetailType.invite)
                    .goalName(goalAlarmRequestDto.getGoalName())
                    .goalAmount(goalAlarmRequestDto.getGoalAmount())
                    .waitingGoalId(waitingGoal.getId())
                    .friendNickname(currentMember.getNickname())
                    .member(member)
                    .build();
            alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
        }
    }

    //challengeGoalServiceImpl에서도 사용
    static void exitWaitingGoal(Member currentMemberTemp, Long id, MemberRepository memberRepository, WaitingGoalRepository waitingGoalRepository, MemberWaitingGoalRepository memberWaitingGoalRepository) {
        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        WaitingGoal waitingGoal = Optional.of(waitingGoalRepository.findWaitingGoalById(id)).orElseThrow(() -> new ErrorException(GOAL_NOT_EXIST));
        MemberWaitingGoal memberWaitingGoal = Optional
                .of(memberWaitingGoalRepository.findMemberWaitingGoalByMemberAndWaitingGoal(currentMember, waitingGoal))
                .orElseThrow(() -> new ErrorException(GOAL_MEMBER_NOT_MATCH));

        waitingGoalRepository.delete(waitingGoal);
    }
}
