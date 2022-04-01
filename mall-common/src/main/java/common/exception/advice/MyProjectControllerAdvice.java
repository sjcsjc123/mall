package common.exception.advice;

import common.exception.constant.MyProjectExceptionEnum;
import common.exception.MyProjectException;
import common.exception.result.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyProjectControllerAdvice {

    @ExceptionHandler(MyProjectException.class)
    public ResponseEntity<ExceptionResult> HandlerException(MyProjectException myProjectException){
        MyProjectExceptionEnum myProjectExceptionEnum = myProjectException.getMyProjectExceptionEnum();
        return ResponseEntity.status(myProjectException.getMyProjectExceptionEnum().getCode()).body(new ExceptionResult(myProjectException.getMyProjectExceptionEnum()));
    }
}
