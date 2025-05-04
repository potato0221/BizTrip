package com.ll.biztrip.global.exceptions;

import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.standard.base.Empty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<RsData<Empty>> handleGlobalException(GlobalException ex) {
        return ResponseEntity
                .status(ex.getRsData().getStatusCode())
                .body(ex.getRsData());
    }
}
