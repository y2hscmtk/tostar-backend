package com.likelion.tostar.global.socket;

import java.util.HashSet;
import java.util.Set;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * 웹 소켓 연결, 종료 시 Event Listener
 */
@Component
public class WebSocketHandler {
	private final Set<WebSocketSession> sessions = new HashSet<>();

	/**
	 * 웹 소켓 연결 시
	 */
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		System.out.println("WebSocket connected: " + sessionId);
		// 연결 시 필요한 작업 수행
	}

	/**
	 * 웹 소켓 연결 종료 시
	 */
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		System.out.println("WebSocket disconnected: " + sessionId);
		// 연결 종료 시 필요한 작업 수행
	}
}
