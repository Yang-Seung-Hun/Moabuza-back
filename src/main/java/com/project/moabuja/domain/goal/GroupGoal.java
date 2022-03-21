package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class GroupGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_goal_id")
    private Long id;

    private String groupGoalName;

    private int groupGoalAmount;

    private int currentAmount;

    private boolean isAcceptedGroup;

    @OneToMany(mappedBy = "groupGoal",cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @Builder
    public GroupGoal(String groupGoalName, int groupGoalAmount, int groupCurrentAmount, boolean isAcceptedGroup) {
        this.groupGoalName = groupGoalName;
        this.groupGoalAmount = groupGoalAmount;
        this.currentAmount = groupCurrentAmount;
        this.isAcceptedGroup = isAcceptedGroup;
    }

    public void addMember(Member member){
        this.members.add(member);
        member.changeGroupGoal(this);
    }

    public void removeMember(Member member){
        this.members.remove(member);
        member.changeGroupGoal(null);
    }

    protected GroupGoal () {}

    //테스트용 setter입니다.
    public void setIsAcceptedGroup(Boolean bool){
        this.isAcceptedGroup = bool;
    }
}
