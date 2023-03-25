package dddhexagonal.foundations.integration.message.registry;

import java.lang.reflect.Method;

import static java.lang.String.format;

public class MessageHandlerConfigurationException extends RuntimeException {

  public MessageHandlerConfigurationException(String message) {
    super(message);
  }


  public MessageHandlerConfigurationException(Throwable throwable) {
    super(throwable);
  }


  public static MessageHandlerConfigurationException noAccess(Method method) {
    return new MessageHandlerConfigurationException(format(
        "Could not configure handler '%s' because it is not accessible. Handler method should be public.",
        method));
  }


  public static MessageHandlerConfigurationException illegalArguments(Method method, int argsCount, Class argClass) {
    return new MessageHandlerConfigurationException(format(
        "Could not configure handler '%s' because it does not have the correct arguments. This handler should have %d argument that is assignable to class %s",
        method, argsCount, argClass.getSimpleName()));
  }


  public static MessageHandlerConfigurationException illegalInvocation(Method method) {
    return new MessageHandlerConfigurationException(format(
        "Could not invoke handler '%s' because the provided arguments are not correct.",
        method));
  }


  public static MessageHandlerConfigurationException illegalReturnType(Method method, Class<?> type) {
    return new MessageHandlerConfigurationException(format(
        "Could not configure handler '%s' because it does not have the correct return type assignable to %s",
        method, type));
  }


  public static MessageHandlerConfigurationException illegalArgumentType(Method method) {
    return new MessageHandlerConfigurationException(format(
        "Could not configure handler '%s' because it does not have the correct argument types.",
        method));
  }
}
