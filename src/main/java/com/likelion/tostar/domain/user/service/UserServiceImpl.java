package com.likelion.tostar.domain.user.service;

import com.likelion.tostar.domain.user.converter.UserConverter;
import com.likelion.tostar.domain.user.dto.LoginResponseDTO;
import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import com.likelion.tostar.domain.user.dto.UserJoinDTO;
import com.likelion.tostar.domain.user.dto.LoginRequestDTO;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.jwt.util.JwtUtil;
import com.likelion.tostar.global.response.ApiResponse;
import com.likelion.tostar.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserConverter userConverter;
    private final S3Service s3Service;

    /**
     * 로그인
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> login(LoginRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // 비밀 번호 검증
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_CORRECT);
        }

        String accessToken = "Bearer " + jwtUtil.createJwt(user.getEmail(), user.getRole());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);

        // 반환 DTO 생성
        LoginResponseDTO loginResponseDTO = userConverter.toLoginResponseDTO(user, accessToken);

        return ResponseEntity.ok().headers(headers)
                .body(ApiResponse.onSuccess(loginResponseDTO));
    }

    /**
     * 회원 가입
     */
    @Override
    public ResponseEntity<?> join(MultipartFile image, UserJoinDTO userJoinDTO) throws IOException {

        // 동일 username 사용자 생성 방지
        if (userRepository.existsUserByEmail(userJoinDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.onFailure(ErrorStatus._USER_IS_EXISTS, "회원가입에 실패하였습니다."));
        }

        User user = userConverter.toUser(userJoinDTO);

        userRepository.save(user);

        user.changeProfileImage(s3Service.uploadFile(image));

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
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // 회원 정보 반환
        return ResponseEntity.ok()
                .body(ApiResponse.onSuccess(userConverter.toUserInfoDTO(user)));
    }

    /**
     * 회원 개인 정보 수정
     */
    @Override
    public ResponseEntity<?> edit(MultipartFile image, UserInfoDTO userInfoDTO, String email) throws IOException {
        // 해당 회원이 실제로 존재 하는지 확인
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // <Multipart Image>
        // 1. 이미지가 존재할 경우 -> 프로필 변경 요청에 해당 ; 기존 이미지 삭제, 새 이미지 저장
        // 2. 이미지가 존재하지 않을 경우 -> 기본 프로필 변경에 해당 ; 기존 이미지 삭제

        // 기존 프로필 이미지 삭제
        String existingProfileImage = user.getProfileImage();
        // 기존에 이미지가 존재하며, 아직 S3에 존재하는 경우에 해당
        if (existingProfileImage != null && !existingProfileImage.isEmpty()) {
            String fileName = extractFileNameFromUrl(existingProfileImage);
            s3Service.deleteFile(fileName);
        }

        user.changeUserInfo(userInfoDTO); // 회원 정보 수정

        if (!image.isEmpty()) {
            // 새로운 프로필 이미지 업로드
            user.changeProfileImage(s3Service.uploadFile(image)); // 사용자 정보 갱신
        }

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

    // URL에서 파일명 추출
    private String extractFileNameFromUrl(String url) {
        try {
            // URL에서 파일명 부분만 추출 후, +를 공백으로 되돌리기
            String decodedUrl = java.net.URLDecoder.decode(url, "UTF-8");  // URL 디코딩
            return decodedUrl.substring(decodedUrl.lastIndexOf("/") + 1);  // 파일명 추출
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

}
