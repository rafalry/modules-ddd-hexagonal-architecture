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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import dddhexagonal.foundations.integration.message.IntegrationMessage;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@SuppressWarnings({"unchecked", "rawtypes"})
class IntegrationMessageHandlerPostProcessorTest {

  private static List<String> sideEffects;

  @BeforeEach
  public void setUp() {
    sideEffects = new ArrayList<>();
  }

  @Test
  void should_register_handlers() {
    final AnnotationConfigApplicationContext context = createWith(CorrectWithSingleHandler.class, CorrectWithSameMessages.class);

    checkSideEffects(context, TestMessage.class, new TestMessage(), "1", "2a", "2b");
  }


  @Test
  void should_register_handlers_for_different_messages() {
    final AnnotationConfigApplicationContext context = createWith(CorrectWithDifferentMessages.class);

    checkSideEffects(context, TestMessage1.class, new TestMessage1(), "3a");
    sideEffects.clear();
    checkSideEffects(context, TestMessage2.class, new TestMessage2(), "3b");
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
  @ValueSource(classes = {CorrectWithTransactionAndSameMessages.class, CorrectWithTransactionAndDifferentMessages.class})
  void should_register_transactional_handlers(Class<?> handler) {
    final AnnotationConfigApplicationContext context = createWith(handler);

    List<Function> functions = context.getBean(GenericMessageHandlerRegistry.class).get(TestMessage.class);
    assertThat(functions).hasSize(2);

    TransactionManager transactionManager = context.getBean(TransactionManager.class);
    verifyNoInteractions(transactionManager);

    functions.forEach(h -> h.apply(new TestMessage()));
    verify((PlatformTransactionManager) transactionManager, times(2)).getTransaction(any());
    assertThat(sideEffects).containsOnly("tx_a", "tx_b");
  }

  @ParameterizedTest
  @ValueSource(classes = {NoMethodVisibility.class, IllegalArgumentsCount.class, IllegalArgumentType.class, IllegalReturnType.class})
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
  @EnableTransactionManagement(proxyTargetClass = true)
  public static class Config {

    @Bean
    public GenericMessageHandlerRegistry registry() {
      return new GenericMessageHandlerRegistry(true);
    }

    @Bean
    public IntegrationMessageHandlerPostProcessor postProcessor(final GenericMessageHandlerRegistry registry) {
      return new IntegrationMessageHandlerPostProcessor(TestMessageHandler.class, registry, TestMessageResult.class, TestMessage.class);
    }

    @Bean
    public TransactionManager transactionManager() {
      return mock(PlatformTransactionManager.class);
    }
  }

  @Target(METHOD)
  @Retention(RUNTIME)
  @Documented
  public @interface TestMessageHandler {
  }

  public static class CorrectWithSingleHandler {

    @TestMessageHandler
    public TestMessageResult handler(TestMessage message) {
      sideEffects.add("1");
      return null;
    }
  }

  public static class CorrectWithSameMessages {

    @TestMessageHandler
    public TestMessageResult handlerA(TestMessage message) {
      sideEffects.add("2a");
      return null;
    }

    @TestMessageHandler
    public TestMessageResult handlerB(TestMessage message) {
      sideEffects.add("2b");
      return null;
    }
  }

  public static class CorrectWithDifferentMessages {

    @TestMessageHandler
    public TestMessageResult handlerA(TestMessage1 message) {
      sideEffects.add("3a");
      return null;
    }

    @TestMessageHandler
    public TestMessageResult handlerB(TestMessage2 message) {
      sideEffects.add("3b");
      return null;
    }
  }

  @Transactional(propagation = REQUIRES_NEW)
  public static class CorrectWithTransactionAndSameMessages {

    @TestMessageHandler
    public TestMessageResult handlerA(TestMessage message) {
      sideEffects.add("tx_a");
      return null;
    }

    @TestMessageHandler
    public TestMessageResult handlerB(TestMessage message) {
      sideEffects.add("tx_b");
      return null;
    }
  }

  @Transactional(propagation = REQUIRES_NEW)
  public static class CorrectWithTransactionAndDifferentMessages {

    @TestMessageHandler
    public TestMessageResult handlerA(TestMessage message) {
      sideEffects.add("tx_a");
      return null;
    }

    @TestMessageHandler
    public TestMessageResult handlerB(TestMessage message) {
      sideEffects.add("tx_b");
      return null;
    }
  }

  public static class NoMethodVisibility {

    @TestMessageHandler
    private TestMessageResult handler(TestMessage message) {
      return null;
    }
  }

  public static class IllegalArgumentsCount {

    @TestMessageHandler
    public TestMessageResult handler(TestMessage message, Object invalidArgument) {
      return null;
    }
  }

  public static class IllegalArgumentType {

    @TestMessageHandler
    public TestMessageResult handler(String message) {
      return null;
    }
  }

  public static class IllegalReturnType {

    @TestMessageHandler
    public void handler(String message) {
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
