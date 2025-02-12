package com.service.auth.services.interfaces;

import com.service.auth.requests.AddUserRequest;

public interface UserInterface {
  Object addUser(AddUserRequest addUserRequest);
}
