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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type")
    private SenderType senderType; // 보낸 편지, 받은 편지 구분을 위한 변수

    //====== 편의 메소드 ======//
    // content를 100자까지 자르는 메서드
    public String truncate100Content(String content) {
        return content.length() > 100 ? content.substring(0, 100) + "..." : content;
    }
}
