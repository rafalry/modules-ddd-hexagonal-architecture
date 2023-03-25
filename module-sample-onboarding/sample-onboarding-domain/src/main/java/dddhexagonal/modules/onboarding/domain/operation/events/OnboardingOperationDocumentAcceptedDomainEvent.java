package dddhexagonal.modules.onboarding.domain.operation.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OnboardingOperationDocumentAcceptedDomainEvent extends BaseDomainEvent {

  UUID userId;
  UUID documentId;
  String documentName;


  public OnboardingOperationDocumentAcceptedDomainEvent(UUID id, UUID userId, UUID documentId, String documentName) {
    super(id);
    this.userId = userId;
    this.documentId = documentId;
    this.documentName = documentName;
  }
}
