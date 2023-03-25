package dddhexagonal.modules.onboarding.domain.operation.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OnboardingOperationProgressStartedDomainEvent extends BaseDomainEvent {

  UUID templateId;

  public OnboardingOperationProgressStartedDomainEvent(UUID operationId, UUID templateId) {
    super(operationId);
    this.templateId = templateId;
  }
}
