package com.likelion.tostar.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * 회원 정보 열람, 수정에 사용
 */
@Data
@Builder
public class UserInfoDTO {
    @NotNull(message = "애완동물 이름은 필수 입력 사항입니다.")
    private String petName;
    @NotNull(message = "보호자 이름은 필수 입력 사항입니다.")
    private String ownerName;
    private String petGender;
    private String category;
    private LocalDate birthDay; // 애완 동물 생일
    private LocalDate starDay; // 별이 된 날
}
