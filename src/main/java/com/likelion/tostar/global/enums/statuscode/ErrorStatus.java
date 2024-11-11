package com.likelion.tostar.global.enums.statuscode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorStatus implements BaseCode {

	// 공통 오류
	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
	_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

	// User Error
	_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "해당하는 사용자를 찾을 수 없습니다."),
	PASSWORD_NOT_CORRECT(HttpStatus.FORBIDDEN, "USER4002", "비밀번호가 일치하지 않습니다."),
	_USER_IS_EXISTS(HttpStatus.FORBIDDEN, "USER4003", "해당하는 사용자가 이미 존재합니다."),

	// Friend Error
	_FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "FRIEND4041", "친구 ID에 해당하는 회원이 없습니다."),
	_FRIEND_ALREADY_EXISTS(HttpStatus.CONFLICT, "FRIEND4091", "이미 친구로 등록된 사용자입니다."),
	_SELF_FRIEND_REQUEST_NOT_ALLOWED(HttpStatus.CONFLICT, "FRIEND4092", "자기 자신과는 친구를 맺을 수 없습니다"),

	// Member Error
	_MEMBER_ALREADY_JOINED(HttpStatus.FORBIDDEN, "MEMBER4001", "이미 커뮤니티에 존재하는 회원입니다."),
	_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4002", "이미 탈퇴되었거나 커뮤니티에 존재하지 않는 회원입니다."),

	// Article Error
	_ARTICLE_TITLE_MISSING(HttpStatus.BAD_REQUEST, "ARTICLE4001", "제목을 입력해 주세요."),
	_ARTICLE_CONTENT_MISSING(HttpStatus.BAD_REQUEST, "ARTICLE4002", "내용을 입력해 주세요."),
	_NOT_OWNER_OF_ARTICLE(HttpStatus.FORBIDDEN, "ARTICLE4031", "해당 추억(게시글)의 주인이 아닙니다."),
	_ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4041", "해당 추억을 찾을 수 없습니다."),


	// Letter Error
	_LETTER_CONTENT_MISSING(HttpStatus.BAD_REQUEST, "LETTER4001", "편지 내용을 입력해 주세요."),
	_NOT_OWNER_OF_LETTER(HttpStatus.FORBIDDEN, "LETTER4031", "해당 편지의 주인이 아닙니다."),
	_LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER4041", "해당 편지를 찾을 수 없습니다."),
	_OPENAI_RESPONSE_NOT_RECEIVED(HttpStatus.BAD_GATEWAY, "LETTER502", "OpenAI 응답이 없습니다."),

	// S3 Error
	_S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S35001", "S3에 파일 업로드 중 오류가 발생했습니다."),
	_S3_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S35002", "S3 클라이언트 오류가 발생했습니다."),
	_S3_FILE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S35003", "파일 처리 중 오류가 발생했습니다."),
	_S3_REMOVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S35004", "S3 파일 삭제 중 오류가 발생하였습니다."),
	_ARTICLE_TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST, "ARTICLE4003", "최대 5개의 이미지까지 업로드할 수 있습니다."),

	// Community
	_COMMUNITY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMUNITY4001", "삭제되었거나 존재하지 않는 커뮤니티입니다."),
	_DUPLICATE_COMMUNITY_TITLE(HttpStatus.FORBIDDEN, "COMMUNITY4002", "해당하는 이름의 커뮤니티가 이미 존재합니다. 다른 이름으로 작성해주세요."),


	// Resource Error
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE4001", "잘못된 api 요청입니다. " + "요청 형식을 다시 확인해주세요." +
			"반복적인 오류 발생시 관리자에게 문의해주세요."),

	// 로그인 실패 사유
	INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH4001", "아이디 또는 비밀번호가 잘못되었습니다."),

	// JWT Error
	TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "TOKEN4001", "토큰이 없거나 만료 되었습니다."),
	TOKEN_NO_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "TOKEN4002", "토큰에 권한이 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	// implement of BaseCode
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public Integer getStatusValue() {
		return httpStatus.value();
	}
}
