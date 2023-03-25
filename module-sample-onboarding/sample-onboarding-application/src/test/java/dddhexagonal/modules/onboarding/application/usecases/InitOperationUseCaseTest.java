package dddhexagonal.modules.onboarding.application.usecases;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import dddhexagonal.foundations.test.TestIntegrationSpringConfiguration;
import dddhexagonal.foundations.test.integration.TestIntegrationEventBus;
import dddhexagonal.modules.onboarding.application.repository.OnboardingOperationJpaRepository;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.operation.ports.EmailSenderPort;
import dddhexagonal.modules.onboarding.integration.commands.InitOnboardingIntegrationCommand;
import dddhexagonal.modules.onboarding.integration.events.OnboardingOperationCompletedIntegrationEvent;
import dddhexagonal.modules.onboarding.integration.events.OnboardingOperationDocumentAcceptedIntegrationEvent;

import static dddhexagonal.modules.onboarding.application.TestConstants.DOCUMENT_0_NAME;
import static dddhexagonal.modules.onboarding.application.TestConstants.DOCUMENT_1_NAME;
import static dddhexagonal.modules.onboarding.application.TestConstants.EMAIL;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(
    properties = {
        "spring.config.location=classpath:application_infrastructure_persistence.yaml",
        "spring.datasource.url=jdbc:tc:postgresql:15.1-alpine:///dddhexagonal?TC_REUSABLE=true",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.flyway.cleanDisabled=false"
    })
@ContextConfiguration(classes = InitOperationUseCaseTest.SpringConfiguration.class)
class InitOperationUseCaseTest {

  @Autowired private InitOperationUseCase initOperationUseCase;
  @Autowired private TemplateUseCases templateUseCases;
  @Autowired private OnboardingOperationJpaRepository operationRepository;
  @Autowired private TestIntegrationEventBus integrationEventBus;
  @Autowired private OperationDocumentUseCases operationDocumentUseCases;


  @BeforeEach
  void setUp() {
     templateUseCases.create("t1", of(DOCUMENT_0_NAME, DOCUMENT_1_NAME));
  }


  @Test
  void should_init_onboarding() {
    UUID userId = randomUUID();
    initOperationUseCase.initOnboarding(new InitOnboardingIntegrationCommand(userId, EMAIL));

    Optional<OnboardingOperationAggregateRoot> operation = operationRepository.findByUserId(userId);
    assertThat(operation).isPresent();
    assertThat(operation.get().getEmail()).isEqualTo(EMAIL);
    assertThat(operation.get().getDocuments()).hasSize(2);
  }


  @Test
  void should_handle_full_operation_lifecycle() {
    UUID userId = randomUUID();
    initOperationUseCase.initOnboarding(new InitOnboardingIntegrationCommand(userId, EMAIL));
    OnboardingOperationAggregateRoot operation = operationRepository.findByUserId(userId).get();
    UUID operationId = operation.getId();
    UUID doc0Id = operation.getDocuments().get(0).getId();
    UUID doc1Id = operation.getDocuments().get(1).getId();

    operationDocumentUseCases.signDocument(operationId, doc0Id);
    operationDocumentUseCases.signDocument(operationId, doc1Id);
    assertThat(integrationEventBus.isClean()).isTrue();

    operationDocumentUseCases.acceptDocument(operationId, doc0Id);

    assertThat(integrationEventBus.getEvents()).containsOnly(
        new OnboardingOperationDocumentAcceptedIntegrationEvent(userId, doc0Id, DOCUMENT_0_NAME));
    integrationEventBus.clean();

    operationDocumentUseCases.acceptDocument(operationId, doc1Id);

    assertThat(integrationEventBus.getEvents()).containsOnly(
        new OnboardingOperationDocumentAcceptedIntegrationEvent(userId, doc1Id, DOCUMENT_1_NAME),
        new OnboardingOperationCompletedIntegrationEvent(userId));
  }


  @AfterEach
  public void tearDown() {
    log.info("All OnboardingOperation entities: {}", operationRepository.findAll());
  }


  @Configuration
  @EnableJpaRepositories("dddhexagonal.modules.onboarding.application.repository")
  @ComponentScan(basePackages = {"dddhexagonal.modules.onboarding", "dddhexagonal.foundations"})
  @EntityScan(basePackages = {"dddhexagonal.modules.onboarding.domain.operation",
      "dddhexagonal.modules.onboarding.domain.template"})
  @Import(TestIntegrationSpringConfiguration.class)
  static class SpringConfiguration {

    @Bean
    EmailSenderPort emailSenderPort() {
      return mock(EmailSenderPort.class);
    }
  }
}
