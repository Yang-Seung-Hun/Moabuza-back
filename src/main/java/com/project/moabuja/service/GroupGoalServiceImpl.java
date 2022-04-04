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

import static com.project.moabuja.domain.alarm.AlarmDetailType.*;
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

            GroupResponseDto goalResponseDto = GroupResponseDto.builder()
                    .goalStatus(goalStatus)
                    .groupMembers(groupMembers)
                    .groupName(groupGoal.get().getGroupGoalName())
                    .groupGoalAmount(groupGoal.get().getGroupGoalAmount())
                    .groupLeftAmount(leftAmount)
                    .groupNowPercent(percent)
                    .groupDoneGoals(groupDoneGoalNames)
                    .groupLists(groupList)
                    .waitingGoals(null)
                    .build();
            return new ResponseEntity<>(goalResponseDto, HttpStatus.OK);

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

                GroupResponseDto waitingResponseDto = GroupResponseDto.builder()
                        .goalStatus(goalStatus)
                        .groupMembers(null)
                        .groupName(null)
                        .groupGoalAmount(0)
                        .groupLeftAmount(0)
                        .groupNowPercent(0)
                        .groupDoneGoals(groupDoneGoalNames)
                        .groupLists(null)
                        .waitingGoals(waitingGoals)
                        .build();

                return new ResponseEntity<>(waitingResponseDto, HttpStatus.OK);
            } else { //GroupGoal 없을때
                String goalStatus = "noGoal";
                GroupResponseDto noGoalResponseDto = GroupResponseDto.builder()
                        .goalStatus(goalStatus)
                        .groupMembers(null)
                        .groupName(null)
                        .groupGoalAmount(0)
                        .groupLeftAmount(0)
                        .groupNowPercent(0)
                        .groupDoneGoals(groupDoneGoalNames)
                        .groupLists(null)
                        .waitingGoals(null)
                        .build();

                return new ResponseEntity<>(noGoalResponseDto, HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<CreateGroupResponseDto> getGroupMemberCandidates(Member currentMember) {

        List<Friend> friendsTemp = friendRepository.findFriendsByMember(currentMember);
        List<Friend> friends = new ArrayList<>();
        for (Friend friend : friendsTemp) {
            if (friendService.friendCheck(friend.getMember(), friend.getFriend()).equals(FriendStatus.FRIEND)) {
                friends.add(friend);
            }
        }
        List<CreateGroupMemberDto> groupMembers = new ArrayList<>();

        if (friends.size() == 0){
            CreateGroupResponseDto createGroupResponseDto = new CreateGroupResponseDto(groupMembers);
            return new ResponseEntity<>(createGroupResponseDto, HttpStatus.OK);
        }

        for(Friend friend : friends){
            //친구의 그룹 골을 확인
            Member friendById = Optional.of(memberRepository.findById(friend.getFriend().getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
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
        Alarm alarm = Optional.of(alarmRepository.findById(alarmId)).get().orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        MemberWaitingGoal currentMemberWaitingGoal = memberWaitingGoalRepository.findMemberWaitingGoalByMemberAndWaitingGoal(currentMember, waitingGoal);
        currentMemberWaitingGoal.changeIsAcceptedGoal();

        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        // 전체 수락 전
        if (! checkAccepted(friends)) {
            List<String> friendListTmp = new ArrayList<>();
            sendGoalAlarm(friends, friendListTmp, currentMember, GROUP, accept, waitingGoal, alarmRepository);

            alarmRepository.delete(alarm);
        }

        // 전체 수락 후 마지막 수락
        else if (checkAccepted(friends)) {
            List<String> friendListTmp = new ArrayList<>();

            List<String> friendList = sendGoalAlarm(friends, friendListTmp, currentMember, GROUP, create, waitingGoal, alarmRepository);
            goalAlarm(currentMember, currentMember, GROUP, create, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), waitingGoal.getId(), alarmRepository);

            // GroupGoal 생성
            CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto(waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList);
            save(createGroupRequestDto, currentMember);
            waitingGoalRepository.delete(waitingGoal);
            alarmRepository.delete(alarm);

            // 다른 수락대기 상태의 Group Goal 폭파 및 알람
            List<MemberWaitingGoal> deleteMemberWaitingGoals = memberWaitingGoalRepository.findMemberWaitingGoalsByMember(currentMember);
            List<WaitingGoal> deleteWaitings = new ArrayList<>();
            for (MemberWaitingGoal delete : deleteMemberWaitingGoals) {
                WaitingGoal waiting = delete.getWaitingGoal();
                deleteWaitings.add(waiting);
                // WaitingGoal waiting = waitingGoalRepository.findWaitingGoalById(delete.getWaitingGoal().getId());

                List<MemberWaitingGoal> alarmMemberList = waiting.getMemberWaitingGoals();
                sendGoalAlarm(alarmMemberList, friendListTmp, currentMember, GROUP, boom, waiting, alarmRepository);
            }
            // waitingGoal 삭제
            waitingGoalRepository.deleteAll(deleteWaitings);
        }

        return new ResponseEntity<>(new Msg(GroupAccept.getMsg()), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postGroupRefuse(Member currentMemberTemp, Long alarmId) {
        Alarm alarm = Optional.of(alarmRepository.findById(alarmId)).get().orElseThrow(() -> new ErrorException(ErrorCode.ALARM_NOT_EXIST));
        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        List<String> friendListTmp = new ArrayList<>();
        sendGoalAlarm(friends, friendListTmp, currentMember, GROUP, boom, waitingGoal, alarmRepository);
        goalAlarm(currentMember, currentMember, GROUP, boom, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), waitingGoal.getId(), alarmRepository);
        // 자신과 다른 사용자들에게 남아있는 초대 알람을 삭제
        List<Alarm> deleteAlarmList = alarmRepository.findAlarmsByWaitingGoalIdAndAlarmDetailType(alarm.getWaitingGoalId(), AlarmDetailType.invite);
        alarmRepository.deleteAll(deleteAlarmList);
        waitingGoalRepository.delete(waitingGoal);

        return new ResponseEntity<>(new Msg(GroupRefuse.getMsg()), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Msg> exitGroup(Member currentMemberTemp) {

        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        GroupGoal groupGoal = currentMember.getGroupGoal();
        Long id = groupGoal.getId();

        List<Member> memberList = currentMember.getGroupGoal().getMembers();
        if (memberList.size() == 2) {
            while (memberList.size() > 0) {
                GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                        .alarmType(GROUP)
                        .alarmDetailType(AlarmDetailType.boom)
                        .goalName(groupGoal.getGroupGoalName())
                        .goalAmount(groupGoal.getGroupGoalAmount())
                        .waitingGoalId(id)
                        .friendNickname(currentMember.getNickname())
                        .member(memberList.get(0))
                        .build();
                alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));

                groupGoal.removeMember(memberList.get(0));
            } groupGoalRepository.deleteById(id);
        }else {
            List<Member> groupGoalMembers = groupGoal.getMembers();
            for (Member member : groupGoalMembers) {
                goalAlarm(member, currentMember, GROUP, talju, groupGoal.getGroupGoalName(), groupGoal.getGroupGoalAmount(), null, alarmRepository);
            }
            groupGoal.removeMember(currentMember);
        }

        return new ResponseEntity<>(new Msg(GroupExit.getMsg()), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Msg> exitWaitingGroup(Member currentMemberTemp, Long id) {
        exitWaitingGoal(currentMemberTemp, id, GROUP, memberRepository, waitingGoalRepository, memberWaitingGoalRepository, alarmRepository);

        return new ResponseEntity<>(new Msg(GroupExit.getMsg()), HttpStatus.OK);
    }

    //challengeGoalServiceImpl에서도 사용
    static void inviteFriends(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto, WaitingGoal waitingGoal, MemberWaitingGoalRepository memberWaitingGoalRepository, MemberRepository memberRepository, AlarmRepository alarmRepository, AlarmType alarmType) {
        memberWaitingGoalRepository.save(new MemberWaitingGoal(currentMember, waitingGoal, true));

        for (String friendNickname : goalAlarmRequestDto.getFriendNickname()) {
            Member member = Optional.of(memberRepository.findMemberByNickname(friendNickname)).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
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
    static void exitWaitingGoal(Member currentMemberTemp, Long id, AlarmType alarmType, MemberRepository memberRepository, WaitingGoalRepository waitingGoalRepository, MemberWaitingGoalRepository memberWaitingGoalRepository, AlarmRepository alarmRepository) {
        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        WaitingGoal waitingGoal = Optional.of(waitingGoalRepository.findWaitingGoalById(id)).orElseThrow(() -> new ErrorException(GOAL_NOT_EXIST));
        MemberWaitingGoal memberWaitingGoal = Optional.of(memberWaitingGoalRepository.findMemberWaitingGoalByMemberAndWaitingGoal(currentMember, waitingGoal)).orElseThrow(() -> new ErrorException(GOAL_MEMBER_NOT_MATCH));

        List<String> friendListTmp = new ArrayList<>();
        sendGoalAlarm(waitingGoal.getMemberWaitingGoals(), friendListTmp, currentMember, alarmType, boom, waitingGoal, alarmRepository);
        goalAlarm(currentMember, currentMember, alarmType, boom, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), waitingGoal.getId(), alarmRepository);

        waitingGoalRepository.delete(waitingGoal);
    }

    //challengeGoalServiceImpl에서도 사용
    static boolean checkAccepted(List<MemberWaitingGoal> memberWaitingGoals) {
        boolean result = true;

        for (MemberWaitingGoal memberWaitingGoal : memberWaitingGoals) {
            if (! memberWaitingGoal.isAcceptedGoal()) {
                result = false;
                break; }}
        return result;
    }

    //challengeGoalServiceImpl에서도 사용
    static List<String> sendGoalAlarm(List<MemberWaitingGoal> friends, List<String> friendList, Member currentMember, AlarmType alarmType, AlarmDetailType alarmDetailType, WaitingGoal waitingGoal, AlarmRepository alarmRepository) {
        for (MemberWaitingGoal friend : friends) {
            if (friend.getMember() != currentMember) {
                friendList.add(friend.getMember().getNickname());
                GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                        .alarmType(alarmType)
                        .alarmDetailType(alarmDetailType)
                        .goalName(waitingGoal.getWaitingGoalName())
                        .goalAmount(waitingGoal.getWaitingGoalAmount())
                        .waitingGoalId(waitingGoal.getId())
                        .friendNickname(currentMember.getNickname())
                        .member(friend.getMember())
                        .build();
                alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
            }
        }
        return friendList;
    }

    //challengeGoalServiceImpl에서도 사용
    static void goalAlarm(Member member, Member nicknameMember, AlarmType alarmType, AlarmDetailType alarmDetailType, String goalName, int goalAmount, Long id, AlarmRepository alarmRepository) {
        GoalAlarmSaveDto alarmSaveDto = GoalAlarmSaveDto.builder()
                .alarmType(alarmType)
                .alarmDetailType(alarmDetailType)
                .goalName(goalName)
                .goalAmount(goalAmount)
                .waitingGoalId(id)
                .friendNickname(nicknameMember.getNickname())
                .member(member)
                .build();
        alarmRepository.save(GoalAlarmSaveDto.goalToEntity(alarmSaveDto));
    }

}
