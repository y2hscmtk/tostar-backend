package com.likelion.tostar.domain.chat.repository;

import com.likelion.tostar.domain.chat.entity.CommunityChat;
import com.likelion.tostar.domain.community.entity.Community;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityChatRepository extends JpaRepository<CommunityChat,Long> {
    List<CommunityChat> findByCommunity(Community community);
}
