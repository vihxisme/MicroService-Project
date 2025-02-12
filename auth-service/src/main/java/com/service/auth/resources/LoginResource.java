package com.service.auth.resources;

import com.service.auth.dtos.LoginDTO;

public class LoginResource {
  private final String token;
  private final LoginDTO loginDTO;

  public LoginResource(String token, LoginDTO loginDTO) {
    this.token = token;
    this.loginDTO = loginDTO;
  }

  public String getToken() {
    return token;
  }

  public LoginDTO getLoginDTO() {
    return loginDTO;
  }
}
