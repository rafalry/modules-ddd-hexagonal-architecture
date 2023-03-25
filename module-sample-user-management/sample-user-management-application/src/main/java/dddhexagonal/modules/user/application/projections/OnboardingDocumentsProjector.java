package dddhexagonal.modules.user.application.projections;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import dddhexagonal.foundations.integration.events.IntegrationEventHandler;
import dddhexagonal.modules.onboarding.integration.events.OnboardingOperationDocumentAcceptedIntegrationEvent;

@Service
@RequiredArgsConstructor
public class OnboardingDocumentsProjector {

  private final OnboardingDocumentsProjectionJpaRepository repository;

  @IntegrationEventHandler
  public void storeAcceptedOnboardingDocument(OnboardingOperationDocumentAcceptedIntegrationEvent event) {
    OnboardingDocumentProjectionEntity projection = new OnboardingDocumentProjectionEntity(
        event.getUserId(),
        event.getDocumentId(),
        event.getDocumentName());
    repository.save(projection);
  }
}
