package dddhexagonal.foundations.application.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProjectionJpaRepository<E> extends JpaRepository<E, UUID>, JpaSpecificationExecutor<E> {
}
