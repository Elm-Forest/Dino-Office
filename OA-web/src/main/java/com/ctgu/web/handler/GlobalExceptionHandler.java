package com.ctgu.web.handler;

import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author Elm Forest
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        printInfo(e);
        log.error("系统异常:" + e.getMessage());
        return Result.fail("系统异常:" + e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError allError : allErrors) {
            FieldError fieldError = (FieldError) allError;
            sb.append(fieldError.getDefaultMessage()).append(";");
        }
        return Result.fail(sb.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException:" + e.getMessage());
        printInfo(e);
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotImplementedException.class)
    public Result<?> handleNotImplementedException(NotImplementedException e) {
        return Result.fail(e.getMessage());
    }

    private void printInfo(Exception e) {
        StackTraceElement ste = e.getStackTrace()[0];
        log.warn("异常类：" + ste.getClassName());
        log.warn("异常类名：" + ste.getFileName());
        log.warn("异常行号：" + ste.getLineNumber());
        log.warn("异常方法：" + ste.getMethodName());
    }
}
