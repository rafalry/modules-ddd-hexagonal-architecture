package dddhexagonal.modules.onboarding.domain.operation.rules;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus;

import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.PRISTINE;

public record AllowDocumentsModificationOnlyForPristineOperationsRule(
    OnboardingOperationProgressStatus progressStatus)
    implements BusinessRule {

  @Override
  public boolean isRespected() {
    return PRISTINE.equals(progressStatus);
  }
}
