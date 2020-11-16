package com.dancers.service.system.config;


import com.dancers.service.system.core.ErrorResult;
import com.dancers.service.system.exceptions.BusinessException;
import com.dancers.service.system.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@ControllerAdvice(annotations = RestController.class)
public class GlobalControllerErrorAdvice {
    Logger log = LoggerFactory.getLogger(getClass().getName());

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ErrorResult> businessException(BusinessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult().setCode(e.getCode()).setMessage(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResult> notFoundException(NotFoundException e) {
        return this.error(e, HttpStatus.NOT_FOUND.value(), e.getMessage() + "");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ErrorResult> entityNotFoundException(NotFoundException e) {
        return this.error(e, HttpStatus.NOT_FOUND.value(), "您要访问的数据不存在");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResult> assertionException(IllegalArgumentException ex) {
        return this.error(ex, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getLocalizedMessage());
    }

    /**
     * 验证异常
     *
     * @param ex
     * @return
     * @throws MethodArgumentNotValidException
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("全局异常处理：", ex);
        StringBuilder builder = new StringBuilder("输入格式有误: ");

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            builder.append(error.getDefaultMessage() + "；");
        });
        return this.error(ex, HttpStatus.INTERNAL_SERVER_ERROR.value(), builder.toString());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResult> exception(Exception ex) {
        return this.error(ex, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getLocalizedMessage());
    }

    private <E extends Exception> ResponseEntity<ErrorResult> error(E error, int httpStatus, String message) {
        log.info(error.getMessage(), error);
        System.out.println("in error-----------------------");
        if (StringUtils.isEmpty(message)) {
            message = Optional.of(error.getMessage()).orElse(error.getClass().getSimpleName());
        }
        return ResponseEntity.status(httpStatus).body(new ErrorResult().setCode(httpStatus).setMessage(message));
    }
}
