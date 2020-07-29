package kr.taeu.slackbot.service;

import static com.slack.api.model.block.Blocks.actions;
import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.slack.api.app_backend.slash_commands.SlashCommandPayloadParser;
import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {
  private final RestTemplate restTemplate;
  private final MethodsClient methodsClient;
  
  public String postToLineBot(HttpServletRequest request) {
    String msg = "";
    
    try {
      // TODO 요청값에 대한 결과값이 없을경우의 예외처리가 있는경우 변경
      SlashCommandPayload payload = parsePayload(request)
          .orElseThrow(() -> new IllegalArgumentException("SlashCommand payload parse fail"));
      
      // 3. Command 분기
      switch (payload.getCommand()) {
        case "/장애전파": {
          restTemplate.postForEntity("https://taeu-linebot.herokuapp.com/callapi"
              , payload.getText(), String.class);
          
          //4. Slack에 메세지 전송
          try {
            ChatPostMessageResponse response = methodsClient.chatPostMessage(req -> req
                .channel("#notice")
                .blocks(asBlocks(
                    section(section -> section.text(markdownText("**장애전파완료**"))),
                    divider(),
                    actions(actions -> actions
                        .elements(asElements(
                            button(b -> b.text(
                                plainText(pt -> pt.emoji(true)
                                    .text("Test1")))
                                .value("v1")),
                            button(b -> b.text(
                                plainText(pt -> pt.emoji(true)
                                    .text("Test2")))
                                .value("t2"))
                            ))
                        )
                    ))
                );
            log.info("chat post response: " + response.toString());
          } catch (SlackApiException e) {
            log.info("Slack api error: " + e);
          }
        }
        default: {
          msg = "fail " + payload.getCommand() + ": You said " + payload.getText() + ", at <#" + payload.getChannelId() + "|" + payload.getChannelName() + ">";
        }
      }
      
      // 4. Respond to the Slack API server with 200 OK as an acknowledgment
      // 응답값은 slash command 요청자만 보임
      msg = "success " + payload.getCommand() + ": You said " + payload.getText() + ", at <#" + payload.getChannelId() + "|" + payload.getChannelName() + ">";
    } catch (IOException e) {
      log.info("postToLineBot: " + e);
      msg = "fail " + e;
    }
    
    return msg;
  }
  
  private Optional<SlashCommandPayload> parsePayload(HttpServletRequest request) throws IOException {
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
    if ("".equals(requestBody)) {
      throw new IllegalArgumentException("Slash Command request body must not be empty");
    }
    SlashCommandPayload payload = parser.parse(requestBody);
    
    return Optional.ofNullable(payload);
  }
}
