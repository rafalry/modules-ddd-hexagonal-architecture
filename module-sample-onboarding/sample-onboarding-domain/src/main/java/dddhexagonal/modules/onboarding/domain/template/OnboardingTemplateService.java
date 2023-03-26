package dddhexagonal.modules.onboarding.domain.template;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import dddhexagonal.foundations.domain.service.BaseDomainService;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.template.rules.AllowArchiveOnlyWhenNoOperationsInProgressRule;
import dddhexagonal.modules.onboarding.domain.template.rules.NameIsUniqueRule;

@Service
@AllArgsConstructor
public class OnboardingTemplateService extends BaseDomainService {

  /**
   * INFO Create aggregate with domain logic involved
   */
  public OnboardingTemplateAggregateRoot addTemplate(
      String name, List<String> documents, Optional<OnboardingTemplateAggregateRoot> active,
      List<OnboardingTemplateAggregateRoot> all) {
    checkRule(new NameIsUniqueRule(all, name));
    // TODO check rule for document name uniqueness

    OnboardingTemplateAggregateRoot template = new OnboardingTemplateAggregateRoot(name, documents);
    if (active.isPresent()) {
      active.get().setActive(false);
    }
    template.setActive(true);
    return template;
  }


  /**
   * INFO Idempotent use case, no need to validate that the new active template is different the from previous one
   */
  public void activateTemplate(
      OnboardingTemplateAggregateRoot toActive,
      Optional<OnboardingTemplateAggregateRoot> previousActive) {
      if (previousActive.isPresent() && !previousActive.get().equals(toActive)) {
        previousActive.get().setActive(false);
      }
      toActive.setActive(true);
  }


  /**
   * INFO Business rule validation both in service and aggregate
   */
  public void archiveTemplate(
      OnboardingTemplateAggregateRoot template,
      List<OnboardingOperationAggregateRoot> operationsWithoutProgress, long opsInProgressCount) {
    checkRule(new AllowArchiveOnlyWhenNoOperationsInProgressRule(opsInProgressCount));

    template.archive();

    operationsWithoutProgress.forEach(OnboardingOperationAggregateRoot::cancel);
  }
}
