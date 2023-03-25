package dddhexagonal.foundations.application.usecases;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Transactional(propagation = REQUIRES_NEW)
@UseCase
public @interface TransactionalUseCase {

}
