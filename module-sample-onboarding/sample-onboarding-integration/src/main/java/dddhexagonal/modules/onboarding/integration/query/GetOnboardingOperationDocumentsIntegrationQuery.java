package dddhexagonal.modules.onboarding.integration.query;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.integration.query.IntegrationQuery;

@Value
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class GetOnboardingOperationDocumentsIntegrationQuery
    extends IntegrationQuery<GetOnboardingOperationDocumentsIntegrationQueryResult> {

  UUID userId;
}
