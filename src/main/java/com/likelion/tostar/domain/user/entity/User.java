package com.likelion.tostar.domain.user.entity;

import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.entity.Member;
import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String userName; // 사용자 이름
    private String email; // JWT 검증에서 사용자 이름으로 동작
    private String password;
    @Column(name = "pet_name")
    private String petName; // 애완동물 이름
    @Column(name = "owner_name")
    private String ownerName; // 주인으로서의 이름
    @Column(name = "pet_gender")
    private String petGender;
    private String category;
    private LocalDate birthday;
    @Column(name = "profile_image")
    private String profileImage;
    @Column(name = "star_day")
    private LocalDate starDay;
    private String role; // 사용자 권한


    //====== 연관 매핑 ======//

    @Builder.Default
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Community> myCommunities = new ArrayList<>();



    //====== 편의 메소드 ======//
    public void createCommunity(Community community) {
        // 1. 방장 지정
        community.changeOwner(this);

        // 2. 해당 User를 커뮤니티의 첫 번째 회원으로 등록
        Member member = Member.builder()
                .communityMember(this)
                .community(community)
                .build();
        community.getCommunityMembers().add(member); // member save by CASCADE.ALL

        // 3. 내가 만든 커뮤니티 목록에 저장
        this.myCommunities.add(community);
    }

    // 회원 정보 수정 메소드
    public void changeUserInfo(UserInfoDTO userInfoDTO) {
        this.petName = userInfoDTO.getPetName();
        this.ownerName = userInfoDTO.getOwnerName();
        this.petGender = userInfoDTO.getPetGender();
        this.category = userInfoDTO.getCategory();
        this.birthday = userInfoDTO.getBirthDay();
        this.starDay = userInfoDTO.getStarDay();
    }

    // 프로필 이미지 저장
    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
