package dddhexagonal.infrastructure.leaderelection.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties;
import org.springframework.context.annotation.Configuration;

import dddhexagonal.infrastructure.leaderelection.annotation.ConditionalOnLeaderElectionEnabled;

@Configuration
@ConditionalOnLeaderElectionEnabled
@EnableConfigurationProperties(LeaderProperties.class)
public class LeaderConfiguration {
}
