package dddhexagonal.foundations.domain.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

import static java.lang.System.currentTimeMillis;

@Getter
@EqualsAndHashCode
public abstract class BaseDomainEvent {

  @EqualsAndHashCode.Exclude
  private final long eventCreatedTime;
  protected final UUID aggregateRootId;


  protected BaseDomainEvent(UUID aggregateRootId) {
    this.aggregateRootId = aggregateRootId;
    this.eventCreatedTime = currentTimeMillis();
  }
}
