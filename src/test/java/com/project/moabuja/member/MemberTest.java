//package com.project.moabuja.member;
//
//import com.project.moabuja.domain.goal.ChallengeGoal;
//import com.project.moabuja.domain.goal.GroupGoal;
//import com.project.moabuja.domain.member.Hero;
//import com.project.moabuja.domain.member.Member;
//import com.project.moabuja.domain.record.RecordType;
//import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
//import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
//import com.project.moabuja.dto.request.record.RecordRequestDto;
//import com.project.moabuja.dto.response.member.HomeResponseDto;
//import com.project.moabuja.repository.FriendRepository;
//import com.project.moabuja.repository.MemberRepository;
//import com.project.moabuja.service.ChallengeGoalService;
//import com.project.moabuja.service.GroupGoalService;
//import com.project.moabuja.service.MemberService;
//import com.project.moabuja.service.RecordService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
//public class MemberTest {
//
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private GroupGoalService groupGoalService;
//    @Autowired private RecordService recordService;
//    @Autowired private MemberService memberService;
//    @Autowired private ChallengeGoalService challengeGoalService;
//
//    @Test
//    public void homeTest(){
//
//        String now = "2021-11-05 00:00:00.000";
//        String tomorrow = "2021-11-06 00:00:00.000";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//
//        Member member1 = new Member("123456", 1L, "nickname1", "123@email1.com", Hero.hero1);
//        Member member2 = new Member("123456", 2L, "nickname2", "123@email2.com", Hero.hero2);
//        Member member3 = new Member("123456", 3L, "nickname3", "123@email3.com", Hero.hero3);
//        Member member4 = new Member("123456", 4L, "nickname4", "123@email4.com", Hero.hero1);
//
//        Member savedMember1 = memberRepository.save(member1);
//        Member savedMember2 = memberRepository.save(member2);
//        Member savedMember3 = memberRepository.save(member3);
//        Member savedMember4 = memberRepository.save(member4);
//
//        List<String> friends = new ArrayList<>();
//        friends.add("nickname2");
//        friends.add("nickname4");
//        CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto("제주도 여행", 1000000, friends);
//        GroupGoal savedGroup = groupGoalService.save(createGroupRequestDto, savedMember1);
//        savedGroup.setIsAcceptedGroup(true);
//
//        List<String> challengeFriends = new ArrayList<>();
//        challengeFriends.add("nickname3");
//        challengeFriends.add("nickname4");
//        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("각자 100만원씩", 1000000, challengeFriends);
//        ChallengeGoal savedChallenge = challengeGoalService.save(createChallengeRequestDto, savedMember1);
//        savedChallenge.setIsAcceptedChallenge(true);
//
//
//
//        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, now, "편의점 알바", 3000000);
//        recordService.save(recordRequestDto, savedMember1);
//        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, now, "설거지 심부름", 10000);
//        recordService.save(recordRequestDto2, savedMember1);
//
//        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.expense, now, "편이점!", 20000);
//        recordService.save(recordRequestDto4, savedMember3);
//
//        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.group, now, "가즈아!!!", 20000);
//        recordService.save(recordRequestDto3, savedMember1);
//        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.group, now, "세부!!!!", 50000);
//        recordService.save(recordRequestDto5, savedMember2);
//        RecordRequestDto recordRequestDto6 = new RecordRequestDto(RecordType.group, tomorrow, "할라봉 가즈아", 200000);
//        recordService.save(recordRequestDto6, savedMember4);
//
//        RecordRequestDto recordRequestDto7 = new RecordRequestDto(RecordType.challenge, tomorrow, "내가 1빠", 50000);
//        recordService.save(recordRequestDto7, savedMember4);
//        RecordRequestDto recordRequestDto8 = new RecordRequestDto(RecordType.challenge, tomorrow, "나는 2빠", 20000);
//        recordService.save(recordRequestDto8, savedMember3);
//        RecordRequestDto recordRequestDto9 = new RecordRequestDto(RecordType.challenge, tomorrow, "내가 1등할꺼얌", 100000);
//        recordService.save(recordRequestDto9, savedMember1);
//
//
//        RecordRequestDto recordRequestDto10 = new RecordRequestDto(RecordType.group, tomorrow, "떠나요 제주도~~", 800000);
//        recordService.save(recordRequestDto10, savedMember1);
//
//        RecordRequestDto recordRequestDto11 = new RecordRequestDto(RecordType.expense, tomorrow, "학교 준비물", 15000);
//        recordService.save(recordRequestDto11, savedMember1);
//
//        RecordRequestDto recordRequestDto12 = new RecordRequestDto(RecordType.income, tomorrow, "생일선물", 30000);
//        recordService.save(recordRequestDto12, savedMember1);
//
//        RecordRequestDto recordRequestDto13 = new RecordRequestDto(RecordType.challenge, tomorrow, "도전 끝!! 1등~", 1000000);
//        recordService.save(recordRequestDto13, savedMember1);
//
//
//        HomeResponseDto homeInfo = memberService.getHomeInfo(savedMember1);
//
//        System.out.println("==================Group Info=======================");
//        System.out.println(homeInfo.getGroupCurrentAmount());
//        System.out.println(homeInfo.getGroupNeedAmount());
//        System.out.println(homeInfo.getGroupAmount());
//        System.out.println(homeInfo.getGroupPercent());
//        System.out.println(homeInfo.getGroupName());
//
//        System.out.println("==================Challenge Info=======================");
//        System.out.println(homeInfo.getChallengeCurrentAmount());
//        System.out.println(homeInfo.getChallengeNeedAmount());
//        System.out.println(homeInfo.getChallengeAmount());
//        System.out.println(homeInfo.getChallengePercent());
//        System.out.println(homeInfo.getChallengeName());
//
//        System.out.println("==================User Info=======================");
//        System.out.println(homeInfo.getHero());
//        System.out.println(homeInfo.getHeroLevel());
//        System.out.println(homeInfo.getTotalAmount());
//        System.out.println(homeInfo.getWallet());
//
//    }
//}
