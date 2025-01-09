package com.example.sideproject.domain.chat.entity;

public enum MessageType {
    ENTER("입장"),
    LEAVE("퇴장"),
    CHAT("채팅");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
