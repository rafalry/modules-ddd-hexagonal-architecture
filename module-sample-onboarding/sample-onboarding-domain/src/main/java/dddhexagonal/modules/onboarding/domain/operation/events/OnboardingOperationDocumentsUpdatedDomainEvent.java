package dddhexagonal.modules.onboarding.domain.operation.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OnboardingOperationDocumentsUpdatedDomainEvent extends BaseDomainEvent {

  String email;
  List<UUID> documents;


  public OnboardingOperationDocumentsUpdatedDomainEvent(UUID aggregateRootId, String email, List<UUID> documents) {
    super(aggregateRootId);
    this.email = email;
    this.documents = documents;
  }
}
