package com.project.moabuja.service;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.goal.WaitingGoal;
import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.goal.CreateGroupResponseDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import com.project.moabuja.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.project.moabuja.domain.alarm.AlarmDetailType.*;
import static com.project.moabuja.domain.goal.GoalType.GROUP;

@SpringBootTest
@Rollback(value = true)
@Transactional
public class GroupGoalServiceImplTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private GroupGoalService groupGoalService;
    @Autowired private GroupGoalRepository groupGoalRepository;
    @Autowired private RecordService recordService;
    @Autowired private RecordRepository recordRepository;
    @Autowired private EntityManager em;
    @Autowired private FriendRepository friendRepository;
    @Autowired private AlarmRepository alarmRepository;
    @Autowired private WaitingGoalRepository waitingGoalRepository;

    @Test
    @DisplayName("같이해부자 생성")
    public void save() {

        //given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));

        CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto("5만원 모으기", 50000, friends);

        //when
        groupGoalService.save(createGroupRequestDto, savedMember1);

        GroupGoal groupGoal = savedMember1.getGroupGoal();
        Optional<GroupGoal> groupByIdTmp = groupGoalRepository.findById(groupGoal.getId());
        GroupGoal groupById = null;
        if (groupByIdTmp.isPresent()) {
            groupById = groupByIdTmp.get();
        }

        //then
        Assertions.assertThat(groupById.getGroupGoalName()).isEqualTo("5만원 모으기");
        Assertions.assertThat(groupById.getGroupGoalAmount()).isEqualTo(50000);
        Assertions.assertThat(groupById.getMembers().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("생성된 group goal 있을때")
    public void getGroupInfo1() {

        //given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        memberRepository.save(friend2);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));

        CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto("10만원 모으기", 100000, friends);
        groupGoalService.save(createGroupRequestDto, savedMember1);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000", "3월용돈", 50000);
        recordService.save(recordRequestDto1, savedMember1);

        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000", "3월용돈", 50000);
        recordService.save(recordRequestDto2, friend1);

        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000", "3월용돈", 50000);
        recordService.save(recordRequestDto3, friend2);

        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.group, "2022-03-06 00:00:00.000", "도전!!", 10000);
        recordService.save(recordRequestDto4, savedMember1);

        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.group, "2022-03-06 00:00:00.000", "도전!!", 20000);
        recordService.save(recordRequestDto5, friend1);

        RecordRequestDto recordRequestDto6 = new RecordRequestDto(RecordType.group, "2022-03-06 00:00:00.000", "도전!!", 30000);
        recordService.save(recordRequestDto6, friend2);

        RecordRequestDto recordRequestDto7 = new RecordRequestDto(RecordType.group, "2022-03-06 00:00:00.000", "도전!!", 30000);
        recordService.save(recordRequestDto7, savedMember1);


        //when
        ResponseEntity<GroupResponseDto> groupInfoTmp = groupGoalService.getGroupInfo(savedMember1);
        GroupResponseDto groupInfo = groupInfoTmp.getBody();

        //then
        Assertions.assertThat(groupInfo.getGoalStatus()).isEqualTo("goal");
        Assertions.assertThat(groupInfo.getGroupName()).isEqualTo("10만원 모으기");
        Assertions.assertThat(groupInfo.getGroupGoalAmount()).isEqualTo(100000);
        Assertions.assertThat(groupInfo.getGroupLists().size()).isEqualTo(4);
        Assertions.assertThat(groupInfo.getGroupMembers().size()).isEqualTo(3);
        Assertions.assertThat(groupInfo.getGroupNowPercent()).isEqualTo(90);
    }

    @Test
    @DisplayName("group goal 수락대기중")
    public void getGroupInfo2() {

        //given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));
        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(GROUP, "20만원 빨리 모으기", 200000, friends);
        groupGoalService.postGroup(savedMember1, goalAlarmRequestDto);

        em.flush();
        em.clear();

        //when
        ResponseEntity<GroupResponseDto> groupInfoTmp1 = groupGoalService.getGroupInfo(savedMember1);
        GroupResponseDto groupInfo1 = groupInfoTmp1.getBody();

        ResponseEntity<GroupResponseDto> groupInfoTmp2 = groupGoalService.getGroupInfo(savedFriend1);
        GroupResponseDto groupInfo2 = groupInfoTmp2.getBody();

        ResponseEntity<GroupResponseDto> groupInfoTmp3 = groupGoalService.getGroupInfo(savedFriend2);
        GroupResponseDto groupInfo3 = groupInfoTmp3.getBody();

        WaitingGoal waitingGoal1 = waitingGoalRepository.findById(groupInfo1.getWaitingGoals().get(0).getId()).get();
        WaitingGoal waitingGoal2 = waitingGoalRepository.findById(groupInfo2.getWaitingGoals().get(0).getId()).get();
        WaitingGoal waitingGoal3 = waitingGoalRepository.findById(groupInfo3.getWaitingGoals().get(0).getId()).get();

        //then
        Assertions.assertThat(groupInfo1.getGoalStatus()).isEqualTo("waiting");
        Assertions.assertThat(groupInfo1.getGroupLists()).isNull();
        Assertions.assertThat(groupInfo1.getGroupMembers()).isNull();
        Assertions.assertThat(groupInfo1.getGroupName()).isNull();
        Assertions.assertThat(groupInfo1.getWaitingGoals().size()).isEqualTo(1);

        Assertions.assertThat(groupInfo2.getGoalStatus()).isEqualTo("waiting");
        Assertions.assertThat(groupInfo2.getGroupLists()).isNull();
        Assertions.assertThat(groupInfo2.getGroupMembers()).isNull();
        Assertions.assertThat(groupInfo2.getGroupName()).isNull();
        Assertions.assertThat(groupInfo2.getWaitingGoals().size()).isEqualTo(1);

        Assertions.assertThat(groupInfo3.getGoalStatus()).isEqualTo("waiting");
        Assertions.assertThat(groupInfo3.getGroupLists()).isNull();
        Assertions.assertThat(groupInfo3.getGroupMembers()).isNull();
        Assertions.assertThat(groupInfo3.getGroupName()).isNull();
        Assertions.assertThat(groupInfo3.getWaitingGoals().size()).isEqualTo(1);

        Assertions.assertThat(waitingGoal1.getWaitingGoalName()).isEqualTo("20만원 빨리 모으기");
        Assertions.assertThat(waitingGoal1.getWaitingGoalAmount()).isEqualTo(200000);
        Assertions.assertThat(waitingGoal2.getWaitingGoalName()).isEqualTo("20만원 빨리 모으기");
        Assertions.assertThat(waitingGoal2.getWaitingGoalAmount()).isEqualTo(200000);
        Assertions.assertThat(waitingGoal3.getWaitingGoalName()).isEqualTo("20만원 빨리 모으기");
        Assertions.assertThat(waitingGoal3.getWaitingGoalAmount()).isEqualTo(200000);
        Assertions.assertThat(waitingGoal1.getMemberWaitingGoals().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("수락 대기, 생성된 group 없음")
    public void getGroupInfo3() {

        //given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        //when
        ResponseEntity<GroupResponseDto> groupInfoTmp1 = groupGoalService.getGroupInfo(savedMember1);
        GroupResponseDto groupInfo1 = groupInfoTmp1.getBody();

        ResponseEntity<GroupResponseDto> groupInfoTmp2 = groupGoalService.getGroupInfo(savedFriend1);
        GroupResponseDto groupInfo2 = groupInfoTmp2.getBody();

        ResponseEntity<GroupResponseDto> groupInfoTmp3 = groupGoalService.getGroupInfo(savedFriend2);
        GroupResponseDto groupInfo3 = groupInfoTmp3.getBody();


        //then
        Assertions.assertThat(groupInfo1.getGoalStatus()).isEqualTo("noGoal");
        Assertions.assertThat(groupInfo1.getGroupLists()).isNull();
        Assertions.assertThat(groupInfo1.getGroupMembers()).isNull();
        Assertions.assertThat(groupInfo1.getGroupName()).isNull();
        Assertions.assertThat(groupInfo1.getWaitingGoals()).isNull();

        Assertions.assertThat(groupInfo2.getGoalStatus()).isEqualTo("noGoal");
        Assertions.assertThat(groupInfo2.getGroupLists()).isNull();
        Assertions.assertThat(groupInfo2.getGroupMembers()).isNull();
        Assertions.assertThat(groupInfo2.getGroupName()).isNull();
        Assertions.assertThat(groupInfo2.getWaitingGoals()).isNull();

        Assertions.assertThat(groupInfo3.getGoalStatus()).isEqualTo("noGoal");
        Assertions.assertThat(groupInfo3.getGroupLists()).isNull();
        Assertions.assertThat(groupInfo3.getGroupMembers()).isNull();
        Assertions.assertThat(groupInfo3.getGroupName()).isNull();
        Assertions.assertThat(groupInfo3.getWaitingGoals()).isNull();
    }

    @Test
    @DisplayName("같이해부자 초대할 친구 목록")
    public void getGroupMemberCandidates() {

        //given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        Member friend3 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.tanni);
        Member savedFriend3 = memberRepository.save(friend3);

        Member friend4 = new Member("123460", 123460L, "nickname5", "email5@naver.com", Hero.bunny);
        Member savedFriend4 = memberRepository.save(friend4);

        Friend relate1 = new Friend(savedMember1,savedFriend1);
        Friend relate2 = new Friend(friend1,savedMember1);
        friendRepository.save(relate1);
        friendRepository.save(relate2);


        Friend relate3 = new Friend(savedMember1,savedFriend2);
        Friend relate4 = new Friend(savedFriend2,savedMember1);
        friendRepository.save(relate3);
        friendRepository.save(relate4);

        Friend relate5 = new Friend(savedMember1,savedFriend3);
        friendRepository.save(relate5);

        List<String> friends = new ArrayList<>(Arrays.asList(savedFriend4.getNickname()));

        CreateGroupRequestDto createGroupRequestDto1 = new CreateGroupRequestDto("10만원 모으기", 100000, friends);
        groupGoalService.save(createGroupRequestDto1, savedFriend1);

        //when
        ResponseEntity<CreateGroupResponseDto> groupMemberCandidatesTmp = groupGoalService.getGroupMemberCandidates(savedMember1);
        CreateGroupResponseDto allFriends = groupMemberCandidatesTmp.getBody();

        boolean canInvite1 = false;
        String nickname1 = null;
        boolean canInvite2 = false;
        String nickname2 = null;
        if (allFriends != null) {
            canInvite1 = allFriends.getGroupMembers().get(0).isGroupMemberCanInvite();
            nickname1 = allFriends.getGroupMembers().get(0).getGroupMemberNickname();

            canInvite2 = allFriends.getGroupMembers().get(1).isGroupMemberCanInvite();
            nickname2 = allFriends.getGroupMembers().get(1).getGroupMemberNickname();
        }

        //then
        if (allFriends != null) {
            Assertions.assertThat(allFriends.getGroupMembers().size()).isEqualTo(2);
            Assertions.assertThat(canInvite1).isFalse();
            Assertions.assertThat(nickname1).isEqualTo(savedFriend1.getNickname());

            Assertions.assertThat(canInvite2).isTrue();
            Assertions.assertThat(nickname2).isEqualTo(savedFriend2.getNickname());
        }
    }

    @Test
    @DisplayName("같이해부자 나오기")
    public void exitGroup() {

        //given
        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));

        CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto("5만원 모으기", 50000, friends);
        groupGoalService.save(createGroupRequestDto, savedMember1);

        //when
        groupGoalService.exitGroup(savedMember1);

        //then
        Assertions.assertThat(savedMember1.getGroupGoal()).isNull();
        Assertions.assertThat(savedFriend1.getGroupGoal()).isNotNull();
        Assertions.assertThat(savedFriend1.getGroupGoal().getGroupGoalName()).isEqualTo("5만원 모으기");
        Assertions.assertThat(savedFriend2.getGroupGoal()).isNotNull();
        Assertions.assertThat(savedFriend2.getGroupGoal().getGroupGoalName()).isEqualTo("5만원 모으기");
    }

    @Test
    @DisplayName("같이해부자 초대 1개")
    public void postGroup1(){

        //given
        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedMember3 = memberRepository.save(member3);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
        //when
        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(GROUP,"100만원 도전",1000000,friends);
        groupGoalService.postGroup(savedMember1,goalAlarmRequestDto);

        List<Alarm> allByMember1 = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember2 = alarmRepository.findAllByMember(savedMember3);

        //then
        Assertions.assertThat(savedMember1.getGroupGoal()).isNull();
        Assertions.assertThat(allByMember1).isNotEmpty();
        Assertions.assertThat(allByMember1.get(0).getAlarmDetailType()).isEqualTo(invite);
        Assertions.assertThat(allByMember2).isNotEmpty();
        Assertions.assertThat(allByMember2.get(0).getAlarmDetailType()).isEqualTo(invite);
    }

    @Test
    @DisplayName("같이해부자 초대 여러개")
    public void postGroup2(){

        //given
        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedMember3 = memberRepository.save(member3);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3"));
        //when
        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(GROUP,"100만원 도전",1000000,friends);
        groupGoalService.postGroup(savedMember1,goalAlarmRequestDto);

        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(GROUP,"10만원 도전",100000,friends2);
        groupGoalService.postGroup(savedMember2,goalAlarmRequestDto2);


        List<Alarm> allByMember1 = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember2 = alarmRepository.findAllByMember(savedMember3);

        //then
        Assertions.assertThat(savedMember1.getGroupGoal()).isNull();
        Assertions.assertThat(allByMember1).isNotEmpty();
        Assertions.assertThat(allByMember1.size()).isEqualTo(1);
        Assertions.assertThat(allByMember1.get(0).getAlarmDetailType()).isEqualTo(invite);

        Assertions.assertThat(allByMember2).isNotEmpty();
        Assertions.assertThat(allByMember2.size()).isEqualTo(2);
        Assertions.assertThat(allByMember2.get(0).getAlarmDetailType()).isEqualTo(invite);
        Assertions.assertThat(allByMember2.get(1).getAlarmDetailType()).isEqualTo(invite);
    }

    @Test
    @DisplayName("같이해부자 부분 수락")
    public void postGroupAccept1(){

        //given
        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedMember3 = memberRepository.save(member3);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));

        //when
        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(GROUP,"100만원 도전",1000000,friends);
        groupGoalService.postGroup(savedMember1,goalAlarmRequestDto);

        List<Alarm> allByMember2Before = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember3Before = alarmRepository.findAllByMember(savedMember3);
        Long alarmId1 = allByMember2Before.get(0).getId();
        Long alarmId2 = allByMember3Before.get(0).getId();

        //member2의 invite알람 삭제, member1 이랑 member2한테 accept 알람 보냄
        groupGoalService.postGroupAccept(savedMember2, alarmId1);

        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);

        //then
        Assertions.assertThat(allByMember1After.size()).isEqualTo(1);//member2의 수락
        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(accept);
        Assertions.assertThat(allByMember2After.size()).isEqualTo(0);
        Assertions.assertThat(allByMember3After.size()).isEqualTo(2);//member2의 수락, member1의 초대
        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(invite);
        Assertions.assertThat(allByMember3After.get(1).getAlarmDetailType()).isEqualTo(accept);
    }

    @Test
    @DisplayName("같이해부자 전체 수락")
    public void postGroupAccept2(){

        //given
        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedMember3 = memberRepository.save(member3);

        Member member4 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.bunny);
        Member savedMember4 = memberRepository.save(member4);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3"));
        List<String> friends3 = new ArrayList<>(Arrays.asList("nickname4"));

        //member2,3한테 invite 알람
        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(GROUP,"100만원 도전",1000000,friends);
        groupGoalService.postGroup(savedMember1,goalAlarmRequestDto);

        //member3한테 invite 알람
        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(GROUP,"300만원 도전",3000000,friends2);
        groupGoalService.postGroup(savedMember2,goalAlarmRequestDto2);

        GoalAlarmRequestDto goalAlarmRequestDto3 = new GoalAlarmRequestDto(GROUP,"300만원 도전",3000000,friends3);
        groupGoalService.postGroup(savedMember3,goalAlarmRequestDto3);

        List<Alarm> allByMember2Before = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember3Before = alarmRepository.findAllByMember(savedMember3);
        Long alarmId1 = allByMember2Before.get(0).getId();
        Long alarmId2 = allByMember3Before.get(0).getId();
        Long alarmId3 = allByMember3Before.get(1).getId();

        //when
        //member2의 invite알람 삭제, member1 이랑 member3한테 accept 알람 보냄
        groupGoalService.postGroupAccept(savedMember2, alarmId1);
        //member3의 invite알람 삭제, member1 이랑 member2한테 accept 알람 보냄, member1,2,3한테 create알람 보냄, member2,3한테 대기 challenge boom 알람
        groupGoalService.postGroupAccept(savedMember3, alarmId2);

        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);

        //then
        Assertions.assertThat(allByMember1After.size()).isEqualTo(2);
        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(accept);
        Assertions.assertThat(allByMember1After.get(1).getAlarmDetailType()).isEqualTo(create);
        Assertions.assertThat(allByMember2After.size()).isEqualTo(2);
        Assertions.assertThat(allByMember2After.get(0).getAlarmDetailType()).isEqualTo(create);
        Assertions.assertThat(allByMember2After.get(1).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember3After.size()).isEqualTo(4);
        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(accept);
        Assertions.assertThat(allByMember3After.get(1).getAlarmDetailType()).isEqualTo(create);
        Assertions.assertThat(allByMember3After.get(2).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember3After.get(3).getAlarmDetailType()).isEqualTo(boom);
    }

    @Test
    @DisplayName("같이해부자 거절")
    public void postGroupRefuse(){
        //given
        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedMember3 = memberRepository.save(member3);

        Member member4 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.bunny);
        Member savedMember4 = memberRepository.save(member4);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3", "nickname4"));

        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(GROUP,"100만원 도전",1000000,friends);
        groupGoalService.postGroup(savedMember1,goalAlarmRequestDto);

        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(GROUP,"200만원 도전",2000000,friends2);
        groupGoalService.postGroup(savedMember2,goalAlarmRequestDto2);

        //when
        List<Alarm> allByMember2Before = alarmRepository.findAllByMember(savedMember2);
        Long alarmId1 = allByMember2Before.get(0).getId();
        groupGoalService.postGroupRefuse(savedMember2, alarmId1);

        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);
        List<Alarm> allByMember4After = alarmRepository.findAllByMember(savedMember4);

        List<WaitingGoal> allWaitingGoal = waitingGoalRepository.findAll();

        //then
        Assertions.assertThat(allByMember1After.size()).isEqualTo(1);
        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember2After.size()).isEqualTo(2);
        Assertions.assertThat(allByMember2After.get(0).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember2After.get(1).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember3After.size()).isEqualTo(2);
        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(invite);
        Assertions.assertThat(allByMember3After.get(1).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember4After.size()).isEqualTo(1);
        Assertions.assertThat(allByMember4After.get(0).getAlarmDetailType()).isEqualTo(invite);
        Assertions.assertThat(allWaitingGoal.size()).isEqualTo(1);
        Assertions.assertThat(allWaitingGoal.get(0).getWaitingGoalName()).isEqualTo("200만원 도전");
    }

    @Test
    @DisplayName("waiting 상태에서 취소")
    public void exitWaitingGroup(){
        //given
        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
        Member savedMember3 = memberRepository.save(member3);

        Member member4 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.tongki);
        Member savedMember4 = memberRepository.save(member4);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3","nickname4"));

        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(GROUP,"100만원 도전",1000000,friends);
        groupGoalService.postGroup(savedMember1,goalAlarmRequestDto);

        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(GROUP,"200만원 도전",2000000,friends2);
        groupGoalService.postGroup(savedMember2,goalAlarmRequestDto2);

        //when
        List<WaitingGoal> allWaitingGoalTemmp = waitingGoalRepository.findAll();

        Alarm alarmTemp = alarmRepository.findAllByMember(savedMember2).get(0);

        groupGoalService.postGroupAccept(savedMember2, alarmTemp.getId());
        groupGoalService.exitWaitingGroup(savedMember2,allWaitingGoalTemmp.get(0).getId());

        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);

        List<WaitingGoal> allWaitingGoal = waitingGoalRepository.findAll();

        //then
        Assertions.assertThat(allByMember1After.size()).isEqualTo(1);
        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember2After.size()).isEqualTo(1);
        Assertions.assertThat(allByMember2After.get(0).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allByMember3After.size()).isEqualTo(2);
        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(invite);
        Assertions.assertThat(allByMember3After.get(1).getAlarmDetailType()).isEqualTo(boom);
        Assertions.assertThat(allWaitingGoal.size()).isEqualTo(1);
        Assertions.assertThat(allWaitingGoal.get(0).getWaitingGoalName()).isEqualTo("200만원 도전");
    }

}
