package dddhexagonal.modules.user.application.projections;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "projection_onboarding_documents", schema = "user_management")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingDocumentProjectionEntity {

  @Column
  @Id
  private UUID userId;
  @Column
  private UUID documentId;
  @Column
  private String documentName;

}
