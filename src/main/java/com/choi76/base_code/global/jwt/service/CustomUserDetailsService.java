package com.choi76.base_code.global.jwt.service;

import com.choi76.base_code.domain.member.entity.Member;
import com.choi76.base_code.domain.member.repository.MemberRepository;
import com.choi76.base_code.global.enums.statuscode.ErrorStatus;
import com.choi76.base_code.global.exception.GeneralException;
import com.choi76.base_code.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// userDetails를 생성하여 반환
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        // email로 회원 조회 후 UserDetails 객체 생성 -> JWT Filter에서 검증시 사용
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        return new CustomUserDetails(member);
    }
}
