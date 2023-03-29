package dddhexagonal.modules.user.adapter.email;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import dddhexagonal.modules.user.domain.user.events.UserActivatedDomainEvent;
import dddhexagonal.modules.user.domain.user.ports.EmailSenderPort;

@Slf4j
@Service
public class UserEmailSenderAdapter implements EmailSenderPort {


  @Override
  public void sendWelcomeEmail(UserActivatedDomainEvent event) {
    log.info("[EMAIL to={}] WELCOME! YOU ARE NOW ACTIVATED.", event.getEmail());
  }
}
