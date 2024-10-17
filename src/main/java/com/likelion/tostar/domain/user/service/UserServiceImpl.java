package com.likelion.tostar.domain.user.service;

import com.likelion.tostar.domain.user.converter.UserConverter;
import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import com.likelion.tostar.domain.user.dto.UserJoinDTO;
import com.likelion.tostar.domain.user.dto.LoginRequestDTO;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.jwt.util.JwtUtil;
import com.likelion.tostar.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserConverter userConverter;

    /**
     * 로그인
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> login(LoginRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 비밀 번호 검증
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_CORRECT);
        }

        return getJwtResponseEntity(user);
    }

    /**
     * 회원 가입
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> join(UserJoinDTO userJoinDTO) {

        // 동일 username 사용자 생성 방지
        if (userRepository.existsUserByEmail(userJoinDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.onFailure(ErrorStatus._MEMBER_IS_EXISTS, "회원가입에 실패하였습니다."));
        }

        User user = userConverter.toUser(userJoinDTO);
        userRepository.save(user);

        return getJwtResponseEntity(user);
    }

    /**
     * 회원 개인 정보 열람
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> info(String email) {
        // 해당 회원이 실제로 존재 하는지 확인
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 회원 정보 반환
        return ResponseEntity.ok()
                .body(ApiResponse.onSuccess(userConverter.toUserInfoDTO(user)));
    }

    /**
     * 회원 개인 정보 수정
     */
    @Override
    public ResponseEntity<?> edit(UserInfoDTO userInfoDTO, String email) {
        // 해당 회원이 실제로 존재 하는지 확인
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        // 회원 정보 수정
        user.changeUserInfo(userInfoDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess("회원정보가 수정되었습니다."));
    }

    // 회원 가입 & 로그인 성공시 JWT 생성 후 반환
    public ResponseEntity<?> getJwtResponseEntity(User user) {
        String accessToken = jwtUtil.createJwt(user.getEmail(), user.getRole());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok().headers(headers)
                .body(ApiResponse.onSuccess("Bearer " + accessToken));
    }
}
