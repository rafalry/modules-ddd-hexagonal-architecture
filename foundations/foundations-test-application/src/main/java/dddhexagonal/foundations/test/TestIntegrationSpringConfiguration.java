package dddhexagonal.foundations.test;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

import dddhexagonal.foundations.test.integration.TestIntegrationCommandBus;
import dddhexagonal.foundations.test.integration.TestIntegrationEventBus;
import dddhexagonal.foundations.test.integration.TestIntegrationQueryBus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TestIntegrationSpringConfiguration {

  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return flyway -> {
      flyway.clean();
      flyway.migrate();
    };
  }

  @Bean
  public TestIntegrationCommandBus integrationCommandBus() {
    return spy(new TestIntegrationCommandBus());
  }

  @Bean
  public TestIntegrationQueryBus integrationQueryBus() {
    return spy(TestIntegrationQueryBus.class);
  }

  @Bean
  public TestIntegrationEventBus integrationEventBus() {
    return spy(TestIntegrationEventBus.class);
  }

}
