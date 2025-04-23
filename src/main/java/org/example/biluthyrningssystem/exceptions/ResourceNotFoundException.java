package org.example.biluthyrningssystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
  private final String item;
  private final String message;
  private final Object value;

  public ResourceNotFoundException(String item, String message, Object value) {
    this.item = item;
    this.message = message;
    this.value = value;
  }

  public String getItem() {
    return item;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public Object getValue() {
    return value;
  }
}
