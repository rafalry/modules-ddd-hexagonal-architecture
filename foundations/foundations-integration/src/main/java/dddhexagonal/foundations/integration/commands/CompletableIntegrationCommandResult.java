/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.integration.commands;

import lombok.Value;

@Value
public class CompletableIntegrationCommandResult extends IntegrationCommandResult  {
  boolean success;
}
