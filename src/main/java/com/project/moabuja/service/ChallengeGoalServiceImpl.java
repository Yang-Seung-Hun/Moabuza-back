package com.project.moabuja.service;

import com.project.moabuja.domain.friend.Friend;
import com.project.moabuja.domain.goal.*;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.domain.record.Record;
import com.project.moabuja.domain.record.RecordType;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.dto.response.goal.*;
import com.project.moabuja.exception.exceptionClass.MemberNotFoundException;
import com.project.moabuja.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final WaitingGoalRepository waitingGoalRepository;
    private final MemberWaitingGoalRepository memberWaitingGoalRepository;

    @Transactional
    @Override
    public ResponseEntity<String> save(CreateChallengeRequestDto createChallengeRequestDto, Member current) {


        Optional<Member> currentUserTmp = memberRepository.findById(current.getId());
        Member currentUser = null;
        if(currentUserTmp.isPresent()){
            currentUser = currentUserTmp.get();
        }else{
            throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다.");
        }

        ChallengeGoal challengeGoal = new ChallengeGoal(createChallengeRequestDto.getCreateChallengeName(), createChallengeRequestDto.getCreateChallengeAmount(), 0);

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
        return ResponseEntity.ok().body("도전해부자 생성 완료");
    }

    @Override
    public ResponseEntity<ChallengeResponseDto> getChallengeInfo(Member current) {

        //여기는 프록시 생명주기 문제 땜에 필요
        Optional<Member> currentUserTmp = memberRepository.findById(current.getId());
        Member currentUser = currentUserTmp.get();

        Optional<ChallengeGoal> challengeGoal = Optional.ofNullable(currentUser.getChallengeGoal());
        List<String> challengeDoneGoalNames = new ArrayList<>();
        for (DoneGoal doneGoal : currentUser.getDoneGaols()) {
            if (doneGoal.getGoalType() == GoalType.CHALLENGE) {
                challengeDoneGoalNames.add(doneGoal.getDoneGoalName());
            }
        }
        List<MemberWaitingGoal> memberWaitingGoals = currentUser.getMemberWaitingGoals();

        //challengeGoal 있을때
        if (challengeGoal.isPresent()) {
            String goalStatus = "goal";
            List<ChallengeMemberDto> challengeMembers = new ArrayList<>();
            for (Member user : challengeGoal.get().getMembers()) {
                int currentAmount = 0;
                List<Record> records = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, user);
                for (Record record : records) {
                    currentAmount += record.getRecordAmount();
                }
                int leftAmount = challengeGoal.get().getChallengeGoalAmount() - currentAmount;
                int percent = (int) (((double) currentAmount / (double) (challengeGoal.get().getChallengeGoalAmount())) * 100);
                challengeMembers.add(new ChallengeMemberDto(user.getNickname(), user.getHero(), leftAmount, percent));
            }

            List<Record> challengeRecords = recordRepository.findRecordsByRecordTypeAndMember(RecordType.challenge, currentUser);
            List<ChallengeListDto> challengeLists = challengeRecords.stream().map(record -> {
                return new ChallengeListDto(record.getRecordDate(), record.getMemo(), record.getRecordAmount());
            }).collect(Collectors.toList());

            ChallengeResponseDto goalResponseDto = new ChallengeResponseDto(goalStatus, challengeMembers, challengeGoal.get().getChallengeGoalName(), challengeDoneGoalNames, challengeLists,null);

            return ResponseEntity.ok().body(goalResponseDto);
        } else {
            /**
             * 승훈님 고쳐주세요 ㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜ ==> 일단 수정
             */
            if (! memberWaitingGoals.isEmpty()) { //수락대기중
                String goalStatus = "waiting";

                List<WaitingGoalResponseDto> waitingGoals = new ArrayList<>();
                for (MemberWaitingGoal memberWaitingGoal : memberWaitingGoals) {
                    waitingGoals.add(new WaitingGoalResponseDto(memberWaitingGoal.getId(),memberWaitingGoal.getWaitingGoal().getWaitingGoalName()));
                }

                ChallengeResponseDto waitingResponseDto = new ChallengeResponseDto(goalStatus, null, null, challengeDoneGoalNames, null, waitingGoals);

                return ResponseEntity.ok().body(waitingResponseDto);
            } else { //challengeGoal 없을때
                String goalStatus = "noGoal";
                ChallengeResponseDto noGoalResponseDto = new ChallengeResponseDto(goalStatus, null, null, challengeDoneGoalNames, null, null);

                return ResponseEntity.ok().body(noGoalResponseDto);
            }
        }
    }

    @Override
    public ResponseEntity<CreateChallengeResponseDto> getChallengeMemberCandidates(Member currentUser) {

        List<Friend> friends = friendRepository.findFriendsByMember(currentUser);
        List<CreateChallengeMemberDto> challengeMembers = new ArrayList<>();

        if (friends.size() == 0){
            CreateChallengeResponseDto createChallengeResponseDto = new CreateChallengeResponseDto(challengeMembers);
            return ResponseEntity.ok().body(createChallengeResponseDto);
        }

        for(Friend friend : friends){
            //친구의 챌린지 골을 확인
            Optional<Member> friendById = memberRepository.findById(friend.getFriend().getId());
            Optional<ChallengeGoal> friendChallengeGoal = Optional.ofNullable(friendById.get().getChallengeGoal());

            //이미 진행중인 챌린지 있음
            if(friendChallengeGoal.isPresent()) {
                if (friendById.isPresent()) {
                    challengeMembers.add(new CreateChallengeMemberDto(friendById.get().getNickname(), false));
                } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
            } else { // 진행 중인 챌린지 없음
                if (friendById.isPresent()){
                    challengeMembers.add(new CreateChallengeMemberDto(friendById.get().getNickname(),true));
                } else { throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다."); }
            }
        }

        CreateChallengeResponseDto challengeResponseDto = new CreateChallengeResponseDto(challengeMembers);
        return ResponseEntity.ok().body(challengeResponseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<String> exitChallenge(Long id) {

        Optional<WaitingGoal> byId = waitingGoalRepository.findById(id);
        if (byId.isPresent()){
            waitingGoalRepository.delete(byId.get());
        }else{
            throw new MemberNotFoundException("해당 사용자는 존재하지 않습니다.");
        }

        return ResponseEntity.ok().body("도전해부자 취소 완료");
    }
}
