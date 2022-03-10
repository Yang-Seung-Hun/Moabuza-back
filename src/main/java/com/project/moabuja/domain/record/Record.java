package com.project.moabuja.domain.record;

import com.project.moabuja.domain.Timestamped;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    public Record(RecordRequestDto recordRequestDto, Member currentMember){
        this.recordType = recordRequestDto.getRecordType();
        this.recordDate = recordRequestDto.getRecordDate();
        this.memo = recordRequestDto.getMemos();
        this.recordAmount = recordRequestDto.getRecordAmount();
        this.member = currentMember;
    }

}
