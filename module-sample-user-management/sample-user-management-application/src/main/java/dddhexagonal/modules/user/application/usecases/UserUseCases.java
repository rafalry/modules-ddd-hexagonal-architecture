package dddhexagonal.modules.user.application.usecases;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import org.springframework.stereotype.Component;

import dddhexagonal.foundations.application.usecases.TransactionalUseCase;
import dddhexagonal.foundations.integration.events.IntegrationEventHandler;
import dddhexagonal.modules.onboarding.integration.events.OnboardingOperationCompletedIntegrationEvent;
import dddhexagonal.modules.user.application.repository.UserJpaRepository;
import dddhexagonal.modules.user.domain.user.UserAggregateRoot;
import dddhexagonal.modules.user.domain.user.UserService;

@Component
@RequiredArgsConstructor
@TransactionalUseCase
public class UserUseCases {

  private final UserJpaRepository userRepository;
  private final UserService userService;


  public UUID createUser(String email, boolean triggerOnboarding) {
    UserAggregateRoot user = userService.createUser(email, triggerOnboarding);
    userRepository.save(user);
    return user.getId();
  }


  public UserAggregateRoot getUser(UUID id) {
    return userRepository.getById(id);
  }


  @IntegrationEventHandler
  public void activateUserAfterOnboardingCompleted(OnboardingOperationCompletedIntegrationEvent event) {
    UserAggregateRoot user = userRepository.getById(event.getUserId());
    userService.activateAfterOnboardingCompleted(user);
    userRepository.save(user);
  }


  public void activateUserDirectly(UUID id) {
    UserAggregateRoot user = userRepository.getById(id);
    userService.activateDirectly(user);
    userRepository.save(user);
  }


  public void deactivateUser(UUID id) {
    UserAggregateRoot user = userRepository.getById(id);
    userService.deactivate(user);
    userRepository.save(user);
  }


  public void deleteUser(UUID id) {
    UserAggregateRoot user = userRepository.getById(id);
    user.markAsDeleted();
    userRepository.save(user);
  }
}
