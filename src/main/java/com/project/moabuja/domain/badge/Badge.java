package com.project.moabuja.domain.badge;

import com.project.moabuja.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BadgeName badgeName;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
