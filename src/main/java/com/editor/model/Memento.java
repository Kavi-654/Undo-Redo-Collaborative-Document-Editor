package com.editor.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Memento {
    private String content;
    private int cursorPosition;
    private Map<Integer,String> formattingMap;
    private String id;
    private long timestamp;

    public  Memento(String content, int cursorPosition, HashMap<Integer,String> formattingMap) {
         this.content=content;
         this.cursorPosition=cursorPosition;
         this.formattingMap=formattingMap;
         this.id= UUID.randomUUID().toString();
         this.timestamp=System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public Map<Integer, String> getFormattingMap() {
        return Collections.unmodifiableMap(formattingMap);
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }



}
