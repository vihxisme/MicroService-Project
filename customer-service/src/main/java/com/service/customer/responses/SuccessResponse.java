package com.service.customer.responses;

public class SuccessResponse<T> {
  private final String message;
  private final T data;

  public SuccessResponse(String message, T data) {
    this.message = message;
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public T getData() {
    return data;
  }

}
