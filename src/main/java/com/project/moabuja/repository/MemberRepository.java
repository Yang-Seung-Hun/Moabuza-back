package com.project.moabuja.repository;

import com.project.moabuja.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByNickname(String nickname);

    @Query("select m from Member m where m.email  = :email")
    Member findByEmail(@Param("email") String email);

    @Query("select m from Member m where m.password  = :password")
    Member findByPassword(@Param("password") String password);

    @Query("select m from Member m where m.kakaoId  = :kakaoId")
    Member findByKakaoId(@Param("kakaoId") Long kakaoId);

    boolean existsByEmail(@Param("email") String email);

    boolean existsByKakaoId(@Param("kakaoId") Long kakaoId);

    boolean existsByNickname(@Param("nickname") String nickname);

    @Query("select m from Member m where m.email  = :email")
    Optional<Member> findByEmails(@Param("email") String email);

}
