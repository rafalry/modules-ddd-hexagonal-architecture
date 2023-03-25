/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.user.application.repository;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import org.springframework.stereotype.Component;

import dddhexagonal.modules.user.domain.user.ports.UserRepositoryPort;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserJpaRepository userRepository;


  @Override
  public boolean isUserActive(UUID id) {
    return userRepository.getById(id).isActive();
  }


  @Override
  public boolean isEmailAlreadyInUse(String email) {
    return userRepository.existsByEmailAndDeleted(email, false);
  }
}
