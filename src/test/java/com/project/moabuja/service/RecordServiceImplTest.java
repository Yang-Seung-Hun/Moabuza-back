package com.project.moabuja.service;

import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.RecordResponseDto;
import com.project.moabuja.exception.exceptionClass.RecordErrorException;
import com.project.moabuja.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class RecordServiceImplTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private RecordService recordService;
    @Autowired private ChallengeGoalService challengeGoalService;

    @Test
    @DisplayName("가지고 있는 돈 초과 지출 시 오류 발생")
    public void save1(){

        //given
        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        Member member = new Member("123456",123456L,"nickname1","emial1@naver.com", Hero.tongki);
        Member savedMember = memberRepository.save(member);

        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-10 00:00:00.000","문화상품권",20000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.expense, "2022-03-15 00:00:00.000","운동화",50000);

        //when
        ResponseEntity<RecordResponseDto> savedRecord = recordService.save(recordRequestDto1, savedMember);
        ResponseEntity<RecordResponseDto> savedRecord2 = recordService.save(recordRequestDto2, savedMember);

        //then
        org.junit.jupiter.api.Assertions.assertThrows(RecordErrorException.class, () -> {
            recordService.save(recordRequestDto3, savedMember);
        });
    }

    @Test
    @DisplayName("goal 완료 안된 로직")
    public void save2(){

        //given
        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        Member member = new Member("123456",123456L,"nickname1","emial1@naver.com", Hero.tongki);
        Member savedMember = memberRepository.save(member);

        //when
        ResponseEntity<RecordResponseDto> savedRecord = recordService.save(recordRequestDto, savedMember);

        //then
        RecordResponseDto body = savedRecord.getBody();
        Assertions.assertThat(body.isComplete()).isFalse();
    }

    @Test
    @DisplayName("challenge goal 완료 로직")
    public void save3(){

        //given
        RecordRequestDto recordRequestDto1 = new RecordRequestDto(RecordType.income, "2022-03-05 00:00:00.000","3월용돈",50000);
        Member member = new Member("123456",123456L,"nickname1","emial1@naver.com", Hero.tongki);
        Member savedMember = memberRepository.save(member);

        CreateChallengeRequestDto createChallengeRequestDto = new CreateChallengeRequestDto("5만원 모으기", 50000, null);
        ResponseEntity<String> savedChallenge = challengeGoalService.save(createChallengeRequestDto, savedMember);

        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.expense, "2022-03-06 00:00:00.000","편의점",5000);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.challenge, "2022-03-10 00:00:00.000","가즈아!!",45000);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.income, "2022-03-12 00:00:00.000","심부름",10000);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.challenge, "2022-03-15 00:00:00.000","완료!!!",5000);

        //when
        ResponseEntity<RecordResponseDto> result1 = recordService.save(recordRequestDto1, savedMember);
        ResponseEntity<RecordResponseDto> result2 = recordService.save(recordRequestDto2, savedMember);
        ResponseEntity<RecordResponseDto> result3 = recordService.save(recordRequestDto3, savedMember);
        ResponseEntity<RecordResponseDto> result4 = recordService.save(recordRequestDto4, savedMember);
        ResponseEntity<RecordResponseDto> result5 = recordService.save(recordRequestDto5, savedMember);

        //then
        Assertions.assertThat(result1.getBody().isComplete()).isFalse();
        Assertions.assertThat(result2.getBody().isComplete()).isFalse();
        Assertions.assertThat(result3.getBody().isComplete()).isFalse();
        Assertions.assertThat(result4.getBody().isComplete()).isFalse();
        Assertions.assertThat(result5.getBody().isComplete()).isTrue();
    }


}