package personal.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum Errorcode {
    DUPLICATE_USERNAME(HttpStatus.UNAUTHORIZED, "회원 아이디가 중복 되었습니다."),
    NOT_MATCH_AUTH(HttpStatus.INTERNAL_SERVER_ERROR, "회원 아이디 비밀번호가 일치하지 않습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다");

    private final HttpStatus status;
    private final String message;

}
