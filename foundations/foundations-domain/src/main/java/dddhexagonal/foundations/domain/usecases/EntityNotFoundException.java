package dddhexagonal.foundations.domain.usecases;

import java.util.UUID;
import java.util.function.Supplier;

import dddhexagonal.foundations.domain.entity.BaseDomainEntity;

public class EntityNotFoundException extends RuntimeException {

  public static Supplier<EntityNotFoundException> entityNotFound(Class<? extends BaseDomainEntity> type, UUID id) {
    return () -> new EntityNotFoundException(
        "Entity of type " + type.getSimpleName() + " with id " + id + " not found.");
  }

  public static Supplier<EntityNotFoundException> entityNotFound(Class<? extends BaseDomainEntity> type, String message) {
    return () -> new EntityNotFoundException(
        "Entity of type " + type.getSimpleName() + " not found. " + message);
  }

  private EntityNotFoundException(String message) {
    super(message);
  }

}
