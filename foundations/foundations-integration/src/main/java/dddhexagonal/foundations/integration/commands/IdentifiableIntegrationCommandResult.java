package dddhexagonal.foundations.integration.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public final class IdentifiableIntegrationCommandResult extends IntegrationCommandResult  {

  public static IdentifiableIntegrationCommandResult success(UUID id) {
    return new IdentifiableIntegrationCommandResult(of(id));
  }

  public static IdentifiableIntegrationCommandResult failure() {
    return new IdentifiableIntegrationCommandResult(empty());
  }

  @Getter
  private final Optional<UUID> id;

  public boolean isSuccess() {
    return id.isPresent();
  }
}
