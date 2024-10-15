package com.likelion.tostar.domain.user.converter;

import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import com.likelion.tostar.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final BCryptPasswordEncoder passwordEncoder;

    public User toUser(UserInfoDTO userInfoDTO) {
        return User.builder()
                .userName(userInfoDTO.getUserName())
                .email(userInfoDTO.getEmail())
                .password(passwordEncoder.encode(userInfoDTO.getPassword()))
                .petName(userInfoDTO.getPetName())
                .ownerName(userInfoDTO.getOwnerName())
                .petGender(userInfoDTO.getPetGender())
                .category(userInfoDTO.getCategory())
                .birthday(userInfoDTO.getBirthDay())
                .starDay(userInfoDTO.getStarDay())
                .role("ROLE_USER")
                .build();
    }
}
