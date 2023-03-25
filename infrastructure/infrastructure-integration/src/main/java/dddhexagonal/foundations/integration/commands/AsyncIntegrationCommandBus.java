/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.commands;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

import dddhexagonal.foundations.integration.message.invoke.AsyncGenericMessageHandlerInvoker;

@RequiredArgsConstructor
class AsyncIntegrationCommandBus implements IntegrationCommandBus {

  private final AsyncGenericMessageHandlerInvoker invoker;

  @Override
  public <C extends IntegrationCommand<R>, R extends IntegrationCommandResult> CompletableFuture<R> dispatchAsync(C command) {
    return invoker.invoke(command);
  }

}
