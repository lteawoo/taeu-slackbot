package kr.taeu.slackbot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slack.api.methods.MethodsClient;

import kr.taeu.slackbot.service.BotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotContorller {
  private final MethodsClient methodsClient;
  private final BotService botService;
  
  @PostMapping("/callapi")
  public String callApi(HttpServletRequest request) {
    return botService.postToLineBot(request);
  }
  
  @GetMapping("/isRunning")
  public String test() {  
    return "run...";
  }
}