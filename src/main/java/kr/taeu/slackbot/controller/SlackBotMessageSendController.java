package kr.taeu.slackbot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;

import kr.taeu.slackbot.service.SlackBotMessageSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlackBotMessageSendController {
  private final SlackBotMessageSendService slackBotMessageSendService;
  
  @PostMapping("/notifytoline")
  public String notifyToLine(HttpServletRequest request) {
    return slackBotMessageSendService.postToLineBot(request);
  }
  
  @GetMapping("/isRunning")
  public String test() {  
    return "run...";
  }
}