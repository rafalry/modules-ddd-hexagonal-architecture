package dddhexagonal.foundations.integration.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dddhexagonal.foundations.integration.message.registry.MissingMessageHandlerRegistrationException;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class IntegrationQueryBusTest {

  @Test
  void should_call_handler() throws ExecutionException, InterruptedException {
    final AnnotationConfigApplicationContext context = createWith(Correct.class);

    IntegrationQueryBus queryBus = context.getBean(IntegrationQueryBus.class);

    TestIntegrationQueryResult result = queryBus.queryAsync(new TestIntegrationQuery()).get();
    assertThat(result.contents).isEqualTo("1");
  }

  @Test
  void should_fail_when_no_handler() {
    final AnnotationConfigApplicationContext context = createWith(NoHandler.class);

    IntegrationQueryBus queryBus = context.getBean(IntegrationQueryBus.class);

    assertThatExceptionOfType(MissingMessageHandlerRegistrationException.class)
        .isThrownBy(() -> queryBus.queryAsync(new TestIntegrationQuery()));
  }


  @ParameterizedTest
  @ValueSource(classes = {IllegalMultipleHandlers.class, IllegalHandlerVisibility.class, IllegalArgumentsCount.class, IllegalArgumentType.class, IllegalReturnType.class, NoReturnType.class})
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

    IntegrationQueryBus queryBus = context.getBean(IntegrationQueryBus.class);

    CompletableFuture<TestIntegrationQueryResult> future = queryBus.queryAsync(new TestIntegrationQuery());

    waitForFutureCompletion(future);
    assertThat(future.isCompletedExceptionally()).isTrue();
    assertThatExceptionOfType(ExecutionException.class).isThrownBy(future::get);
  }


  private AnnotationConfigApplicationContext createWith(final Class<?>... beans) {
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(IntegrationQueryBusConfiguration.class);
    context.register(beans);
    context.refresh();
    return context;
  }


  public static class Correct {

    @IntegrationQueryHandler
    public TestIntegrationQueryResult handler(TestIntegrationQuery query) {
      return new TestIntegrationQueryResult("1");
    }
  }


  public static class Duplicated {

    @IntegrationQueryHandler
    public TestIntegrationQueryResult handler2(TestIntegrationQuery query) {
      return new TestIntegrationQueryResult("2");
    }
  }

  public static class NoHandler {

  }

  public static class IllegalMultipleHandlers {

    @IntegrationQueryHandler
    public TestIntegrationQueryResult handlerA(TestIntegrationQuery query) {
      return new TestIntegrationQueryResult("2a");
    }


    @IntegrationQueryHandler
    public TestIntegrationQueryResult handlerB(TestIntegrationQuery query) {
      return new TestIntegrationQueryResult("2b");
    }
  }

  public static class IllegalHandlerVisibility {

    @IntegrationQueryHandler
    private TestIntegrationQueryResult handler(TestIntegrationQuery query) {
      return new TestIntegrationQueryResult("should never reach here");
    }
  }

  public static class IllegalArgumentsCount {

    @IntegrationQueryHandler
    public TestIntegrationQueryResult handler(TestIntegrationQuery query, Object invalidArgument) {
      return new TestIntegrationQueryResult("should never reach here");
    }
  }

  public static class IllegalArgumentType {

    @IntegrationQueryHandler
    public TestIntegrationQueryResult handler(String message) {
      return new TestIntegrationQueryResult("should never reach here");
    }
  }

  public static class IllegalReturnType {

    @IntegrationQueryHandler
    public String handler(TestIntegrationQuery query) {
      return "should never reach here";
    }
  }

  public static class NoReturnType {

    @IntegrationQueryHandler
    public void handler(TestIntegrationQuery query) {
    }
  }

  public static class Throws {

    @IntegrationQueryHandler
    public TestIntegrationQueryResult handler(TestIntegrationQuery message) {
      throw new RuntimeException("test exception");
    }
  }

  private static class TestIntegrationQuery extends IntegrationQuery<TestIntegrationQueryResult> { }
  
  @RequiredArgsConstructor
  private static class TestIntegrationQueryResult extends IntegrationQueryResult { 
    final String contents;
  }


  @SneakyThrows
  private void waitForFutureCompletion(CompletableFuture<?> future) {
    try {
      future.join();
    } catch (Throwable throwable) {
    }
  }
}
