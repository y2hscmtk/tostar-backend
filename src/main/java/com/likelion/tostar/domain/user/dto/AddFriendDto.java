package com.likelion.tostar.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddFriendDto {
    private Long friendId;
}
