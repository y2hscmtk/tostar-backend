package com.likelion.tostar.domain.articles.repository;

import com.likelion.tostar.domain.articles.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
}
