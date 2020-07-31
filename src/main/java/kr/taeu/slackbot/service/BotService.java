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
import java.time.Instant;
import java.util.Enumeration;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
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
          SlashCommandPayload payload = parseSlashCommandPayload(request)
                  .orElseThrow(() -> new IllegalArgumentException());

          // 3. Command 분기
          switch (payload.getCommand()) {
          case "/장애전파": {
              restTemplate.postForEntity("https://taeu-linebot.herokuapp.com/callapi", payload.getText(),
                      String.class);

              // 4. Slack에 메세지 전송
              try {
                  ChatPostMessageResponse response = methodsClient
                          .chatPostMessage(req -> req.channel("#notice")
                                  .blocks(asBlocks(
                                          section(section -> section.text(markdownText("*장애전파완료!*"))),
                                          divider(),
                                          actions(actions -> actions.elements(
                                                  asElements(
                                                          button(b -> b.text(
                                                                  plainText(pt -> pt.emoji(true).text("Test1"))).value("v1")),
                                                          button(b -> b.text(
                                                                  plainText(pt -> pt.emoji(true).text("Test2"))).value("t2"))))))));
                  log.info("chat post response: " + response.toString());
              } catch (SlackApiException e) {
                  log.info("Slack api error: " + e);
              }
          }
          default: {
              msg = "fail " + payload.getCommand() + ": You said " + payload.getText() + ", at <#"
                      + payload.getChannelId() + "|" + payload.getChannelName() + ">";
          }
          }

          // 4. Respond to the Slack API server with 200 OK as an acknowledgment
          // 응답값은 slash command 요청자만 보임
          msg = "success " + payload.getCommand() + ": You said " + payload.getText() + ", at <#"
                  + payload.getChannelId() + "|" + payload.getChannelName() + ">";
      } catch (IOException e) {
          log.info("postToLineBot: " + e);
          msg = "fail " + e;
      }

      return msg;
  }
  
  private Optional<SlashCommandPayload> parseSlashCommandPayload(HttpServletRequest request) {
      Optional<SlashCommandPayload> ret = null;
      
      try {
          // 1. slack request 검증
          if (verifyRequestFromSlack(request)) {
              return Optional.empty();
          };
          
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
          if (!"".equals(requestBody)) {
              ret = Optional.ofNullable(parser.parse(requestBody));
          }
      } catch (Exception e) {
          ret = Optional.empty();
      }
      
      return ret;
  }
  
  private boolean verifyRequestFromSlack(HttpServletRequest request) throws Exception {
      // Verify requests from Slack
      // https://api.slack.com/docs/verifying-requests-from-slack
      // This needs "X-Slack-Signature" header, "X-Slack-Request-Timestamp" header, and raw request body

      // 1. 현재시간과 5분 이상 다르지 않은지 확인\
      Enumeration<String> headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
          String headerName = headerNames.nextElement();
          log.info("header " + headerName + request.getHeader(headerName));
      }
      
      Long timestamp = Long.parseLong(request.getHeader("x-slack-request-timestamp"));
      Long currentTimeStamp = Instant.now().getEpochSecond();
      if (Math.abs(currentTimeStamp - timestamp) > 60 * 5) {
          return false;
      }
      
      String requestBody = request.getReader().lines()
              .collect(Collectors.joining(System.lineSeparator()));
      if (!"".equals(requestBody)) {
          return false;
      }
      
      // 2. 각 파트 연결
      String baseString = "v0:" + timestamp + ":" + requestBody;
      
      // 3. 서명 생성
      String signature = request.getHeader("x-slack-signature");
      String mySignature = "v0=" + new HmacUtils(HmacAlgorithms.HMAC_SHA_256, signature).hmacHex(baseString);
      log.info("sign: " + signature + ", my: " + mySignature);
      if (!signature.equals(mySignature)) {
          return false;
      }
      
      return true;
  }
}
