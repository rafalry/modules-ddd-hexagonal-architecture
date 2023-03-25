package dddhexagonal.foundations.integration.message.registry;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static dddhexagonal.foundations.integration.message.registry.MessageHandlerConfigurationException.illegalInvocation;
import static dddhexagonal.foundations.integration.message.registry.MessageHandlerConfigurationException.noAccess;

@RequiredArgsConstructor
@ToString
public class MessageHandlerWrapper<M, R> implements Function<M, R> {
  protected final Object bean;
  protected final Method method;


  @Override
  public R apply(M message) {
    try {
      return (R) method.invoke(bean, message);
    } catch (final IllegalAccessException e) {
      throw noAccess(method);
    } catch (final IllegalArgumentException e) {
      throw illegalInvocation(method);
    } catch (final InvocationTargetException e) {
      throw new MessageHandlerConfigurationException(e.getTargetException());
    }
  }

}
