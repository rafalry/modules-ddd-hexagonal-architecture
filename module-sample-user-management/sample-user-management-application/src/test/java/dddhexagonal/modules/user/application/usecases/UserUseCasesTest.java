package dddhexagonal.modules.user.application.usecases;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import dddhexagonal.foundations.test.TestIntegrationSpringConfiguration;
import dddhexagonal.foundations.test.integration.TestIntegrationCommandBus;
import dddhexagonal.modules.onboarding.integration.commands.InitOnboardingIntegrationCommand;
import dddhexagonal.modules.user.application.repository.UserJpaRepository;
import dddhexagonal.modules.user.domain.user.UserAggregateRoot;

import static dddhexagonal.modules.user.application.TestConstants.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "spring.config.location=classpath:application_infrastructure_persistence.yaml",
    "spring.datasource.url=jdbc:tc:postgresql:15.1-alpine:///dddhexagonal?TC_REUSABLE=true",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
    "spring.flyway.cleanDisabled=false"
})
@ContextConfiguration(classes = UserUseCasesTest.SpringConfiguration.class)
class UserUseCasesTest {

  @Autowired private UserJpaRepository repository;
  @Autowired private UserUseCases userUseCases;
  @Autowired private TestIntegrationCommandBus commandBus;


  @Test
  void should_create_user() {
    userUseCases.createUser(EMAIL, false);

    assertThat(repository.existsByEmailAndDeleted(EMAIL, false)).isTrue();
    assertThat(commandBus.getCommands()).isEmpty();
  }


  @Test
  void should_create_user_with_onboarding() {
    userUseCases.createUser(EMAIL, true);

    UserAggregateRoot user = repository.findAll().get(0);

    assertThat(repository.existsByEmailAndDeleted(EMAIL, false)).isTrue();
    assertThat(commandBus.getCommands()).containsOnly(new InitOnboardingIntegrationCommand(user.getId(), EMAIL));
  }


  @Configuration
  @EnableJpaRepositories("dddhexagonal.modules.user.application.repository")
  @ComponentScan(basePackages = {"dddhexagonal.modules.user", "dddhexagonal.foundations"})
  @EntityScan(basePackages = "dddhexagonal.modules.user.domain.user")
  @Import(TestIntegrationSpringConfiguration.class)
  static class SpringConfiguration {

  }
}
