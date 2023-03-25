/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.message.registry;

public class MissingMessageHandlerRegistrationException extends RuntimeException {

  public MissingMessageHandlerRegistrationException(Class<?> messageType) {
    super("No implemented handler for message type " + messageType.getSimpleName());
  }
}
