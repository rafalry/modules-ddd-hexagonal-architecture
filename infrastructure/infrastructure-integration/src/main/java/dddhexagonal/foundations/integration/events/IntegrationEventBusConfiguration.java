package dddhexagonal.foundations.integration.events;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dddhexagonal.foundations.integration.message.invoke.AsyncGenericMessageHandlerInvoker;
import dddhexagonal.foundations.integration.message.registry.GenericMessageHandlerRegistry;
import dddhexagonal.foundations.integration.message.registry.IntegrationMessageHandlerPostProcessor;

@Configuration
public class IntegrationEventBusConfiguration {

  @Bean
  public GenericMessageHandlerRegistry integrationEventHandlerRegistry() {
    return new GenericMessageHandlerRegistry(true);
  }

  @Bean
  public IntegrationMessageHandlerPostProcessor integrationEventHandlerPostProcessor(
      @Qualifier("integrationEventHandlerRegistry") GenericMessageHandlerRegistry registry) {
    return new IntegrationMessageHandlerPostProcessor(IntegrationEventHandler.class, registry, Void.TYPE,
        IntegrationEvent.class);
  }

  @Bean
  public AsyncGenericMessageHandlerInvoker integrationEventHandlerAsyncInvoker(
      @Qualifier("integrationEventHandlerRegistry") GenericMessageHandlerRegistry registry) {
    return new AsyncGenericMessageHandlerInvoker(registry, false);
  }

  @Bean
  public IntegrationEventBus integrationEventBus(
      @Qualifier("integrationEventHandlerAsyncInvoker") AsyncGenericMessageHandlerInvoker invoker) {
    return new AsyncIntegrationEventBus(invoker);
  }
}
