package com.project.moabuja.domain.friend;

import com.project.moabuja.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Long friend;

    //테스트용 생성자입니다.
    public Friend(Member member, Long friend) {
        this.member = member;
        this.friend = friend;
    }
}
