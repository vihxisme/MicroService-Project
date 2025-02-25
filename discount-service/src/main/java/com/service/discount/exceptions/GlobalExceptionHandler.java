package com.service.discount.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.service.discount.responses.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Xử lý lỗi khi validation thất bại (@Valid trong DTO)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    ErrorResponse errorResponse = new ErrorResponse(400, "Validation Error", errors);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  // Xử lý lỗi khi validation thất bại với @RequestParam hoặc @PathVariable
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
    ErrorResponse errorResponse = new ErrorResponse(400, "Validation Error", errors);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  // Xử lý lỗi khi trùng lặp dữ liệu (ví dụ: email đã tồn tại)
  @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(Exception ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", "Data does not exist or is invalid!");
    ErrorResponse errorResponse = new ErrorResponse(409, "Data Integrity Violation", errors);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  // xử lý lỗi khi không tìm thấy endpoint yêu cầu
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", "No handler found for your request!");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Not Found", errors));
  }

  // Xử lý lỗi khi tham số truyền vào không hợp lệ
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Bad Request", errors));
  }

  // Xử lý lỗi khi không tìm thấy entity trong database
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Not Found", errors));
  }

  // Xử lý lỗi khi người dùng không có đủ quyền truy cập
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", "Access Denied: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, "Forbidden", errors));
  }

  // Xử lý lỗi khi không tìm thấy dữ liệu trong database
  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", "Resource Not Found: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Not Found", errors));
  }

  // Xử lý lỗi khi có vấn đề truy cập dữ liệu
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", "Data Access Error: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(500, "Internal Server Error", errors));
  }

  // Xử lý lỗi khi có vấn đề giao dịch
  @ExceptionHandler(TransactionException.class)
  public ResponseEntity<ErrorResponse> handleTransactionException(TransactionException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", "Transaction Error: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(500, "Internal Server Error", errors));
  }

  // Xử lý lỗi chung cho toàn bộ hệ thống
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", "System error: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(500, "Internal Server Error", errors));
  }
}
