package com.project.moabuja.challenge;


import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.member.Hero;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.request.record.RecordRequestDto;
import com.project.moabuja.dto.response.goal.GroupListDto;
import com.project.moabuja.dto.response.goal.GroupMemberDto;
import com.project.moabuja.dto.response.goal.GroupResponseDto;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.service.GroupGoalService;
import com.project.moabuja.service.RecordService;
import org.apache.catalina.filters.ExpiresFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class GoalTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private GroupGoalService groupGoalService;
    @Autowired private RecordService recordService;
    @Autowired private FriendRepository friendRepository;

    @Test
    public void groupSave(){

        Member member1 = new Member("123456", 1L, "nickname1", "123@email1.com", Hero.hero1);
        Member member2 = new Member("123456", 2L, "nickname2", "123@email2.com", Hero.hero2);
        Member member3 = new Member("123456", 3L, "nickname3", "123@email3.com", Hero.hero3);
        Member member4 = new Member("123456", 4L, "nickname4", "123@email4.com", Hero.hero1);

        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Member savedMember4 = memberRepository.save(member4);


        List<String> friends = new ArrayList<>();
        friends.add("nickname2");
        friends.add("nickname4");
        CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto("제주도 여행", 1000000, friends);
        groupGoalService.save(createGroupRequestDto,savedMember1);
    }

    @Test
    public void getInfo(){

        String now = "2021-11-05 00:00:00.000";
        String tomorrow = "2021-11-06 00:00:00.000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        Member member1 = new Member("123456", 1L, "nickname1", "123@email1.com", Hero.hero1);
        Member member2 = new Member("123456", 2L, "nickname2", "123@email2.com", Hero.hero2);
        Member member3 = new Member("123456", 3L, "nickname3", "123@email3.com", Hero.hero3);
        Member member4 = new Member("123456", 4L, "nickname4", "123@email4.com", Hero.hero1);

        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);
        Member savedMember3 = memberRepository.save(member3);
        Member savedMember4 = memberRepository.save(member4);

        List<String> friends = new ArrayList<>();
        friends.add("nickname2");
        friends.add("nickname4");
        CreateGroupRequestDto createGroupRequestDto = new CreateGroupRequestDto("제주도 여행", 1000000, friends);
        GroupGoal savedGroup = groupGoalService.save(createGroupRequestDto, savedMember1);
        savedGroup.setIsAcceptedGroup(true);

        RecordRequestDto recordRequestDto = new RecordRequestDto(RecordType.expense, now, "편의점", 10000);
        recordService.save(recordRequestDto, savedMember1);
        RecordRequestDto recordRequestDto2 = new RecordRequestDto(RecordType.income, now, "설거지 심부름", 10000);
        recordService.save(recordRequestDto2, savedMember1);
        RecordRequestDto recordRequestDto3 = new RecordRequestDto(RecordType.group, now, "가즈아!!!", 20000);
        recordService.save(recordRequestDto3, savedMember1);
        RecordRequestDto recordRequestDto4 = new RecordRequestDto(RecordType.expense, now, "편이점!", 20000);
        recordService.save(recordRequestDto4, savedMember3);
        RecordRequestDto recordRequestDto5 = new RecordRequestDto(RecordType.group, now, "세부!!!!", 50000);
        recordService.save(recordRequestDto5, savedMember2);
        RecordRequestDto recordRequestDto6 = new RecordRequestDto(RecordType.group, tomorrow, "할라봉 가즈아", 200000);
        recordService.save(recordRequestDto6, savedMember4);
//        RecordRequestDto recordRequestDto7 = new RecordRequestDto(RecordType.group, tomorrow, "떠나요 제주도~~", 800000);
//        recordService.save(recordRequestDto7, savedMember1);

        GroupResponseDto groupInfo = groupGoalService.getGroupInfo(savedMember2);
        System.out.println("=============================goal status=================================");
        System.out.println(groupInfo.getGoalStatus());
        System.out.println("=============================member info=================================");
        List<GroupMemberDto> groupMembers = groupInfo.getGroupMembers();
        if(groupMembers != null){
            for (GroupMemberDto groupMember : groupMembers) {
                System.out.print(groupMember.getGroupMemberHero()+" ");
                System.out.print(groupMember.getGroupMemberNickname());
                System.out.println();
            }
        }
        System.out.println("=============================left amount=================================");
        System.out.println(groupInfo.getGroupLeftAmount());
        System.out.println("=============================now percent=================================");
        System.out.println(groupInfo.getGroupNowPercent());
        System.out.println("=============================done goals=================================");
        List<String> groupDoneGoals = groupInfo.getGroupDoneGoals();
        if(!groupDoneGoals.isEmpty()){
            for (String groupDoneGoal : groupDoneGoals) {
                System.out.println(groupDoneGoal);
            }
        }
        System.out.println("=============================내 역=================================");
        List<GroupListDto> groupLists = groupInfo.getGroupLists();
        if(groupLists != null){
            for (GroupListDto groupList : groupLists) {
                System.out.print(groupList.getGroupDate()+" ");
                System.out.print(groupList.getHero()+" ");
                System.out.print(groupList.getNickname()+" ");
                System.out.print(groupList.getGroupMemo()+" ");
                System.out.print(groupList.getGroupAmount());
                System.out.println();
            }
        }
    }
}
