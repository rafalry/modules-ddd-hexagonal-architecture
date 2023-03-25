package dddhexagonal.modules.onboarding.domain.template.rules;

import java.util.UUID;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.modules.onboarding.domain.operation.ports.OnboardingOperationRepositoryPort;

public record OnboardingForUserHasNotStartedYetRule(OnboardingOperationRepositoryPort onboardingOperationRepositoryPort, UUID userid) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return !onboardingOperationRepositoryPort.existsByUserId(userid);
  }

}
