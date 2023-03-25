/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.domain.user.rules;

import java.util.Map;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.modules.user.domain.user.ports.UserRepositoryPort;

import static java.util.Map.of;

public record EmailIsUniqueAcrossUsersRule(String email, UserRepositoryPort emailCheckerPort) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return !emailCheckerPort.isEmailAlreadyInUse(email);
  }


  @Override
  public Map<String, String> getDetails() {
    return of("email", email);
  }
}
