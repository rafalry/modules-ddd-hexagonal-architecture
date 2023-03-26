package dddhexagonal.foundations.domain.service;

import org.springframework.data.domain.Persistable;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.foundations.domain.rule.BusinessRuleException;

/**
 * Domain service responsibilities:
 * - execute domain logic related to multiple aggregate instances
 */
public abstract class BaseDomainService {

  protected <ID> void checkRule(BusinessRule rule, Persistable<ID> entity) {
    if (!rule.isRespected()) {
      throw new BusinessRuleException(rule, entity);
    }
  }

  protected <ID> void checkRule(BusinessRule rule) {
    if (!rule.isRespected()) {
      throw new BusinessRuleException(rule);
    }
  }
}
