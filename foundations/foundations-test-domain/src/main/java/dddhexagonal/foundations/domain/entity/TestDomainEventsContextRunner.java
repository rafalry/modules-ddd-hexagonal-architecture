package dddhexagonal.foundations.domain.entity;

import java.util.Map;
import java.util.function.Supplier;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import dddhexagonal.foundations.domain.events.BaseDomainEvent;

public class TestDomainEventsContextRunner {

  private Map<Class<?>, Supplier<?>> suppliers;


  public  TestDomainEventsContextRunner(Class<?> listenerClass, Supplier<?> supplier) {
    this.suppliers = Map.of(listenerClass, supplier);
  }


  public TestDomainEventsContextRunner(Map<Class<?>, Supplier<?>> suppliers) {
    this.suppliers = suppliers;
  }


  public <T> void run(BaseAggregateRoot aggregateRoot) {
    ApplicationContextRunner runner = new ApplicationContextRunner();

    for (Class<?> listenerClass : suppliers.keySet()) {
      runner = runner.<T>withBean((Class<T>)listenerClass, (Supplier<T>) suppliers.get(listenerClass));
    }

    runner.run(context -> {
      for (BaseDomainEvent event : aggregateRoot.domainEvents()) {
        context.publishEvent(event);
      }
    });


  }

}
