/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.onboarding.application.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import dddhexagonal.foundations.domain.repository.BaseJpaRepository;
import dddhexagonal.modules.onboarding.domain.template.OnboardingTemplateAggregateRoot;

@Repository
public interface OnboardingTemplateJpaRepository extends BaseJpaRepository<OnboardingTemplateAggregateRoot> {

  Optional<OnboardingTemplateAggregateRoot> findFirstByActiveIsTrue();
  Optional<OnboardingTemplateAggregateRoot> findFirstByName(String name);
}
