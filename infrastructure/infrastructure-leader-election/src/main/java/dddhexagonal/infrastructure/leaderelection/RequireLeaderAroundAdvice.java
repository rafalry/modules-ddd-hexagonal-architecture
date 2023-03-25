package dddhexagonal.infrastructure.leaderelection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import dddhexagonal.infrastructure.leaderelection.annotation.ConditionalOnLeaderElectionEnabled;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnLeaderElectionEnabled
public class RequireLeaderAroundAdvice {

  private final LeaderContext leaderContext;

  @Around("requiredLeaderMethod()")
  public void processMethodWhenLeader(final ProceedingJoinPoint joinPoint) throws Throwable {
    if (leaderContext.isLeader()) {
      log.trace("Proceeding with method {} execution, leader", joinPoint.getSignature().getName());
      joinPoint.proceed();
    } else {
      log.trace("Skipped method {} execution, not a leader", joinPoint.getSignature().getName());
    }
  }

  @Pointcut("@annotation(dddhexagonal.infrastructure.leaderelection.annotation.RequireLeader)")
  private void requiredLeaderMethod() {
  }
}
