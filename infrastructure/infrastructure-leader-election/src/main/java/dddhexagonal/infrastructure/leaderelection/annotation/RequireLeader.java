package dddhexagonal.infrastructure.leaderelection.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation that ensures that the annotated method will only
 * be executed when application instance is acting as a leader.
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface RequireLeader {
}
