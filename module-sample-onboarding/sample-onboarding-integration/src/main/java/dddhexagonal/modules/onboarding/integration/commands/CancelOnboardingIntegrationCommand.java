/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.onboarding.integration.commands;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.integration.commands.IntegrationCommand;
import dddhexagonal.foundations.integration.commands.VoidIntegrationCommandResult;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CancelOnboardingIntegrationCommand extends IntegrationCommand<VoidIntegrationCommandResult> {
  UUID userId;
}
