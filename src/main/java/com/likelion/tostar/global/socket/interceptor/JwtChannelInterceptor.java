package com.choi76.web_socket_jwt.global.socket.interceptor;

import com.choi76.web_socket_jwt.global.jwt.util.JwtUtil;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    // http://localhost:8080/ws/chat
    // Authorization : Bearer eyJldkjk...
    // 요청시, JWT 인증 수행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader("Authorization");
            log.info("Received Authorization header: {}", authorization);

            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);
                if (!jwtUtil.isExpired(token)) {
                    String email = jwtUtil.getEmail(token); // getEmail 메서드를 통해 이메일 추출
                    accessor.setUser(() -> email); // Principal 등록
                    log.info("User authenticated: {}", email);
                } else {
                    throw new RuntimeException("토큰이 만료되었거나 유효하지 않습니다.");
                }
            } else {
                throw new RuntimeException("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
            }
        }

        return message;
    }
}

