package dddhexagonal.modules.onboarding.domain.operation;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import dddhexagonal.foundations.domain.entity.BaseDomainEntity;
import dddhexagonal.modules.onboarding.domain.operation.rules.OnlyPendingDocumentCanBeSignedRule;
import dddhexagonal.modules.onboarding.domain.operation.rules.OnlySignedOrRejectedDocumentCanBeAcceptedRule;

import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.ACCEPTED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.PENDING;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.REJECTED;
import static dddhexagonal.modules.onboarding.domain.operation.OnboardingOperationDocumentStatus.SIGNED;
import static java.util.UUID.randomUUID;

@Entity
@Table(name = "operationdocuments", schema = "onboarding")
@Getter
@ToString
@NoArgsConstructor
public class OnboardingOperationDocument extends BaseDomainEntity<UUID> {

  public OnboardingOperationDocument(String name, OnboardingOperationAggregateRoot operation) {
    super(randomUUID());
    this.name = name;
    this.status = PENDING;
    this.operation = operation;
  }

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "operation_id", nullable = false)
  private OnboardingOperationAggregateRoot operation;

  private String name;
  private OnboardingOperationDocumentStatus status;

  public void sign() {
    checkRule(new OnlyPendingDocumentCanBeSignedRule(status));

    status = SIGNED;
  }


  public void accept() {
    checkRule(new OnlySignedOrRejectedDocumentCanBeAcceptedRule(status));

    status = ACCEPTED;
  }


  public void reject() {
    status = REJECTED;
  }
}
