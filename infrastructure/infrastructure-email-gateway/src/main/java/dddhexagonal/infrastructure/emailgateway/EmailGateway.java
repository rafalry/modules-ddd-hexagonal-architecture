package dddhexagonal.infrastructure.emailgateway;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailGateway {

  private static final String FROM_EMAIL = "dddhexagonal@example.com";
  private final JavaMailSender sender;


  public void send(String email, String subject, String html) {
    try {
      MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
      helper.setFrom(FROM_EMAIL);
      helper.setTo(email);
      helper.setSubject(subject);
      helper.setText(html, true);
      sender.send(message);
    } catch (Exception exception) {
      exception.printStackTrace();
      throw new EmailSendingException("Unable to send email to " + email, exception);
    }
  }
}
