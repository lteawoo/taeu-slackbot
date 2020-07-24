package kr.taeu.slackbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.slack.api.Slack;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SlackConfig {
  
  @Bean(name = "slack")
  public Slack getSlackInstance() {
    
    log.info("slack instance");
    return Slack.getInstance();
  }
}
