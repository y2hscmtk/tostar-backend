package com.likelion.tostar.domain.user.converter;

import com.likelion.tostar.domain.user.dto.UserInfoRequestDTO;
import com.likelion.tostar.domain.user.dto.UserInfoResponseDTO;
import com.likelion.tostar.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final BCryptPasswordEncoder passwordEncoder;

    public User toUser(UserInfoRequestDTO userInfoRequestDTO) {
        return User.builder()
                .userName(userInfoRequestDTO.getUserName())
                .email(userInfoRequestDTO.getEmail())
                .password(passwordEncoder.encode(userInfoRequestDTO.getPassword()))
                .petName(userInfoRequestDTO.getPetName())
                .ownerName(userInfoRequestDTO.getOwnerName())
                .petGender(userInfoRequestDTO.getPetGender())
                .category(userInfoRequestDTO.getCategory())
                .birthday(userInfoRequestDTO.getBirthDay())
                .starDay(userInfoRequestDTO.getStarDay())
                .role("ROLE_USER")
                .build();
    }

    public UserInfoResponseDTO toUserInfoResponseDTO(User user) {
        return UserInfoResponseDTO.builder()
                .petName(user.getPetName())
                .ownerName(user.getOwnerName())
                .petGender(user.getPetGender())
                .category(user.getCategory())
                .birthDay(user.getBirthday())
                .starDay(user.getStarDay())
                .build();
    }
}
