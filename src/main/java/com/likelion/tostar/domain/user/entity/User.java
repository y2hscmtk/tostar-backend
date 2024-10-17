package com.likelion.tostar.domain.user.entity;

import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import jakarta.persistence.*;
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

    // 회원 정보 수정 메소드
    public void changeUserInfo(UserInfoDTO userInfoDTO) {
        this.petName = userInfoDTO.getPetName();
        this.ownerName = userInfoDTO.getOwnerName();
        this.petGender = userInfoDTO.getPetGender();
        this.category = userInfoDTO.getCategory();
        this.birthday = userInfoDTO.getBirthDay();
        this.starDay = userInfoDTO.getStarDay();
    }
}
