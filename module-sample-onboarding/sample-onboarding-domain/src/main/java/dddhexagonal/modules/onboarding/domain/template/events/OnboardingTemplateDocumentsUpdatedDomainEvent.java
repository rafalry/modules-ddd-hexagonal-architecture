package dddhexagonal.modules.onboarding.domain.template.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OnboardingTemplateDocumentsUpdatedDomainEvent extends BaseDomainEvent {

  boolean templateActive;
  List<String> documentNames;

  public OnboardingTemplateDocumentsUpdatedDomainEvent(
      UUID templateId,
      boolean templateActive,
      List<String> documentNames) {
    super(templateId);
    this.templateActive = templateActive;
    this.documentNames = documentNames;
  }
}
