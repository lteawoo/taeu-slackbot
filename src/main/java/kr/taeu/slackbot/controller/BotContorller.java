package kr.taeu.slackbot.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
  private final MethodsClient methodsClient;
  
  @GetMapping("/callApi")
  public String callApi() {
    return "error";
  }
  
  @PostMapping("/test")
  public String test() {
    log.info("test");
    
    ChatPostMessageRequest request = ChatPostMessageRequest.builder()
        .channel("#notice")
        .text(":wave: 장애발생!")
        .build();
    
    try {
      ChatPostMessageResponse response = methodsClient.chatPostMessage(request);
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