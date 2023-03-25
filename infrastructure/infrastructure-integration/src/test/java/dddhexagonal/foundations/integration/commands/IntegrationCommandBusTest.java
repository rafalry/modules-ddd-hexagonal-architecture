package dddhexagonal.foundations.integration.commands;

import lombok.SneakyThrows;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dddhexagonal.foundations.integration.message.registry.MissingMessageHandlerRegistrationException;
import dddhexagonal.foundations.integration.query.IntegrationQuery;

import static dddhexagonal.foundations.integration.commands.IdentifiableIntegrationCommandResult.failure;
import static dddhexagonal.foundations.integration.commands.IdentifiableIntegrationCommandResult.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class IntegrationCommandBusTest {


  public static final UUID CORRECT_UUID = UUID.fromString("900547bb-cf15-4e45-bec0-4a283db7454b");


  @Test
  void should_call_handler() throws ExecutionException, InterruptedException {
    final AnnotationConfigApplicationContext context = createWith(Correct.class);

    IntegrationCommandBus commandBus = context.getBean(IntegrationCommandBus.class);

    IdentifiableIntegrationCommandResult result = commandBus.dispatchAsync(new TestIntegrationCommand()).get();
    assertThat(result.getId()).hasValue(CORRECT_UUID);
  }


  @Test
  void should_fail_when_no_handler() {
    final AnnotationConfigApplicationContext context = createWith(NoHandler.class);

    IntegrationCommandBus commandBus = context.getBean(IntegrationCommandBus.class);

    assertThatExceptionOfType(MissingMessageHandlerRegistrationException.class)
        .isThrownBy(() -> commandBus.dispatchAsync(new TestIntegrationCommand()));
  }


  @ParameterizedTest
  @ValueSource(classes = {IllegalMultipleHandlers.class, IllegalHandlerVisibility.class, IllegalArgumentsCount.class, IllegalArgumentType.class, IntegrationRelatedButIllegalArgumentType.class, IllegalReturnType.class, NoReturnType.class})
  void should_not_register_handler(Class<?> handler) {
    assertThatExceptionOfType(BeanCreationException.class)
        .isThrownBy(() -> createWith(handler));
  }


  @Test
  void should_not_register_duplicate_handlers() {
    assertThatExceptionOfType(BeanCreationException.class)
        .isThrownBy(() -> createWith(Correct.class, Duplicated.class));
  }


  @Test
  void should_propagate_handler_exception_as_failed_future() {
    final AnnotationConfigApplicationContext context = createWith(Throws.class);

    IntegrationCommandBus commandBus = context.getBean(IntegrationCommandBus.class);

    CompletableFuture<IdentifiableIntegrationCommandResult> future = commandBus.dispatchAsync(new TestIntegrationCommand());

    waitForFutureCompletion(future);
    assertThat(future.isCompletedExceptionally()).isTrue();
    assertThatExceptionOfType(ExecutionException.class).isThrownBy(future::get);
  }


  private AnnotationConfigApplicationContext createWith(final Class<?>... beans) {
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(IntegrationCommandBusConfiguration.class);
    context.register(beans);
    context.refresh();
    return context;
  }


  public static class Correct {

    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handler(TestIntegrationCommand command) {
      return success(CORRECT_UUID);
    }
  }


  public static class Duplicated {

    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handler2(TestIntegrationCommand command) {
      return failure();
    }
  }

  public static class NoHandler {

  }

  public static class IllegalMultipleHandlers {

    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handlerA(TestIntegrationCommand command) {
      return failure();
    }


    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handlerB(TestIntegrationCommand command) {
      return failure();
    }
  }

  public static class IllegalHandlerVisibility {

    @IntegrationCommandHandler
    private IdentifiableIntegrationCommandResult handler(TestIntegrationCommand command) {
      return failure();
    }
  }

  public static class IllegalArgumentsCount {

    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handler(TestIntegrationCommand command, Object invalidArgument) {
      return failure();
    }
  }

  public static class IllegalArgumentType {

    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handler(String message) {
      return failure();
    }
  }

  public static class IntegrationRelatedButIllegalArgumentType {

    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handler(IntegrationQuery message) {
      return failure();
    }
  }

  public static class IllegalReturnType {

    @IntegrationCommandHandler
    public String handler(TestIntegrationCommand command) {
      return "should never reach here";
    }
  }

  public static class NoReturnType {

    @IntegrationCommandHandler
    public void handler(TestIntegrationCommand command) {
    }
  }

  public static class Throws {

    @IntegrationCommandHandler
    public IdentifiableIntegrationCommandResult handler(TestIntegrationCommand command) {
      throw new RuntimeException("test exception");
    }
  }

  private static class TestIntegrationCommand extends IntegrationCommand<IdentifiableIntegrationCommandResult> {
  }


  @SneakyThrows
  private void waitForFutureCompletion(CompletableFuture<?> future) {
    try {
      future.join();
    } catch (Throwable throwable) {
    }
  }
}
