package dddhexagonal.modules.user.adapter.web;

import dddhexagonal.modules.user.domain.user.UserAggregateRoot;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dddhexagonal.modules.user.application.usecases.UserUseCases;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users/v1/users")
@RequiredArgsConstructor
public class UserApiController {

  private final UserUseCases userUseCases;

  @PostMapping
  @ResponseBody
  @ResponseStatus(CREATED)
  public CreateUserResponseDto createUser(@RequestBody CreateUserRequestDto body) {
    UUID userId = userUseCases.createUser(body.email(), body.triggerOnboarding());
    return new CreateUserResponseDto(userId);
  }

  @GetMapping("{id}")
  public UserAggregateRoot getUser(@PathVariable UUID id) {
    return userUseCases.getUser(id);
  }

  @PostMapping("{id}/activate")
  public void activateUser(@PathVariable UUID id) {
    userUseCases.activateUserDirectly(id);
  }

  @DeleteMapping("{id}")
  public void deleteUser(@PathVariable UUID id) {
    userUseCases.deleteUser(id);
  }
}
