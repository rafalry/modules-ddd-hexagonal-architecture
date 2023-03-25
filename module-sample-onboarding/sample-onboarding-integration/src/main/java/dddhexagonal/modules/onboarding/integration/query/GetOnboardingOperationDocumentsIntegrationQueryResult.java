package dddhexagonal.modules.onboarding.integration.query;

import lombok.Value;

import java.util.Map;
import java.util.UUID;

import dddhexagonal.foundations.integration.query.IntegrationQueryResult;

@Value
public class GetOnboardingOperationDocumentsIntegrationQueryResult extends IntegrationQueryResult {

  Map<UUID, OnboardingOperationDocumentSigningStatus> documentStatuses;
}
