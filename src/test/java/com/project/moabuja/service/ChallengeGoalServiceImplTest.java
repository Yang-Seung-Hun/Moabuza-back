package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.repository.ChallengeGoalRepository;
import com.project.moabuja.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = true)
@Transactional
class ChallengeGoalServiceImplTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private ChallengeGoalService challengeGoalService;
    @Autowired private ChallengeGoalRepository challengeGoalRepository;

    @Test
    @DisplayName("1인 도전해부자 생성")
    public void save1(){

        //given
        Member member = new Member("123456",123456L,"nickname1","email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);

        //when
        challengeGoalService.save(createChallengeRequestDto, savedMember1);

        ChallengeGoal challengeGoal = savedMember1.getChallengeGoal();
        Optional<ChallengeGoal> challengeByIdTmp = challengeGoalRepository.findById(challengeGoal.getId());
        ChallengeGoal challengeById = null;
        if(challengeByIdTmp.isPresent()) {
            challengeById = challengeByIdTmp.get();
        }

        //then
        Assertions.assertThat(challengeById.getChallengeGoalName()).isEqualTo("5만원 모으기");
        Assertions.assertThat(challengeById.getMembers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("2인이상 도전해부자 생성")
    public void save2(){

        //given
        Member member = new Member("123456",123456L,"nickname1","email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member friend1 = new Member("123457",123457L,"nickname2","email2@naver.com", Hero.bunny);
        Member savedFriend1 = memberRepository.save(friend1);

        Member friend2 = new Member("123458",123458L,"nickname3","email3@naver.com", Hero.bunny);
        Member savedFriend2 = memberRepository.save(friend2);

        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, friends);

        //when
        challengeGoalService.save(createChallengeRequestDto, savedMember1);

        ChallengeGoal challengeGoal = savedMember1.getChallengeGoal();
        Optional<ChallengeGoal> challengeByIdTmp = challengeGoalRepository.findById(challengeGoal.getId());
        ChallengeGoal challengeById = null;
        if(challengeByIdTmp.isPresent()) {
            challengeById = challengeByIdTmp.get();
        }

        //then
        Assertions.assertThat(challengeById.getChallengeGoalName()).isEqualTo("5만원 모으기");
        Assertions.assertThat(challengeById.getMembers().size()).isEqualTo(3);
    }

//    @Test
//    @DisplayName("생성된 challenge goal 있을때")
//    public void getChallengeInfo(){
//
//        //given
//        Member member = new Member("123456",123456L,"nickname1","email1@naver.com", Hero.tongki);
//        Member savedMember1 = memberRepository.save(member);
//
//        Member friend1 = new Member("123457",123457L,"nickname2","email2@naver.com", Hero.bunny);
//        Member savedFriend1 = memberRepository.save(friend1);
//
//        Member friend2 = new Member("123458",123458L,"nickname3","email3@naver.com", Hero.bunny);
//        Member savedFriend2 = memberRepository.save(friend2);
//
//        List<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3"));
//
//        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, friends);
//        challengeGoalService.save(createChallengeRequestDto, savedMember1);
//
//        //when
//        ResponseEntity<ChallengeResponseDto> challengeInfoTmp = challengeGoalService.getChallengeInfo(savedMember1);
//        ChallengeResponseDto challengeInfo = challengeInfoTmp.getBody();
//
//        //then
//
//    }
}