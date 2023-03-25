package dddhexagonal.modules.onboarding.adapter.web;

import dddhexagonal.modules.onboarding.domain.template.OnboardingTemplateAggregateRoot;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dddhexagonal.modules.onboarding.application.usecases.TemplateUseCases;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/onboarding/v1/templates")
@RequiredArgsConstructor
public class TemplateApiController {

  private final TemplateUseCases templateUseCases;

  @PostMapping
  @ResponseStatus(CREATED)
  public CreateTemplateResponseDto create(@RequestBody CreateTemplateRequestDto body){
    UUID templateId = templateUseCases.create(body.name(), body.documentNames());
    return new CreateTemplateResponseDto(templateId);
  }


  @GetMapping
  public List<OnboardingTemplateAggregateRoot> search(){
    return templateUseCases.search();
  }



  @GetMapping("{id}")
  public OnboardingTemplateAggregateRoot create(@PathVariable UUID id){
    return templateUseCases.get(id);
  }


  @PostMapping("{id}/activate")
  public void activate(@PathVariable UUID id){
    templateUseCases.activate(id);
  }


  @PostMapping("{id}/archive")
  public void archive(@PathVariable UUID id){
    templateUseCases.archive(id);
  }


}
