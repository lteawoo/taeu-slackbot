package kr.taeu.slackbot.dto;

import kr.taeu.slackbot.model.LineBotGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyToLineRequest {
    private LineBotGroup lineGroupId;
    private String message;
    
    @Builder
    public NotifyToLineRequest(LineBotGroup lineGroupId, String message) {
        this.lineGroupId = lineGroupId;
        this.message = message;
    }
}
