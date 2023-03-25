package dddhexagonal.modules.onboarding.adapter.web;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dddhexagonal.modules.onboarding.application.usecases.OperationDocumentUseCases;

@RestController
@RequestMapping("/api/onboarding/v1/operations/{operationId}/documents/{documentId}/")
@RequiredArgsConstructor
public class OperationDocumentsApiController {

  private final OperationDocumentUseCases onboardingOperationDocumentUseCases;

  @PostMapping("sign")
  public void signDocument(@PathVariable("operationId") UUID operationId, @PathVariable("documentId") UUID documentId){
    onboardingOperationDocumentUseCases.signDocument(operationId, documentId);
  }

  @PostMapping("accept")
  public void acceptDocument(@PathVariable("operationId") UUID operationId, @PathVariable("documentId") UUID documentId){
    onboardingOperationDocumentUseCases.acceptDocument(operationId, documentId);
  }


  @PostMapping("reject")
  public void rejectDocument(@PathVariable("operationId") UUID operationId, @PathVariable("documentId") UUID documentId){
    onboardingOperationDocumentUseCases.rejectDocument(operationId, documentId);
  }

}
