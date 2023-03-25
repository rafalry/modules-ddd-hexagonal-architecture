package dddhexagonal.foundations.integration.message.registry;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dddhexagonal.foundations.integration.message.IntegrationMessage;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Addition to {@link IntegrationMessageHandlerPostProcessorTest} that tests logic related to supporting single handler.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
class SingleHandlerIntegrationMessageHandlerPostProcessorTest {

  private static List<String> sideEffects;

  @BeforeEach
  public void setUp() {
    sideEffects = new ArrayList<>();
  }

  @Test
  void should_register_handlers() {
    final AnnotationConfigApplicationContext context = createWith(CorrectWithSingleHandler.class);

    checkSideEffects(context, TestMessage.class, new TestMessage(), "1");
  }

  @Test
  void should_register_handlers_for_different_messages() {
    final AnnotationConfigApplicationContext context = createWith(CorrectWithDifferentMessages.class);

    checkSideEffects(context, TestMessage1.class, new TestMessage1(), "2a");
    sideEffects.clear();
    checkSideEffects(context, TestMessage2.class, new TestMessage2(), "2b");
  }


  private <T> void checkSideEffects(
      AnnotationConfigApplicationContext context,
      Class<T> messageType, T messageInstance,
      String... expectedSideEffects) {
    List<Function> functions = context.getBean(GenericMessageHandlerRegistry.class).get(messageType);
    assertThat(functions).hasSize(expectedSideEffects.length);
    functions.forEach(h -> h.apply(messageInstance));
    assertThat(sideEffects).containsOnly(expectedSideEffects);
  }

  @ParameterizedTest
  @ValueSource(classes = {IllegalDuplicateHandlers.class})
  void should_not_register_handler(Class<?> handler) {
    assertThatExceptionOfType(BeanCreationException.class)
        .isThrownBy(() -> createWith(handler));
  }

  private AnnotationConfigApplicationContext createWith(final Class<?>... beans) {
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(Config.class);
    context.register(beans);
    context.refresh();
    return context;
  }


  @Configuration
  public static class Config {

    @Bean
    public GenericMessageHandlerRegistry registry() {
      return new GenericMessageHandlerRegistry(false);
    }

    @Bean
    public IntegrationMessageHandlerPostProcessor postProcessor(final GenericMessageHandlerRegistry registry) {
      return new IntegrationMessageHandlerPostProcessor(TestMessageHandler.class, registry, Void.TYPE, TestMessage.class);
    }
  }

  @Target(METHOD)
  @Retention(RUNTIME)
  @Documented
  public @interface TestMessageHandler {
  }

  public static class CorrectWithSingleHandler {

    @TestMessageHandler
    public void handler(TestMessage message) {
      sideEffects.add("1");
    }
  }

  public static class CorrectWithDifferentMessages {

    @TestMessageHandler
    public void handlerA(TestMessage1 message) {
      sideEffects.add("2a");
    }

    @TestMessageHandler
    public void handlerB(TestMessage2 message) {
      sideEffects.add("2b");
    }
  }

  public static class IllegalDuplicateHandlers {

    @TestMessageHandler
    public void handlerA(TestMessage message) {
    }

    @TestMessageHandler
    public void handlerB(TestMessage message) {
    }
  }

  private static class TestMessage extends IntegrationMessage {
  }

  private static class TestMessage1 extends TestMessage {
  }

  private static class TestMessage2 extends TestMessage {
  }

  private static class TestMessageResult {
  }

}
