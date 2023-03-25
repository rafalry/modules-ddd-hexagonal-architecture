/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.domain.user.ports;

import java.util.UUID;

public interface UserRepositoryPort {

  boolean isUserActive(UUID id);

  boolean isEmailAlreadyInUse(String email);
}
