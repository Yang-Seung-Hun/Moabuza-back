package com.project.moabuja.repository;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Optional<Record> findRecordById(Long recordId);

    List<Record> findRecordsByRecordDate(LocalDateTime recordDate);

    List<Record> findRecordsByRecordDateAndMember(LocalDateTime recordDate, Member currentUser);

    List<Record> findRecordsByRecordTypeAndMember(RecordType recordType, Member member);

    List<Record> findRecordsByRecordType(RecordType recordType);

    void deleteRecordById(Long id);
}
