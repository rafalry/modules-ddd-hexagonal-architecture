package dddhexagonal.modules.onboarding.adapter.email;

import dddhexagonal.infrastructure.emailgateway.EmailGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import dddhexagonal.modules.onboarding.domain.operation.ports.EmailSenderPort;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingEmailSenderAdapter implements EmailSenderPort {

  public static final String SIGN_SUBJECT = "Sing documents to complete account onboarding";
  public static final String SIGN_TEXT = "<a href='http://127.0.0.1:8082/api/onboarding/v1/operations/%s/documents/%s/sign'>http://127.0.0.1:8082/api/onboarding/v1/operations/%s/documents/%s/sign</a><br/>";
  public static final String ACCEPTED_SUBJECT = "Your documents are accepted";
  public static final String CANCELLED_SUBJECT = "Your onboarding is cancelled";

  private final EmailGateway emailGateway;


  @Override
  public void sendDocumentsEmail(String email, UUID operationId, List<UUID> documentIds) {
    emailGateway.send(email, SIGN_SUBJECT, documentIds.stream().map((docId) ->
        format(SIGN_TEXT, operationId, docId, operationId, docId)).collect(Collectors.joining(""))
    );
    log.info("DOCUMENTS FOR EMAIL {} AND OPERATION {}: {}%n", email, operationId,
        documentIds.stream().map((docId) ->
            format("http://127.0.0.1:8082/api/onboarding/v1/operations/%s/documents/%s/sign%n", operationId, docId) +
            format("http://127.0.0.1:8082/api/onboarding/v1/operations/%s/documents/%s/accept%n", operationId, docId) +
            format("http://127.0.0.1:8082/api/onboarding/v1/operations/%s/documents/%s/reject%n", operationId, docId)
        ).collect(Collectors.joining("")));
  }


  @Override
  public void sendDocumentRejectedEmail(String email, UUID documentId) {
    emailGateway.send(email, ACCEPTED_SUBJECT, "");

  }


  @Override
  public void sendOnboardingCancelledEmail(String email) {
    emailGateway.send(email, CANCELLED_SUBJECT, "");

  }
}
