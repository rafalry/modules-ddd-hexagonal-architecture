package dddhexagonal.modules.onboarding.domain.template;

import java.util.List;
import org.junit.jupiter.api.Test;

import dddhexagonal.foundations.domain.rule.BusinessRuleException;

import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.TEMPLATE_1_NAME;
import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.TEMPLATE_2_NAME;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OnboardingTemplateDomainLifecycleTest {

  public OnboardingTemplateService service = new OnboardingTemplateService();

  @Test
  void should_deactivate_previous_active_template_when_new_template_is_added() {
    OnboardingTemplateAggregateRoot previous = new OnboardingTemplateAggregateRoot(TEMPLATE_1_NAME, emptyList());

    OnboardingTemplateAggregateRoot current = service.addTemplate(TEMPLATE_2_NAME, emptyList(), of(previous), List.of(previous));

    assertThat(previous.isActive()).isFalse();
    assertThat(current.isActive()).isTrue();
  }

  @Test
  void should_fail_when_new_template_is_added_with_duplicate_name() {
    OnboardingTemplateAggregateRoot previous = new OnboardingTemplateAggregateRoot(TEMPLATE_1_NAME, emptyList());

    assertThatExceptionOfType(BusinessRuleException.class)
        .isThrownBy(() -> service.addTemplate(TEMPLATE_1_NAME, emptyList(), of(previous), List.of(previous)));
  }

}
