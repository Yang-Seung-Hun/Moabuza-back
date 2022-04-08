package com.project.moabuja.domain.record;

import com.project.moabuja.domain.Timestamped;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
public class Record extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    private int recordAmount;

    private String memo;

    private LocalDateTime recordDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    @Builder
    public Record(RecordRequestDto recordRequestDto, Member currentMember){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss.SSS"));
        this.recordType = recordRequestDto.getRecordType();
        this.recordDate = LocalDateTime.parse(recordRequestDto.getRecordDate(),formatter);
        this.memo = recordRequestDto.getMemos();
        this.recordAmount = recordRequestDto.getRecordAmount();
        this.member = currentMember;
    }

    protected Record () {}

}
