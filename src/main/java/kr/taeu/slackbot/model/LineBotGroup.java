package kr.taeu.slackbot.model;

import lombok.Getter;

@Getter
public enum LineBotGroup {
    DEV1("Cf5dc393dbfc047212f20cb10f622baee"),
    DEV2("Cf5dc393dbfc047212f20cb10f622baee");
    
    private String groupId;
    
    LineBotGroup(String groupId) {
        this.groupId = groupId;
    }
}
