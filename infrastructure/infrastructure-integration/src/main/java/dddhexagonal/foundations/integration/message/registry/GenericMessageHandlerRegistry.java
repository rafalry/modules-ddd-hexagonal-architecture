package dddhexagonal.foundations.integration.message.registry;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.List.of;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class GenericMessageHandlerRegistry {

  private final boolean allowMultipleOrMissingHandlers;
  private final Map<String, List<MessageHandlerWrapper>> handlers = new HashMap<>();


  public void register(Class<?> messageType, MessageHandlerWrapper newHandler) {
    String typeName = messageType.getName();
    if (!allowMultipleOrMissingHandlers && handlers.containsKey(typeName) &&
        areUnrelated(newHandler, handlers.get(typeName).get(0))) {
      throw new DuplicateMessageHandlerRegistrationException(messageType);
    }
    if (!handlers.containsKey(typeName)) {
      handlers.put(typeName, new ArrayList<>());
    }
    if (handlers.get(typeName).isEmpty()) {
      // add first handler
      handlers.get(typeName).add(newHandler);
    } else if (overridesAny(newHandler, handlers.get(typeName))) {
      // replace existing overridden handler with the overriding one
      handlers.get(typeName).remove(handlers.get(typeName).get(0));
      handlers.get(typeName).add(newHandler);
    } else if (allowMultipleOrMissingHandlers && !anyOverrides(handlers.get(typeName), newHandler)) {
      // add handler unless it is overriden by already existing newHandler
      handlers.get(typeName).add(newHandler);
    }
  }


  private boolean overridesAny(MessageHandlerWrapper subHandler, List<MessageHandlerWrapper> superHandlers) {
    return superHandlers.stream().anyMatch(superHandler -> overrides(subHandler, superHandler));
  }


  private boolean anyOverrides(List<MessageHandlerWrapper> subHandlers, MessageHandlerWrapper superHandler) {
    return subHandlers.stream().anyMatch(subHandler -> overrides(subHandler, superHandler));
  }


  private boolean areUnrelated(MessageHandlerWrapper newHandler, MessageHandlerWrapper existingHandler) {
    return !overrides(newHandler, existingHandler) && !overrides(existingHandler, newHandler);
  }


  private boolean overrides(MessageHandlerWrapper subHandler, MessageHandlerWrapper superHandler) {
    return subHandler.bean.equals(superHandler.bean)
        && subHandler.method.getName().equals(superHandler.method.getName())
        && Arrays.equals(subHandler.method.getParameterTypes(), superHandler.method.getParameterTypes())
        && superHandler.method.getDeclaringClass().isAssignableFrom(subHandler.method.getDeclaringClass());
  }


  public List<Function> get(Class<?> messageType) {
    List<MessageHandlerWrapper> messageHandlers = handlers.get(messageType.getName());
    boolean noHandlers = messageHandlers == null || messageHandlers.isEmpty();
    if (allowMultipleOrMissingHandlers && noHandlers) {
      return of();
    } else if (!allowMultipleOrMissingHandlers && noHandlers) {
      throw new MissingMessageHandlerRegistrationException(messageType);
    } else {
      return new ArrayList<>(messageHandlers);
    }
  }

}
