package com.service.apigateway.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateResource {
  private final Boolean valid;

  @JsonProperty("valid")
  public Boolean getValid() {
    return valid;
  }

  public ValidateResource(@JsonProperty("valid") Boolean valid) {
    this.valid = valid;
  }

}
