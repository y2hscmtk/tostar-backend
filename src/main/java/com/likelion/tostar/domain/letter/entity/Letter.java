package com.likelion.tostar.domain.letter.entity;

import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Letter")
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // VARCHAR -> TEXT 처리
    @Lob
    // 받은 편지에 이모지 작성을 위한 utf8mb4 처리
    @Column(nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type")
    private SenderType senderType; // 보낸 편지, 받은 편지 구분을 위한 변수
}
