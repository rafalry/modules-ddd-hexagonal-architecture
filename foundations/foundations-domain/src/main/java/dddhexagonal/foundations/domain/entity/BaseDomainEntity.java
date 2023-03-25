/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.foundations.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import dddhexagonal.foundations.domain.rule.BusinessRule;
import dddhexagonal.foundations.domain.rule.BusinessRuleException;

import static org.springframework.data.util.ProxyUtils.getUserClass;

/**
 * Abstract base class for entities inspired by {@link org.springframework.data.jpa.domain.AbstractPersistable}.
 */
@MappedSuperclass
@NoArgsConstructor
@ToString
public abstract class BaseDomainEntity<ID extends Serializable> implements Persistable<ID> {


  @Id
  @Getter
  private @Nullable
  ID id;
  @Version
  @Column(name = "_entity_version")
  @Setter
  private Long entityVersion = null;


  protected BaseDomainEntity(ID id) {
    this.id = id;
  }


  @Transient // DATAJPA-622
  @Override
  public boolean isNew() {
    return null == entityVersion;
  }


  public @NotNull Optional<Long> getEntityVersion() {
    return Optional.ofNullable(entityVersion);
  }


  protected void checkRule(BusinessRule rule) {
    if (!rule.isRespected()) {
      throw new BusinessRuleException(rule, this);
    }
  }


  @Override
  public boolean equals(Object obj) {
    if (null == obj) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!getClass().equals(getUserClass(obj))) {
      return false;
    }
    BaseDomainEntity<?> that = (BaseDomainEntity<?>) obj;
    return null == this.getId() ? false : this.getId().equals(that.getId());
  }


  @Override
  public int hashCode() {
    int hashCode = 17;
    hashCode += null == getId() ? 0 : getId().hashCode() * 31;
    return hashCode;
  }
}
