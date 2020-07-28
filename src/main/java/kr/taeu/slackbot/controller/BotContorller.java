package kr.taeu.slackbot.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.slack.api.app_backend.slash_commands.SlashCommandPayloadParser;
import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import kr.taeu.slackbot.service.BotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotContorller {
  private final RestTemplate restTemplate;
  private final MethodsClient methodsClient;
  private final BotService botService;
  
  @PostMapping("/callapi")
  public String callApi(HttpServletRequest request) {
    return botService.postToLineBot(request);
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