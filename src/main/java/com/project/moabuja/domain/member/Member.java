package com.project.moabuja.domain.member;

import com.project.moabuja.domain.alarm.Alarm;
import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.goal.MemberWaitingGoal;
import com.project.moabuja.dto.KakaoUserInfoDto;
import com.project.moabuja.dto.request.member.MemberUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private Long kakaoId;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Hero hero;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_goal_id")
    private ChallengeGoal challengeGoal;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_goal_id")
    private GroupGoal groupGoal;

    @OneToMany(mappedBy = "member")
    private List<DoneGoal> doneGaols = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Alarm> alarms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberWaitingGoal> memberWaitingGoals = new ArrayList<>();

    @Builder
    public Member(String password, Long kakaoId, String nickname, String email, Hero hero) {
        this.password = password;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.hero = hero;
    }

    public Member updateInfo(MemberUpdateRequestDto dto){
        this.nickname = dto.getNickname();
        this.hero = dto.getHero();
        return this;
    }

    public Member fromDto(KakaoUserInfoDto dto, String password) {
        return new Member().builder()
                .password(password)
                .kakaoId(dto.getKakaoId())
                .email(dto.getEmail())
                .build();
    }

    //반대쪽에서 연관관계편의메소드에서 사용될 setter
    public void changeChallengeGoal(ChallengeGoal challengeGoal){
        this.challengeGoal = challengeGoal;
    }

    //반대쪽에서 연관관계편의메소드에서 사용될 setter
    public void changeGroupGoal(GroupGoal groupGoal){
        this.groupGoal = groupGoal;
    }

    public void addDoneGoal(DoneGoal doneGoal){
        this.doneGaols.add(doneGoal);
        doneGoal.changeMember(this);
    }
}
