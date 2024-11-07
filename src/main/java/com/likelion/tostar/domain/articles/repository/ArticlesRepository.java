package com.likelion.tostar.domain.articles.repository;

import com.likelion.tostar.domain.chat.entity.CommunityChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository extends JpaRepository<CommunityChat,Long> {
}
