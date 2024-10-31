package com.likelion.tostar.domain.chat.repository;

import com.likelion.tostar.domain.chat.entity.CommunityChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityChatRepository extends JpaRepository<CommunityChat,Long> {
}
