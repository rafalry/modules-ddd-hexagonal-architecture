package dddhexagonal.modules.onboarding.domain.operation.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OnboardingOperationCompletedDomainEvent extends BaseDomainEvent {

  UUID userId;

  public OnboardingOperationCompletedDomainEvent(UUID operationId, UUID userId) {
    super(operationId);
    this.userId = userId;
  }
}
