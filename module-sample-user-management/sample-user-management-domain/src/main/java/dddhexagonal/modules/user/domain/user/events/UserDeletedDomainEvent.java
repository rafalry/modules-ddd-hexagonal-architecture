/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.domain.user.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserDeletedDomainEvent extends BaseDomainEvent {

  public UserDeletedDomainEvent(UUID userId) {
    super(userId);
  }
}
