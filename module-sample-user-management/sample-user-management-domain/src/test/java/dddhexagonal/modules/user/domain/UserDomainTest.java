package dddhexagonal.modules.user.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dddhexagonal.foundations.domain.entity.TestDomainEventsContextRunner;
import dddhexagonal.foundations.domain.rule.BusinessRuleException;
import dddhexagonal.modules.user.domain.user.UserAggregateRoot;
import dddhexagonal.modules.user.domain.user.UserService;
import dddhexagonal.modules.user.domain.user.events.UserActivatedDomainEvent;
import dddhexagonal.modules.user.domain.user.events.UserCreatedDomainEvent;
import dddhexagonal.modules.user.domain.user.events.UserDeletedDomainEvent;
import dddhexagonal.modules.user.domain.user.ports.EmailSenderPort;
import dddhexagonal.modules.user.domain.user.ports.OnboardingPort;
import dddhexagonal.modules.user.domain.user.ports.UserRepositoryPort;

import static dddhexagonal.modules.user.domain.TestConstants.EMAIL;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDomainTest {


  @InjectMocks UserService userService;
  @Mock UserRepositoryPort userRepositoryPort;
  @Mock OnboardingPort onboardingPort;
  @Mock EmailSenderPort emailSenderPort;
  TestDomainEventsContextRunner domainEventsContextRunner;

  @BeforeEach
  void setUp() {
    domainEventsContextRunner = new TestDomainEventsContextRunner(
        of(OnboardingPort.class, () -> onboardingPort,
           EmailSenderPort.class, () -> emailSenderPort) );
  }


  @Test
  void should_create_user() {
    UserAggregateRoot user = userService.createUser(EMAIL, true);

    domainEventsContextRunner.run(user);

    assertThat(user.getEmail()).isEqualTo(EMAIL);
    verify(onboardingPort).initOnboarding(new UserCreatedDomainEvent(user.getId(), EMAIL, true));
  }


  @Test
  void should_activate_user() {
    UserAggregateRoot user = userService.createUser(EMAIL, true);
    userService.activateDirectly(user);

    domainEventsContextRunner.run(user);

    verify(emailSenderPort).sendWelcomeEmail(new UserActivatedDomainEvent(user.getId(), EMAIL));
  }


  @Test
  void should_not_activate_deleted_user() {
    UserAggregateRoot user = userService.createUser(EMAIL, true);
    userService.deleteUser(user);

    assertThrows(BusinessRuleException.class, () -> userService.activateDirectly(user));
  }


  @Test
  void should_not_create_user_for_exising_email() {
    when(userRepositoryPort.isEmailAlreadyInUse(EMAIL)).thenReturn(true);
    assertThatExceptionOfType(BusinessRuleException.class)
        .isThrownBy(() -> userService.createUser(EMAIL, true));
  }


  @Test
  void should_delete_user() {
    UserAggregateRoot user = userService.createUser(EMAIL, true);
    userService.deleteUser(user);

    domainEventsContextRunner.run(user);

    verify(onboardingPort).cancelOnboarding(new UserDeletedDomainEvent(user.getId()));
  }

}
