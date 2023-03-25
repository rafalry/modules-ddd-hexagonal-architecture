/*
 * Copyright (c) 2000-2023, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.infrastructure.emailgateway;

public class EmailSendingException extends RuntimeException {
  public EmailSendingException(String message, Throwable cause) {
    super(message, cause);
  }
}
