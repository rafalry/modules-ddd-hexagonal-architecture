package dddhexagonal.infrastructure.leaderelection.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@ConditionalOnProperty(value = "leader.enabled", havingValue = "true")
public @interface ConditionalOnLeaderElectionEnabled {
}
