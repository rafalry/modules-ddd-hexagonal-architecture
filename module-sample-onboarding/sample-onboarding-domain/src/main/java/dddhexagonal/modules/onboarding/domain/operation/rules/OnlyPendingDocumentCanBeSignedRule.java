package dddhexagonal.modules.onboarding.domain.operation.rules;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus;

import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.PENDING;

public record OnlyPendingDocumentCanBeSignedRule(OnboardingOperationDocumentStatus status) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return PENDING.equals(status);
  }
}
