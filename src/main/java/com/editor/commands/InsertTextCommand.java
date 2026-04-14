package com.editor.commands;

import com.editor.model.DocumentModel;
import java.util.UUID;

public class InsertTextCommand implements Command {

    private final String id;
    private final String userId;
    private final long timestamp;
    private final DocumentModel document;
    private final int position;
    private final String text;

    public InsertTextCommand(DocumentModel document, int position, String text, String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.document = document;
        this.position = position;
        this.text = text;
    }

    @Override
    public void execute() {
        document.insertText(position, text);
    }

    @Override
    public void undo() {
        document.deleteText(position, text.length());
    }

    @Override public String getId() { return id; }
    @Override public String getType() { return "INSERT"; }
    @Override public String getUserId() { return userId; }
    @Override public long getTimestamp() { return timestamp; }

    @Override
    public String getDescription() {
        return "INSERT \"" + text + "\" at position " + position;
    }
}