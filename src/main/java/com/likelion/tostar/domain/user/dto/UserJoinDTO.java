package com.likelion.tostar.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * 회원 가입에 사용
 */
@Data
@Builder
public class UserJoinDTO {
    private String profileImage; // 애완 동물 이미지
    @NotNull(message = "이름은 필수 입력 항목입니다.")
    private String userName; // 사용자 이름
    @NotNull(message = "이메일은 필수 입력 항목입니다.")
    private String email;
    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;
    @NotNull(message = "애완동물 이름은 필수 입력 항목입니다.")
    private String petName;
    @NotNull(message = "주인 이름은 필수 입력 항목입니다.")
    private String ownerName;
    private String petGender;

    @NotNull(message = "애완동물 분류는 필수 입력 항목입니다.")
    private String category;

    private LocalDate birthDay; // 애완동물 생일
    private LocalDate starDay; // 별이 된 날
}
