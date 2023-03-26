package dddhexagonal.foundations.integration.commands;

import java.util.concurrent.CompletableFuture;

public interface IntegrationCommandBus {

  <C extends IntegrationCommand<R>, R extends IntegrationCommandResult> CompletableFuture<R> dispatchAsync(C command);
}
