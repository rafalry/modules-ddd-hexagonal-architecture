package dddhexagonal.modules.onboarding.domain.operation.rules;

import java.util.HashSet;
import java.util.List;

import dddhexagonal.foundations.domain.rule.BusinessRule;

public record DocumentNamesMustBeUniqueRule(List<String> documentNames) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return documentNames.size() == new HashSet<>(documentNames).size();
  }
}
