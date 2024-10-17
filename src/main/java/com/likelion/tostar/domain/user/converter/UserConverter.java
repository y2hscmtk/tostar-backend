package com.likelion.tostar.domain.user.converter;

import com.likelion.tostar.domain.user.dto.UserJoinDTO;
import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import com.likelion.tostar.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final BCryptPasswordEncoder passwordEncoder;

    public User toUser(UserJoinDTO userJoinDTO) {
        return User.builder()
                .profileImage(userJoinDTO.getProfileImage())
                .userName(userJoinDTO.getUserName())
                .email(userJoinDTO.getEmail())
                .password(passwordEncoder.encode(userJoinDTO.getPassword()))
                .petName(userJoinDTO.getPetName())
                .ownerName(userJoinDTO.getOwnerName())
                .petGender(userJoinDTO.getPetGender())
                .category(userJoinDTO.getCategory())
                .birthday(userJoinDTO.getBirthDay())
                .starDay(userJoinDTO.getStarDay())
                .role("ROLE_USER")
                .build();
    }

    public UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .petName(user.getPetName())
                .ownerName(user.getOwnerName())
                .petGender(user.getPetGender())
                .category(user.getCategory())
                .birthDay(user.getBirthday())
                .starDay(user.getStarDay())
                .build();
    }
}
