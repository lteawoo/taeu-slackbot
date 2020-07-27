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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotContorller {
  private final RestTemplate restTemplate;
  private final MethodsClient methodsClient;
  
  @PostMapping("/callapi")
  public String callApi(HttpServletRequest request) throws IOException {
    // TODO 1. Verify requests from Slack
    // https://api.slack.com/docs/verifying-requests-from-slack
    // This needs "X-Slack-Signature" header, "X-Slack-Request-Timestamp" header, and raw request body
    
    // 2. Parse the request body and check if the `command` is the one you'd like to handle
    SlashCommandPayloadParser parser = new SlashCommandPayloadParser();
    
    // The request body looks like this:
    //   token=gIkuvaNzQIHg97ATvDxqgjtO&team_id=T0001&team_domain=example
    //   &enterprise_id=E0001&enterprise_name=Globular%20Construct%20Inc
    //   &channel_id=C2147483705&channel_name=test
    //   &user_id=U2147483697&user_name=Steve
    //   &command=weather&text=94070&response_url=https://hooks.slack.com/commands/1234/5678
    //   &trigger_id=123.123.123
    String requestBody = request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));
    
    SlashCommandPayload payload = parser.parse(requestBody);
    
    // 3. Command 분기
    switch (payload.getCommand()) {
    case "장애전파": {
      // 원하면 채널분기 가능
      restTemplate.postForEntity("https://taeu-linebot.herokuapp.com/callapi", payload.getText(), String.class);
      return "success: You said " + payload.getText() + ", at <#" + payload.getChannelId() + "|" + payload.getChannelName() + ">";
    }
    }
    
    return "fail: You said " + payload.getText() + ", at <#" + payload.getChannelId() + "|" + payload.getChannelName() + ">";
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
  
  @PostMapping("/test1")
  public String test1(HttpServletRequest request) throws IOException {
    String requestBody = request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));
    
    log.info(requestBody);
    
    return requestBody;
  }
}