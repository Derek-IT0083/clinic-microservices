package com.clinic.appointment_service.exception;

import com.clinic.appointment_service.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 攔截我們自訂或執行階段拋出的 RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        // 回傳 400 Bad Request 與清晰的錯誤訊息
        ApiResponse<Object> response = ApiResponse.error(400, ex.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    // 攔截其他未知系統例外 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.error(500, "系統內部錯誤: " + ex.getMessage());
        return ResponseEntity.status(500).body(response);
    }

}
