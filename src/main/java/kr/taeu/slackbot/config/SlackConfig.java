package kr.taeu.slackbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SlackConfig {
  private final String slackToken;
  
  public SlackConfig() {
    this.slackToken = System.getenv("SLACK_TOKEN");
  }
  
  @Bean("methodsClient")
  public MethodsClient getMethodsClient() {
    log.info(slackToken);
    return Slack.getInstance().methods(slackToken);
  }
}
