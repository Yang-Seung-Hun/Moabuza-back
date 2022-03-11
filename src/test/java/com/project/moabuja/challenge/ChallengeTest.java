package com.project.moabuja.challenge;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.goal.ChallengeResponseDto;
import com.project.moabuja.dto.response.goal.CreateChallengeMemberDto;
import com.project.moabuja.dto.response.goal.CreateChallengeResponseDto;
import com.project.moabuja.repository.ChallengeGoalRepository;
import com.project.moabuja.repository.FriendRepository;
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
import java.time.LocalDateTime;
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
    @Autowired
    private FriendRepository friendRepository;

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

    @Test
    public void getChallengeInfo(){

        LocalDateTime now = LocalDateTime.now();

        Member member1 = new Member("member1", "nickname1");
        Member savedMember1 = memberRepository.save(member1);
        Member member2 = new Member("member2", "nickname2");
        Member savedMember2 = memberRepository.save(member2);
        Member member3 = new Member("member2", "nickname3");
        Member savedMember3 = memberRepository.save(member3);
        Member member4 = new Member("member3", "nickname4");
        Member savedMember4 = memberRepository.save(member4);

        Friend friend1 = new Friend(savedMember1, savedMember2.getId());
        Friend friend2 = new Friend(savedMember2, savedMember1.getId());
        Friend savedFriend1 = friendRepository.save(friend1);
        Friend savedFriend2 = friendRepository.save(friend2);

        //친구목록(초대자포함) 만드는 과정
        List<Friend> friendsByMember = friendRepository.findFriendsByMember(savedMember1);
        List<String> friends = new ArrayList<>();
        friends.add(savedMember1.getNickname());
        for(Friend friend : friendsByMember){
            Optional<Member> memberById = memberRepository.findById(friend.getFriend());
            if(memberById.isPresent()){
                friends.add(memberById.get().getNickname());
            }
        }

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("100만원 모으기", 1000000, friends);
        ChallengeGoal savedChallenge = challengeGoalService.save(createChallengeRequestDto);
        System.out.println(savedChallenge.getClass());
        savedChallenge.setIsAcceptedChallenge(true);

        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, now, "편의점", 10000);
        Record savedRecord = recordService.save(recordRequestDto, savedMember1);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, now, "설거지 심부름", 10000);
        Record savedRecord2 = recordService.save(recordRequestDto2, savedMember1);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, now, "가즈아!!!", 20000);
        Record savedRecord3 = recordService.save(recordRequestDto3, savedMember1);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.challenge, now, "내가 일등!!", 20000);
        Record savedRecord4 = recordService.save(recordRequestDto4, savedMember1);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, now, "어림 없지 나두 간다!!!", 50000);
        Record savedRecord5 = recordService.save(recordRequestDto5, savedMember2);

        ChallengeResponseDto challengeInfo = challengeGoalService.getChallengeInfo(savedMember1);
        Assertions.assertThat(challengeInfo.getGoalStatus()).isEqualTo("goal");
        Assertions.assertThat(challengeInfo.getChallengeMembers().size()).isEqualTo(2);

    }

    @Test
    public void getChallengeCandidates(){

        LocalDateTime now = LocalDateTime.now();

        //member1기준!!
        Member member1 = new Member("member1", "nickname1");
        Member savedMember1 = memberRepository.save(member1);
        Member member2 = new Member("member2", "nickname2");
        Member savedMember2 = memberRepository.save(member2);
        Member member3 = new Member("member2", "nickname3");
        Member savedMember3 = memberRepository.save(member3);
        Member member4 = new Member("member3", "nickname4");
        Member savedMember4 = memberRepository.save(member4);

        //친구 없을때
        CreateChallengeResponseDto challengeMemberCandidates = challengeGoalService.getChallengeMemberCandidates(savedMember1);
        Assertions.assertThat(challengeMemberCandidates.getChallengeMembers().size()).isEqualTo(0);

        Friend friend1 = new Friend(savedMember1, savedMember2.getId());
        Friend friend2 = new Friend(savedMember2, savedMember1.getId());
        Friend friend3 = new Friend(savedMember1, savedMember3.getId());
        Friend friend4 = new Friend(savedMember3, savedMember1.getId());
        Friend savedFriend1 = friendRepository.save(friend1);
        Friend savedFriend2 = friendRepository.save(friend2);
        Friend savedFriend3 = friendRepository.save(friend3);
        Friend savedFriend4 = friendRepository.save(friend4);

        List<Friend> friendsByMember = friendRepository.findFriendsByMember(savedMember1);
        List<String> friends = new ArrayList<>();
        friends.add(savedMember1.getNickname());
        for(Friend friend : friendsByMember){
            Optional<Member> memberById = memberRepository.findById(friend.getFriend());
            if(memberById.isPresent()){
                friends.add(memberById.get().getNickname());
            }
        }

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("100만원 모으기", 1000000, friends);
        ChallengeGoal savedChallenge = challengeGoalService.save(createChallengeRequestDto);

        //수락대기중일때
        CreateChallengeResponseDto challengeMemberCandidates2 = challengeGoalService.getChallengeMemberCandidates(savedMember1);
        Assertions.assertThat(challengeMemberCandidates2.getChallengeMembers().get(0).getChallengeMemberNickname()).isEqualTo("nickname2");
        Assertions.assertThat(challengeMemberCandidates2.getChallengeMembers().get(0).isChallengeMemberCanInvite()).isTrue();
        Assertions.assertThat(challengeMemberCandidates2.getChallengeMembers().get(1).getChallengeMemberNickname()).isEqualTo("nickname3");
        Assertions.assertThat(challengeMemberCandidates2.getChallengeMembers().get(1).isChallengeMemberCanInvite()).isTrue();



        savedChallenge.setIsAcceptedChallenge(true);
        CreateChallengeResponseDto challengeMemberCandidates3 = challengeGoalService.getChallengeMemberCandidates(savedMember1);
        Assertions.assertThat(challengeMemberCandidates3.getChallengeMembers().get(0).getChallengeMemberNickname()).isEqualTo("nickname2");
        Assertions.assertThat(challengeMemberCandidates3.getChallengeMembers().get(0).isChallengeMemberCanInvite()).isFalse();
        Assertions.assertThat(challengeMemberCandidates3.getChallengeMembers().get(1).getChallengeMemberNickname()).isEqualTo("nickname3");
        Assertions.assertThat(challengeMemberCandidates3.getChallengeMembers().get(1).isChallengeMemberCanInvite()).isFalse();

    }
}
