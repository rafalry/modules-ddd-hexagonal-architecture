package dddhexagonal.foundations.domain.entity;

import java.util.Collection;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

public class TestDomainEventsAccessor {

  public static Collection<BaseDomainEvent> getDomainEvents(BaseAggregateRoot aggregateRoot) {
    return aggregateRoot.domainEvents();
  }

  public static void clearDomainEvents(BaseAggregateRoot aggregateRoot) {
    aggregateRoot.clearDomainEvents();
  }

}
