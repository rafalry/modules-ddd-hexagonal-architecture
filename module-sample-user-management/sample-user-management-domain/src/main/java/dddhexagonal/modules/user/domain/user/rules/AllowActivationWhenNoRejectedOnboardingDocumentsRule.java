package dddhexagonal.modules.user.domain.user.rules;

import java.util.UUID;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.modules.user.domain.user.ports.OnboardingPort;

public record AllowActivationWhenNoRejectedOnboardingDocumentsRule(OnboardingPort onboardingPort, UUID userId) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return !onboardingPort.hasRejectedDocuments(userId);
  }
}
