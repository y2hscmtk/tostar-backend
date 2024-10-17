package com.likelion.tostar.domain.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * 회원 정보 열람, 수정에 사용
 */
@Data
@Builder
public class UserInfoDTO {
    private String petName;
    private String ownerName;
    private String petGender;
    private String category;
    private LocalDate birthDay; // 애완 동물 생일
    private LocalDate starDay; // 별이 된 날
}
