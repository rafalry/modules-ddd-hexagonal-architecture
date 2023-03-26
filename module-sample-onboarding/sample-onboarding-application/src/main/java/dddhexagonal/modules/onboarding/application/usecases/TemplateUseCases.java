package dddhexagonal.modules.onboarding.application.usecases;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

import dddhexagonal.foundations.application.usecases.TransactionalUseCase;
import dddhexagonal.modules.onboarding.application.repository.OnboardingOperationJpaRepository;
import dddhexagonal.modules.onboarding.application.repository.OnboardingTemplateJpaRepository;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.template.OnboardingTemplateAggregateRoot;
import dddhexagonal.modules.onboarding.domain.template.OnboardingTemplateService;

import static dddhexagonal.foundations.domain.usecases.EntityNotFoundException.entityNotFound;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.IN_PROGRESS;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.PRISTINE;

@Service
@RequiredArgsConstructor
@TransactionalUseCase
public class TemplateUseCases {

  private final OnboardingTemplateJpaRepository templateRepository;
  private final OnboardingOperationJpaRepository operationRepository;
  private final OnboardingTemplateService service;

  public UUID create(String name, List<String> documents) {
    Optional<OnboardingTemplateAggregateRoot> active = templateRepository.findFirstByActiveIsTrue();
    List<OnboardingTemplateAggregateRoot> all = templateRepository.findAll();
    
    OnboardingTemplateAggregateRoot template = service.addTemplate(name, documents, active, all);

    if (active.isPresent()) {
      templateRepository.save(active.get());
    }
    templateRepository.save(template);
    return template.getId();
  }


  public OnboardingTemplateAggregateRoot get(UUID id) {
    return getTemplate(id);
  }


  public List<OnboardingTemplateAggregateRoot> search() {
    return templateRepository.findAll();
  }


  /**
   * INFO Interaction: modification of multiple aggregates in a single transaction. Alternative approaches are:
   * - modify previously active template as a side effect run by DomainEventHandler on the domain layer
   * - reorganise aggregate so that a Template is a root which includes all Operations
   */
  public void activate(UUID id) {
    Optional<OnboardingTemplateAggregateRoot> active = templateRepository.findFirstByActiveIsTrue();
    OnboardingTemplateAggregateRoot toActivate = getTemplate(id);

    service.activateTemplate(toActivate, active);

    if (active.isPresent()) {
      templateRepository.save(active.get());
    }
    templateRepository.save(toActivate);
  }
  /**
   * Involves business logic:
   * - disallow template archive if there are any operations in progress
   * - on template archive cancel all operations with no progress
   */
  public void archive(UUID id) {
    OnboardingTemplateAggregateRoot template = getTemplate(id);
    List<OnboardingOperationAggregateRoot> opsWithoutProgress = operationRepository.findByTemplateIdAndProgressStatus(
        template.getId(),
        PRISTINE);
    long opsInProgressCount = operationRepository.countByTemplateIdAndProgressStatus(template.getId(), IN_PROGRESS);

    service.archiveTemplate(template, opsWithoutProgress, opsInProgressCount);

    templateRepository.save(template);
  }


  // TODO expose API


  public void modifyDocuments(UUID id, List<String> documentNamesToAdd, List<String> documentNamesToRemove) {
    OnboardingTemplateAggregateRoot template = getTemplate(id);
    template.modifyDocuments(documentNamesToAdd, documentNamesToRemove);
    templateRepository.save(template);
  }


  private OnboardingTemplateAggregateRoot getTemplate(UUID id) {
    Optional<OnboardingTemplateAggregateRoot> template = templateRepository.findById(id);
    if (template.isPresent()) {
      return template.get();
    }
    throw new IllegalArgumentException("Could not find onboarding template with id: " + id);
  }
}
