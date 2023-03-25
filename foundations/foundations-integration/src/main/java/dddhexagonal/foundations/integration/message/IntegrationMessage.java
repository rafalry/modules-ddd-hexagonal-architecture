package dddhexagonal.foundations.integration.message;

import lombok.Getter;
import lombok.ToString;

import static java.lang.System.currentTimeMillis;

@ToString
public abstract class IntegrationMessage {

  @Getter
  private final long createdTime;

  protected IntegrationMessage() {
    this.createdTime = currentTimeMillis();
  }
}
