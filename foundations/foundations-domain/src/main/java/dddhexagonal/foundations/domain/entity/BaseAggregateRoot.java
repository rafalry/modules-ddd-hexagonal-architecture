/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.domain.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

@MappedSuperclass
@NoArgsConstructor
public abstract class BaseAggregateRoot extends BaseDomainEntity<UUID> {

  private final @Transient List<BaseDomainEvent> domainEvents = new ArrayList<>();


  protected BaseAggregateRoot(UUID aggregateRootId) {
    super(aggregateRootId);
  }


  protected void addDomainEvent(@NotNull BaseDomainEvent event) {
    domainEvents.add(Objects.requireNonNull(event));
  }


  @AfterDomainEventPublication
  protected void clearDomainEvents() {
    this.domainEvents.clear();
  }


  @DomainEvents
  protected Collection<BaseDomainEvent> domainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

}
