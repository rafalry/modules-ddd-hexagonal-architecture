package dddhexagonal.foundations.test.integration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import dddhexagonal.foundations.integration.events.IntegrationEvent;
import dddhexagonal.foundations.integration.events.IntegrationEventBus;

public class TestIntegrationEventBus implements IntegrationEventBus {

  @Getter
  private List<IntegrationEvent> events = new ArrayList<>();

  @Override
  public void publishAsync(IntegrationEvent event) {
    events.add(event);
  }


  public void clean() {
    events = new ArrayList<>();
  }

  public boolean isClean() {
    return events.isEmpty();
  }
}
