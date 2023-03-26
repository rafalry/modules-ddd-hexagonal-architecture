package dddhexagonal.modules.user.domain.user.ports;

import org.springframework.transaction.event.TransactionalEventListener;

import dddhexagonal.modules.user.domain.user.events.UserActivatedDomainEvent;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

public interface EmailSenderPort {

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  void sendWelcomeEmail(UserActivatedDomainEvent event);
}
