package com.editor.commands;

import com.editor.model.DocumentModel;
import java.util.UUID;

public class DeleteTextCommand implements Command {
    private final String id;
    private final String userId;
    private final long timestamp;
    private final DocumentModel document;
    private final int position;
    private final int length;
    private final String deletedText; // The "Undo" payload

    public DeleteTextCommand(DocumentModel document, int position, int length, String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.document = document;
        this.position = position;
        this.length = length;

        // CRITICAL: Capture the text before it's deleted
        this.deletedText = document.getContent().substring(position, position + length);
    }

    @Override
    public void execute() {
        document.deleteText(position, length);
    }

    @Override
    public void undo() {
        // Re-insert exactly what was removed
        document.insertText(position, deletedText);
    }

    @Override public String getId() { return id; }
    @Override public String getType() { return "DELETE"; }
    @Override public String getUserId() { return userId; }
    @Override public long getTimestamp() { return timestamp; }
    @Override public String getDescription() { return "Deleted " + length + " chars at " + position; }
}