package dddhexagonal.modules.onboarding.integration.events;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

import dddhexagonal.foundations.integration.events.IntegrationEvent;

@Value
@EqualsAndHashCode(callSuper = false)
public class OnboardingOperationCompletedIntegrationEvent extends IntegrationEvent {

  UUID userId;
}
