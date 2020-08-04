package kr.taeu.slackbot.model;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum LineBotGroup {
    DEV1("Cf5dc393dbfc047212f20cb10f622baee"),
    DEV2("Cf5dc393dbfc047212f20cb10f622baee");
    
    @JsonValue  
    private String groupId;
    
    LineBotGroup(String groupId) {
        this.groupId = groupId;
    }
}
