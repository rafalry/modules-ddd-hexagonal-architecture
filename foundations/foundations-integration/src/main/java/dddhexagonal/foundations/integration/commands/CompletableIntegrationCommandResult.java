package dddhexagonal.foundations.integration.commands;

import lombok.Value;

@Value
public class CompletableIntegrationCommandResult extends IntegrationCommandResult  {
  boolean success;
}
