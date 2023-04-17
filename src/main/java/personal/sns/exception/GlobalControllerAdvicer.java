package personal.sns.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import personal.sns.controller.response.Response;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvicer {

    @ExceptionHandler(SnsException.class)
    public ResponseEntity<?> applicationHandler(SnsException e){
        log.error("Error: {}", e.getMessage());
        return ResponseEntity.status(e.getErrorcode().getStatus())
                .body(Response.error(e.getErrorcode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeHandler(RuntimeException e) {
        log.error("Error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(Errorcode.INTERNAL_SERVER_ERROR.name()));
    }

}
