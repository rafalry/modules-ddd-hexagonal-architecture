package dddhexagonal.modules.onboarding.domain.operation.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OnboardingOperationCancelledDomainEvent extends BaseDomainEvent {

  UUID userId;
  String email;

  public OnboardingOperationCancelledDomainEvent(UUID operationId, UUID userId, String email) {
    super(operationId);
    this.userId = userId;
    this.email = email;
  }
}
