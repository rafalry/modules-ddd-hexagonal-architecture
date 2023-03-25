package dddhexagonal.modules.onboarding.domain.template.rules;

import java.util.Collection;

import dddhexagonal.foundations.domain.entity.NamedEntity;
import dddhexagonal.foundations.domain.rule.BusinessRule;

public record NameIsUniqueRule(Collection<? extends NamedEntity> namedEntities, String newName) implements BusinessRule {

  @Override
  public boolean isRespected() {
    return namedEntities.stream().noneMatch(d -> d.getName().equals(newName));
  }

}
