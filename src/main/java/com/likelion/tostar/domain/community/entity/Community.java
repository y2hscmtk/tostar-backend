package com.likelion.tostar.domain.community.entity;

import com.likelion.tostar.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Community")
public class Community {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // 커뮤니티 생성자 - 방장
    private String title; // 커뮤니티 제목
    private String description; // 커뮤니티 설명
    @Column(name = "profile_image")
    private String profileImage;
}
