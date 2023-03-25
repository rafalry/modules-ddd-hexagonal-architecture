package dddhexagonal.foundations.integration.message.registry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import static dddhexagonal.foundations.integration.message.registry.MessageHandlerConfigurationException.illegalArguments;
import static dddhexagonal.foundations.integration.message.registry.MessageHandlerConfigurationException.illegalReturnType;
import static dddhexagonal.foundations.integration.message.registry.MessageHandlerConfigurationException.noAccess;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

public class IntegrationMessageHandlerPostProcessor implements BeanPostProcessor {


  private final ReflectionUtils.MethodFilter hasAnnotation;
  private final GenericMessageHandlerRegistry registry;
  private final Class returnTypeSuperclass;
  private final Class argumentSuperclass;


  public IntegrationMessageHandlerPostProcessor(
      Class annotationType,
      GenericMessageHandlerRegistry registry,
      Class returnTypeSuperclass,
      Class argumentSuperclass) {
    this.hasAnnotation = method -> findAnnotation(method, annotationType) != null;;
    this.registry = registry;
    this.returnTypeSuperclass = returnTypeSuperclass;
    this.argumentSuperclass = argumentSuperclass;
  }


  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
    ReflectionUtils.doWithMethods(bean.getClass(), method -> {
      if (!method.canAccess(bean)) {
        throw noAccess(method);
      }
      if (!returnTypeSuperclass.isAssignableFrom(method.getReturnType())) {
        throw illegalReturnType(method, returnTypeSuperclass);
      }
      if (method.getParameterCount() == 1
          && argumentSuperclass.isAssignableFrom(method.getParameterTypes()[0])) {
        registry.register(method.getParameterTypes()[0], new MessageHandlerWrapper<>(bean, method));
      } else {
        throw illegalArguments(method, 1, argumentSuperclass);
      }
    }, hasAnnotation);
    return bean;
  }


}
