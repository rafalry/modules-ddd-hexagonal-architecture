package dddhexagonal.foundations.integration.events;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import dddhexagonal.foundations.integration.commands.IntegrationCommand;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class IntegrationEventBusTest {

  private static List<String> sideEffects;

  @BeforeEach
  public void setUp() {
    sideEffects = new ArrayList<>();
  }

  @Test
  void should_call_single_handler() {
    final AnnotationConfigApplicationContext context = createWith(Correct1.class);

    IntegrationEventBus eventBus = context.getBean(IntegrationEventBus.class);

    eventBus.publishAsync(new TestIntegrationEvent());

    waitForAsyncHandlersInvocation();
    assertThat(sideEffects).containsOnly("1");
  }

  @Test
  void should_call_multiple_handlers() {
    final AnnotationConfigApplicationContext context = createWith(Correct1.class, Correct2.class, MultipleHandlers.class, NoHandler.class);

    IntegrationEventBus eventBus = context.getBean(IntegrationEventBus.class);

    eventBus.publishAsync(new TestIntegrationEvent());

    waitForAsyncHandlersInvocation();
    assertThat(sideEffects).containsOnly("1", "2", "3a", "3b");
  }


  @Test
  void should_not_fail_when_no_handler() {
    final AnnotationConfigApplicationContext context = createWith(NoHandler.class);

    IntegrationEventBus eventBus = context.getBean(IntegrationEventBus.class);

    eventBus.publishAsync(new TestIntegrationEvent());

    waitForAsyncHandlersInvocation();
    assertThat(sideEffects).isEmpty();
  }


  @ParameterizedTest
  @ValueSource(classes = {IllegalHandlerVisibility.class, IllegalArgumentsCount.class, IllegalArgumentType.class, IntegrationRelatedButIllegalArgumentType.class, IllegalReturnType.class})
  void should_not_register_handler(Class<?> handler) {
    assertThatExceptionOfType(BeanCreationException.class)
        .isThrownBy(() -> createWith(handler));
  }



  private AnnotationConfigApplicationContext createWith(final Class<?>... beans) {
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(IntegrationEventBusConfiguration.class);
    context.register(beans);
    context.refresh();
    return context;
  }


  public static class Correct1 {


    @IntegrationEventHandler
    public void handler1(TestIntegrationEvent event) {
      sideEffects.add("1");
    }

  }
  public static class Correct2 {


    @IntegrationEventHandler
    public void handler2(TestIntegrationEvent event) {
      sideEffects.add("2");
    }

  }
  public static class NoHandler {


  }
  public static class MultipleHandlers {

    @IntegrationEventHandler
    public void handlerA(TestIntegrationEvent event) {
      sideEffects.add("3a");
    }


    @IntegrationEventHandler
    public void handlerB(TestIntegrationEvent event) {
      sideEffects.add("3b");
    }


  }
  public static class IllegalHandlerVisibility {

    @IntegrationEventHandler
    private void handler(TestIntegrationEvent event) {
    }

  }
  public static class IllegalArgumentsCount {

    @IntegrationEventHandler
    public void handler(TestIntegrationEvent event, Object invalidArgument) {
    }

  }
  public static class IllegalArgumentType {

    @IntegrationEventHandler
    public void handler(String message) {
    }

  }
  public static class IntegrationRelatedButIllegalArgumentType {

    @IntegrationEventHandler
    public void handler(IntegrationCommand message) {
    }

  }
  public static class IllegalReturnType {

    @IntegrationEventHandler
    public String handler(TestIntegrationEvent event) {
      return "should never reach here";
    }

  }
  private static class TestIntegrationEvent extends IntegrationEvent { }


  @SneakyThrows
  private void waitForAsyncHandlersInvocation() {
    sleep(10);
  }
}
