package dddhexagonal.foundations.integration.events;

public interface IntegrationEventBus {

  void publishAsync(IntegrationEvent event);
}
