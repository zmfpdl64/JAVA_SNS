package personal.sns.exception.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum Errorcode {
    DUPLICATE_USERNAME(HttpStatus.UNAUTHORIZED, "회원 아이디가 중복 되었습니다.");


    private final HttpStatus status;
    private final String message;

}
