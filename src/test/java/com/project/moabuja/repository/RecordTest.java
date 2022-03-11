package com.project.moabuja.repository;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.record.DayListRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.record.DayListResponseDto;
import com.project.moabuja.service.RecordService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class RecordTest {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private RecordService recordService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void save(){

        LocalDateTime now = LocalDateTime.now();

        Member member = new Member("email1", "nickname1");
        Member savedMember = memberRepository.save(member);

        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, now, "편의점", 10000);
        Record savedRecord = recordService.save(recordRequestDto, savedMember);

        List<Record> findRecord = recordRepository.findRecordsByRecordDate(now);

        Assertions.assertThat(findRecord.get(0).getMemo()).isEqualTo("편의점");
    }

    @Test
    public void getDayList(){

        LocalDateTime now = LocalDateTime.now();

        Member member = new Member("email1", "nickname1");
        Member savedMember = memberRepository.save(member);

        Member member2 = new Member("email2", "nickname2");
        Member savedMember2 = memberRepository.save(member2);

        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, now, "편의점", 10000);
        Record savedRecord = recordService.save(recordRequestDto, savedMember);

        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, now, "설거지 심부름", 10000);
        Record savedRecord2 = recordService.save(recordRequestDto2, savedMember);

        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.expense, now, "점심값", 2000);
        Record savedRecord3 = recordService.save(recordRequestDto3, savedMember);

        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.challenge, now, "내가 일등!!", 2000);
        Record savedRecord4 = recordService.save(recordRequestDto4, savedMember2);

        DayListRequestDto dayListRequestDto = new DayListRequestDto(now);

        DayListResponseDto dayList = recordService.getDayList(dayListRequestDto, savedMember);


        Assertions.assertThat(dayList.getDayExpenseAmount()).isEqualTo(2000);
        Assertions.assertThat(dayList.getDayIncomeAmount()).isEqualTo(20000);
    }

    @Test
    public void deleteRecord(){

        LocalDateTime now = LocalDateTime.now();

        Member member = new Member("email1", "nickname1");
        Member savedMember = memberRepository.save(member);

        Member member2 = new Member("email2", "nickname2");
        Member savedMember2 = memberRepository.save(member2);

        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.income, now, "편의점", 10000);
        Record savedRecord = recordService.save(recordRequestDto, savedMember);

        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, now, "설거지 심부름", 10000);
        Record savedRecord2 = recordService.save(recordRequestDto2, savedMember);

        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.expense, now, "점심값", 2000);
        Record savedRecord3 = recordService.save(recordRequestDto3, savedMember);

        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.challenge, now, "내가 일등!!", 2000);
        Record savedRecord4 = recordService.save(recordRequestDto4, savedMember2);

        DayListRequestDto dayListRequestDto = new DayListRequestDto(now);

        DayListResponseDto dayList = recordService.getDayList(dayListRequestDto, savedMember);

        Assertions.assertThat(dayList.getDayExpenseAmount()).isEqualTo(2000);
        Assertions.assertThat(dayList.getDayIncomeAmount()).isEqualTo(20000);


        recordService.deleteRecord(savedRecord2.getId());
        DayListResponseDto dayList2 = recordService.getDayList(dayListRequestDto, savedMember);
        Assertions.assertThat(dayList2.getDayIncomeAmount()).isEqualTo(10000);
    }

}