package dddhexagonal.foundations.integration.query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dddhexagonal.foundations.integration.message.invoke.AsyncGenericMessageHandlerInvoker;
import dddhexagonal.foundations.integration.message.registry.GenericMessageHandlerRegistry;
import dddhexagonal.foundations.integration.message.registry.IntegrationMessageHandlerPostProcessor;

@Configuration
public class IntegrationQueryBusConfiguration {

  @Bean
  public GenericMessageHandlerRegistry integrationQueryHandlerRegistry() {
    return new GenericMessageHandlerRegistry(false);
  }

  @Bean
  public IntegrationMessageHandlerPostProcessor integrationQueryHandlerPostProcessor(
      @Qualifier("integrationQueryHandlerRegistry") GenericMessageHandlerRegistry registry) {
    return new IntegrationMessageHandlerPostProcessor(IntegrationQueryHandler.class, registry,
        IntegrationQueryResult.class, IntegrationQuery.class);
  }

  @Bean
  public AsyncGenericMessageHandlerInvoker integrationQueryHandlerAsyncInvoker(
      @Qualifier("integrationQueryHandlerRegistry") GenericMessageHandlerRegistry registry) {
    return new AsyncGenericMessageHandlerInvoker(registry, true);
  }

  @Bean
  public IntegrationQueryBus integrationQueryBus(
      @Qualifier("integrationQueryHandlerAsyncInvoker") AsyncGenericMessageHandlerInvoker invoker) {
    return new AsyncIntegrationQueryBus(invoker);
  }
}
