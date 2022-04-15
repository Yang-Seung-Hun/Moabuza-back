//package com.project.moabuja.service;
//
//import com.project.moabuja.domain.alarm.Alarm;
//import com.project.moabuja.domain.alarm.AlarmDetailType;
//import com.project.moabuja.domain.goal.GoalType;
//import com.project.moabuja.domain.member.Hero;
//import com.project.moabuja.domain.member.Member;
//import com.project.moabuja.dto.Msg;
//import com.project.moabuja.dto.request.alarm.FriendAlarmDto;
//import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
//import com.project.moabuja.dto.response.alarm.FriendAlarmResponseDto;
//import com.project.moabuja.dto.response.alarm.GoalAlarmResponseDto;
//import com.project.moabuja.repository.AlarmRepository;
//import com.project.moabuja.repository.MemberRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.project.moabuja.dto.ResponseMsg.AlarmDelete;
//
//@SpringBootTest
//@Rollback(value = true)
//@Transactional
//public class AlarmServiceImplTest {
//
//    @Autowired private AlarmService alarmService;
//    @Autowired private AlarmRepository alarmRepository;
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private FriendService friendService;
//    @Autowired private GroupGoalService groupGoalService;
//    @Autowired private ChallengeGoalService challengeGoalService;
//
//    @Test
//    @DisplayName("친구 알람 조회")
//    void friendAlarmList() {
//        // given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        Member friend3 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.tanni);
//        Member savedFriend3 = memberRepository.save(friend3);
//
//        FriendAlarmDto friendAlarmDto1 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedFriend1).friendNickname(savedMember1.getNickname()).build();
//        friendService.postFriend(friendAlarmDto1, savedFriend1);
//        FriendAlarmDto friendAlarmDto2 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedMember1).friendNickname(savedFriend2.getNickname()).build();
//        friendService.postFriend(friendAlarmDto2, savedMember1);
//        FriendAlarmDto friendAlarmDto3 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedMember1).friendNickname(savedFriend3.getNickname()).build();
//        friendService.postFriend(friendAlarmDto3, savedMember1);
//
//        List<Alarm> alarmList1 = alarmRepository.findAllByMember(savedFriend2);
//        List<Alarm> alarmList2 = alarmRepository.findAllByMember(savedFriend3);
//
//        friendService.postFriendAccept(savedFriend2, alarmList1.get(0).getId());
//        friendService.postFriendRefuse(savedFriend3, alarmList2.get(0).getId());
//
//        // when
//        ResponseEntity<List<FriendAlarmResponseDto>> alarmResponse = alarmService.getFriendAlarm(savedMember1);
//
//        // then
//        Assertions.assertThat(alarmResponse.getBody().get(2).getAlarmDetailType()).isEqualTo(AlarmDetailType.request);
//        Assertions.assertThat(alarmResponse.getBody().get(2).getFriendNickname()).isEqualTo(savedFriend1.getNickname());
//        Assertions.assertThat(alarmResponse.getBody().get(1).getAlarmDetailType()).isEqualTo(AlarmDetailType.accept);
//        Assertions.assertThat(alarmResponse.getBody().get(1).getFriendNickname()).isEqualTo(savedFriend2.getNickname());
//        Assertions.assertThat(alarmResponse.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.refuse);
//        Assertions.assertThat(alarmResponse.getBody().get(0).getFriendNickname()).isEqualTo(savedFriend3.getNickname());
//    }
//
//    @Test
//    @DisplayName("같이해부자 알람 조회 1")
//    void groupGoalAlarmList1() {
//        // given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friendList = new ArrayList<>(Arrays.asList("nickname1", "nickname3"));
//
//        GoalAlarmRequestDto goalAlarmRequestDto = GoalAlarmRequestDto.builder().goalType(GoalType.GROUP).goalName("같이모으기").goalAmount(10000).friendNickname(friendList).build();
//
//        groupGoalService.postGroup(savedFriend1, goalAlarmRequestDto);
//
//        // when
//        ResponseEntity<List<GoalAlarmResponseDto>> alarmResponse = alarmService.getGroupGoalAlarm(savedMember1);
//
//        Alarm alarmTemp = alarmRepository.findAllByMember(savedFriend2).get(0);
//        groupGoalService.postGroupAccept(savedFriend2, alarmTemp.getId());
//
//        ResponseEntity<List<GoalAlarmResponseDto>> alarmResponse2 = alarmService.getGroupGoalAlarm(savedMember1);
//
//        // then
//        Assertions.assertThat(alarmResponse.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.invite);
//        Assertions.assertThat(alarmResponse.getBody().get(0).getFriendNickname()).isEqualTo(savedFriend1.getNickname());
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalName()).isEqualTo("같이모으기");
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalAmount()).isEqualTo(10000);
//
//        Assertions.assertThat(alarmResponse2.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.accept);
//        Assertions.assertThat(alarmResponse2.getBody().get(0).getFriendNickname()).isEqualTo(savedFriend2.getNickname());
//    }
//
//    @Test
//    @DisplayName("같이해부자 알람 조회 2")
//    void groupGoalAlarmList2() {
//        // given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friendList = new ArrayList<>();
//        friendList.add(savedMember1.getNickname());
//        friendList.add(savedFriend2.getNickname());
//
//        GoalAlarmRequestDto goalAlarmRequestDto = GoalAlarmRequestDto.builder().goalType(GoalType.GROUP).goalName("같이모으기").goalAmount(10000).friendNickname(friendList).build();
//        groupGoalService.postGroup(savedFriend1, goalAlarmRequestDto);
//
//        Alarm alarmTemp = alarmRepository.findAllByMember(savedFriend2).get(0);
//        groupGoalService.postGroupAccept(savedFriend2, alarmTemp.getId());
//
//        Alarm alarmTemp2 = alarmRepository.findAllByMember(savedMember1).get(0);
//        groupGoalService.postGroupAccept(savedMember1, alarmTemp2.getId());
//
//        // when
//        ResponseEntity<List<GoalAlarmResponseDto>> alarmResponse = alarmService.getGroupGoalAlarm(savedMember1);
//
//        // then
//        Assertions.assertThat(alarmResponse.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.create);
//        Assertions.assertThat(alarmResponse.getBody().get(0).getFriendNickname()).isEqualTo(savedMember1.getNickname());
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalName()).isEqualTo("같이모으기");
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalAmount()).isEqualTo(10000);
//    }
//
//    @Test
//    @DisplayName("같이해부자 알람 조회 3")
//    void groupGoalAlarmList3() {
//        // given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friendList = new ArrayList<>(Arrays.asList("nickname1", "nickname3"));
//
//        GoalAlarmRequestDto goalAlarmRequestDto = GoalAlarmRequestDto.builder().goalType(GoalType.GROUP).goalName("같이모으기").goalAmount(10000).friendNickname(friendList).build();
//        groupGoalService.postGroup(savedFriend1, goalAlarmRequestDto);
//
//        Alarm alarmTemp = alarmRepository.findAllByMember(savedMember1).get(0);
//        groupGoalService.postGroupRefuse(savedMember1, alarmTemp.getId());
//
//        // when
//        ResponseEntity<List<GoalAlarmResponseDto>> alarmResponse = alarmService.getGroupGoalAlarm(savedMember1);
//
//        // then
//        Assertions.assertThat(alarmResponse.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.boom);
//        Assertions.assertThat(alarmResponse.getBody().get(0).getFriendNickname()).isEqualTo(savedMember1.getNickname());
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalName()).isEqualTo("같이모으기");
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalAmount()).isEqualTo(10000);
//    }
//
//    @Test
//    @DisplayName("도전해부자 알람 조회")
//    void challengeGoalAlarmList() {
//        // given
//        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedMember2 = memberRepository.save(member2);
//
//        Member friend1 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        List<String> friendList = new ArrayList<>(Arrays.asList("nickname2"));
//
//        GoalAlarmRequestDto goalAlarmRequestDto1 = GoalAlarmRequestDto.builder().goalType(GoalType.CHALLENGE).goalName("도전모으기").goalAmount(10000).friendNickname(null).build();
//        GoalAlarmRequestDto goalAlarmRequestDto2 = GoalAlarmRequestDto.builder().goalType(GoalType.CHALLENGE).goalName("도전모으기1").goalAmount(20000).friendNickname(friendList).build();
//
//        challengeGoalService.postChallenge(savedMember1, goalAlarmRequestDto1);
//        challengeGoalService.postChallenge(savedFriend1, goalAlarmRequestDto2);
//
//        // when
//        ResponseEntity<List<GoalAlarmResponseDto>> alarmResponse = alarmService.getChallengeGoalAlarm(savedMember1);
//
//        ResponseEntity<List<GoalAlarmResponseDto>> alarmResponse2 = alarmService.getChallengeGoalAlarm(savedMember2);
//
//        Alarm alarmTemp2 = alarmRepository.findAllByMember(savedMember2).get(0);
//        challengeGoalService.postChallengeRefuse(savedMember2, alarmTemp2.getId());
//
//        ResponseEntity<List<GoalAlarmResponseDto>> alarmResponse3 = alarmService.getChallengeGoalAlarm(savedMember2);
//
//        // then
//        Assertions.assertThat(alarmResponse.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.create);
//        Assertions.assertThat(alarmResponse.getBody().get(0).getFriendNickname()).isEqualTo(savedMember1.getNickname());
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalName()).isEqualTo("도전모으기");
//        Assertions.assertThat(alarmResponse.getBody().get(0).getGoalAmount()).isEqualTo(10000);
//
//        Assertions.assertThat(alarmResponse2.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.invite);
//        Assertions.assertThat(alarmResponse2.getBody().get(0).getFriendNickname()).isEqualTo(savedFriend1.getNickname());
//        Assertions.assertThat(alarmResponse2.getBody().get(0).getGoalName()).isEqualTo("도전모으기1");
//        Assertions.assertThat(alarmResponse2.getBody().get(0).getGoalAmount()).isEqualTo(20000);
//
//        Assertions.assertThat(alarmResponse3.getBody().get(0).getAlarmDetailType()).isEqualTo(AlarmDetailType.boom);
//        Assertions.assertThat(alarmResponse3.getBody().get(0).getFriendNickname()).isEqualTo(savedMember2.getNickname());
//    }
//
//    @Test
//    @DisplayName("알람 삭제 기능")
//    void deleteAlarm() {
//        // given
//        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedMember2 = memberRepository.save(member2);
//
//        Member member3 = new Member("123460", 123460L, "nickname3", "email3@naver.com", Hero.tongki);
//        Member savedMember3 = memberRepository.save(member3);
//
//        Member friend1 = new Member("123458", 123458L, "nickname4", "email4@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123459", 123459L, "nickname5", "email5@naver.com", Hero.tanni);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        FriendAlarmDto friendAlarmDto1 = FriendAlarmDto.builder().alarmDetailType(AlarmDetailType.request).member(savedFriend1).friendNickname(savedFriend2.getNickname()).build();
//        friendService.postFriend(friendAlarmDto1, savedFriend1);
//        Alarm alarmTemp = alarmRepository.findAllByMember(savedFriend2).get(0);
//        friendService.postFriendAccept(savedFriend2, alarmTemp.getId());
//
//        GoalAlarmRequestDto goalAlarmRequestDto1 = GoalAlarmRequestDto.builder().goalType(GoalType.CHALLENGE).goalName("도전모으기").goalAmount(10000).friendNickname(null).build();
//        challengeGoalService.postChallenge(savedMember1, goalAlarmRequestDto1);
//
//        List<String> friendList = new ArrayList<>(Arrays.asList(savedMember3.getNickname()));
//        GoalAlarmRequestDto goalAlarmRequestDto2 = GoalAlarmRequestDto.builder().goalType(GoalType.GROUP).goalName("같이모으기").goalAmount(20000).friendNickname(friendList).build();
//        groupGoalService.postGroup(savedMember2, goalAlarmRequestDto2);
//        Alarm alarmTemp2 = alarmRepository.findAllByMember(savedMember3).get(0);
//        groupGoalService.postGroupRefuse(savedMember3, alarmTemp2.getId());
//
//        // when
//        Alarm alarmTest1 = alarmRepository.findAllByMember(savedFriend1).get(0);
//        ResponseEntity<Msg> alarmResponse1 = alarmService.deleteAlarm(savedFriend1, alarmTest1.getId());
//        List<Alarm> alarmList1 = alarmRepository.findAllByMember(savedFriend1);
//
//        Alarm alarmTest2 = alarmRepository.findAllByMember(savedMember1).get(0);
//        ResponseEntity<Msg> alarmResponse2 = alarmService.deleteAlarm(savedMember1, alarmTest2.getId());
//        List<Alarm> alarmList2 = alarmRepository.findAllByMember(savedMember1);
//
//        Alarm alarmTest3 = alarmRepository.findAllByMember(savedMember2).get(0);
//        ResponseEntity<Msg> alarmResponse3 = alarmService.deleteAlarm(savedMember2, alarmTest3.getId());
//        List<Alarm> alarmList3 = alarmRepository.findAllByMember(savedMember2);
//
//
//        // then
//        Assertions.assertThat(alarmTest1.getAlarmDetailType()).isEqualTo(AlarmDetailType.accept);
//        Assertions.assertThat(alarmResponse1.getBody().getMsg()).isEqualTo(AlarmDelete.getMsg());
//        Assertions.assertThat(alarmList1).isEmpty();
//
//        Assertions.assertThat(alarmTest2.getAlarmDetailType()).isEqualTo(AlarmDetailType.create);
//        Assertions.assertThat(alarmResponse2.getBody().getMsg()).isEqualTo(AlarmDelete.getMsg());
//        Assertions.assertThat(alarmList2).isEmpty();
//
//        Assertions.assertThat(alarmTest3.getAlarmDetailType()).isEqualTo(AlarmDetailType.boom);
//        Assertions.assertThat(alarmResponse3.getBody().getMsg()).isEqualTo(AlarmDelete.getMsg());
//        Assertions.assertThat(alarmList3).isEmpty();
//    }
//
//}
