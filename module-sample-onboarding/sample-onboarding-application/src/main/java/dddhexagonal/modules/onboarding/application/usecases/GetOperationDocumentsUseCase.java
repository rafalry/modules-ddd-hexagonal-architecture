package dddhexagonal.modules.onboarding.application.usecases;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

import dddhexagonal.foundations.application.usecases.NonTransactionalUseCase;
import dddhexagonal.foundations.domain.entity.BaseDomainEntity;
import dddhexagonal.foundations.integration.query.IntegrationQueryHandler;
import dddhexagonal.modules.onboarding.application.repository.OnboardingOperationJpaRepository;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.integration.query.GetOnboardingOperationDocumentsIntegrationQuery;
import dddhexagonal.modules.onboarding.integration.query.GetOnboardingOperationDocumentsIntegrationQueryResult;
import dddhexagonal.modules.onboarding.integration.query.OnboardingOperationDocumentSigningStatus;

import static dddhexagonal.foundations.domain.usecases.EntityNotFoundException.entityNotFound;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@NonTransactionalUseCase
public class GetOperationDocumentsUseCase {

  private final OnboardingOperationJpaRepository repository;


  @IntegrationQueryHandler
  public GetOnboardingOperationDocumentsIntegrationQueryResult get(
      GetOnboardingOperationDocumentsIntegrationQuery query) {
    OnboardingOperationAggregateRoot operation = repository.findByUserId(query.getUserId())
        .orElseThrow(entityNotFound(OnboardingOperationAggregateRoot.class, "userId=" + query.getUserId()));

    Map<UUID, OnboardingOperationDocumentSigningStatus> statusMapConverted = operation.getDocuments()
        .stream()
        .collect(
            toMap(
                BaseDomainEntity::getId,
                d -> OnboardingOperationDocumentSigningStatus.valueOf(d.getStatus().toString())));
    return new GetOnboardingOperationDocumentsIntegrationQueryResult(statusMapConverted);
  }
}
