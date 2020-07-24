package kr.taeu.slackbot.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotContorller {
  private final Slack slack;
  
  @PostMapping("/test")
  public String test() {
    log.info("test");
    String token = "xoxb-1251509934998-1284369695216-thQJGwSyn60PrvU9VW4hLLtd";
    
    MethodsClient methods = slack.methods(token);
    
    ChatPostMessageRequest request = ChatPostMessageRequest.builder()
        .channel("#notice")
        .text(":wave: Hi from a bot written in Java!")
        .build();
    
    try {
      ChatPostMessageResponse response = methods.chatPostMessage(request);
      log.info("response: " + response.toString());
      return response.getMessage().toString();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SlackApiException e) {
      e.printStackTrace();
    }
    return "fail";
  }
}
