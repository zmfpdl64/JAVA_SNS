package personal.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum Errorcode {
    DUPLICATE_USERNAME(HttpStatus.NOT_FOUND, "회원 아이디가 중복 되었습니다."),
    NOT_EXISTS_USERNAME(HttpStatus.NOT_FOUND, "회원 아이디가 존재하지 않습니다"),
    NOT_MATCH_PASSWORD(HttpStatus.NOT_FOUND, "회원 비밀번호가 일치하지 않습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "회원님은 권한이 없습니다."),
    NOT_EXISTS_POST(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
    ALREADY_LIKE(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다.");
    private final HttpStatus status;
    private final String message;

}
