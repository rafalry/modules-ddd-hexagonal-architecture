package dddhexagonal.foundations.integration.message.invoke;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import dddhexagonal.foundations.integration.message.registry.DuplicateMessageHandlerRegistrationException;
import dddhexagonal.foundations.integration.message.registry.GenericMessageHandlerRegistry;
import dddhexagonal.foundations.integration.message.registry.MissingMessageHandlerRegistrationException;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RequiredArgsConstructor
@Slf4j
public class AsyncGenericMessageHandlerInvoker {

  private final GenericMessageHandlerRegistry registry;
  private final boolean requireSingularHandlersWithReturnValue;
  private final ExecutorService executorService = Executors.newCachedThreadPool();

  public CompletableFuture invoke(Object message) {
    List<Function> handlers = registry.get(message.getClass());
    if (requireSingularHandlersWithReturnValue) {
      if (handlers.isEmpty()) {
        throw new MissingMessageHandlerRegistrationException(message.getClass());
      }
      if (handlers.size() > 1) {
        throw new DuplicateMessageHandlerRegistrationException(message.getClass());
      }
      Function function = handlers.get(0);
      return invokeHandler(message, function);
    } else {
      List<CompletableFuture<Object>> futures = handlers.stream()
          .map(h -> invokeHandler(message, h))
          .toList();
      return allOf(futures.toArray(new CompletableFuture[futures.size()]));
    }
  }


  private CompletableFuture<Object> invokeHandler(Object message, Function function) {
    try {
      return supplyAsync(() -> {
        try {
          return function.apply(message);
        } catch (Throwable throwable) {
          log.error("Exception occurred while invoking handler for {}: {}", message.getClass().getSimpleName(), function, throwable);
          throwable.printStackTrace();
          throw new HandlerInvocationException(message.getClass(), function);
        }
      }, executorService);
    } catch (Throwable throwable) {
      return failedFuture(throwable);
    }
  }
}
