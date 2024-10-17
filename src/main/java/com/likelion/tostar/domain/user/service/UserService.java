package com.likelion.tostar.domain.user.service;

import com.likelion.tostar.domain.user.dto.LoginRequestDTO;
import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> login(LoginRequestDTO dto);
    ResponseEntity<?> join(UserInfoDTO userInfoDTO);
    ResponseEntity<?> info(String email);
}
