package com.likelion.tostar.domain.chat.entity;

import com.likelion.tostar.domain.chat.entity.enums.MessageType;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_chat")
public class CommunityChat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_chat_id")
    private Long id;
    private String content; // 채팅 메시지

    @Enumerated(EnumType.STRING)
    private MessageType type; // 채팅, 공지 여부

    // 연관 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    public static CommunityChat toCommunityChat(String content, MessageType messageType,
                                         Community community, User sender) {
        return CommunityChat.builder()
                .content(content)
                .community(community)
                .sender(sender)
                .type(messageType)
                .build();
    }
}
