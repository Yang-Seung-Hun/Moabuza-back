package com.project.moabuja.security.userdetails;

import com.project.moabuja.domain.member.Member;
import com.project.moabuja.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String password) throws UsernameNotFoundException {

        Member member = Optional
                .ofNullable(memberRepository.findByPassword(password))
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음"));

        return new UserDetailsImpl(member);
    }
}
