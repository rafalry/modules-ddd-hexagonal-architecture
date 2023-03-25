package dddhexagonal.modules.onboarding.domain.template.ports;

import java.util.UUID;

import dddhexagonal.modules.onboarding.domain.template.OnboardingTemplateAggregateRoot;

public interface OnboardingTemplateRepositoryPort {

  OnboardingTemplateAggregateRoot getById(UUID templateId);
}
