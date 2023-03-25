package dddhexagonal.foundations.integration.commands;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dddhexagonal.foundations.integration.message.invoke.AsyncGenericMessageHandlerInvoker;
import dddhexagonal.foundations.integration.message.registry.GenericMessageHandlerRegistry;
import dddhexagonal.foundations.integration.message.registry.IntegrationMessageHandlerPostProcessor;

@Configuration
public class IntegrationCommandBusConfiguration {

  @Bean
  public GenericMessageHandlerRegistry integrationCommandHandlerRegistry() {
    return new GenericMessageHandlerRegistry(false);
  }

  @Bean
  public IntegrationMessageHandlerPostProcessor integrationCommandHandlerPostProcessor(
      @Qualifier("integrationCommandHandlerRegistry") GenericMessageHandlerRegistry registry) {
    return new IntegrationMessageHandlerPostProcessor(IntegrationCommandHandler.class, registry,
        IntegrationCommandResult.class, IntegrationCommand.class);
  }

  @Bean
  public AsyncGenericMessageHandlerInvoker integrationCommandHandlerAsyncInvoker(
      @Qualifier("integrationCommandHandlerRegistry") GenericMessageHandlerRegistry registry) {
    return new AsyncGenericMessageHandlerInvoker(registry, true);
  }

  @Bean
  public IntegrationCommandBus integrationCommandBus(
      @Qualifier("integrationCommandHandlerAsyncInvoker") AsyncGenericMessageHandlerInvoker invoker) {
    return new AsyncIntegrationCommandBus(invoker);
  }
}
