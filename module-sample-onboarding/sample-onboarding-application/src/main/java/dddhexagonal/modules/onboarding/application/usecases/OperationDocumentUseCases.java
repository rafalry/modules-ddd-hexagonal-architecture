package dddhexagonal.modules.onboarding.application.usecases;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import org.springframework.stereotype.Service;

import dddhexagonal.foundations.application.usecases.TransactionalUseCase;
import dddhexagonal.modules.onboarding.application.repository.OnboardingOperationJpaRepository;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;

import static dddhexagonal.foundations.domain.usecases.EntityNotFoundException.entityNotFound;

@Service
@RequiredArgsConstructor
@TransactionalUseCase
public class OperationDocumentUseCases {

  private final OnboardingOperationJpaRepository operationRepository;

  public void signDocument(UUID operationId, UUID documentId) {
    OnboardingOperationAggregateRoot operation = getOperation(operationId);
    operation.signDocument(documentId);
    operationRepository.save(operation);
  }


  public void acceptDocument(UUID operationId, UUID documentId) {
    OnboardingOperationAggregateRoot operation = getOperation(operationId);
    operation.acceptDocument(documentId);
    operationRepository.save(operation);
  }


  public void rejectDocument(UUID operationId, UUID documentId) {
    OnboardingOperationAggregateRoot operation = getOperation(operationId);
    operation.rejectDocument(documentId);
    operationRepository.save(operation);
  }


  private OnboardingOperationAggregateRoot getOperation(UUID operationId) {
    return operationRepository.findById(operationId)
        .orElseThrow(entityNotFound(OnboardingOperationAggregateRoot.class, operationId));
  }
}
