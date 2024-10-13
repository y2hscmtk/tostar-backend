package com.likelion.tostar.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinDTO {
    private String email;
    private String password;
}
