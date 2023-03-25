/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.events;

import lombok.RequiredArgsConstructor;

import dddhexagonal.foundations.integration.message.invoke.AsyncGenericMessageHandlerInvoker;

@RequiredArgsConstructor
class AsyncIntegrationEventBus implements IntegrationEventBus {

  private final AsyncGenericMessageHandlerInvoker invoker;


  @Override
  public void publishAsync(IntegrationEvent event) {
    invoker.invoke(event);
  }

}
