package com.service.auth.wrappers;

import com.service.auth.dtos.InfoCustomer;
import com.service.auth.requests.RegisterUserRequest;

public class RegisterUserRequestWrapper {
  private RegisterUserRequest registerUserRequest;
  private InfoCustomer infoCustomer;

  public RegisterUserRequestWrapper(RegisterUserRequest registerUserRequest, InfoCustomer infoCustomer) {
    this.registerUserRequest = registerUserRequest;
    this.infoCustomer = infoCustomer;
  }

  public RegisterUserRequest getRegisterUserRequest() {
    return registerUserRequest;
  }

  public void setRegisterUserRequest(RegisterUserRequest registerUserRequest) {
    this.registerUserRequest = registerUserRequest;
  }

  public InfoCustomer getInfoCustomer() {
    return infoCustomer;
  }

  public void setInfoCustomer(InfoCustomer infoCustomer) {
    this.infoCustomer = infoCustomer;
  }

}
