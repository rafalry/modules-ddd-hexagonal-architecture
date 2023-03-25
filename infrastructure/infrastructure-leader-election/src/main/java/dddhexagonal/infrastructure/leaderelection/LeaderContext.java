package dddhexagonal.infrastructure.leaderelection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.stereotype.Component;

import dddhexagonal.infrastructure.leaderelection.annotation.ConditionalOnLeaderElectionEnabled;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnLeaderElectionEnabled
public class LeaderContext implements Context {

  private final LeaderProperties leaderProperties;
  private boolean leader;

  @Override
  public boolean isLeader() {
    return leader;
  }

  public String getConfigMapName() {
    return leaderProperties.getConfigMapName();
  }

  public void onLeadershipGranted(final OnGrantedEvent event) {
    if (roleIs(event.getRole())) {
      markAsLeader();
    } else {
      log.warn("Leadership granted for {} role, while service subscribed for {}", event.getRole(), leaderProperties.getRole());
    }
  }

  public void onLeadershipRevoked(final OnRevokedEvent event) {
    if (roleIs(event.getRole())) {
      yieldLeadership();
    } else {
      log.warn("Leadership revoked for {} role, while service subscribed for {}", event.getRole(), leaderProperties.getRole());
    }
  }

  private boolean roleIs(final String role) {
    return StringUtils.isNotBlank(leaderProperties.getRole()) &&
        StringUtils.isNotBlank(role) &&
        leaderProperties.getRole().equals(role);
  }

  void markAsLeader() {
    leader = true;
  }

  void yieldLeadership() {
    leader = false;
  }
}
