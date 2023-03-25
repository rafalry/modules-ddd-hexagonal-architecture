/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.domain.user;

import dddhexagonal.modules.user.domain.user.rules.DisallowDeletedUserModificationsRule;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import dddhexagonal.foundations.domain.service.BaseDomainService;
import dddhexagonal.modules.user.domain.user.ports.OnboardingPort;
import dddhexagonal.modules.user.domain.user.ports.UserRepositoryPort;
import dddhexagonal.modules.user.domain.user.rules.AllowActivationWhenNoRejectedOnboardingDocumentsRule;
import dddhexagonal.modules.user.domain.user.rules.EmailIsUniqueAcrossUsersRule;

@Service
@RequiredArgsConstructor
public class UserService extends BaseDomainService {

  private final UserRepositoryPort userRepositoryPort;
  private final OnboardingPort onboardingPort;


  public UserAggregateRoot createUser(String email, boolean triggerOnboarding) {
    checkRule(new EmailIsUniqueAcrossUsersRule(email, userRepositoryPort));

    return new UserAggregateRoot(email, triggerOnboarding);
  }


  public void deleteUser(UserAggregateRoot user) {
    checkRule(new DisallowDeletedUserModificationsRule(user.isDeleted()));

    user.markAsDeleted();
  }


  public void activateAfterOnboardingCompleted(UserAggregateRoot user) {
    checkRule(new DisallowDeletedUserModificationsRule(user.isDeleted()));

    user.activate();
  }


  public void activateDirectly(UserAggregateRoot user) {
    checkRule(new AllowActivationWhenNoRejectedOnboardingDocumentsRule(onboardingPort, user.getId()));
    checkRule(new DisallowDeletedUserModificationsRule(user.isDeleted()));

    user.activate();
  }


  public void deactivate(UserAggregateRoot user) {
    checkRule(new DisallowDeletedUserModificationsRule(user.isDeleted()));

    user.deactivate();
  }

}
