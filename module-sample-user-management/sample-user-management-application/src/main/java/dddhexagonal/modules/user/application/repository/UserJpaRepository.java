package dddhexagonal.modules.user.application.repository;

import org.springframework.stereotype.Repository;

import dddhexagonal.foundations.domain.repository.BaseJpaRepository;
import dddhexagonal.modules.user.domain.user.UserAggregateRoot;

@Repository
public interface UserJpaRepository extends BaseJpaRepository<UserAggregateRoot> {

  boolean existsByEmailAndDeleted(String email, boolean deleted);
}
