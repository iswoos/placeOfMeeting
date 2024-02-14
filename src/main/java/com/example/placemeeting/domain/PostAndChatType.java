package com.example.placemeeting.domain;

public enum PostAndChatType {
    TALK("잡담"),
    LODGING("숙소"),
    FOOD("음식"),
    HOTPLACE("핫플"),
    TRAFFIC("교통");

    private final String value;

    PostAndChatType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}