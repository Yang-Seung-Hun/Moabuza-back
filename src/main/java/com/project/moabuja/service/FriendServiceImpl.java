package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.friend.FriendStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.moabuja.domain.friend.FriendStatus.*;
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
    public ResponseEntity<FriendListResponseDto> listFriend(Member currentMemberTemp) {
        Member currentMember = Optional
                .of(memberRepository.findById(currentMemberTemp.getId())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        List<Friend> friendList = friendRepository.findFriendsByMember(currentMember);
        List<Friend> friendList2 = friendRepository.findFriendsByFriend(currentMember);

        List<FriendListDto> waitingFriendListDto = new ArrayList<>();
        for (Friend friend : friendList) {
            if (friendCheck(friend.getMember(), friend.getFriend()).equals(WAITING)) {
                waitingFriendListDto.add(new FriendListDto(friend.getFriend().getNickname(), friend.getFriend().getHero()));
            }
        }
        for (Friend friend : friendList2) {
            if (friendCheck(friend.getMember(), friend.getFriend()).equals(WAITING)) {
                waitingFriendListDto.add(new FriendListDto(friend.getMember().getNickname(), friend.getMember().getHero()));
            }
        }

        List<FriendListDto> friendListDto = new ArrayList<>();
        for (Friend friend : friendList) {
            if (friendCheck(friend.getMember(), friend.getFriend()).equals(FRIEND)) {
                friendListDto.add(new FriendListDto(friend.getFriend().getNickname(), friend.getFriend().getHero()));
            }
        }

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
        if (friendCheck(currentMember, friend.get()).equals(FRIEND)) {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(null, null, FriendShipExist.getMsg());
            return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
        }
        if (friendCheck(currentMember, friend.get()).equals(WAITING)) {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(null, null, FriendPostValid.getMsg());
            return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
        }

        FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(friend.get().getNickname(), friend.get().getHero(), FriendSerchOK.getMsg());
        return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postFriend(FriendAlarmDto friendAlarmDto, Member currentMemberTemp) {
        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        Optional<Member> friend = Optional.ofNullable(memberRepository.findMemberByNickname(friendAlarmDto.getFriendNickname())).get();
        if (friend.isEmpty()) {
            return new ResponseEntity<>(new Msg(FriendNotExist.getMsg()), HttpStatus.OK);
        }
        if (friendCheck(currentMember, friend.get()).equals(FRIEND)) {
            return new ResponseEntity<>(new Msg(FriendShipExist.getMsg()), HttpStatus.OK);
        }
        if (friendCheck(currentMember, friend.get()).equals(WAITING)) {
            return new ResponseEntity<>(new Msg(FriendPostValid.getMsg()), HttpStatus.OK);
        }
        save(currentMember, friend.get());
        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.request, friend.get(), currentMember.getNickname()));
        return new ResponseEntity<>(new Msg(FriendPost.getMsg()), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postFriendAccept(Member currentMemberTemp, Long alarmId) {
        Alarm alarm = Optional.of(alarmRepository.findById(alarmId)).get().orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        Member friend = Optional.of(memberRepository.findMemberByNickname(alarm.getFriendNickname())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        save(currentMember, friend);

        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.accept, friend, currentMember.getNickname()));

        alarmRepository.delete(alarm);

        return new ResponseEntity<>(new Msg(FriendAccept.getMsg()), HttpStatus.OK);
    }

    @Transactional
    @Override
    public void save(Member currentMember, Member friend) {
        friendRepository.save(new Friend(currentMember, friend));
    }

    @Transactional
    @Override
    public ResponseEntity<Msg> postFriendRefuse(Member currentMemberTemp, Long alarmId) {
        Member currentMember = Optional.of(memberRepository.findById(currentMemberTemp.getId())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));
        Alarm alarm = Optional.of(alarmRepository.findById(alarmId)).get().orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
        Member friend = Optional.of(memberRepository.findMemberByNickname(alarm.getFriendNickname())).get().orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        Friend friendship = friendRepository.findByMemberAndFriend(friend, currentMember);
        friendRepository.delete(friendship);

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

    @Transactional
    @Override
    public FriendStatus friendCheck(Member currentMember, Member friend) {
        Optional<Friend> friendship1 = Optional.ofNullable(friendRepository.findByMemberAndFriend(currentMember, friend));
        Optional<Friend> friendship2 = Optional.ofNullable(friendRepository.findByMemberAndFriend(friend, currentMember));

        if (friendship1.isPresent() && friendship2.isPresent()) {
            return FRIEND;
        }
        else if (friendship1.isPresent() || friendship2.isPresent()) {
            return WAITING;
        }
        else { return NOT_FRIEND; }
    }
}
