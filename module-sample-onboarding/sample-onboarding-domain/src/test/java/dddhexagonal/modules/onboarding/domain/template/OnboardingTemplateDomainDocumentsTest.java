package dddhexagonal.modules.onboarding.domain.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dddhexagonal.foundations.domain.entity.TestDomainEventsContextRunner;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.operation.ports.OnboardingOperationRepositoryPort;

import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.DOCUMENT_NAME;
import static dddhexagonal.modules.onboarding.domain.OnboardingTestConstants.TEMPLATE_1_NAME;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.PRISTINE;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OnboardingTemplateDomainDocumentsTest {

  TestDomainEventsContextRunner domainEventsContextRunner;
  @Mock OnboardingOperationRepositoryPort operationRepositoryPort;
  @InjectMocks OnboardingTemplateDomainEventHandlers documentsUpdatedDomainEventHandler;


  @BeforeEach
  void setUp() {
    domainEventsContextRunner = new TestDomainEventsContextRunner(
        OnboardingTemplateDomainEventHandlers.class, () -> documentsUpdatedDomainEventHandler);
  }


  /**
   * INFO unit test for side effect to another aggregate
   */
  @Test
  void should_update_documents_for_pristine_operations_when_template_documents_updated() {
    OnboardingOperationAggregateRoot operation1 = mock(OnboardingOperationAggregateRoot.class);
    OnboardingOperationAggregateRoot operation2 = mock(OnboardingOperationAggregateRoot.class);
    OnboardingTemplateAggregateRoot template = new OnboardingTemplateAggregateRoot(TEMPLATE_1_NAME, emptyList());
    template.setActive(true);
    when(operationRepositoryPort.findByTemplateIdAndProgressStatus(template.getId(), PRISTINE))
        .thenReturn(of(operation1, operation2));

    template.modifyDocuments(of(DOCUMENT_NAME), emptyList());
    domainEventsContextRunner.run(template);

    verify(operation1).setDocuments(of(DOCUMENT_NAME));
    verify(operation2).setDocuments(of(DOCUMENT_NAME));
  }
}
