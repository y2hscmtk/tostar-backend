package com.likelion.tostar.domain.community.repository;


import com.likelion.tostar.domain.community.entity.Community;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    /**
     * 커뮤니티 미리보기 랜덤 3개 반환
     */
    @Query(value = "SELECT * FROM community ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Community> getRandomPreviews();

    Optional<Community> findByTitle(String title);
}
