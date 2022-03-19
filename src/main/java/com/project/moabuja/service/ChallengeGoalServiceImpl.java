package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.goal.DoneGoal;
import com.project.moabuja.domain.goal.DoneGoalType;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.*;
import com.project.moabuja.repository.ChallengeGoalRepository;
import com.project.moabuja.repository.FriendRepository;
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

public class ChallengeGoalServiceImpl implements ChallengeGoalService{

    private final MemberRepository memberRepository;
    private final ChallengeGoalRepository challengeGoalRepository;
    private final RecordRepository recordRepository;
    private final FriendRepository friendRepository;

    @Transactional
    @Override
    public ChallengeGoal save(CreateChallengeRequestDto createChallengeRequestDto, Member current) {

        Optional<Member> currentUserTmp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTmp.get();

        ChallengeGoal challengeGoal = new ChallengeGoal(createChallengeRequestDto.getCreateChallengeName(), createChallengeRequestDto.getCreateChallengeAmount(), 0, false);

        for(String name :createChallengeRequestDto.getChallengeFriends()){
            Optional<Member> memberByNickname = memberRepository.findMemberByNickname(name);
            if (memberByNickname.isPresent()) {
                challengeGoal.addMember(memberByNickname.get());
            } else {
                throw new IllegalArgumentException("선택하신 친구 중 존재하지 않는 사용자가 있습니다.");
            }
        }

        //member랑 challengegoal 연관관계 맺음
        ChallengeGoal savedGoal = challengeGoalRepository.save(challengeGoal);
        savedGoal.addMember(currentUser);
        return savedGoal;
    }

    @Override
    public ChallengeResponseDto getChallengeInfo(Member current) {

        //여기는 프록시 생명주기 문제 땜에 필요
        Optional<Member> currentUserTmp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTmp.get();

        Optional<ChallengeGoal> challengeGoal = Optional.ofNullable(currentUser.getChallengeGoal());
        List<String> challengeDoneGoalNames = new ArrayList<>();
        for(DoneGoal doneGoal:currentUser.getDoneGaols()){
            if(doneGoal.getDoneGoalType() == DoneGoalType.CHALLENGE){
                challengeDoneGoalNames.add(doneGoal.getDoneGoalName());
            }
        }

        //challengeGoal 있을때
        if (challengeGoal.isPresent()){
            if(challengeGoal.get().isAcceptedChallenge()){
                String goalStatus = "goal";
                List<ChallengeMemberDto> challengeMembers = new ArrayList<>();
                for(Member user: challengeGoal.get().getMembers()){
                    int currentAmount = 0;
                    List<Record> records = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, user);
                    for (Record record:records){
                        currentAmount += record.getRecordAmount();
                    }
                    int leftAmount = challengeGoal.get().getChallengeGoalAmount() - currentAmount;
                    int percent = (int)(((double)currentAmount/(double)(challengeGoal.get().getChallengeGoalAmount())) * 100);
                    challengeMembers.add(new ChallengeMemberDto(user.getNickname(),user.getHero(),leftAmount,percent));
                }

                List<Record> challengeRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, currentUser);
                List<ChallengeListDto> challengeLists = challengeRecords.stream().map(record -> {
                    return new ChallengeListDto(record.getRecordDate(), record.getMemo(), record.getRecordAmount());
                }).collect(Collectors.toList());

                return new ChallengeResponseDto(challengeGoal.get().getId(),goalStatus,challengeMembers,challengeGoal.get().getChallengeGoalName(),challengeDoneGoalNames, challengeLists);

            }
            else{//수락대기중
                String goalStatus = "waiting";
                return new ChallengeResponseDto(challengeGoal.get().getId(), goalStatus,null,null,challengeDoneGoalNames,null);
            }
        }
        //challengeGoal 없을때
        else{
            String goalStatus = "noGoal";
            return new ChallengeResponseDto(null,goalStatus,null,null,challengeDoneGoalNames,null);
        }

    }

    @Override
    public CreateChallengeResponseDto getChallengeMemberCandidates(Member currentUser) {

        List<Friend> friends = friendRepository.findFriendsByMember(currentUser);
        List<CreateChallengeMemberDto> challengeMembers = new ArrayList<>();

        if (friends.size() == 0){
            return new CreateChallengeResponseDto(challengeMembers);
        }

        for(Friend friend : friends){
            //친구의 챌린지 골을 확인
            Optional<Member> friendById = memberRepository.findById(friend.getFriend().getId());
            Optional<ChallengeGoal> friendChallengeGoal = Optional.ofNullable(friendById.get().getChallengeGoal());

            if(friendChallengeGoal.isPresent()){
                //이미 진행중인 챌린지 있음
                if(friendChallengeGoal.get().isAcceptedChallenge()){
                    if (friendById.isPresent()){
                        challengeMembers.add(new CreateChallengeMemberDto(friendById.get().getNickname(),false));
                    }
                }
                //진행중인 챌린지 없고, 대기만 있음
                else{
                    if(friendById.isPresent()){
                        challengeMembers.add(new CreateChallengeMemberDto(friendById.get().getNickname(),true));
                    }
                }
            }
            else{//초대받은 챌린지 없고 진행중인것도 없을때
                if (friendById.isPresent()){
                    challengeMembers.add(new CreateChallengeMemberDto(friendById.get().getNickname(),true));
                }
            }
        }

        return new CreateChallengeResponseDto(challengeMembers);
    }

    @Override
    @Transactional
    public void exitChallenge(Long id) {

        Optional<ChallengeGoal> challengeGoal = challengeGoalRepository.findById(id);
        if(challengeGoal.isPresent() && !challengeGoal.get().isAcceptedChallenge()){

            List<Member> members = challengeGoal.get().getMembers();
            while (members.size() > 0){
                challengeGoal.get().removeMember(members.get(0));
            }
            challengeGoalRepository.deleteChallengeGoalById(id);
        }
    }
}
