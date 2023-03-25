/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import dddhexagonal.foundations.domain.entity.BaseAggregateRoot;
import dddhexagonal.modules.user.domain.user.events.UserActivatedDomainEvent;
import dddhexagonal.modules.user.domain.user.events.UserCreatedDomainEvent;
import dddhexagonal.modules.user.domain.user.events.UserDeletedDomainEvent;

@Entity
@Table(name = "users", schema = "user_management")
@Getter
@ToString
@NoArgsConstructor
public class UserAggregateRoot extends BaseAggregateRoot {

  public UserAggregateRoot(String email, boolean triggerOnboarding) {
    super(UUID.randomUUID());
    this.email = email;
    this.active = !triggerOnboarding;

    addDomainEvent(new UserCreatedDomainEvent(getId(), email, !active));
  }

  @Column
  private String email;
  @Column
  private boolean active;
  @Column
  private boolean deleted;

  void activate() {
    if (!active) {
      addDomainEvent(new UserActivatedDomainEvent(getId(), email));
    }
    active = true;
  }

  void deactivate() {
    active = false;
  }

  public void markAsDeleted() {
    deleted = true;
    addDomainEvent(new UserDeletedDomainEvent(getId()));
  }
}
