package dddhexagonal.modules.onboarding.application.integration;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import dddhexagonal.foundations.integration.events.IntegrationEventBus;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationCompletedDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationDocumentAcceptedDomainEvent;
import dddhexagonal.modules.onboarding.integration.events.OnboardingOperationCompletedIntegrationEvent;
import dddhexagonal.modules.onboarding.integration.events.OnboardingOperationDocumentAcceptedIntegrationEvent;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Service
@RequiredArgsConstructor
public class IntegrationEventsPublisher {

  private final IntegrationEventBus eventBus;


  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void onOperationCompleted(OnboardingOperationCompletedDomainEvent event) {
    eventBus.publishAsync(new OnboardingOperationCompletedIntegrationEvent(event.getUserId()));
  }


  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void onDocumentAccepted(OnboardingOperationDocumentAcceptedDomainEvent event) {
    eventBus.publishAsync(new OnboardingOperationDocumentAcceptedIntegrationEvent(event.getUserId(),
        event.getDocumentId(),
        event.getDocumentName()));
  }
}
