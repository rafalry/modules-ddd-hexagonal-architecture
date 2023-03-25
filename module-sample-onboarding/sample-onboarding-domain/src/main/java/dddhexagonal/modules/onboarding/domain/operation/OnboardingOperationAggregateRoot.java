/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.onboarding.domain.operation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.Cascade;

import dddhexagonal.foundations.domain.entity.BaseAggregateRoot;
import dddhexagonal.foundations.domain.entity.BaseDomainEntity;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationCancelledDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationCompletedDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationDocumentAcceptedDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationDocumentsUpdatedDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.events.OnboardingOperationInitiatedDomainEvent;
import dddhexagonal.modules.onboarding.domain.operation.rules.AllowDocumentsActionsOnlyForNotClosedOperationsRule;
import dddhexagonal.modules.onboarding.domain.operation.rules.AllowDocumentsModificationOnlyForPristineOperationsRule;
import dddhexagonal.modules.onboarding.domain.operation.rules.OnlyUncompletedOnboardingCanBeCancelledRule;

import static dddhexagonal.foundations.domain.usecases.EntityNotFoundException.entityNotFound;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.ACCEPTED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.REJECTED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.CANCELLED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.COMPLETED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.IN_PROGRESS;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationProgressStatus.PRISTINE;
import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@Table(name = "operations", schema = "onboarding")
@Getter
@ToString
@NoArgsConstructor
public class OnboardingOperationAggregateRoot extends BaseAggregateRoot {

  public OnboardingOperationAggregateRoot(
      UUID userId,
      UUID templateId,
      String email,
      List<String> documentNames) {
    super(UUID.randomUUID());
    this.userId = userId;
    this.templateId = templateId;
    this.email = email;
    this.documents = documentNames.stream()
        .map(name -> new OnboardingOperationDocument(name, this))
        .toList();
    this.progressStatus = PRISTINE;

    List<UUID> documentIds = documents.stream().map(BaseDomainEntity::getId).toList();
    addDomainEvent(new OnboardingOperationInitiatedDomainEvent(getId(), email, documentIds));
  }


  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "template_id")
  private UUID templateId;

  @Column(name = "email")
  private String email;

  @Column(name = "progress_status")
  @Enumerated(EnumType.STRING)
  private OnboardingOperationProgressStatus progressStatus;

  @OneToMany(mappedBy = "operation", fetch = FetchType.EAGER)
  @Cascade(ALL)
  private List<OnboardingOperationDocument> documents;


  public void setDocuments(List<String> documentNames) {
    checkRule(new AllowDocumentsModificationOnlyForPristineOperationsRule(progressStatus));

    documents.removeIf(d -> !documentNames.contains(d.getName()));

    List<String> currentNames = documents.stream().map(OnboardingOperationDocument::getName).toList();
    List<String> toAdd = new ArrayList<>(documentNames);
    toAdd.removeIf(currentNames::contains);

    toAdd.forEach(dn -> documents.add(new OnboardingOperationDocument(dn, this)));

    List<UUID> documentIds = documents.stream()
        .filter(d -> documentNames.contains(d.getName()))
        .map(BaseDomainEntity::getId)
        .toList();

    addDomainEvent(new OnboardingOperationDocumentsUpdatedDomainEvent(getId(), email, documentIds));
  }


  public void signDocument(UUID documentId) {
    checkRule(new AllowDocumentsActionsOnlyForNotClosedOperationsRule(progressStatus));

    OnboardingOperationDocument document = getDocument(documentId);
    document.sign();
    progressStatus = IN_PROGRESS;
    checkOperationClosed();
  }


  public void acceptDocument(UUID documentId) {
    checkRule(new AllowDocumentsActionsOnlyForNotClosedOperationsRule(progressStatus));

    OnboardingOperationDocument document = getDocument(documentId);
    document.accept();
    addDomainEvent(new OnboardingOperationDocumentAcceptedDomainEvent(getId(), userId, documentId, document.getName()));
    checkOperationClosed();
  }


  public void rejectDocument(UUID documentId) {
    checkRule(new AllowDocumentsActionsOnlyForNotClosedOperationsRule(progressStatus));

    OnboardingOperationDocument document = getDocument(documentId);
    document.reject();
    checkOperationClosed();
  }


  private OnboardingOperationDocument getDocument(UUID documentId) {
    OnboardingOperationDocument document = documents.stream()
        .filter(d -> documentId.equals(d.getId()))
        .findFirst().orElseThrow(entityNotFound(OnboardingOperationDocument.class, documentId));
    return document;
  }


  private void checkOperationClosed() {
    boolean allDocumentsSigned = documents.stream().allMatch(d -> ACCEPTED.equals(d.getStatus()));
    boolean anyDocumentRejected = documents.stream().anyMatch(d -> REJECTED.equals(d.getStatus()));

    if (allDocumentsSigned) {
      addDomainEvent(new OnboardingOperationCompletedDomainEvent(getId(), userId));
      progressStatus = COMPLETED;
    }

    if (anyDocumentRejected) {
      addDomainEvent(new OnboardingOperationCancelledDomainEvent(getId(), userId, email));
      progressStatus = CANCELLED;
    }
  }


  public void cancel() {
    checkRule(new OnlyUncompletedOnboardingCanBeCancelledRule(progressStatus));

    addDomainEvent(new OnboardingOperationCancelledDomainEvent(getId(), userId, email));
  }
}
