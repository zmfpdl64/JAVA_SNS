package personal.sns.exception.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import personal.sns.controller.response.Response;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvicer {

    @ExceptionHandler(SnsException.class)
    public ResponseEntity<?> applicationHandler(SnsException e){
        return ResponseEntity.status(e.getErrorcode().getStatus())
                .body(Response.error(e.getErrorcode().name()));
    }
}
