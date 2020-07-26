package kr.taeu.slackbot.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
  
  @PostMapping("/callapi")
  public String callApi() {
    String ret = "";
    
    ChatPostMessageRequest request = ChatPostMessageRequest.builder()
        .channel("#notice")
        .text(":wave: 송신완료!")
        .build();
    
    try {
      ChatPostMessageResponse response = methodsClient.chatPostMessage(request);
      log.info("response: " + response.toString());
      
      RestTemplate restTemplate = new RestTemplate();
      ret = restTemplate.getForObject("https://taeu-linebot.herokuapp.com/callapi", String.class);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SlackApiException e) {
      e.printStackTrace();
    }
    
    return ret;
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