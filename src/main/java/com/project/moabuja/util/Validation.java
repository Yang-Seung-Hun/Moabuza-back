package com.project.moabuja.util;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.repository.MemberRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Validation {

    private MemberRepository memberRepository;

    public Member memberValidation(Member currentMember) {
        Optional<Member> member = Optional.ofNullable(memberRepository.findByEmails(currentMember.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보가 없습니다.")
        ));
        return member.get();
    }
}
