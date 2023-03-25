/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.message.registry;

public class DuplicateMessageHandlerRegistrationException extends RuntimeException {

  public DuplicateMessageHandlerRegistrationException(Class<?> messageType) {
    super("Attempted to register more than one handler for message type " + messageType.getSimpleName());
  }
}
