package dddhexagonal.modules.onboarding.application.repository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus;
import dddhexagonal.modules.onboarding.domain.operation.ports.OnboardingOperationRepositoryPort;

// TODO document why JpaRepo & Spring Data are in application layer (complex queries etc)
@Component
@RequiredArgsConstructor
public class OnboardingOperationRepositoryAdapter implements OnboardingOperationRepositoryPort {

  private final OnboardingOperationJpaRepository jpaRepository;

  @Override
  public boolean existsByUserId(UUID userId) {
    return jpaRepository.existsByUserId(userId);
  }


  @Override
  public List<OnboardingOperationAggregateRoot> findByTemplateIdAndProgressStatus(
      UUID templateId, OnboardingOperationProgressStatus progressStatus) {
    return jpaRepository.findByTemplateIdAndProgressStatus(templateId, progressStatus);
  }


  @Override
  public void saveAll(Iterable<OnboardingOperationAggregateRoot> operations) {
    jpaRepository.saveAll(operations);
  }
}
