package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.model.FriendAcceptResponse;
import com.project.moabuja.model.FriendDeleteResponse;
import com.project.moabuja.model.FriendPostResponse;
import com.project.moabuja.model.FriendRefuseResponse;
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
    public ResponseEntity<FriendSearchResponseDto> searchFriend(FriendRequestDto friendRequestDto) {
        Optional<Member> friend = memberRepository.findMemberByNickname(friendRequestDto.getFriendNickname());
        if (friend.isPresent()) {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(true, friend.get().getNickname(), friend.get().getHero());
            return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
        } else {
            FriendSearchResponseDto friendSearchResponseDto = new FriendSearchResponseDto(false, null, null);
            return new ResponseEntity<>(friendSearchResponseDto, HttpStatus.OK);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<FriendPostResponse> postFriend(FriendAlarmDto friendAlarmDto, Member currentMember) {
        Member friend = Optional
                .of(memberRepository.findMemberByNickname(friendAlarmDto.getFriendNickname())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.request, friend, currentMember.getNickname()));

        return new ResponseEntity<>(new FriendPostResponse(), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<FriendAcceptResponse> postFriendAccept(Member currentMember, Long alarmId) {
        Alarm alarm = Optional
                .of(alarmRepository.findById(alarmId)).get()
                .orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
        Member friend = Optional
                .of(memberRepository.findMemberByNickname(alarm.getFriendNickname())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        save(currentMember, friend);

        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.accept, friend, currentMember.getNickname()));

        alarmRepository.delete(alarm);

        return new ResponseEntity<>(new FriendAcceptResponse(), HttpStatus.OK);
    }

    @Transactional
    @Override
    public void save(Member currentMember, Member friend) {
        friendRepository.save(new Friend(currentMember, friend));
        friendRepository.save(new Friend(friend, currentMember));
    }

    @Transactional
    @Override
    public ResponseEntity<FriendRefuseResponse> postFriendRefuse(Member currentMember, Long alarmId) {
        Alarm alarm = Optional
                .of(alarmRepository.findById(alarmId)).get()
                .orElseThrow(() -> new ErrorException(ALARM_NOT_EXIST));
        Member friend = Optional
                .of(memberRepository.findMemberByNickname(alarm.getFriendNickname())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.refuse, friend, currentMember.getNickname()));

        alarmRepository.delete(alarm);

        return new ResponseEntity<>(new FriendRefuseResponse(), HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<FriendDeleteResponse> deleteFriend(Member currentMember, FriendRequestDto friendRequestDto) {
        Member friend = Optional
                .of(memberRepository.findMemberByNickname(friendRequestDto.getFriendNickname())).get()
                .orElseThrow(() -> new ErrorException(MEMBER_NOT_FOUND));

        friendRepository.delete(friendRepository.findByMemberAndFriend(currentMember, friend));
        friendRepository.delete(friendRepository.findByMemberAndFriend(friend, currentMember));

        return new ResponseEntity<>(new FriendDeleteResponse(), HttpStatus.OK);
    }
}
