package com.project.moabuja.repository;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void save(){

        LocalDateTime recordDate = LocalDateTime.parse("2019-12-10 11:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Record record = new Record(10000, "편의점에서 쇼핑!!", recordDate, null, RecordType.expense);

        Record savedRecord = recordRepository.save(record);


        System.out.println("========================================================");
        System.out.println(savedRecord.getId());
        System.out.println("========================================================");
    }

}