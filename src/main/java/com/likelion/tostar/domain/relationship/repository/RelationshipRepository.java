package com.likelion.tostar.domain.relationship.repository;

import com.likelion.tostar.domain.relationship.entity.Relationship;
import com.likelion.tostar.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    // 해당 follower, followee를 가진 relationship 탐색
    Optional<Relationship> findByFollowerAndFollowee(User follower, User followee);

    // 해당 followerId을 가진 모든 relationship 반환
    List<Relationship> findAllByFollower_Id(Long followerId);

}
