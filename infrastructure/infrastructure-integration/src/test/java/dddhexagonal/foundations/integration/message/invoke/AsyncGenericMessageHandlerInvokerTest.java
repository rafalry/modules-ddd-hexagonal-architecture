package dddhexagonal.foundations.integration.message.invoke;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dddhexagonal.foundations.integration.message.IntegrationMessage;
import dddhexagonal.foundations.integration.message.registry.DuplicateMessageHandlerRegistrationException;
import dddhexagonal.foundations.integration.message.registry.GenericMessageHandlerRegistry;
import dddhexagonal.foundations.integration.message.registry.MissingMessageHandlerRegistrationException;

import static java.util.Collections.synchronizedList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AsyncGenericMessageHandlerInvokerTest {

  @Mock(strictness = LENIENT)
  GenericMessageHandlerRegistry registry;
  AsyncGenericMessageHandlerInvoker invokerWithSingularHandlers;
  AsyncGenericMessageHandlerInvoker invokerWithMultipleHandlers;

  Function<Object, Object> handler1 = createHandlerFunction("handler1");
  Function<Object, Object> handler2a = createHandlerFunction("handler2a");
  Function<Object, Object> handler2b = createHandlerFunction("handler2b");
  Function<Object, Object> handler3 = createHandlerFunction("handler3", true);
  Function<Object, Object> handler4a = createHandlerFunction("handler4a");
  Function<Object, Object> handler4b = createHandlerFunction("handler4b", true);
  Function<Object, Object> handler4c = createHandlerFunction("handler4c");

  private List<String> triggeredHandlers = synchronizedList(new ArrayList<>());
  private Function<Object, Object> createHandlerFunction(String name) {
    return createHandlerFunction(name, false);
  }
  private Function<Object, Object> createHandlerFunction(String name, boolean throwException) {
    return (message) -> {
      sleep(25);
      if (throwException) {
        throw new IllegalStateException("Exception from handler " + name);
      }
      triggeredHandlers.add(name);
      sleep(25);
      return name;
    };
  }

  private static class TestMessageSingleHandler extends IntegrationMessage { }
  private static class TestMessageMultipleHandlers extends IntegrationMessage { }
  private static class TestMessageNotRegistered extends IntegrationMessage { }
  private static class TestMessageExceptionSingleHandler extends IntegrationMessage { }
  private static class TestMessageExceptionMultipleHandlers extends IntegrationMessage { }


  @BeforeEach
  void setUp() {
    invokerWithSingularHandlers = new AsyncGenericMessageHandlerInvoker(registry, true);
    invokerWithMultipleHandlers = new AsyncGenericMessageHandlerInvoker(registry, false);

    when(registry.get(TestMessageSingleHandler.class)).thenReturn(of(handler1));
    when(registry.get(TestMessageMultipleHandlers.class)).thenReturn(of(handler2a, handler2b));
    when(registry.get(TestMessageExceptionSingleHandler.class)).thenReturn(of(handler3));
    when(registry.get(TestMessageExceptionMultipleHandlers.class)).thenReturn(of(handler4a, handler4b, handler4c));
  }


  @SneakyThrows
  @Test
  void should_trigger_singular_handler() {
    CompletableFuture future = invokerWithSingularHandlers.invoke(new TestMessageSingleHandler());
    future.get();

    assertThat(triggeredHandlers).containsOnly("handler1");
  }


  @SneakyThrows
  @Test
  void should_trigger_singular_handler_asynchronously() {
    invokerWithSingularHandlers.invoke(new TestMessageSingleHandler());

    assertThat(triggeredHandlers).isEmpty();
  }


  @Test
  void should_fail_for_multiple_handlers() {
    assertThatExceptionOfType(DuplicateMessageHandlerRegistrationException.class)
        .isThrownBy(() -> invokerWithSingularHandlers.invoke(new TestMessageMultipleHandlers()));
  }


  @Test
  void should_fail_for_missing_handlers() {
    assertThatExceptionOfType(MissingMessageHandlerRegistrationException.class)
        .isThrownBy(() -> invokerWithSingularHandlers.invoke(new TestMessageNotRegistered()));
  }

  @Test
  @SneakyThrows
  void should_fail_for_handlers_with_exception() {
    CompletableFuture future = invokerWithSingularHandlers.invoke(new TestMessageExceptionSingleHandler());

    assertThatExceptionOfType(ExecutionException.class)
        .isThrownBy(future::get);
  }


  @SneakyThrows
  @Test
  void should_trigger_multiple_handlers() {
    CompletableFuture future = invokerWithMultipleHandlers.invoke(new TestMessageMultipleHandlers());
    future.get();

    assertThat(triggeredHandlers).containsOnly("handler2a", "handler2b");
  }


  @Test
  void should_trigger_multiple_handlers_asynchronously() {
    invokerWithMultipleHandlers.invoke(new TestMessageMultipleHandlers());

    assertThat(triggeredHandlers).isEmpty();
  }


  @SneakyThrows
  @Test
  void should_trigger_no_handlers() {
    CompletableFuture future = invokerWithMultipleHandlers.invoke(new TestMessageNotRegistered());
    future.get();

    assertThat(triggeredHandlers).isEmpty();
  }


  @SneakyThrows
  @Test
  void should_trigger_only_handler_without_exception() {
    CompletableFuture future = invokerWithMultipleHandlers.invoke(new TestMessageExceptionMultipleHandlers());
    try {
      future.get();
    } catch (Throwable throwable) {
      log.info("Expected exception");
    }

    assertThat(triggeredHandlers).containsOnly("handler4a", "handler4c");
  }

  @SneakyThrows
  private static void sleep(long millis) {
      Thread.sleep(millis);
    }

}
