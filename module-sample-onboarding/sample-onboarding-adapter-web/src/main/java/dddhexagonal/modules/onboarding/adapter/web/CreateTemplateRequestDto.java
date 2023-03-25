package dddhexagonal.modules.onboarding.adapter.web;

import java.util.List;

public record CreateTemplateRequestDto(String name, List  <String> documentNames) {
}
