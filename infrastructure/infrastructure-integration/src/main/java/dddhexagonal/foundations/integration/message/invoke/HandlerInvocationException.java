/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.message.invoke;

import java.util.function.Function;

public class HandlerInvocationException extends RuntimeException {

  private final Class<?> messageType;
  private final Function<?, ?> handler;


  public HandlerInvocationException(Class<?> messageType, Function<?, ?> handler) {
    super("Exception occurred while invoking handler for " + messageType.getSimpleName() + ": " + handler);
    this.messageType = messageType;
    this.handler = handler;
  }
}
