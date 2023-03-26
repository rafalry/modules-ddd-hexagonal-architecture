package dddhexagonal.modules.user.domain.user.ports;

import java.util.UUID;

public interface UserRepositoryPort {

  boolean isUserActive(UUID id);

  boolean isEmailAlreadyInUse(String email);
}
