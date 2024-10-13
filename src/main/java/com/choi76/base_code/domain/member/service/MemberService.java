package com.choi76.base_code.domain.member.service;

import com.choi76.base_code.domain.member.dto.JoinDTO;
import com.choi76.base_code.domain.member.dto.LoginRequestDTO;
import com.choi76.base_code.domain.member.entity.Member;
import com.choi76.base_code.domain.member.repository.MemberRepository;
import com.choi76.base_code.global.enums.statuscode.ErrorStatus;
import com.choi76.base_code.global.exception.GeneralException;
import com.choi76.base_code.global.jwt.util.JwtUtil;
import com.choi76.base_code.global.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    @Transactional
    public ResponseEntity<?> login(LoginRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 비밀번호 검증
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_CORRECT);
        }

        String accessToken = jwtUtil.createJwt(member.getEmail(), member.getRole());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // JWT 발급 성공시 Header에 삽입하여 반환

        return ResponseEntity.ok().headers(headers)
                .body(ApiResponse.onSuccess("Bearer " + accessToken));
    }

    public ResponseEntity<?> join(JoinDTO joinDTO) {

        // 동일 username 사용자 생성 방지
        if (memberRepository.existsMemberByEmail(joinDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.onFailure(ErrorStatus._MEMBER_IS_EXISTS, "회원가입에 실패하였습니다."));
        }

        // 새로운 회원 생성 - OAuth2 를 통한 회원가입을 수행할 경우 비밀번호는 저장하지 않아야함
        Member member = Member.builder()
                .email(joinDTO.getEmail())
                .password(passwordEncoder.encode(joinDTO.getPassword())) // 암호화 후 저장
                .role("ROLE_USER") // 사용자 권한 설정 접두사 ROLE 작성 필요
                .build();
        memberRepository.save(member);

        String accessToken = jwtUtil.createJwt(member.getEmail(), member.getRole());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok().headers(headers)
                .body(ApiResponse.onSuccess("Bearer " + accessToken));
    }
}
