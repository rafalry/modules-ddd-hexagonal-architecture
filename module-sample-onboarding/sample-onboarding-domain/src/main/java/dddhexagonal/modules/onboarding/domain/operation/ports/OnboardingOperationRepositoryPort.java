package dddhexagonal.modules.onboarding.domain.operation.ports;

import java.util.List;
import java.util.UUID;

import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus;

public interface OnboardingOperationRepositoryPort {

  boolean existsByUserId(UUID userId);

  List<OnboardingOperationAggregateRoot> findByTemplateIdAndProgressStatus(UUID templateId, OnboardingOperationProgressStatus progressStatus);

  void saveAll(Iterable<OnboardingOperationAggregateRoot> pristineOperations);

}
