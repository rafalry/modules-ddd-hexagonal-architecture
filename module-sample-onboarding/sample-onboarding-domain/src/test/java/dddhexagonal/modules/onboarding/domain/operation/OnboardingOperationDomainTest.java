package dddhexagonal.modules.onboarding.domain.operation;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dddhexagonal.foundations.domain.entity.BaseDomainEntity;
import dddhexagonal.foundations.domain.entity.TestDomainEventsContextRunner;
import dddhexagonal.modules.onboarding.domain.operation.ports.EmailSenderPort;

import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.DOCUMENT_NAME;
import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.EMAIL;
import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.TEMPLATE_1_ID;
import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.USER_ID;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.COMPLETED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.IN_PROGRESS;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.PRISTINE;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class})
class OnboardingOperationDomainTest {

  @Mock EmailSenderPort emailSenderPort;
  OnboardingOperationDomainEventHandler onboardingOperationDomainEventHandler;
  TestDomainEventsContextRunner contextRunner;


  @BeforeEach
  void setUp() {
    onboardingOperationDomainEventHandler = new OnboardingOperationDomainEventHandler(emailSenderPort);
    contextRunner = new TestDomainEventsContextRunner(
        OnboardingOperationDomainEventHandler.class,
        () -> onboardingOperationDomainEventHandler);
  }


  @Test
  void should_handle_basic_operation_lifecycle() {
    OnboardingOperationAggregateRoot operation = new OnboardingOperationAggregateRoot(
        USER_ID,
        TEMPLATE_1_ID,
        EMAIL,
        of(DOCUMENT_NAME));
    UUID documentId = operation.getDocuments().get(0).getId();

    assertThat(operation.getProgressStatus()).isEqualTo(PRISTINE);

    operation.signDocument(documentId);

    assertThat(operation.getProgressStatus()).isEqualTo(IN_PROGRESS);
    emailSenderPort.sendDocumentsEmail(EMAIL, operation.getId(), operation.getDocuments().stream().map(BaseDomainEntity::getId).toList());

    operation.acceptDocument(documentId);

    assertThat(operation.getProgressStatus()).isEqualTo(COMPLETED);
  }
}
