
package dddhexagonal.modules.user.domain.user.rules;

import dddhexagonal.foundations.domain.rule.BusinessRule;

@Deprecated // TODO use aspect to disallow user modifications on all aggregate methods
public record DisallowDeletedUserModificationsRule(boolean deleted) implements BusinessRule {
  @Override
  public boolean isRespected() {
    return !deleted;
  }
}
