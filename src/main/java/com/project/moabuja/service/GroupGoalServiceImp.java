package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.DoneGoalType;
import com.project.moabuja.domain.goal.GroupGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateGroupRequestDto;
import com.project.moabuja.dto.response.goal.*;
import com.project.moabuja.repository.FriendRepository;
import com.project.moabuja.repository.GroupGoalRepository;
import com.project.moabuja.repository.MemberRepository;
import com.project.moabuja.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupGoalServiceImp implements GroupGoalService{

    private final MemberRepository memberRepository;
    private final GroupGoalRepository groupGoalRepository;
    private final RecordRepository recordRepository;
    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public GroupGoal save(CreateGroupRequestDto groupRequestDto, Member current) {

        Optional<Member> currentUserTmp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTmp.get();

        GroupGoal groupGoal = new GroupGoal(groupRequestDto.getCreateGroupName(), groupRequestDto.getCreateGroupAmount(), 0, false);

        //member랑 groupGoal 연관관계 맺음
        GroupGoal savedGoal = groupGoalRepository.save(groupGoal);
        Optional<GroupGoal> goal = groupGoalRepository.findById(savedGoal.getId());
        goal.get().addMember(currentUser);

        for(String name :groupRequestDto.getGroupFiends()){
            Optional<Member> memberByNickname = memberRepository.findMemberByNickname(name);
            goal.get().addMember(memberByNickname.get());
        }
        return savedGoal;
    }

    @Override
    public GroupResponseDto getGroupInfo(Member current) {

        Optional<Member> currentUserTmp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTmp.get();

        Optional<GroupGoal> groupGoal = Optional.ofNullable(currentUser.getGroupGoal());
        List<String> groupDoneGoalNames = new ArrayList<>();
        for(DoneGoal doneGoal:currentUser.getDoneGaols()){
            if(doneGoal.getDoneGoalType() == DoneGoalType.GROUP){
                groupDoneGoalNames.add(doneGoal.getDoneGoalName());
            }
        }

        //GroupGoal 있을때
        if (groupGoal.isPresent()){
            if(groupGoal.get().isAcceptedGroup()){

                String goalStatus = "goal";

                List<GroupMemberDto> groupMembers = new ArrayList<>();
                List<GroupListDto> groupList = new ArrayList<>();
                List<Member> members = groupGoal.get().getMembers();
                int currentAmount = 0;
                for (Member member : members) {
                    groupMembers.add(new GroupMemberDto(member.getNickname(), member.getHero()));

                    List<Record> memberGroupRecord = recordRepository.findRecordsByRecordTypeAndMember(RecordType.group, member);
                    int tmpAmount = 0;
                    for (Record record : memberGroupRecord) {
                        groupList.add(new GroupListDto(record.getRecordDate(), member.getHero(), member.getNickname(),record.getMemo(),record.getRecordAmount()));
                        tmpAmount = tmpAmount+record.getRecordAmount();
                    }
                    currentAmount += tmpAmount;
                }
                int leftAmount = groupGoal.get().getGroupGoalAmount() - currentAmount;
                int percent = (int) (((double) currentAmount / (double) (groupGoal.get().getGroupGoalAmount())) * 100);

                return new GroupResponseDto(groupGoal.get().getId(),goalStatus,groupMembers,groupGoal.get().getGroupGoalName(),leftAmount,percent,groupDoneGoalNames,groupList);

            }
            else{//수락대기중
                String goalStatus = "waiting";
                return new GroupResponseDto(groupGoal.get().getId(),goalStatus,null,null,0,0,groupDoneGoalNames,null);
            }
        }
        //challengeGoal 없을때
        else{
            String goalStatus = "noGoal";
            return new GroupResponseDto(null,goalStatus,null,null,0,0,groupDoneGoalNames,null);
        }
    }

    @Override
    public CreateGroupResponseDto getGroupMemberCandidates(Member currentUser) {

        List<Friend> friends = friendRepository.findFriendsByMember(currentUser);
        List<CreateGroupMemberDto> groupMembers = new ArrayList<>();

        if (friends.size() == 0){
            return new CreateGroupResponseDto(groupMembers);
        }

        for(Friend friend : friends){
            //친구의 그룹 골을 확인
            Optional<Member> friendById = memberRepository.findById(friend.getFriend());
            Optional<GroupGoal> friendGroupGoal = Optional.ofNullable(friendById.get().getGroupGoal());

            if(friendGroupGoal.isPresent()){
                //이미 진행중인 챌린지 있음
                if(friendGroupGoal.get().isAcceptedGroup()){
                    if (friendById.isPresent()){
                        groupMembers.add(new CreateGroupMemberDto(friendById.get().getNickname(),false));
                    }
                }
                //진행중인 챌린지 없고, 대기만 있음
                else{
                    if(friendById.isPresent()){
                        groupMembers.add(new CreateGroupMemberDto(friendById.get().getNickname(),true));
                    }
                }
            }
            else{//초대받은 챌린지 없고 진행중인것도 없을때
                if (friendById.isPresent()){
                    groupMembers.add(new CreateGroupMemberDto(friendById.get().getNickname(),true));
                }
            }
        }

        return new CreateGroupResponseDto(groupMembers);
    }

    @Override
    @Transactional
    public void exitChallenge(Long id) {

        Optional<GroupGoal> groupGoal = groupGoalRepository.findById(id);
        if(groupGoal.isPresent() && !groupGoal.get().isAcceptedGroup()){

            List<Member> members = groupGoal.get().getMembers();
            while (members.size() > 0){
                groupGoal.get().removeMember(members.get(0));
            }
            groupGoalRepository.deleteGroupGoalById(id);
        }
    }
}
