package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.moabuja.dto.ResponseMsg.*;
import static com.project.moabuja.exception.ErrorCode.ALARM_NOT_EXIST;
import static com.project.moabuja.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public ResponseEntity<FriendListResponseDto> listFriend(Member currentMember) {
        List<Friend> friendList = friendRepository.findFriendsByMember(currentMember);
        List<FriendListDto> friendListDto = friendList.stream().map(friend -> {
            return new FriendListDto(friend.getFriend().getNickname(), friend.getFriend().getHero());
        }).collect(Collectors.toList());

        List<Alarm> waitingFriendList = alarmRepository.findAlarmsByFriendNicknameAndAlarmType(currentMember.getNickname(), AlarmType.FRIEND);
        List<FriendListDto> waitingFriendListDto = waitingFriendList.stream().map(friend -> {
            return new FriendListDto(friend.getMember().getNickname(), friend.getMember().getHero());
        }).collect(Collectors.toList());

        FriendListResponseDto friendListResponseDto = new FriendListResponseDto(waitingFriendListDto, friendListDto);

        return new ResponseEntity<>(friendListResponseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FriendSearchResponseDto> searchFriend(FriendRequestDto friendRequestDto, Member currentMemberTemp) {
        Member currentMember = Optional
                .of(memberRepository.findById(currentMemberTemp.getId())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        Optional<Member> friend = Optional
                .ofNullable(memberRepository.findMemberByNickname(friendRequestDto.getFriendNickname())).get();
        if (friend.isEmpty()) {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(null, null, FriendNotExist.getMsg());
            return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
        }
        Optional<Friend> friendCheck = Optional
                .ofNullable(friendRepository.findByMemberAndFriend(currentMember, friend.get()));
        if (friendCheck.isPresent()) {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(null, null, FriendShipExist.getMsg());
            return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
        }
        Optional<Alarm> friendAlarmCheck = Optional
                .ofNullable(alarmRepository.findAlarmByMemberAndFriendNicknameAndAlarmTypeAndAlarmDetailType(friend.get(), currentMember.getNickname(), AlarmType.FRIEND, AlarmDetailType.request));
        Optional<Alarm> friendAlarmDoubleCheck = Optional
                .ofNullable(alarmRepository.findAlarmByMemberAndFriendNicknameAndAlarmTypeAndAlarmDetailType(currentMember, friend.get().getNickname(), AlarmType.FRIEND, AlarmDetailType.request));
        if (friendAlarmCheck.isPresent() || friendAlarmDoubleCheck.isPresent()) {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(null, null, FriendPostValid.getMsg());
            return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
        }
        FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(friend.get().getNickname(), friend.get().getHero(), FriendSerchOK.getMsg());
        return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postFriend(FriendAlarmDto friendAlarmDto, Member currentMemberTemp) {
        Member currentMember = Optional
                .of(memberRepository.findById(currentMemberTemp.getId())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        Optional<Member> friend = Optional
                .ofNullable(memberRepository.findMemberByNickname(friendAlarmDto.getFriendNickname())).get();
        if (friend.isEmpty()) {
            return new ResponseEntity<>(new Msg(FriendNotExist.getMsg()), HttpStatus.OK);
        }
        Optional<Friend> friendCheck = Optional
                .ofNullable(friendRepository.findByMemberAndFriend(currentMember, friend.get()));
        if (friendCheck.isPresent()) {
            return new ResponseEntity<>(new Msg(FriendShipExist.getMsg()), HttpStatus.OK);
        }
        Optional<Alarm> friendAlarmCheck = Optional
                .ofNullable(alarmRepository.findAlarmByMemberAndFriendNicknameAndAlarmTypeAndAlarmDetailType(friend.get(), currentMember.getNickname(), AlarmType.FRIEND, AlarmDetailType.request));
        Optional<Alarm> friendAlarmDoubleCheck = Optional
                .ofNullable(alarmRepository.findAlarmByMemberAndFriendNicknameAndAlarmTypeAndAlarmDetailType(currentMember, friend.get().getNickname(), AlarmType.FRIEND, AlarmDetailType.request));
        if (friendAlarmCheck.isPresent() && friendAlarmDoubleCheck.isPresent()) {
            return new ResponseEntity<>(new Msg(FriendPostValid.getMsg()), HttpStatus.OK);
        }
        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.request, friend.get(), currentMember.getNickname()));
        return new ResponseEntity<>(new Msg(FriendPost.getMsg()), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postFriendAccept(Member currentMember, Long alarmId) {
        Alarm alarm = Optional
                .of(alarmRepository.findById(alarmId)).get()
                .orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
        Member friend = Optional
                .of(memberRepository.findMemberByNickname(alarm.getFriendNickname())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        save(currentMember, friend);

        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.accept, friend, currentMember.getNickname()));

        alarmRepository.delete(alarm);

        return new ResponseEntity<>(new Msg(FriendAccept.getMsg()), HttpStatus.OK);
    }

    @Transactional
    @Override
    public void save(Member currentMember, Member friend) {
        friendRepository.save(new Friend(currentMember, friend));
        friendRepository.save(new Friend(friend, currentMember));
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postFriendRefuse(Member currentMember, Long alarmId) {
        Alarm alarm = Optional
                .of(alarmRepository.findById(alarmId)).get()
                .orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
        Member friend = Optional
                .of(memberRepository.findMemberByNickname(alarm.getFriendNickname())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.refuse, friend, currentMember.getNickname()));

        alarmRepository.delete(alarm);

        return new ResponseEntity<>(new Msg(FriendRefuse.getMsg()), HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> deleteFriend(Member currentMember, FriendRequestDto friendRequestDto) {
        Member friend = Optional
                .of(memberRepository.findMemberByNickname(friendRequestDto.getFriendNickname())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        friendRepository.delete(friendRepository.findByMemberAndFriend(currentMember, friend));
        friendRepository.delete(friendRepository.findByMemberAndFriend(friend, currentMember));

        return new ResponseEntity<>(new Msg(FriendDelete.getMsg()), HttpStatus.OK);
    }
}
