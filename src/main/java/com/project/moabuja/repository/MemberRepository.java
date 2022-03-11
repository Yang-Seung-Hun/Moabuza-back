package com.project.moabuja.repository;

import com.project.moabuja.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByNickname(String nickname);

}
