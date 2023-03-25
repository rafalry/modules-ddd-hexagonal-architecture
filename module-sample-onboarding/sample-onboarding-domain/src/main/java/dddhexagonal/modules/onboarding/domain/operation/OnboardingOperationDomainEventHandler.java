package dddhexagonal.modules.onboarding.domain.operation;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationCancelledDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationDocumentsUpdatedDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationInitiatedDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.ports.EmailSenderPort;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Service
@RequiredArgsConstructor
public class OnboardingOperationDomainEventHandler {

  private final EmailSenderPort emailSenderPort;

  /**
   * INFO side effect propagated from domain layer
   */
  @TransactionalEventListener(phase = AFTER_COMMIT)
  public void onOnboardingInitiated(OnboardingOperationInitiatedDomainEvent event) {
    emailSenderPort.sendDocumentsEmail(event.getEmail(), event.getAggregateRootId(), event.getDocumentIds());
  }

  @TransactionalEventListener(phase = AFTER_COMMIT)
  public void onOnboardingDocumentsUpdated(OnboardingOperationDocumentsUpdatedDomainEvent event) {
    emailSenderPort.sendDocumentsEmail(event.getEmail(), event.getAggregateRootId(), event.getDocuments());
  }

  @TransactionalEventListener(phase = AFTER_COMMIT)
  public void onOnboardingCancelled(OnboardingOperationCancelledDomainEvent event) {
    emailSenderPort.sendOnboardingCancelledEmail(event.getEmail());
  }

}
