package com.project.moabuja.domain.goal;

import com.project.moabuja.domain.member.Member;
import lombok.Getter;

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

    @OneToMany(mappedBy = "groupGoal",cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    public GroupGoal(String groupGoalName, int groupGoalAmount, int groupCurrentAmount) {
        this.groupGoalName = groupGoalName;
        this.groupGoalAmount = groupGoalAmount;
        this.currentAmount = groupCurrentAmount;
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
}
