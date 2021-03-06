package kr.taeu.slackbot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;

import kr.taeu.slackbot.service.SlackBotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlackBotMessageController {
  private final SlackBotMessageService slackBotMessageService;
  
  @PostMapping("/notifytoline")
  public String notifyToLine(HttpServletRequest request) {
    return slackBotMessageService.postToLineBot(request);
  }
  
  @GetMapping("/isRunning")
  public String test() {  
    return "run...";
  }
}