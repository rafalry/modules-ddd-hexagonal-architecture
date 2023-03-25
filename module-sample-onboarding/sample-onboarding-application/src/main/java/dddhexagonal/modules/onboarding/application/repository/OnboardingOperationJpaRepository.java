/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.onboarding.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

import dddhexagonal.foundations.domain.repository.BaseJpaRepository;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus;

@Repository
public interface OnboardingOperationJpaRepository extends BaseJpaRepository<OnboardingOperationAggregateRoot> {

  Optional<OnboardingOperationAggregateRoot> findByUserId(UUID userId);

  boolean existsByUserId(UUID userId);

  List<OnboardingOperationAggregateRoot> findByTemplateIdAndProgressStatus(
      UUID templateId, OnboardingOperationProgressStatus progressStatus);

  long countByTemplateIdAndProgressStatus(UUID templateId, OnboardingOperationProgressStatus progressStatus);
}
