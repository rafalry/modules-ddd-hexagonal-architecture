/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.onboarding.domain.template;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.annotations.Cascade;

import dddhexagonal.foundations.domain.entity.BaseAggregateRoot;
import dddhexagonal.foundations.domain.entity.NamedEntity;
import dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationAggregateRoot;
import dddhexagonal.modules.onboarding.domain.template.events.OnboardingTemplateDocumentsUpdatedDomainEvent;
import dddhexagonal.modules.onboarding.domain.template.rules.AllowArchiveOnlyWhenInactiveRule;
import dddhexagonal.modules.onboarding.domain.template.rules.NameIsUniqueRule;

import static jakarta.persistence.FetchType.EAGER;
import static lombok.AccessLevel.MODULE;
import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@Getter
@Table(name = "templates", schema = "onboarding")
@NoArgsConstructor
public class OnboardingTemplateAggregateRoot extends BaseAggregateRoot implements NamedEntity {

  @OneToMany(mappedBy = "template", fetch = EAGER)
  @Cascade(ALL)
  private List<OnboardingTemplateDocument> documents = new ArrayList<>();

  @Column(name = "name", unique = true)
  private String name;

  @Setter(MODULE)
  @Column(name = "active")
  private boolean active;

  @Column(name = "archived")
  private boolean archived;


  public OnboardingTemplateAggregateRoot(String name, List<String> documents) {
    super(UUID.randomUUID());
    this.name = name;
    documents.forEach(this::addDocument);
  }

  void archive() {
    checkRule(new AllowArchiveOnlyWhenInactiveRule(active));

    archived = true;
  }

  public void modifyDocuments(List<String> documentNamesToAdd, List<String> documentNamesToRemove) {
    documentNamesToAdd.forEach(this::addDocument);
    documentNamesToRemove.forEach(this::removeDocument);

    List<String> documentNames = documents.stream().map(OnboardingTemplateDocument::getName).toList();
    addDomainEvent(new OnboardingTemplateDocumentsUpdatedDomainEvent(getId(), active, documentNames));
  }


  private void addDocument(String documentName) {
    checkRule(new NameIsUniqueRule(documents, documentName));

    documents.add(new OnboardingTemplateDocument(documentName, this));
  }


  private void removeDocument(String documentName) {
    Optional<OnboardingTemplateDocument> document = documents.stream().filter(d -> d.getName().equals(documentName)).findAny();
    if (document.isPresent()) {
      documents.remove(document.get());
    }
  }


  public OnboardingOperationAggregateRoot createOperation(UUID userId, String email) {
    List<String> documentNames = documents.stream().map(OnboardingTemplateDocument::getName).toList();
    return new OnboardingOperationAggregateRoot(userId, getId(), email, documentNames);
  }
}
