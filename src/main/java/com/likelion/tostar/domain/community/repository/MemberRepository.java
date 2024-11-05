package com.likelion.tostar.domain.community.repository;

import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.entity.mapping.Member;
import com.likelion.tostar.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 회원이 커뮤니티 회원인지 확인
    @Query("SELECT m FROM Member m WHERE m.community = :community AND m.communityMember = :communityMember")
    Optional<Member> findMembership(@Param("community") Community community, @Param("communityMember") User communityMember);

    @Query("SELECT c FROM Member m JOIN m.community c WHERE m.communityMember = :user")
    Page<Community> findMyCommunities(@Param("user") User user, Pageable pageable);
}

