package dddhexagonal.foundations.domain.repository;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import dddhexagonal.foundations.domain.entity.BaseAggregateRoot;

@NoRepositoryBean
public interface BaseJpaRepository<Aggregate extends BaseAggregateRoot> extends JpaRepository<Aggregate, UUID>,
    JpaSpecificationExecutor<Aggregate> {


  default @NotNull Aggregate getById(@NotNull UUID id) {
    return findById(id).orElseThrow(() -> new InvalidAggregateIdException(id));
  }
}
