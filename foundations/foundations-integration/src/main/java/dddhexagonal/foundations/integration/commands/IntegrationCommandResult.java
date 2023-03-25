/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.commands;

import lombok.Getter;

import static java.lang.System.currentTimeMillis;

@Getter
public abstract sealed class IntegrationCommandResult
    permits IdentifiableIntegrationCommandResult,
            CompletableIntegrationCommandResult, VoidIntegrationCommandResult {

  @Getter
  private final long resultCreatedTime;


  protected IntegrationCommandResult() {
    this.resultCreatedTime = currentTimeMillis();
  }

}
