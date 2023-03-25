package dddhexagonal.modules.onboarding.domain.operation.rules;

import java.util.Map;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus;

import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.CANCELLED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.COMPLETED;
import static java.util.Map.of;

public record AllowDocumentsActionsOnlyForNotClosedOperationsRule(
    OnboardingOperationProgressStatus progressStatus)
    implements BusinessRule {

  @Override
  public boolean isRespected() {
    return !COMPLETED.equals(progressStatus) && !CANCELLED.equals(progressStatus);
  }


  @Override
  public Map<String, String> getDetails() {
    return of("Current status", progressStatus.toString());
  }
}
