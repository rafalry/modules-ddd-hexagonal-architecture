
package dddhexagonal.infrastructure.emailgateway;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailSenderConfiguration {

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setProtocol("smtp");
    sender.setHost("127.0.0.1");
    sender.setPort(1025);
    Properties props = sender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "false");
    props.put("mail.smtp.starttls.enable", "false");
    props.put("mail.debug", "false");
    return sender;
  }

}
