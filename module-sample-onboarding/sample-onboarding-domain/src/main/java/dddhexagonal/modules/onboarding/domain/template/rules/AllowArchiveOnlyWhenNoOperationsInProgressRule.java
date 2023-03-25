package dddhexagonal.modules.onboarding.domain.template.rules;

import dddhexagonal.foundations.domain.rule.BusinessRule;

public record AllowArchiveOnlyWhenNoOperationsInProgressRule(long operationsInProgressCount)
    implements BusinessRule {

  @Override
  public boolean isRespected() {
    return operationsInProgressCount == 0;
  }
}
