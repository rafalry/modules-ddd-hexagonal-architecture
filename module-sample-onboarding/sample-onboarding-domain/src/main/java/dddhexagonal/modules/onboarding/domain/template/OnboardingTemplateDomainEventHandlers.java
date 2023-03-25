package dddhexagonal.modules.onboarding.domain.template;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.operation.ports.OnboardingOperationRepositoryPort;
import dddhexagonal.modules.onboarding.domain.template.events.OnboardingTemplateDocumentsUpdatedDomainEvent;

import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.PRISTINE;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Service
@RequiredArgsConstructor
class OnboardingTemplateDomainEventHandlers {

  private final OnboardingOperationRepositoryPort operationRepositoryPort;

  /**
   * INFO Interaction: side-effect with domain logic triggered by DomainEvent.
   * INFO: Modification of multiple aggregates in a single transaction
   */
  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void onOnboardingTemplateDocumentsUpdatedDomainEvent(OnboardingTemplateDocumentsUpdatedDomainEvent event) {
    if (event.isTemplateActive()) {
      List<OnboardingOperationAggregateRoot> pristineOperations = operationRepositoryPort.findByTemplateIdAndProgressStatus(
          event.getAggregateRootId(),
          PRISTINE);
      pristineOperations.forEach(op -> op.setDocuments(event.getDocumentNames()));
      operationRepositoryPort.saveAll(pristineOperations);
    }
  }
}
