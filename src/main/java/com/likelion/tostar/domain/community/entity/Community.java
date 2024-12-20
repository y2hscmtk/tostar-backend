package com.likelion.tostar.domain.community.entity;

import com.likelion.tostar.domain.chat.entity.CommunityChat;
import com.likelion.tostar.domain.community.dto.CommunityFormDTO;
import com.likelion.tostar.domain.community.entity.mapping.Member;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Community")
public class Community extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // 커뮤니티 생성자 - 방장
    @Column(unique = true) // 커뮤니티 이름은 고유
    private String title; // 커뮤니티 제목
    private String description; // 커뮤니티 설명
    @Column(name = "profile_image")
    private String profileImage;

    //=== 연관 매핑 ===//

    // 커뮤니티 멤버들
    @Builder.Default
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<Member> communityMembers = new ArrayList<>();

    // 커뮤니티에서 주고받은 채팅 내역
    @Builder.Default
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<CommunityChat> communityChats = new ArrayList<>();

    // ==== 편의 메소드 ==== //
    public void addMember(User user) {
        Member newMember = Member.builder()
                .communityMember(user)
                .community(this)
                .build();
        this.communityMembers.add(newMember);
    }

    public void deleteMember(Member member) {
        this.communityMembers.remove(member);
    }


    public void changeOwner(User user) {
        this.owner = user;
    }

    // 커뮤니티 정보 수정
    public void changeCommunityInfo(CommunityFormDTO communityFormDTO) {
        this.title = communityFormDTO.getTitle();
        this.description = communityFormDTO.getDescription();
    }

    // 이미지 수정
    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
