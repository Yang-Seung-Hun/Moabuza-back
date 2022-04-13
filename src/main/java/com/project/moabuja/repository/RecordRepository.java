package com.project.moabuja.repository;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Optional<Record> findRecordById(Long recordId);

    List<Record> findRecordsByRecordDateAndMember(LocalDateTime recordDate, Member currentUser);

    List<Record> findRecordsByRecordTypeAndMember(RecordType recordType, Member member);

    List<Record> findRecordsByMember(Member member);


    @Query("select sum(r.recordAmount) from Record r where r.recordType =:recordType and r.createdAt >:createdAt and r.member =:member")
    int sumCurrentAmount(@Param("recordType") RecordType recordType, @Param("createdAt") LocalDateTime createAt, @Param("member") Member member);

    void deleteRecordById(Long id);
}
