package dddhexagonal.modules.onboarding.domain.template.rules;

import dddhexagonal.foundations.domain.rule.BusinessRule;

public record AllowArchiveOnlyWhenInactiveRule(boolean active) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return !active;
  }
}
