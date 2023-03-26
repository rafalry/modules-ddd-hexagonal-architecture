package dddhexagonal.modules.user.adapter.web;

public record CreateUserRequestDto(String email, boolean triggerOnboarding) {

}
