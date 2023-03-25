/*
 * Copyright (c) 2000-2022, Efinity Sp. z o.o. All rights reserved.
 */

package dddhexagonal.modules.onboarding.domain.operation.ports;

import java.util.List;
import java.util.UUID;

public interface EmailSenderPort {

  void sendDocumentsEmail(String email, UUID operationId, List<UUID> documentIds);
  void sendDocumentRejectedEmail(String email, UUID documentId);
  void sendOnboardingCancelledEmail(String email);
}
