package com.likelion.tostar.domain.article.repository;

import com.likelion.tostar.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    // 회원 ID로 게시글 조회
    @Query("SELECT a FROM Article a WHERE a.memberId = :userId ORDER BY a.createdAt DESC")
    List<Article> findAllByUserId(Long userId);

    // 나/친구들을 제외한 다른 회원들 게시글 조회
    @Query("SELECT a FROM Article a WHERE a.memberId <> ?1")
    List<Article> findAllOthers(Pageable pageable);
}