package kr.taeu.slackbot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyToLineRequest {
    private String lineGroupId;
    private String message;
    
    @Builder
    public NotifyToLineRequest(String lineGroupId, String message) {
        this.lineGroupId = lineGroupId;
        this.message = message;
    }
}
