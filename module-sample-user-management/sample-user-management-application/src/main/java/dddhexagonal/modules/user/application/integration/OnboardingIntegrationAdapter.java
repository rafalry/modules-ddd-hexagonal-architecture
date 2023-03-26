package dddhexagonal.modules.user.application.integration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;

import dddhexagonal.foundations.integration.commands.IntegrationCommandBus;
import dddhexagonal.foundations.integration.query.IntegrationQueryBus;
import dddhexagonal.modules.onboarding.integration.commands.CancelOnboardingIntegrationCommand;
import dddhexagonal.modules.onboarding.integration.commands.InitOnboardingIntegrationCommand;
import dddhexagonal.modules.onboarding.integration.commands.UpdateOnboardingContactDetailsIntegrationCommand;
import dddhexagonal.modules.onboarding.integration.query.GetOnboardingOperationDocumentsIntegrationQuery;
import dddhexagonal.modules.onboarding.integration.query.GetOnboardingOperationDocumentsIntegrationQueryResult;
import dddhexagonal.modules.user.domain.user.events.UserCreatedDomainEvent;
import dddhexagonal.modules.user.domain.user.events.UserDeletedDomainEvent;
import dddhexagonal.modules.user.domain.user.ports.OnboardingPort;

import static dddhexagonal.modules.onboarding.integration.query.OnboardingOperationDocumentSigningStatus.REJECTED;

@Component
@RequiredArgsConstructor
public class OnboardingIntegrationAdapter implements OnboardingPort {


  private final IntegrationCommandBus commandBus;
  private final IntegrationQueryBus queryBus;


  @Override
  public void initOnboarding(UserCreatedDomainEvent event) {
    if (event.isTriggerOnboarding()) {
      commandBus.dispatchAsync(new InitOnboardingIntegrationCommand(event.getAggregateRootId(), event.getEmail()));
    }
  }


  @Override
  public void cancelOnboarding(UserDeletedDomainEvent event) {
    commandBus.dispatchAsync(new CancelOnboardingIntegrationCommand(event.getAggregateRootId()));
  }


  @Override
  public void updateContactDetails(UUID userId, String email) {
    commandBus.dispatchAsync(new UpdateOnboardingContactDetailsIntegrationCommand(userId, email));
  }


  @Override
  @SneakyThrows
  public boolean hasRejectedDocuments(UUID userId) {
    CompletableFuture<GetOnboardingOperationDocumentsIntegrationQueryResult> future =
        queryBus.queryAsync(new GetOnboardingOperationDocumentsIntegrationQuery(userId));
    return future.get().getDocumentStatuses().values().stream().anyMatch(REJECTED::equals);
  }
}
