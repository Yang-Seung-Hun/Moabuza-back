package com.project.moabuja.repository;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.DayRecordResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.service.ChallengeGoalService;
import com.project.moabuja.service.RecordService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class RecordTest {

    @Autowired private RecordRepository recordRepository;
    @Autowired private RecordService recordService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ChallengeGoalService challengeGoalService;
    @Autowired private FriendRepository friendRepository;

    @Test
    public void save(){

        String now = "2021-11-05 00:00:00.000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Member member = new Member("email1", "nickname1");
        Member savedMember = memberRepository.save(member);

        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, now, "편의점", 10000);
        RecordResponseDto isDoneGoal = recordService.save(recordRequestDto, savedMember);

        List<Record> findRecord = recordRepository.findRecordsByRecordDate(LocalDateTime.parse(now, formatter));

        Assertions.assertThat(findRecord.get(0).getMemo()).isEqualTo("편의점");
        Assertions.assertThat(isDoneGoal.isComplete()).isFalse();
    }

    @Test
    public void getDayList(){

        String now = "2021-11-05 00:00:00.000";
        String tomorrow = "2021-11-06 00:00:00.000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Member member1 = new Member("member1", "nickname1", Hero.hero1);
        Member savedMember1 = memberRepository.save(member1);
        Member member2 = new Member("member2", "nickname2", Hero.hero2);
        Member savedMember2 = memberRepository.save(member2);
        Member member3 = new Member("member2", "nickname3", Hero.hero3);
        Member savedMember3 = memberRepository.save(member3);
        Member member4 = new Member("member3", "nickname4", Hero.hero2);
        Member savedMember4 = memberRepository.save(member4);

        Friend friend1 = new Friend(savedMember1, savedMember2.getId());
        Friend friend2 = new Friend(savedMember2, savedMember1.getId());
        friendRepository.save(friend1);
        friendRepository.save(friend2);

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
        recordService.save(recordRequestDto, savedMember1);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, now, "설거지 심부름", 10000);
        recordService.save(recordRequestDto2, savedMember1);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, now, "가즈아!!!", 20000);
        recordService.save(recordRequestDto3, savedMember1);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.challenge, now, "내가 일등!!", 20000);
        RecordResponseDto isDone1 = recordService.save(recordRequestDto4, savedMember1);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, now, "어림 없지 나두 간다!!!", 50000);
        recordService.save(recordRequestDto5, savedMember2);
        RecordRequestDto recordRequestDto6 = new RecordRequestDto(RecordType.challenge, tomorrow, "내가 다시 일등!!", 990000);
        RecordResponseDto isDone2 = recordService.save(recordRequestDto6, savedMember1);
        RecordRequestDto recordRequestDto7 = new RecordRequestDto(RecordType.expense, tomorrow, "파마", 200000);
        recordService.save(recordRequestDto7, savedMember1);

        Assertions.assertThat(isDone1.isComplete()).isFalse();
        Assertions.assertThat(isDone2.isComplete()).isTrue();

        DayListRequestDto dayListRequestDto = new DayListRequestDto(now);
        DayListResponseDto dayList = recordService.getDayList(dayListRequestDto, savedMember1);
        System.out.println("====================================================");
        List<DayRecordResponseDto> list = dayList.getDayRecordList();
        for (DayRecordResponseDto dto : list) {
            System.out.println(dto.getRecordType());
            System.out.println(dto.getRecordAmount());
            System.out.println(dto.getMemos());
            System.out.println(dto.getRecordDate());
        }
        System.out.println("====================================================");
        System.out.println(dayList.getDayExpenseAmount());
        System.out.println(dayList.getDayIncomeAmount());
        System.out.println(dayList.getDayGroupAmount());
        System.out.println(dayList.getDayChallengeAmount());
        System.out.println(dayList.getTotalAmount());
        System.out.println(dayList.getWallet());
    }

    @Test
    public void deleteRecord(){

        String now = "2021-11-05 00:00:00.000";
        String tomorrow = "2021-11-06 00:00:00.000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Member member = new Member("email1", "nickname1");
        Member savedMember = memberRepository.save(member);

        Member member2 = new Member("email2", "nickname2");
        Member savedMember2 = memberRepository.save(member2);

        List<String> friends = new ArrayList<>();
        friends.add("nickname1");
        friends.add("nickname2");
        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("100만원 모으기", 1000000, friends);
        ChallengeGoal savedChallenge = challengeGoalService.save(createChallengeRequestDto);

        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, now, "편의점", 10000);
        recordService.save(recordRequestDto, savedMember);

        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, now, "설거지 심부름", 10000);
        recordService.save(recordRequestDto2, savedMember);

        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.expense, now, "점심값", 2000);
        recordService.save(recordRequestDto3, savedMember);

        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.challenge, now, "내가 일등!!", 2000);
        recordService.save(recordRequestDto4, savedMember2);

        DayListRequestDto dayListRequestDto = new DayListRequestDto(now);

        DayListResponseDto dayList = recordService.getDayList(dayListRequestDto, savedMember);

        Assertions.assertThat(dayList.getDayExpenseAmount()).isEqualTo(2000);
        Assertions.assertThat(dayList.getDayIncomeAmount()).isEqualTo(20000);


        recordService.deleteRecord(2L);
        DayListResponseDto dayList2 = recordService.getDayList(dayListRequestDto, savedMember);
        Assertions.assertThat(dayList2.getDayIncomeAmount()).isEqualTo(10000);
    }

}