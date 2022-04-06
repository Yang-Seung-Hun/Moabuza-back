package com.project.moabuja.service;

import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;


@SpringBootTest
@Rollback(value = true)
@Transactional
class RecordServiceImplTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private RecordService recordService;
    @Autowired private ChallengeGoalService challengeGoalService;
    @Autowired private GroupGoalService groupGoalService;


    @Test
    @DisplayName("가지고 있는 돈 초과 지출 시 오류 발생")
    public void save1(){
        //given

        Member member = new Member("654321",654321L,"nonickname1","email1@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-10 00:00:00.000","문화상품권",20000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.expense, "2022-03-15 00:00:00.000","운동화",50000);

        //when
        recordService.save(recordRequestDto1, savedMember1);
        recordService.save(recordRequestDto2, savedMember1);

        //then
        org.junit.jupiter.api.Assertions.assertThrows(ErrorException.class, () -> {
            recordService.save(recordRequestDto3, savedMember1);
        });
    }

    @Test
    @DisplayName("goal 완료 안된 로직")
    public void save2(){

        //given
        Member member = new Member("987654",987654L,"nonickname2","email2@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);
        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);

        //when
        ResponseEntity<RecordResponseDto> savedRecord = recordService.save(recordRequestDto, savedMember1);

        //then
        RecordResponseDto body = savedRecord.getBody();
        Assertions.assertThat(body.isComplete()).isFalse();
    }

    @Test
    @DisplayName("challenge goal 완료 로직")
    public void save3(){

        //given
        Member member1 = new Member("456789",456789L,"nonickname3","email3@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);
        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);
        challengeGoalService.save(createChallengeRequestDto, savedMember1);

        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-06 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","가즈아!!",45000);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.income, "2022-03-12 00:00:00.000","심부름",10000);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, "2022-03-15 00:00:00.000","완료!!!",5000);

        //when
        ResponseEntity<RecordResponseDto> result1 = recordService.save(recordRequestDto1, savedMember1);
        ResponseEntity<RecordResponseDto> result2 = recordService.save(recordRequestDto2, savedMember1);
        ResponseEntity<RecordResponseDto> result3 = recordService.save(recordRequestDto3, savedMember1);
        ResponseEntity<RecordResponseDto> result4 = recordService.save(recordRequestDto4, savedMember1);
        ResponseEntity<RecordResponseDto> result5 = recordService.save(recordRequestDto5, savedMember1);

        //then
        Assertions.assertThat(result1.getBody().isComplete()).isFalse();
        Assertions.assertThat(result2.getBody().isComplete()).isFalse();
        Assertions.assertThat(result3.getBody().isComplete()).isFalse();
        Assertions.assertThat(result4.getBody().isComplete()).isFalse();
        Assertions.assertThat(result5.getBody().isComplete()).isTrue();
    }

    @Test
    @DisplayName("challenge goal 생성전 입력하면 오류 발생")
    public void save4(){

        //given
        Member member1 = new Member("751423",751423L,"nonickname10","email10@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-06 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","가즈아!!",45000);


        //when
        recordService.save(recordRequestDto1, savedMember1);
        recordService.save(recordRequestDto2, savedMember1);

        //then
        org.junit.jupiter.api.Assertions.assertThrows(ErrorException.class, ()-> {
            recordService.save(recordRequestDto3, savedMember1);
        });
    }

    @Test
    @DisplayName("group goal 완료 로직")
    public void save5(){

        //given
        Member member1 = new Member("123456",123456L,"nickname1","email1@naver.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member1);

        Member member2 = new Member("123457",123457L,"nickname2","email2@naver.com", Hero.bunny);
        Member savedMember2 = memberRepository.save(member2);

        Member member3 = new Member("123458",123458L,"nickname3","email3@naver.com", Hero.tanni);
        Member savedMember3 = memberRepository.save(member3);

        Member member4 = new Member("123459",123459L,"nickname4","email4@naver.com", Hero.tanni);
        Member savedMember4 = memberRepository.save(member4);

        ArrayList<String> friends = new ArrayList<>(Arrays.asList("nickname2", "nickname3", "nickname4"));
        CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto("울릉도 같이 가보자!", 500000, friends);
        groupGoalService.save(createGroupRequestDto, savedMember1);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",200000);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",200000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",200000);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",200000);

        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.expense, "2022-03-06 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto6 = new RecordRequestDto(RecordType.group, "2022-03-10 00:00:00.000"," 울릉도가즈아!!",45000);
        RecordRequestDto recordRequestDto7 = new RecordRequestDto(RecordType.income, "2022-03-12 00:00:00.000","심부름",10000);
        RecordRequestDto recordRequestDto8 = new RecordRequestDto(RecordType.group, "2022-03-15 00:00:00.000","울릉도 기다려라!!!",55000);

        RecordRequestDto recordRequestDto9 = new RecordRequestDto(RecordType.group, "2022-03-17 00:00:00.000","울릉도 기다려라!!!",200000);
        RecordRequestDto recordRequestDto10 = new RecordRequestDto(RecordType.group, "2022-03-18 00:00:00.000","울릉도 기다려라!!!",100000);
        RecordRequestDto recordRequestDto11 = new RecordRequestDto(RecordType.group, "2022-03-19 00:00:00.000","울릉도 기다려라!!!",100000);

        //when
        recordService.save(recordRequestDto1, savedMember1);
        recordService.save(recordRequestDto2, savedMember2);
        recordService.save(recordRequestDto3, savedMember3);
        recordService.save(recordRequestDto4, member4);
        recordService.save(recordRequestDto5, savedMember1);
        recordService.save(recordRequestDto6, savedMember1);
        recordService.save(recordRequestDto7, savedMember1);
        recordService.save(recordRequestDto8, savedMember1);
        recordService.save(recordRequestDto9, savedMember2);
        recordService.save(recordRequestDto10, savedMember3);

        //then
        ResponseEntity<RecordResponseDto> result11 = recordService.save(recordRequestDto11, savedMember4);
        Assertions.assertThat(result11.getBody().isComplete()).isTrue();
    }

    @Test
    @DisplayName("하루 내역 보기")
    public void getDayList(){

        //given
        Member member = new Member("321654",321654L,"nonickname4","email4@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);
        challengeGoalService.save(createChallengeRequestDto, savedMember1);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-05 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","가즈아!!",45000);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.income, "2022-03-10 00:00:00.000","심부름",10000);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","완료!!!",4000);

        recordService.save(recordRequestDto1, savedMember1);
        recordService.save(recordRequestDto2, savedMember1);
        recordService.save(recordRequestDto3, savedMember1);
        recordService.save(recordRequestDto4, savedMember1);
        recordService.save(recordRequestDto5, savedMember1);

        DayListRequestDto dayListRequestDto1 = new DayListRequestDto("2022-03-05 00:00:00.000");
        DayListRequestDto dayListRequestDto2 = new DayListRequestDto("2022-03-10 00:00:00.000");
        DayListRequestDto dayListRequestDto3 = new DayListRequestDto("2022-03-20 00:00:00.000");

        //when

        ResponseEntity<DayListResponseDto> dayList1 = recordService.getDayList(dayListRequestDto1, savedMember1);
        ResponseEntity<DayListResponseDto> dayList2 = recordService.getDayList(dayListRequestDto2, savedMember1);
        ResponseEntity<DayListResponseDto> dayList3 = recordService.getDayList(dayListRequestDto3, savedMember1);


        //then
        Assertions.assertThat(dayList1.getBody().getDayIncomeAmount()).isEqualTo(50000);
        Assertions.assertThat(dayList1.getBody().getDayRecordList().size()).isEqualTo(2);
        Assertions.assertThat(dayList1.getBody().getDayExpenseAmount()).isEqualTo(5000);
        Assertions.assertThat(dayList1.getBody().getDayChallengeAmount()).isEqualTo(0);
        Assertions.assertThat(dayList1.getBody().getDayGroupAmount()).isEqualTo(0);

        Assertions.assertThat(dayList2.getBody().getDayIncomeAmount()).isEqualTo(10000);
        Assertions.assertThat(dayList2.getBody().getDayRecordList().size()).isEqualTo(3);
        Assertions.assertThat(dayList2.getBody().getDayExpenseAmount()).isEqualTo(0);
        Assertions.assertThat(dayList2.getBody().getDayChallengeAmount()).isEqualTo(49000);
        Assertions.assertThat(dayList2.getBody().getDayGroupAmount()).isEqualTo(0);

        Assertions.assertThat(dayList3.getBody().getDayIncomeAmount()).isEqualTo(0);
        Assertions.assertThat(dayList3.getBody().getDayRecordList().size()).isEqualTo(0);
        Assertions.assertThat(dayList3.getBody().getDayExpenseAmount()).isEqualTo(0);
        Assertions.assertThat(dayList3.getBody().getDayChallengeAmount()).isEqualTo(0);
        Assertions.assertThat(dayList3.getBody().getDayGroupAmount()).isEqualTo(0);

    }

    @Test
    @DisplayName("기록 삭제 성공")
    public void deleteRecord(){

        //given
        Member member = new Member("147258",147258L,"nonickname5","email5@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);
        challengeGoalService.save(createChallengeRequestDto, savedMember1);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-05 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","가즈아!!",45000);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.income, "2022-03-10 00:00:00.000","심부름",10000);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","완료!!!",4000);

        recordService.save(recordRequestDto1, savedMember1);
        recordService.save(recordRequestDto2, savedMember1);
        recordService.save(recordRequestDto3, savedMember1);
        recordService.save(recordRequestDto4, savedMember1);
        recordService.save(recordRequestDto5, savedMember1);

        DayListRequestDto dayListRequestDto1 = new DayListRequestDto("2022-03-05 00:00:00.000");
        DayListRequestDto dayListRequestDto2 = new DayListRequestDto("2022-03-10 00:00:00.000");

        ResponseEntity<DayListResponseDto> dayList1Tmp = recordService.getDayList(dayListRequestDto1, savedMember1);
        ResponseEntity<DayListResponseDto> dayList2Tmp = recordService.getDayList(dayListRequestDto2, savedMember1);

        //when
        recordService.deleteRecord(dayList1Tmp.getBody().getDayRecordList().get(1).getId(),savedMember1);
        recordService.deleteRecord(dayList2Tmp.getBody().getDayRecordList().get(1).getId(),savedMember1);

        ResponseEntity<DayListResponseDto> dayList1 = recordService.getDayList(dayListRequestDto1,savedMember1);
        ResponseEntity<DayListResponseDto> dayList2 = recordService.getDayList(dayListRequestDto2,savedMember1);

        //then
        Assertions.assertThat(dayList1.getBody().getDayRecordList().size()).isEqualTo(1);
        Assertions.assertThat(dayList1.getBody().getDayExpenseAmount()).isEqualTo(0);
        Assertions.assertThat(dayList2.getBody().getDayRecordList().size()).isEqualTo(2);
        Assertions.assertThat(dayList2.getBody().getDayIncomeAmount()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 권한 없는 기록 삭제 시 오류 발생")
    public void deleteRecord2(){

        //given
        Member member = new Member("258369",258369L,"nonickname6","email6@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        Member member2 = new Member("963852",963852L,"nonickname7","email7@gmail.com", Hero.tongki);
        Member savedMember2 = memberRepository.save(member2);

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);
        challengeGoalService.save(createChallengeRequestDto, savedMember1);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-05 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","가즈아!!",45000);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.income, "2022-03-10 00:00:00.000","심부름",10000);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","완료!!!",4000);

        recordService.save(recordRequestDto1, savedMember1);
        recordService.save(recordRequestDto2, savedMember1);
        recordService.save(recordRequestDto3, savedMember1);
        recordService.save(recordRequestDto4, savedMember1);
        recordService.save(recordRequestDto5, savedMember1);

        DayListRequestDto dayListRequestDto1 = new DayListRequestDto("2022-03-05 00:00:00.000");
        DayListRequestDto dayListRequestDto2 = new DayListRequestDto("2022-03-10 00:00:00.000");

        ResponseEntity<DayListResponseDto> dayList1Tmp = recordService.getDayList(dayListRequestDto1, savedMember1);
        ResponseEntity<DayListResponseDto> dayList2Tmp = recordService.getDayList(dayListRequestDto2, savedMember1);

        //when
        Long recordId = dayList1Tmp.getBody().getDayRecordList().get(1).getId();

        //then
        org.junit.jupiter.api.Assertions.assertThrows(ErrorException.class, ()->{
            recordService.deleteRecord(recordId, savedMember2);
        });
    }

    @Test
    @DisplayName("없는 기록 삭제 시 오류 생성")
    public void deleteRecord3(){

        //given
        Member member = new Member("111111",111111L,"nonickname7","email7@gmail.com", Hero.tongki);
        Member savedMember1 = memberRepository.save(member);

        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-05 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.income, "2022-03-10 00:00:00.000","심부름",10000);

        recordService.save(recordRequestDto1, savedMember1);
        recordService.save(recordRequestDto2, savedMember1);
        recordService.save(recordRequestDto3, savedMember1);

        //when
        Long recordId = 99L;

        //then
        org.junit.jupiter.api.Assertions.assertThrows(ErrorException.class, ()->{
            recordService.deleteRecord(recordId, savedMember1);
        });
    }
}