package com.likelion.tostar.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// 펫 이름으로 회원 검색 후 리턴해줄 때 사용
public class UserSearchDto {
    private Long id;
    private String petName;
    private String profileImage;
    private String category;
    private String birthday;
    private String starDay;
}
