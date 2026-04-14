package com.editor.commands;

import com.editor.model.DocumentModel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormatCommand implements Command {
    private final String id;
    private final String userId;
    private final long timestamp;
    private final DocumentModel document;

    private final int rangeStart;
    private final int rangeEnd;
    private final String newStyle;
    private final Map<Integer, String> previousStyles; // Stores old styles for undo

    public FormatCommand(DocumentModel document, int start, int end, String newStyle, String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.document = document;
        this.rangeStart = start;
        this.rangeEnd = end;
        this.newStyle = newStyle;
        this.previousStyles = new HashMap<>();

        // Capture current formatting for this range before we change it
        Map<Integer, String> currentDocStyles = document.getFormattingMap();
        for (int i = rangeStart; i < rangeEnd; i++) {
            if (currentDocStyles.containsKey(i)) {
                previousStyles.put(i, currentDocStyles.get(i));
            }
        }
    }

    @Override
    public void execute() {
        document.applyFormat(rangeStart, rangeEnd, newStyle);
    }

    @Override
    public void undo() {
        // First, clear the new style for this range
        for (int i = rangeStart; i < rangeEnd; i++) {
            document.getFormattingMap().remove(i);
        }
        // Then, restore exactly what was there before
        for (Map.Entry<Integer, String> entry : previousStyles.entrySet()) {
            document.applyFormat(entry.getKey(), entry.getKey() + 1, entry.getValue());
        }
    }

    @Override public String getId() { return id; }
    @Override public String getType() { return "FORMAT"; }
    @Override public String getUserId() { return userId; }
    @Override public long getTimestamp() { return timestamp; }
    @Override public String getDescription() { return "Applied " + newStyle + " from " + rangeStart + " to " + rangeEnd; }
}