package com.choi76.base_code.domain.member.repository;

import com.choi76.base_code.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Boolean existsMemberByEmail(String email);
    Optional<Member> findMemberByEmail(String email);
}
