package com.editor.observer;

import com.editor.commands.Command;
import java.util.HashMap;
import java.util.Map;

public class CollaboratorCursorTracker implements DocumentObserver {
    private final Map<String, Integer> cursorPositions = new HashMap<>();

    @Override
    public void onDocumentChanged(Command command, String documentSnapshot) {
        String userId = command.getUserId();
        // We simulate the cursor position being at the end of the action taken
        // In a real app, this might be sent separately, but here we track via command
        int newPos = command.getDescription().contains("at position") ?
                extractPosition(command.getDescription()) : 0;

        cursorPositions.put(userId, newPos);

        System.out.println("\u001B[33m[Tracker]\u001B[0m User " + userId +
                " moved to position " + newPos);
    }

    private int extractPosition(String desc) {
        // Simple helper to find the last integer in the description string
        String[] parts = desc.split(" ");
        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void printAllCursors() {
        System.out.println("\u001B[33m--- Active Cursors ---\u001B[0m");
        cursorPositions.forEach((user, pos) ->
                System.out.println("User: " + user + " | Position: " + pos));
    }
}