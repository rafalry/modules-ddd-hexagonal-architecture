package dddhexagonal.modules.user.application.projections;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

import dddhexagonal.foundations.application.repository.ProjectionJpaRepository;

@Repository
public interface OnboardingDocumentsProjectionJpaRepository
    extends ProjectionJpaRepository<OnboardingDocumentProjectionEntity> {

  List<OnboardingDocumentProjectionEntity> findByUserId(UUID userId);
}
