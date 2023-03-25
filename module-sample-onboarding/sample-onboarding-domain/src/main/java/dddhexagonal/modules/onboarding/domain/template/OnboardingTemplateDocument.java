/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.onboarding.domain.template;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import dddhexagonal.foundations.domain.entity.BaseDomainEntity;
import dddhexagonal.foundations.domain.entity.NamedEntity;

import static java.util.UUID.randomUUID;

@Entity
@Table(name = "template_documents", schema = "onboarding")
@NoArgsConstructor
class OnboardingTemplateDocument extends BaseDomainEntity<UUID> implements NamedEntity {

  public OnboardingTemplateDocument(String name, OnboardingTemplateAggregateRoot template) {
    super(randomUUID());
    this.name = name;
    this.template = template;
  }

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "template_id", nullable = false, referencedColumnName = "id")
  private OnboardingTemplateAggregateRoot template;

  @Getter
  @Setter
  private String name;
}
