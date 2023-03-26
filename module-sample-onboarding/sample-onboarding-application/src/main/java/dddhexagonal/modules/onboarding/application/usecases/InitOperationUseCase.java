package dddhexagonal.modules.onboarding.application.usecases;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import org.springframework.stereotype.Service;

import dddhexagonal.foundations.application.usecases.TransactionalUseCase;
import dddhexagonal.foundations.integration.commands.IntegrationCommandHandler;
import dddhexagonal.foundations.integration.commands.VoidIntegrationCommandResult;
import dddhexagonal.modules.onboarding.application.repository.OnboardingOperationJpaRepository;
import dddhexagonal.modules.onboarding.application.repository.OnboardingTemplateJpaRepository;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.template.OnboardingTemplateAggregateRoot;
import dddhexagonal.modules.onboarding.domain.template.CreateOnboardingOperationService;
import dddhexagonal.modules.onboarding.integration.commands.CancelOnboardingIntegrationCommand;
import dddhexagonal.modules.onboarding.integration.commands.InitOnboardingIntegrationCommand;

import static dddhexagonal.foundations.domain.usecases.EntityNotFoundException.entityNotFound;

@Service
@RequiredArgsConstructor
@TransactionalUseCase
public class InitOperationUseCase {

  private final OnboardingTemplateJpaRepository templateRepository;
  private final OnboardingOperationJpaRepository operationRepository;
  private final CreateOnboardingOperationService templateService;

  @IntegrationCommandHandler
  public VoidIntegrationCommandResult initOnboarding(InitOnboardingIntegrationCommand command) {
    OnboardingTemplateAggregateRoot currentTemplate = getCurrentOnboardingTemplate();
    OnboardingOperationAggregateRoot operation = templateService.initOperation(currentTemplate, command.getUserId(), command.getEmail());
    operationRepository.save(operation);
    return new VoidIntegrationCommandResult();
  }

  @IntegrationCommandHandler
  public VoidIntegrationCommandResult cancelOnboarding(CancelOnboardingIntegrationCommand command) {
    return new VoidIntegrationCommandResult();
  }


  private OnboardingTemplateAggregateRoot getCurrentOnboardingTemplate() {
    Optional<OnboardingTemplateAggregateRoot> template = templateRepository.findFirstByActiveIsTrue();
      return template.orElseThrow(entityNotFound(OnboardingTemplateAggregateRoot.class,
        "Expected to find one active OnboardingTemplate, not found any."));
  }
}
