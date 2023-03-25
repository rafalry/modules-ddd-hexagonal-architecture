package dddhexagonal.foundations.test.integration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import dddhexagonal.foundations.integration.commands.IntegrationCommand;
import dddhexagonal.foundations.integration.commands.IntegrationCommandBus;
import dddhexagonal.foundations.integration.commands.IntegrationCommandResult;

public class TestIntegrationCommandBus implements IntegrationCommandBus {

  @Getter
  private List<IntegrationCommand> commands = new ArrayList<>();

  @Override
  public <C extends IntegrationCommand<R>, R extends IntegrationCommandResult> CompletableFuture<R> dispatchAsync(
      C command) {
    commands.add(command);
    return null;
  }

  public void clean() {
    commands = new ArrayList<>();
  }

  public boolean isClean() {
    return commands.isEmpty();
  }
}
