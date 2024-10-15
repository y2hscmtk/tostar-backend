package com.likelion.tostar.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    @NotNull(message = "이메일 입력은 필수입니다.")
    @Email
    private String email;
    @NotNull(message = "패스워드 입력은 필수입니다.")
    private String password;
}
