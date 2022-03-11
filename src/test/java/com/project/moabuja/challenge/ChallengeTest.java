package com.project.moabuja.challenge;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.repository.ChallengeGoalRepository;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.repository.RecordRepository;
import com.project.moabuja.service.ChallengeGoalService;
import com.project.moabuja.service.RecordService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ChallengeTest {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private RecordService recordService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChallengeGoalRepository challengeGoalRepository;
    @Autowired
    private ChallengeGoalService challengeGoalService;

    @Test
    public void save(){

        Member member1 = new Member("member1", "nickname1");
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("member2", "nickname2");
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("member2", "nickname3");
        Member savedMember3 = memberRepository.save(member3);

        Member member4 = new Member("member3", "nickname4");
        Member savedMember4 = memberRepository.save(member4);


        List<String> friends = new ArrayList<>();
        friends.add("nickname1");
        friends.add("nickname2");
        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("100만원 모으기", 1000000, friends);
        ChallengeGoal savedChallenge = challengeGoalService.save(createChallengeRequestDto);

        Optional<ChallengeGoal> byId = challengeGoalRepository.findById(1L);
        Assertions.assertThat(byId.get().getMembers().size()).isEqualTo(2);
        Assertions.assertThat(byId.get().getChallengeGoalAmount()).isEqualTo(1000000);
    }
}
