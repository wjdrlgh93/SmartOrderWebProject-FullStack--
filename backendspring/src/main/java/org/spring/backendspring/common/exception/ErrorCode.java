package org.spring.backendspring.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 🔹 Member
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원이 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    ALREADY_REGISTERED_LOCAL_USER(HttpStatus.CONFLICT, "이미 일반 회원으로 가입된 사용자입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // Refresh ErrorCode
    REFRESH_TOKEN_MISSING(HttpStatus.BAD_REQUEST, "refresh 토큰이 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "refresh 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh 토큰이 아닙니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "access 토큰이 만료되었습니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 토큰 서명입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 토큰이 DB에 존재하지 않습니다."),

    //crew
    CREW_NOT_FOUND(HttpStatus.NOT_FOUND, "크루가 존재하지 않습니다."),
    //crewJoin
    CREW_JOIN_ALREADY_MEMBER(HttpStatus.CONFLICT, "이미 해당 크루 멤버입니다."),
    CREW_JOIN_REQUEST_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 크루 가입 요청 대기중입니다."),
    CREW_JOIN_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "가입 신청이 존재하지 않습니다."),
    CREW_JOIN_REQUEST_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "이미 처리된 가입 신청입니다."),
    CREW_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "크루 권한이 없습니다."),
    //crewMember
    CREW_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "크루원이 존재하지 않습니다."),
    //crewRun, crewRunMember
    CREW_RUN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 런닝 스케줄이 존재하지 않습니다."),
    CREW_RUN_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 런닝에 참가했습니다."),
    CREW_RUN_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 크루 런닝 스케줄에 참가하지 않았습니다."),
    RUN_TIME_REQUIRED(HttpStatus.BAD_REQUEST, "시작/종료 시간이 없는데 어떻게 함?"),
    RUN_START_BEFORE_NOW(HttpStatus.BAD_REQUEST, "시작 시간이 이미 지난 시간인데 어떻게 함?"),
    RUN_END_BEFORE_NOW(HttpStatus.BAD_REQUEST, "끝나는 시간이 이미 지난 시간인데 어떻게 함?"),
    RUN_END_NOT_AFTER_START(HttpStatus.BAD_REQUEST, "끝나는 시간이 시작 시간보다 빠르거나 같은데 어떻게 함?");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
