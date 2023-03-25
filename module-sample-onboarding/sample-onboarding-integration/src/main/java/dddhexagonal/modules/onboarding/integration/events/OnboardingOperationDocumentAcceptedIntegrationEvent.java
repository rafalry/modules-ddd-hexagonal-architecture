package dddhexagonal.modules.onboarding.integration.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.integration.events.IntegrationEvent;

@Value
@EqualsAndHashCode(callSuper = false)
public class OnboardingOperationDocumentAcceptedIntegrationEvent extends IntegrationEvent {

  UUID userId;
  UUID documentId;
  String documentName;
}
