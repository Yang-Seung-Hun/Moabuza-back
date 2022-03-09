package com.project.moabuja.repository;
import com.project.moabuja.domain.record.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Optional<Record> findById(Long aLong);
}
