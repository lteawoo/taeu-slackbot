package kr.taeu.slackbot.dto;

import kr.taeu.slackbot.model.LineBotGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyToLineRequest {
    private LineBotGroup lineBotGroup;
    private String message;
    
    @Builder
    public NotifyToLineRequest(LineBotGroup lineBotGroup, String message) {
        this.lineBotGroup = lineBotGroup;
        this.message = message;
    }
}
