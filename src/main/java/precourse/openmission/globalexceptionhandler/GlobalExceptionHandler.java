package precourse.openmission.globalexceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller 계층에서의 예외 발생을 가로채서 해결하는 클래스입니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Service에서 발생한 에러 코드를, HTTP Status 400으로 변환합니다.
     * 유효값 문제로 발생한 에러를 담당합니다.
     *
     * @param e Service에서 발생한 에러 코드
     * @return HTTP Status 400과 해당 에러 메시지
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Service에서 발생한 에러 코드를, HTTP Status 400으로 변환합니다.
     * 값 저장 순서 등의 상태 문제로 발생한 에러를 담당합니다.
     *
     * @param e Service에서 발생한 에러 코드
     * @return HTTP Status 409와 해당 에러 메시지
     */
    @ExceptionHandler
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}
