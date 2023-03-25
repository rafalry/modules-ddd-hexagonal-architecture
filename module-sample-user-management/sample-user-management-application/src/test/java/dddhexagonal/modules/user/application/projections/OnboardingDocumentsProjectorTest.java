package dddhexagonal.modules.user.application.projections;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import dddhexagonal.foundations.test.TestIntegrationSpringConfiguration;
import dddhexagonal.modules.onboarding.integration.events.OnboardingOperationDocumentAcceptedIntegrationEvent;

import static dddhexagonal.modules.user.application.TestConstants.DOCUMENT_ID;
import static dddhexagonal.modules.user.application.TestConstants.DOCUMENT_NAME;
import static dddhexagonal.modules.user.application.TestConstants.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@TestPropertySource(
    properties = {
        "spring.config.location=classpath:application_infrastructure_persistence.yaml",
        "spring.datasource.url=jdbc:tc:postgresql:15.1-alpine:///dddhexagonal?TC_REUSABLE=true",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.flyway.cleanDisabled=false"
    })
@ContextConfiguration(classes = OnboardingDocumentsProjectorTest.SpringConfiguration.class)
class OnboardingDocumentsProjectorTest {


  @Autowired private OnboardingDocumentsProjector projector;
  @Autowired private OnboardingDocumentsProjectionJpaRepository repository;


  @Test
  void should_store_document() {
    OnboardingOperationDocumentAcceptedIntegrationEvent event =
        new OnboardingOperationDocumentAcceptedIntegrationEvent(USER_ID, DOCUMENT_ID, DOCUMENT_NAME);
    projector.storeAcceptedOnboardingDocument(event);

    List<OnboardingDocumentProjectionEntity> documents = repository.findByUserId(USER_ID);
    assertThat(documents).hasSize(1);
    assertThat(documents.get(0).getUserId()).isEqualTo(USER_ID);
    assertThat(documents.get(0).getDocumentId()).isEqualTo(DOCUMENT_ID);
    assertThat(documents.get(0).getDocumentName()).isEqualTo(DOCUMENT_NAME);
  }


  @Configuration
  @EnableJpaRepositories("dddhexagonal.modules.user.application.projections")
  @ComponentScan(basePackages = {"dddhexagonal.modules.user.application.projections", "dddhexagonal.foundations"})
  @EntityScan(basePackages = {"dddhexagonal.modules.user.application.projections"})
  @Import(TestIntegrationSpringConfiguration.class)
  static class SpringConfiguration {

  }
}
