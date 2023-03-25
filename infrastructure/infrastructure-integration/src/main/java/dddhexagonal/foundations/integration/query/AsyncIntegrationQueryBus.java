/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.query;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

import dddhexagonal.foundations.integration.message.invoke.AsyncGenericMessageHandlerInvoker;

@RequiredArgsConstructor
class AsyncIntegrationQueryBus implements IntegrationQueryBus {

  private final AsyncGenericMessageHandlerInvoker invoker;


  @Override
  public <R extends IntegrationQueryResult> CompletableFuture<R> queryAsync(IntegrationQuery<R> query) {
    return invoker.invoke(query);
  }

}
