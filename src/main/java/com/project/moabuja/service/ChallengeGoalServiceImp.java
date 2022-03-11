package com.project.moabuja.service;

import com.project.moabuja.domain.goal.ChallengeGoal;
import com.project.moabuja.domain.member.Member;
import com.project.moabuja.dto.request.goal.CreateChallengeRequestDto;
import com.project.moabuja.repository.ChallengeGoalRepository;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeGoalServiceImp implements ChallengeGoalService{

    private final MemberRepository memberRepository;
    private final ChallengeGoalRepository challengeGoalRepository;

    @Transactional
    @Override
    public ChallengeGoal save(CreateChallengeRequestDto createChallengeRequestDto) {


        ChallengeGoal challengeGoal = new ChallengeGoal(createChallengeRequestDto.getCreateChallengeName(),
                                                        createChallengeRequestDto.getCreateChallengeAmount(),
                                                        0,
                                                        false);

        for(String name :createChallengeRequestDto.getChallengeFiends()){
            Optional<Member> memberByNickname = memberRepository.findMemberByNickname(name);
            challengeGoal.addMember(memberByNickname.get());
        }

        challengeGoalRepository.save(challengeGoal);

        return challengeGoal;
    }
}
