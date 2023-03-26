package dddhexagonal.foundations.domain.rule;

import org.springframework.data.domain.Persistable;

public class BusinessRuleException extends RuntimeException {

  public <ID> BusinessRuleException(BusinessRule rule, Persistable<ID> entity) {
    super("Business rule '" + rule.getClass().getSimpleName() + "' violated for entity " + entity.getClass().getSimpleName() + " with id " + entity.getId() + "." + formatDetails(rule));
  }

  public <ID> BusinessRuleException(BusinessRule rule) {
    super("Business rule '" + rule.getClass().getSimpleName() + "' violated." + formatDetails(rule));
  }


  private static String formatDetails(BusinessRule rule) {
    if (rule.getDetails().isEmpty()) {
      return "";
    }
    return " Details: " + rule.getDetails().toString();
  }

}
