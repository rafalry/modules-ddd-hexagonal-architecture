package dddhexagonal.modules.onboarding.domain.template;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import org.springframework.stereotype.Service;

import dddhexagonal.foundations.domain.service.BaseDomainService;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.operation.ports.OnboardingOperationRepositoryPort;
import dddhexagonal.modules.onboarding.domain.template.rules.OnboardingForUserHasNotStartedYetRule;

@Service
@RequiredArgsConstructor
public class CreateOnboardingOperationService extends BaseDomainService {

  private final OnboardingOperationRepositoryPort onboardingOperationRepositoryPort;


  public OnboardingOperationAggregateRoot initOperation(
      OnboardingTemplateAggregateRoot template, UUID userId, String email) {
    checkRule(new OnboardingForUserHasNotStartedYetRule(onboardingOperationRepositoryPort, userId));

    return template.createOperation(userId, email);
  }
}
