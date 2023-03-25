/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.domain.user.ports;

import java.util.UUID;

import org.springframework.transaction.event.TransactionalEventListener;

import dddhexagonal.modules.user.domain.user.events.UserCreatedDomainEvent;
import dddhexagonal.modules.user.domain.user.events.UserDeletedDomainEvent;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

public interface OnboardingPort {

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  void cancelOnboarding(UserDeletedDomainEvent event);

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  void initOnboarding(UserCreatedDomainEvent event);

  void updateContactDetails(UUID userId, String email);

  boolean hasRejectedDocuments(UUID userId);
}
