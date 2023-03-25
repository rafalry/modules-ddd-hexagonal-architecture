/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.commands;

import java.util.concurrent.CompletableFuture;

public interface IntegrationCommandBus {

  <C extends IntegrationCommand<R>, R extends IntegrationCommandResult> CompletableFuture<R> dispatchAsync(C command);
}
