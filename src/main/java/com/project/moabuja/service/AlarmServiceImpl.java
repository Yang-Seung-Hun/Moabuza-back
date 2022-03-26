package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.GoalType;
import com.project.moabuja.domain.goal.MemberWaitingGoal;
import com.project.moabuja.domain.goal.WaitingGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.alarm.GoalAlarmSaveDto;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.request.goal.WaitingGoalSaveDto;
import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
import com.project.moabuja.exception.exceptionClass.AlarmErrorException;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.domain.alarm.AlarmType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final WaitingGoalRepository waitingGoalRepository;
    private final MemberWaitingGoalRepository memberWaitingGoalRepository;
    private final FriendService friendService;
    private final GroupGoalService groupGoalService;
    private final ChallengeGoalService challengeGoalService;

    @Override
    public ResponseEntity<List<FriendAlarmResponseDto>> getFriendAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, FRIEND);
        List<FriendAlarmResponseDto> friendAlarmList = alarms.stream()
                .map(FriendAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(friendAlarmList);
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendAlarm(FriendAlarmDto friendAlarmDto, Member currentMember) {
        Optional<Member> friend = memberRepository.findMemberByNickname(friendAlarmDto.getFriendNickname());
        if (friend.isPresent()) {
            Optional<Friend> isFriend = Optional.ofNullable(friendRepository.findByMemberAndFriend(currentMember, friend.get()));
            if (isFriend.isPresent()) { throw new IllegalArgumentException("해당 닉네임의 사용자와 친구 상태입니다."); }
            alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.request, friend.get(), currentMember.getNickname()));
        } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }

        return ResponseEntity.ok().body("친구 요청 알람 보내기 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendAcceptAlarm(Member currentMember, String friendNickname) {
        friendService.save(currentMember, friendNickname);

        Optional<Member> toFriend = memberRepository.findMemberByNickname(friendNickname);
        if (toFriend.isPresent()) {
            alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.accept, toFriend.get(), currentMember.getNickname()));
        } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }

        Alarm alarm = alarmRepository.findByMemberAndFriendNickname(currentMember, friendNickname);
        alarmRepository.delete(alarm);

        return ResponseEntity.ok().body("친구 요청을 수락하였습니다.");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postFriendRefuseAlarm(Member currentMember, String friendNickname) {
        Optional<Member> toFriend = memberRepository.findMemberByNickname(friendNickname);
        if (toFriend.isPresent()) {
            alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.refuse, toFriend.get(), currentMember.getNickname()));
        } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }

        Alarm alarm = alarmRepository.findByMemberAndFriendNickname(currentMember, friendNickname);
        alarmRepository.delete(alarm);

        return ResponseEntity.ok().body("친구 요청을 거절하였습니다.");
    }

    @Override
    public ResponseEntity<List<GoalAlarmResponseDto>> getGroupGoalAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, GROUP);
        List<GoalAlarmResponseDto> groupAlarmList = alarms.stream()
                .map(GoalAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(groupAlarmList);
    }

    @Override
    public ResponseEntity<List<GoalAlarmResponseDto>> getChallengeGoalAlarm(Member currentMember) {
        List<Alarm> alarms = alarmRepository.findAlarmsByMemberAndAlarmTypeOrderByCreatedAtDesc(currentMember, CHALLENGE);
        List<GoalAlarmResponseDto> groupAlarmList = alarms.stream()
                .map(GoalAlarmResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(groupAlarmList);
    }

    @Transactional
    @Override
    public ResponseEntity<String> postGoalAlarm(Member currentMember, GoalAlarmRequestDto goalAlarmRequestDto) {
        if (goalAlarmRequestDto.getGoalType() == GoalType.GROUP) {
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
        } else if (goalAlarmRequestDto.getGoalType() == GoalType.CHALLENGE) {
            if (goalAlarmRequestDto.getFriendNickname().isEmpty()) {
                CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto(goalAlarmRequestDto.getGoalName(), goalAlarmRequestDto.getGoalAmount(), null);
                challengeGoalService.save(createChallengeRequestDto, currentMember);
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
        }

        return ResponseEntity.ok().body("해부자 요청 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> postGoalAcceptAlarm(Member currentMember, Long alarmId) {
        Alarm alarm = alarmRepository.findAlarmById(alarmId);
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        MemberWaitingGoal currentMemberWaitingGoal = memberWaitingGoalRepository.findMemberWaitingGoalByMemberAndWaitingGoal(currentMember, waitingGoal);
        currentMemberWaitingGoal.changeIsAcceptedGoal();

        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        // 전체 수락 전
        if (! checkAccepted(friends)) {
            List<String> friendList = new ArrayList<>();
            if (waitingGoal.getGoalType() == GoalType.GROUP) {
                for (MemberWaitingGoal friend : friends) {
                    if (friend.getMember() != currentMember) {
                        friendList.add(friend.getMember().getNickname());
                        alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.GROUP, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                                waitingGoal.getId(), GROUP, AlarmDetailType.accept, currentMember.getNickname(), friend.getMember()));
                    }
                }
            } else if (waitingGoal.getGoalType() == GoalType.CHALLENGE) {
                for (MemberWaitingGoal friend : friends) {
                    if (friend.getMember() != currentMember) {
                        friendList.add(friend.getMember().getNickname());
                        alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.CHALLENGE, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                                waitingGoal.getId(), CHALLENGE, AlarmDetailType.accept, currentMember.getNickname(), friend.getMember()));
                    }
                }
            }
            alarmRepository.delete(alarm);
        }

        // 전체 수락 후 마지막 수락
        else if (checkAccepted(friends)) {
            List<String> friendList = new ArrayList<>();
            if (waitingGoal.getGoalType() == GoalType.GROUP) {
                for (MemberWaitingGoal friend : friends) {
                    if (friend.getMember() != currentMember) {
                        friendList.add(friend.getMember().getNickname());
                        alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.GROUP, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                                waitingGoal.getId(), GROUP, AlarmDetailType.create, currentMember.getNickname(), friend.getMember()));
                    }
                }
                // GroupGoal 생성
                CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto(waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList);
                groupGoalService.save(createGroupRequestDto, currentMember);
                waitingGoalRepository.delete(waitingGoal);
            } else if (waitingGoal.getGoalType() == GoalType.CHALLENGE) {
                for (MemberWaitingGoal friend : friends) {
                    if (friend.getMember() != currentMember) {
                        friendList.add(friend.getMember().getNickname());
                        alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.CHALLENGE, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                                waitingGoal.getId(), CHALLENGE, AlarmDetailType.create, currentMember.getNickname(), friend.getMember()));
                    }
                }
                // ChallengeGoal 생성
                CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto(waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList);
                challengeGoalService.save(createChallengeRequestDto, currentMember);
                waitingGoalRepository.delete(waitingGoal);
            }
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
    public ResponseEntity<String> postGoalRefuseAlarm(Member currentMember, Long alarmId) {
        Alarm alarm = alarmRepository.findAlarmById(alarmId);
        WaitingGoal waitingGoal = waitingGoalRepository.findWaitingGoalById(alarm.getWaitingGoalId());
        List<MemberWaitingGoal> friends = memberWaitingGoalRepository.findMemberWaitingGoalsByWaitingGoal(waitingGoal);

        List<String> friendList = new ArrayList<>();
        if (waitingGoal.getGoalType() == GoalType.GROUP) {
            for (MemberWaitingGoal friend : friends) {
                friendList.add(friend.getMember().getNickname());
                alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.GROUP, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                        waitingGoal.getId(), GROUP, AlarmDetailType.boom, currentMember.getNickname(), friend.getMember()));
            }
        } else if (waitingGoal.getGoalType() == GoalType.CHALLENGE) {
            for (MemberWaitingGoal friend : friends) {
                friendList.add(friend.getMember().getNickname());
                alarmRepository.save(GoalAlarmSaveDto.goalToEntity(new GoalAlarmRequestDto(GoalType.CHALLENGE, waitingGoal.getWaitingGoalName(), waitingGoal.getWaitingGoalAmount(), friendList),
                        waitingGoal.getId(), CHALLENGE, AlarmDetailType.boom, currentMember.getNickname(), friend.getMember()));
            }
        }
        alarmRepository.delete(alarm);
        waitingGoalRepository.delete(waitingGoal);

        return ResponseEntity.ok().body("해부자 거절 완료");
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteAlarm(Member currentMember, Long alarmId) {
        if (alarmRepository.findById(alarmId).isEmpty()) { throw new AlarmErrorException("해당 알람이 없습니다."); }

        Member member = alarmRepository.findById(alarmId).get().getMember();

        if (member.getClass().isInstance(currentMember)) {
            alarmRepository.deleteById(alarmId);

            return ResponseEntity.ok().body("알람이 삭제되었습니다.");
        }

        throw new AlarmErrorException("알람에 해당하는 사용자가 아닙니다.");
    }

}
