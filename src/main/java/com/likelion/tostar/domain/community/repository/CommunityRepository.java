package com.likelion.tostar.domain.community.repository;


import com.likelion.tostar.domain.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Long, Community> {
}
