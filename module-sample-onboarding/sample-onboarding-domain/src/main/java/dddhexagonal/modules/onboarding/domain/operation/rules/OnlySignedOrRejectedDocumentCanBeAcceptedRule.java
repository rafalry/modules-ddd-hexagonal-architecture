package dddhexagonal.modules.onboarding.domain.operation.rules;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus;

import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.REJECTED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.SIGNED;

public record OnlySignedOrRejectedDocumentCanBeAcceptedRule(OnboardingOperationDocumentStatus status) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return SIGNED.equals(status) || REJECTED.equals(status);
  }
}
