package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.alarm.AlarmDetailType;
import com.project.moabuja.domain.alarm.AlarmType;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.friend.FriendStatus;
import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.Msg;
import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
import com.project.moabuja.dto.request.friend.FriendRequestDto;
import com.project.moabuja.dto.response.friend.FriendListResponseDto;
import com.project.moabuja.dto.response.friend.FriendSearchResponseDto;
import com.project.moabuja.repository.AlarmRepository;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.project.moabuja.dto.ResponseMsg.*;

@SpringBootTest
@Rollback(value = true)
@Transactional
public class FriendServiceImplTest {

    @Autowired private FriendService friendService;
    @Autowired private FriendRepository friendRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private AlarmRepository alarmRepository;
    @Autowired private EntityManager em;

    @Test
    @DisplayName("친구 목록 조회")
    void friendList() {
        // given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        Member friend3 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.tanni);
        Member savedFriend3 = memberRepository.save(friend3);

        Member friend4 = new Member("123460", 123460L, "nickname5", "email5@naver.com", Hero.tanni);
        Member savedFriend4 = memberRepository.save(friend4);

        Friend relate1 = new Friend(savedMember1,savedFriend1);
        Friend relate2 = new Friend(savedFriend1,savedMember1);
        friendRepository.save(relate1);
        friendRepository.save(relate2);


        Friend relate3 = new Friend(savedMember1,savedFriend2);
        Friend relate4 = new Friend(savedFriend2,savedMember1);
        friendRepository.save(relate3);
        friendRepository.save(relate4);

        Friend relate5 = new Friend(savedMember1,savedFriend3);
        friendRepository.save(relate5);

        Friend relate6 = new Friend(savedFriend4,savedMember1);
        friendRepository.save(relate6);

        // when
        ResponseEntity<FriendListResponseDto> friendListTemp = friendService.listFriend(savedMember1);
        FriendListResponseDto allFriends = friendListTemp.getBody();

        String friendNickname1 = null;
        String friendNickname2 = null;
        String friendNickname3 = null;
        String friendNickname4 = null;

        if (allFriends != null) {
            friendNickname1 = allFriends.getFriendListDto().get(0).getNickname();
            friendNickname2 = allFriends.getFriendListDto().get(1).getNickname();
            friendNickname3 = allFriends.getWaitingFriendListDto().get(0).getNickname();
            friendNickname4 = allFriends.getWaitingFriendListDto().get(1).getNickname();
        }

        //then
        if (allFriends != null) {
            Assertions.assertThat(allFriends.getFriendListDto().size()).isEqualTo(2);
            Assertions.assertThat(allFriends.getWaitingFriendListDto().size()).isEqualTo(2);
            Assertions.assertThat(friendNickname1).isEqualTo("nickname2");
            Assertions.assertThat(friendNickname2).isEqualTo("nickname3");
            Assertions.assertThat(friendNickname3).isEqualTo("nickname4");
            Assertions.assertThat(friendNickname4).isEqualTo("nickname5");
        }

    }

    @Test
    @DisplayName("친구 추가 시 멤버 검색 기능")
    void friendSearch() {
        // given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        Member friend3 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.tanni);
        Member savedFriend3 = memberRepository.save(friend3);

        Friend relate1 = new Friend(savedMember1, savedFriend1);
        Friend relate2 = new Friend(savedFriend1, savedMember1);
        friendRepository.save(relate1);
        friendRepository.save(relate2);


        Friend relate3 = new Friend(savedFriend2, savedMember1);
        friendRepository.save(relate3);

        // when
        FriendRequestDto friendRequestDto1 = new FriendRequestDto("nickname2");
        FriendRequestDto friendRequestDto2 = new FriendRequestDto("nickname3");
        FriendRequestDto friendRequestDto3 = new FriendRequestDto("nullname");
        FriendRequestDto friendRequestDto4 = new FriendRequestDto("nickname4");
        ResponseEntity<FriendSearchResponseDto> friendTemp1 = friendService.searchFriend(friendRequestDto1, savedMember1);
        FriendSearchResponseDto friendResponse1 = friendTemp1.getBody();
        ResponseEntity<FriendSearchResponseDto> friendTemp2 = friendService.searchFriend(friendRequestDto2, savedMember1);
        FriendSearchResponseDto friendResponse2 = friendTemp2.getBody();
        ResponseEntity<FriendSearchResponseDto> friendTemp3 = friendService.searchFriend(friendRequestDto3, savedMember1);
        FriendSearchResponseDto friendResponse3 = friendTemp3.getBody();
        ResponseEntity<FriendSearchResponseDto> friendTemp4 = friendService.searchFriend(friendRequestDto4, savedMember1);
        FriendSearchResponseDto friendResponse4 = friendTemp4.getBody();

        // then
        Assertions.assertThat(friendResponse1.getMsg()).isEqualTo(FriendShipExist.getMsg());
        Assertions.assertThat(friendResponse2.getMsg()).isEqualTo(FriendPostValid.getMsg());
        Assertions.assertThat(friendResponse3.getMsg()).isEqualTo(FriendNotExist.getMsg());
        Assertions.assertThat(friendResponse4.getNickname()).isEqualTo("nickname4");
    }

    @Test
    @DisplayName("친구 요청 기능")
    void friendPost() {
        // given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        Member friend3 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.tanni);
        Member savedFriend3 = memberRepository.save(friend3);

        Friend relate1 = new Friend(savedMember1, savedFriend1);
        Friend relate2 = new Friend(savedFriend1, savedMember1);
        friendRepository.save(relate1);
        friendRepository.save(relate2);


        Friend relate3 = new Friend(savedFriend2, savedMember1);
        friendRepository.save(relate3);

        // when
        FriendAlarmDto friendAlarmDto1 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedMember1).friendNickname(savedFriend1.getNickname()).build();
        FriendAlarmDto friendAlarmDto2 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedMember1).friendNickname(savedFriend2.getNickname()).build();
        FriendAlarmDto friendAlarmDto3 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedMember1).friendNickname("nullname").build();
        FriendAlarmDto friendAlarmDto4 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedMember1).friendNickname(savedFriend3.getNickname()).build();
        ResponseEntity<Msg> friendResponse1 = friendService.postFriend(friendAlarmDto1, savedMember1);
        ResponseEntity<Msg> friendResponse2 = friendService.postFriend(friendAlarmDto2, savedMember1);
        ResponseEntity<Msg> friendResponse3 = friendService.postFriend(friendAlarmDto3, savedMember1);
        ResponseEntity<Msg> friendResponse4 = friendService.postFriend(friendAlarmDto4, savedMember1);

        List<Alarm> alarmList = alarmRepository.findAllByMember(savedFriend3);

        // then
        Assertions.assertThat(friendResponse1.getBody().getMsg()).isEqualTo(FriendShipExist.getMsg());
        Assertions.assertThat(friendResponse2.getBody().getMsg()).isEqualTo(FriendPostValid.getMsg());
        Assertions.assertThat(friendResponse3.getBody().getMsg()).isEqualTo(FriendNotExist.getMsg());
        Assertions.assertThat(friendResponse4.getBody().getMsg()).isEqualTo(FriendPost.getMsg());
        Assertions.assertThat(alarmList.get(0).getAlarmType()).isEqualTo(AlarmType.FRIEND);
        Assertions.assertThat(alarmList.get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.request);
        Assertions.assertThat(alarmList.get(0).getFriendNickname()).isEqualTo(savedMember1.getNickname());
        Assertions.assertThat(alarmList.get(0).getMember()).isEqualTo(savedFriend3);
    }

    @Test
    @DisplayName("친구 수락 기능")
    void friendAccept() {
        // given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Friend relate1 = new Friend(savedMember1, savedFriend1);
        friendRepository.save(relate1);
        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.request, savedFriend1, savedMember1.getNickname()));

        // when
        Member postedFriend = memberRepository.findMemberByNickname(alarmRepository.findById(1L).get().getFriendNickname()).get();

        ResponseEntity<Msg> friendResponse = friendService.postFriendAccept(savedFriend1, 1L);

        List<Alarm> alarmList1 = alarmRepository.findAllByMember(savedFriend1);
        List<Alarm> alarmList2 = alarmRepository.findAllByMember(savedMember1);

        FriendStatus friendStatus = friendService.friendCheck(savedMember1, savedFriend1);

        // then
        Assertions.assertThat(postedFriend).isEqualTo(savedMember1);
        Assertions.assertThat(friendResponse.getBody().getMsg()).isEqualTo(FriendAccept.getMsg());
        Assertions.assertThat(alarmList1.size()).isEqualTo(0);
        Assertions.assertThat(alarmList2.get(0).getAlarmType()).isEqualTo(AlarmType.FRIEND);
        Assertions.assertThat(alarmList2.get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.accept);
        Assertions.assertThat(alarmList2.get(0).getMember()).isEqualTo(savedMember1);
        Assertions.assertThat(alarmList2.get(0).getFriendNickname()).isEqualTo(savedFriend1.getNickname());
        Assertions.assertThat(friendStatus).isEqualTo(FriendStatus.FRIEND);

    }

    @Test
    @DisplayName("친구 거절 기능")
    void friendRefuse() {
        // given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Friend relate1 = new Friend(savedMember1, savedFriend1);
        friendRepository.save(relate1);
        alarmRepository.save(FriendAlarmDto.friendToEntity(AlarmDetailType.request, savedFriend1, savedMember1.getNickname()));

        // when
        Member postedFriend = memberRepository.findMemberByNickname(alarmRepository.findById(1L).get().getFriendNickname()).get();

        ResponseEntity<Msg> friendResponse = friendService.postFriendRefuse(savedFriend1, 1L);

        List<Alarm> alarmList1 = alarmRepository.findAllByMember(savedFriend1);
        List<Alarm> alarmList2 = alarmRepository.findAllByMember(savedMember1);

        FriendStatus friendStatus = friendService.friendCheck(savedMember1, savedFriend1);

        // then
        Assertions.assertThat(postedFriend).isEqualTo(savedMember1);
        Assertions.assertThat(friendResponse.getBody().getMsg()).isEqualTo(FriendRefuse.getMsg());
        Assertions.assertThat(alarmList1.size()).isEqualTo(0);
        Assertions.assertThat(alarmList2.get(0).getAlarmType()).isEqualTo(AlarmType.FRIEND);
        Assertions.assertThat(alarmList2.get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.refuse);
        Assertions.assertThat(alarmList2.get(0).getMember()).isEqualTo(savedMember1);
        Assertions.assertThat(alarmList2.get(0).getFriendNickname()).isEqualTo(savedFriend1.getNickname());
        Assertions.assertThat(friendStatus).isEqualTo(FriendStatus.NOT_FRIEND);
    }


}
