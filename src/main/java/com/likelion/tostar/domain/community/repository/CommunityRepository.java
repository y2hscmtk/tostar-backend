package com.likelion.tostar.domain.community.repository;


import com.likelion.tostar.domain.community.entity.Community;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Long, Community> {
    /**
     * 커뮤니티 미리보기 랜덤 3개 반환
     */
    @Query(value = "SELECT * FROM Community ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Community> getRandomPreviews();
}
