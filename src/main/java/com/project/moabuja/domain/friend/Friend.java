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

    @ManyToOne
    @JoinColumn(name = "friend_user")
    private Member friend;

    public Friend(Member member, Member friend) {
        this.member = member;
        this.friend = friend;
    }

    protected Friend () {}
}
