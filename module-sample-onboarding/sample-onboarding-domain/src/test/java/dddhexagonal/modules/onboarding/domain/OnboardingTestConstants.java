package dddhexagonal.modules.onboarding.domain;

import java.util.UUID;

import static java.util.UUID.fromString;

public interface OnboardingTestConstants {

  UUID TEMPLATE_1_ID = fromString("676fc177-9630-4d39-ae5a-6aa1df42e4d5");
  UUID TEMPLATE_2_ID = fromString("b1070c11-1a86-45ca-b124-9eca374f14a9");
  String TEMPLATE_1_NAME = "template1";
  String TEMPLATE_2_NAME = "template2";
  String EMAIL = "email@example.com";
  UUID USER_ID = fromString("444f2edd-73ab-4241-b8e6-869ac302983c");
  UUID DOCUMENT_ID = fromString("674fef02-addc-45fd-a355-f2fa01d208fa");
  String DOCUMENT_NAME = "test-document-1";
}
