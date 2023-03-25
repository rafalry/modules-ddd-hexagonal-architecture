package dddhexagonal.modules.onboarding.domain.operation.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OnboardingOperationInitiatedDomainEvent extends BaseDomainEvent {

  String email;
  List<UUID> documentIds;


  public OnboardingOperationInitiatedDomainEvent(UUID aggregateRootId, String email, List<UUID> documentIds) {
    super(aggregateRootId);
    this.email = email;
    this.documentIds = documentIds;
  }
}
