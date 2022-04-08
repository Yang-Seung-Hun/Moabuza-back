//package com.project.moabuja.service;
//
//import com.project.moabuja.domain.alarm.Alarm;
//import com.project.moabuja.domain.alarm.AlarmDetailType;
//import com.project.moabuja.domain.friend.Friend;
//import com.project.moabuja.domain.goal.ChallengeGoal;
//import com.project.moabuja.domain.goal.WaitingGoal;
//import com.project.moabuja.domain.member.Hero;
//import com.project.moabuja.domain.member.Member;
//import com.project.moabuja.domain.record.RecordType;
//import com.project.moabuja.dto.request.alarm.GoalAlarmRequestDto;
//import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
//import com.project.moabuja.dto.request.record.RecordRequestDto;
//import com.project.moabuja.dto.response.goal.ChallengeMemberDto;
//import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
//import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
//import com.project.moabuja.repository.*;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static com.project.moabuja.domain.alarm.AlarmDetailType.*;
//import static com.project.moabuja.domain.goal.GoalType.*;
//
//@SpringBootTest
//@Rollback(value = true)
//@Transactional
//class ChallengeGoalServiceImplTest {
//
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private ChallengeGoalService challengeGoalService;
//    @Autowired private ChallengeGoalRepository challengeGoalRepository;
//    @Autowired private RecordService recordService;
//    @Autowired private EntityManager em;
//    @Autowired private FriendRepository friendRepository;
//    @Autowired private AlarmRepository alarmRepository;
//    @Autowired private WaitingGoalRepository waitingGoalRepository;
//
//    @Test
//    @DisplayName("1인 도전해부자 생성")
//    public void save1() {
//
//        //given
//        Member member = new Member("123456", 123456L, "뭐냐 이게???", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);
//
//        //when
//        challengeGoalService.save(createChallengeRequestDto, savedMember1);
//
//        ChallengeGoal challengeGoal = savedMember1.getChallengeGoal();
//        Optional<ChallengeGoal> challengeByIdTmp = challengeGoalRepository.findById(challengeGoal.getId());
//        ChallengeGoal challengeById = null;
//        if (challengeByIdTmp.isPresent()) {
//            challengeById = challengeByIdTmp.get();
//        }
//
//        //then
//        Assertions.assertThat(challengeById.getChallengeGoalName()).isEqualTo("5만원 모으기");
//        Assertions.assertThat(challengeById.getMembers().size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("2인이상 도전해부자 생성")
//    public void save2() {
//
//        //given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));
//
//        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, friends);
//
//        //when
//        challengeGoalService.save(createChallengeRequestDto, savedMember1);
//
//        ChallengeGoal challengeGoal = savedMember1.getChallengeGoal();
//        Optional<ChallengeGoal> challengeByIdTmp = challengeGoalRepository.findById(challengeGoal.getId());
//        ChallengeGoal challengeById = null;
//        if (challengeByIdTmp.isPresent()) {
//            challengeById = challengeByIdTmp.get();
//        }
//
//        //then
//        Assertions.assertThat(challengeById.getChallengeGoalName()).isEqualTo("5만원 모으기");
//        Assertions.assertThat(challengeById.getMembers().size()).isEqualTo(3);
//    }
//
//    @Autowired RecordRepository recordRepository;
//    @Test
//    @DisplayName("생성된 challenge goal 있을때")
//    public void getChallengeInfo() {
//
//        //given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));
//
//        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, friends);
//        challengeGoalService.save(createChallengeRequestDto, savedMember1);
//
//        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000", "3월용돈", 50000);
//        recordService.save(recordRequestDto1, savedMember1);
//
//        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000", "3월용돈", 50000);
//        recordService.save(recordRequestDto2, friend1);
//
//        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000", "3월용돈", 50000);
//        recordService.save(recordRequestDto3, friend2);
//
//        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.challenge, "2022-03-06 00:00:00.000", "도전!!", 10000);
//        recordService.save(recordRequestDto4, savedMember1);
//
//        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, "2022-03-06 00:00:00.000", "도전!!", 20000);
//        recordService.save(recordRequestDto5, friend1);
//
//        RecordRequestDto recordRequestDto6 = new RecordRequestDto(RecordType.challenge, "2022-03-06 00:00:00.000", "도전!!", 30000);
//        recordService.save(recordRequestDto6, friend2);
//
//        RecordRequestDto recordRequestDto7 = new RecordRequestDto(RecordType.challenge, "2022-03-06 00:00:00.000", "도전!!", 40000);
//        recordService.save(recordRequestDto7, savedMember1);
//
//
//        CreateChallengeRequestDto createChallengeRequestDto2 = new CreateChallengeRequestDto("10만원 모으기", 100000, null);
//        challengeGoalService.save(createChallengeRequestDto2, savedMember1);
//
//        RecordRequestDto recordRequestDto8 = new RecordRequestDto(RecordType.challenge, "2022-03-06 00:00:00.000", "도전!!", 40000);
//        recordService.save(recordRequestDto8, savedMember1);
//
//        //when
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp1 = challengeGoalService.getChallengeInfo(savedMember1);
//        ChallengeResponseDto challengeInfo1 = challengeInfoTmp1.getBody();
//        List<ChallengeMemberDto> challengeMembers = challengeInfo1.getChallengeMembers();
//        int challengeInfo1PercentSum = 0;
//        for (ChallengeMemberDto challengeMember : challengeMembers) {
//            challengeInfo1PercentSum += challengeMember.getChallengeMemberNowPercent();
//        }
//
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp2 = challengeGoalService.getChallengeInfo(friend1);
//        ChallengeResponseDto challengeInfo2 = challengeInfoTmp2.getBody();
//        List<ChallengeMemberDto> challengeMembers2 = challengeInfo2.getChallengeMembers();
//        int challengeInfo2PercentSum = 0;
//        for (ChallengeMemberDto challengeMember : challengeMembers2) {
//            challengeInfo2PercentSum += challengeMember.getChallengeMemberNowPercent();
//        }
//
//        //then
//        Assertions.assertThat(challengeInfo1.getGoalStatus()).isEqualTo("goal");
//        Assertions.assertThat(challengeInfo1.getChallengeLists().size()).isEqualTo(1);
//        Assertions.assertThat(challengeInfo1.getChallengeMembers().size()).isEqualTo(1);
//        Assertions.assertThat(challengeInfo1PercentSum).isEqualTo(40);
//        Assertions.assertThat(challengeInfo1.getChallengeName()).isEqualTo("10만원 모으기");
//
//        Assertions.assertThat(challengeInfo2.getGoalStatus()).isEqualTo("goal");
//        Assertions.assertThat(challengeInfo2.getChallengeLists().size()).isEqualTo(1);
//        Assertions.assertThat(challengeInfo2.getChallengeMembers().size()).isEqualTo(2);
//        Assertions.assertThat(challengeInfo2.getChallengeName()).isEqualTo("5만원 모으기");
//        Assertions.assertThat(challengeInfo2PercentSum).isEqualTo(100);
//    }
//
//    @Test
//    @DisplayName("challenge goal 수락대기중")
//    public void getChallengeInfo2() {
//
//        //given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));
//        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE, "20만원 빨리 모으기", 200000, friends);
//        challengeGoalService.postChallenge(savedMember1, goalAlarmRequestDto);
//
//        em.flush();
//        em.clear();
//
//        //when
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp1 = challengeGoalService.getChallengeInfo(savedMember1);
//        ChallengeResponseDto challengeInfo1 = challengeInfoTmp1.getBody();
//
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp2 = challengeGoalService.getChallengeInfo(savedFriend1);
//        ChallengeResponseDto challengeInfo2 = challengeInfoTmp2.getBody();
//
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp3 = challengeGoalService.getChallengeInfo(savedFriend2);
//        ChallengeResponseDto challengeInfo3 = challengeInfoTmp3.getBody();
//
//
//        //then
//        Assertions.assertThat(challengeInfo1.getGoalStatus()).isEqualTo("waiting");
//        Assertions.assertThat(challengeInfo1.getChallengeLists()).isNull();
//        Assertions.assertThat(challengeInfo1.getChallengeMembers()).isNull();
//        Assertions.assertThat(challengeInfo1.getChallengeName()).isNull();
//        Assertions.assertThat(challengeInfo1.getWaitingGoals().size()).isEqualTo(1);
//
//        Assertions.assertThat(challengeInfo2.getGoalStatus()).isEqualTo("waiting");
//        Assertions.assertThat(challengeInfo2.getChallengeLists()).isNull();
//        Assertions.assertThat(challengeInfo2.getChallengeMembers()).isNull();
//        Assertions.assertThat(challengeInfo2.getChallengeName()).isNull();
//        Assertions.assertThat(challengeInfo2.getWaitingGoals().size()).isEqualTo(1);
//
//        Assertions.assertThat(challengeInfo3.getGoalStatus()).isEqualTo("waiting");
//        Assertions.assertThat(challengeInfo3.getChallengeLists()).isNull();
//        Assertions.assertThat(challengeInfo3.getChallengeMembers()).isNull();
//        Assertions.assertThat(challengeInfo3.getChallengeName()).isNull();
//        Assertions.assertThat(challengeInfo3.getWaitingGoals().size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("수락 대기, 생성된 challenge 없음")
//    public void getChallengeInfo3() {
//
//        //given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        //when
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp1 = challengeGoalService.getChallengeInfo(savedMember1);
//        ChallengeResponseDto challengeInfo1 = challengeInfoTmp1.getBody();
//
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp2 = challengeGoalService.getChallengeInfo(savedFriend1);
//        ChallengeResponseDto challengeInfo2 = challengeInfoTmp2.getBody();
//
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp3 = challengeGoalService.getChallengeInfo(savedFriend2);
//        ChallengeResponseDto challengeInfo3 = challengeInfoTmp3.getBody();
//
//
//        //then
//        Assertions.assertThat(challengeInfo1.getGoalStatus()).isEqualTo("noGoal");
//        Assertions.assertThat(challengeInfo1.getChallengeLists()).isNull();
//        Assertions.assertThat(challengeInfo1.getChallengeMembers()).isNull();
//        Assertions.assertThat(challengeInfo1.getChallengeName()).isNull();
//        Assertions.assertThat(challengeInfo1.getWaitingGoals()).isNull();
//
//        Assertions.assertThat(challengeInfo2.getGoalStatus()).isEqualTo("noGoal");
//        Assertions.assertThat(challengeInfo2.getChallengeLists()).isNull();
//        Assertions.assertThat(challengeInfo2.getChallengeMembers()).isNull();
//        Assertions.assertThat(challengeInfo2.getChallengeName()).isNull();
//        Assertions.assertThat(challengeInfo2.getWaitingGoals()).isNull();
//
//        Assertions.assertThat(challengeInfo3.getGoalStatus()).isEqualTo("noGoal");
//        Assertions.assertThat(challengeInfo3.getChallengeLists()).isNull();
//        Assertions.assertThat(challengeInfo3.getChallengeMembers()).isNull();
//        Assertions.assertThat(challengeInfo3.getChallengeName()).isNull();
//        Assertions.assertThat(challengeInfo3.getWaitingGoals()).isNull();
//    }
//
//    @Test
//    @DisplayName("도전해부자 초대할 친구 목록")
//    public void getChallengeMemberCandidates() {
//
//        //given
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
//        Member friend4 = new Member("123460", 123460L, "nickname5", "email5@naver.com", Hero.bunny);
//        memberRepository.save(friend4);
//
//        Friend relate1 = new Friend(savedMember1,savedFriend1);
//        Friend relate2 = new Friend(friend1,savedMember1);
//        friendRepository.save(relate1);
//        friendRepository.save(relate2);
//
//
//        Friend relate3 = new Friend(savedMember1,savedFriend2);
//        Friend relate4 = new Friend(savedFriend2,savedMember1);
//        friendRepository.save(relate3);
//        friendRepository.save(relate4);
//
//        Friend relate5 = new Friend(savedMember1,savedFriend3);
//        friendRepository.save(relate5);
//
//        CreateChallengeRequestDto createChallengeRequestDto1 = new CreateChallengeRequestDto("10만원 모으기", 100000, null);
//        challengeGoalService.save(createChallengeRequestDto1, savedFriend1);
//
//        //when
//        ResponseEntity<CreateChallengeResponseDto> challengeMemberCandidatesTmp = challengeGoalService.getChallengeMemberCandidates(savedMember1);
//        CreateChallengeResponseDto allFriends = challengeMemberCandidatesTmp.getBody();
//
//        boolean canInvite1 = false;
//        String nickname1 = null;
//        boolean canInvite2 = false;
//        String nickname2 = null;
//        if (allFriends != null) {
//            canInvite1 = allFriends.getChallengeMembers().get(0).isChallengeMemberCanInvite();
//            nickname1 = allFriends.getChallengeMembers().get(0).getChallengeMemberNickname();
//
//            canInvite2 = allFriends.getChallengeMembers().get(1).isChallengeMemberCanInvite();
//            nickname2 = allFriends.getChallengeMembers().get(1).getChallengeMemberNickname();
//        }
//
//        //then
//        if (allFriends != null) {
//            Assertions.assertThat(allFriends.getChallengeMembers().size()).isEqualTo(2);
//            Assertions.assertThat(canInvite1).isFalse();
//            Assertions.assertThat(nickname1).isEqualTo("nickname2");
//
//            Assertions.assertThat(canInvite2).isTrue();
//            Assertions.assertThat(nickname2).isEqualTo("nickname3");
//        }
//
//    }
//
//    @Test
//    @DisplayName("2명 이상 도전 중일 때 challenge 나오기")
//    public void exitChallenge() {
//
//        //given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
//
//        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, friends);
//        challengeGoalService.save(createChallengeRequestDto, savedMember1);
//
//        //when
//        challengeGoalService.exitChallenge(savedMember1);
//
//        //then
//        Assertions.assertThat(savedMember1.getChallengeGoal()).isNull();
//        Assertions.assertThat(savedFriend1.getChallengeGoal()).isNotNull();
//        Assertions.assertThat(savedFriend1.getChallengeGoal().getChallengeGoalName()).isEqualTo("5만원 모으기");
//        Assertions.assertThat(savedFriend2.getChallengeGoal()).isNotNull();
//        Assertions.assertThat(savedFriend2.getChallengeGoal().getChallengeGoalName()).isEqualTo("5만원 모으기");
//
//    }
//
//    @Test
//    @DisplayName("혼자 도전 중일 때 challenge 나오기")
//    public void exitChallenge2() {
//
//        //given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);
//        challengeGoalService.save(createChallengeRequestDto, savedMember1);
//
//        //when
//        challengeGoalService.exitChallenge(savedMember1);
//
//        //then
//        Assertions.assertThat(savedMember1.getChallengeGoal()).isNull();
//    }
//
//    @Test
//    @DisplayName("혼자 도전하기")
//    public void postChallenge(){
//
//        //given
//        Member member = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        //when
//        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE,"100만원 도전",1000000,null);
//        challengeGoalService.postChallenge(savedMember1,goalAlarmRequestDto);
//
//        //then
//        Assertions.assertThat(savedMember1.getChallengeGoal()).isNotNull();
//        Assertions.assertThat(savedMember1.getChallengeGoal().getChallengeGoalName()).isEqualTo("100만원 도전");
//        Assertions.assertThat(savedMember1.getChallengeGoal().getChallengeGoalAmount()).isEqualTo(1000000);
//        Assertions.assertThat(savedMember1.getMemberWaitingGoals()).isEmpty();
//    }
//
//    @Test
//    @DisplayName("같이 도전하기 초대 1개")
//    public void postChallenge2(){
//
//        //given
//        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
//        Member savedMember2 = memberRepository.save(member2);
//
//        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedMember3 = memberRepository.save(member3);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
//        //when
//        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE,"100만원 도전",1000000,friends);
//        challengeGoalService.postChallenge(savedMember1,goalAlarmRequestDto);
//
//        List<Alarm> allByMember1 = alarmRepository.findAllByMember(savedMember2);
//        List<Alarm> allByMember2 = alarmRepository.findAllByMember(savedMember3);
//
//        //then
//        Assertions.assertThat(savedMember1.getChallengeGoal()).isNull();
//        Assertions.assertThat(allByMember1).isNotEmpty();
//        Assertions.assertThat(allByMember1.get(0).getAlarmDetailType()).isEqualTo(invite);
//        Assertions.assertThat(allByMember2).isNotEmpty();
//        Assertions.assertThat(allByMember2.get(0).getAlarmDetailType()).isEqualTo(invite);
//    }
//
//    @Test
//    @DisplayName("같이 도전하기 초대 여러개")
//    public void postChallenge3(){
//
//        //given
//        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
//        Member savedMember2 = memberRepository.save(member2);
//
//        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedMember3 = memberRepository.save(member3);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
//        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3"));
//        //when
//        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE,"100만원 도전",1000000,friends);
//        challengeGoalService.postChallenge(savedMember1,goalAlarmRequestDto);
//
//        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(CHALLENGE,"10만원 도전",100000,friends2);
//        challengeGoalService.postChallenge(savedMember2,goalAlarmRequestDto2);
//
//
//        List<Alarm> allByMember1 = alarmRepository.findAllByMember(savedMember2);
//        List<Alarm> allByMember2 = alarmRepository.findAllByMember(savedMember3);
//
//        //then
//        Assertions.assertThat(savedMember1.getChallengeGoal()).isNull();
//        Assertions.assertThat(allByMember1).isNotEmpty();
//        Assertions.assertThat(allByMember1.size()).isEqualTo(1);
//        Assertions.assertThat(allByMember1.get(0).getAlarmDetailType()).isEqualTo(invite);
//
//        Assertions.assertThat(allByMember2).isNotEmpty();
//        Assertions.assertThat(allByMember2.size()).isEqualTo(2);
//        Assertions.assertThat(allByMember2.get(0).getAlarmDetailType()).isEqualTo(invite);
//        Assertions.assertThat(allByMember2.get(1).getAlarmDetailType()).isEqualTo(invite);
//    }
//
//    @Test
//    @DisplayName("not EveryOne Accepts invitation")
//    public void postChallengeAccept1(){
//
//        //given
//        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
//        Member savedMember2 = memberRepository.save(member2);
//
//        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedMember3 = memberRepository.save(member3);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
//
//        //when
//        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE,"100만원 도전",1000000,friends);
//        challengeGoalService.postChallenge(savedMember1,goalAlarmRequestDto);
//
//        List<Alarm> allByMember2Before = alarmRepository.findAllByMember(savedMember2);
//        List<Alarm> allByMember3Before = alarmRepository.findAllByMember(savedMember3);
//        Long alarmId1 = allByMember2Before.get(0).getId();
//        Long alarmId2 = allByMember3Before.get(0).getId();
//
//        //member2의 invite알람 삭제, member1 이랑 member2한테 accept 알람 보냄
//        challengeGoalService.postChallengeAccept(savedMember2, alarmId1);
//
//        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
//        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
//        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);
//
//        //then
//        Assertions.assertThat(allByMember1After.size()).isEqualTo(1);//member2의 수락
//        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(accept);
//        Assertions.assertThat(allByMember2After.size()).isEqualTo(0);
//        Assertions.assertThat(allByMember3After.size()).isEqualTo(2);//member2의 수락, member1의 초대
//        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(invite);
//        Assertions.assertThat(allByMember3After.get(1).getAlarmDetailType()).isEqualTo(accept);
//    }
//
//    @Test
//    @DisplayName("EveryOne Accepts invitation")
//    public void postChallengeAccept2(){
//
//        //given
//        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
//        Member savedMember2 = memberRepository.save(member2);
//
//        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedMember3 = memberRepository.save(member3);
//
//        Member member4 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.bunny);
//        Member savedMember4 = memberRepository.save(member4);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
//        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3"));
//        List<String> friends3 = new ArrayList<>(Arrays.asList("nickname4"));
//
//        //member2,3한테 invite 알람
//        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE,"100만원 도전",1000000,friends);
//        challengeGoalService.postChallenge(savedMember1,goalAlarmRequestDto);
//
//        //member3한테 invite 알람
//        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(CHALLENGE,"300만원 도전",3000000,friends2);
//        challengeGoalService.postChallenge(savedMember2,goalAlarmRequestDto2);
//
//        GoalAlarmRequestDto goalAlarmRequestDto3 = new GoalAlarmRequestDto(CHALLENGE,"300만원 도전",3000000,friends3);
//        challengeGoalService.postChallenge(savedMember3,goalAlarmRequestDto3);
//
//        List<Alarm> allByMember2Before = alarmRepository.findAllByMember(savedMember2);
//        List<Alarm> allByMember3Before = alarmRepository.findAllByMember(savedMember3);
//        Long alarmId1 = allByMember2Before.get(0).getId();
//        Long alarmId2 = allByMember3Before.get(0).getId();
//        Long alarmId3 = allByMember3Before.get(1).getId();
//
//        //when
//        //member2의 invite알람 삭제, member1 이랑 member3한테 accept 알람 보냄
//        challengeGoalService.postChallengeAccept(savedMember2, alarmId1);
//        //member3의 invite알람 삭제, member1 이랑 member2한테 accept 알람 보냄, member1,2,3한테 create알람 보냄, member2,3한테 대기 challenge boom 알람
//        challengeGoalService.postChallengeAccept(savedMember3, alarmId2);
//
//        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
//        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
//        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);
//
//        //then
////        Assertions.assertThat(allByMember1After.size()).isEqualTo(2);
////        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(accept);
////        Assertions.assertThat(allByMember1After.get(1).getAlarmDetailType()).isEqualTo(create);
////        Assertions.assertThat(allByMember2After.size()).isEqualTo(2);
////        Assertions.assertThat(allByMember2After.get(0).getAlarmDetailType()).isEqualTo(create);
////        Assertions.assertThat(allByMember2After.get(1).getAlarmDetailType()).isEqualTo(boom);
////        Assertions.assertThat(allByMember3After.size()).isEqualTo(3);
////        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(accept);
////        Assertions.assertThat(allByMember3After.get(1).getAlarmDetailType()).isEqualTo(create);
////        Assertions.assertThat(allByMember3After.get(2).getAlarmDetailType()).isEqualTo(boom);
//    }
//
//    @Test
//    @DisplayName("delete waiting goals when one refuse")
//    public void postChallengeRefuse(){
//        //given
//        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
//        Member savedMember2 = memberRepository.save(member2);
//
//        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
//        Member savedMember3 = memberRepository.save(member3);
//
//        Member member4 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.bunny);
//        Member savedMember4 = memberRepository.save(member4);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
//        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3", "nickname4"));
//
//        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE,"100만원 도전",1000000,friends);
//        challengeGoalService.postChallenge(savedMember1,goalAlarmRequestDto);
//
//        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(CHALLENGE,"200만원 도전",2000000,friends2);
//        challengeGoalService.postChallenge(savedMember2,goalAlarmRequestDto2);
//
//        //when
//        List<Alarm> allByMember2Before = alarmRepository.findAllByMember(savedMember2);
//        Long alarmId1 = allByMember2Before.get(0).getId();
//        challengeGoalService.postChallengeRefuse(savedMember2, alarmId1);
//
//        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
//        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
//        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);
//        List<Alarm> allByMember4After = alarmRepository.findAllByMember(savedMember4);
//
//        List<WaitingGoal> allWaitingGoal = waitingGoalRepository.findAll();
//
//        //then
////        Assertions.assertThat(allByMember1After.size()).isEqualTo(1);
////        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(boom);
////        Assertions.assertThat(allByMember2After.size()).isEqualTo(1);
////        Assertions.assertThat(allByMember2After.get(0).getAlarmDetailType()).isEqualTo(boom);
////        Assertions.assertThat(allByMember3After.size()).isEqualTo(2);
////        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(invite);
////        Assertions.assertThat(allByMember3After.get(1).getAlarmDetailType()).isEqualTo(boom);
////        Assertions.assertThat(allByMember4After.size()).isEqualTo(1);
////        Assertions.assertThat(allByMember4After.get(0).getAlarmDetailType()).isEqualTo(invite);
////        Assertions.assertThat(allWaitingGoal.size()).isEqualTo(1);
////        Assertions.assertThat(allWaitingGoal.get(0).getWaitingGoalName()).isEqualTo("200만원 도전");
//    }
//
////    @Test
////    @DisplayName("delete waiting goals when one cancel")
////    public void exitWaitingChallenge(){
////        //given
////        Member member1 = new Member("123456", 123456L, "nickname1", "email1@naver.com", Hero.tongki);
////        Member savedMember1 = memberRepository.save(member1);
////
////        Member member2 = new Member("123457", 123457L, "nickname2", "email2@naver.com", Hero.tanni);
////        Member savedMember2 = memberRepository.save(member2);
////
////        Member member3 = new Member("123458", 123458L, "nickname3", "email3@naver.com", Hero.bunny);
////        Member savedMember3 = memberRepository.save(member3);
////
////        Member member4 = new Member("123459", 123459L, "nickname4", "email4@naver.com", Hero.tongki);
////        Member savedMember4 = memberRepository.save(member4);
////
////        List<String> friends = new ArrayList<>(Arrays.asList("nickname2","nickname3"));
////        List<String> friends2 = new ArrayList<>(Arrays.asList("nickname3","nickname4"));
////
////        GoalAlarmRequestDto goalAlarmRequestDto = new GoalAlarmRequestDto(CHALLENGE,"100만원 도전",1000000,friends);
////        challengeGoalService.postChallenge(savedMember1,goalAlarmRequestDto);
////
////        GoalAlarmRequestDto goalAlarmRequestDto2 = new GoalAlarmRequestDto(CHALLENGE,"200만원 도전",2000000,friends2);
////        challengeGoalService.postChallenge(savedMember2,goalAlarmRequestDto2);
////
////        //when
////        List<Alarm> allByMember2Before = alarmRepository.findAllByMember(savedMember2);
////        List<WaitingGoal> all = waitingGoalRepository.findAll();
////
////        challengeGoalService.exitWaitingChallenge(savedMember2,all.get(0).getId());
////
////        List<Alarm> allByMember1After = alarmRepository.findAllByMember(savedMember1);
////        List<Alarm> allByMember2After = alarmRepository.findAllByMember(savedMember2);
////        List<Alarm> allByMember3After = alarmRepository.findAllByMember(savedMember3);
////
////        List<WaitingGoal> allWaitingGoal = waitingGoalRepository.findAll();
////
////        //then
//////        Assertions.assertThat(allByMember1After.size()).isEqualTo(1);
//////        Assertions.assertThat(allByMember1After.get(0).getAlarmDetailType()).isEqualTo(boom);
//////        Assertions.assertThat(allByMember2After.size()).isEqualTo(1);
//////        Assertions.assertThat(allByMember2After.get(0).getAlarmDetailType()).isEqualTo(boom);
//////        Assertions.assertThat(allByMember3After.size()).isEqualTo(1);
//////        Assertions.assertThat(allByMember3After.get(0).getAlarmDetailType()).isEqualTo(boom);
//////        Assertions.assertThat(allWaitingGoal.size()).isEqualTo(0);
////    }
//}