package com.editor.observer;

import com.editor.commands.Command;
import java.util.ArrayList;
import java.util.List;

public class ChangeBroadcaster implements DocumentObserver {
    private final List<String> broadcastLog = new ArrayList<>();

    @Override
    public void onDocumentChanged(Command command, String documentSnapshot) {
        String message = String.format("[BROADCAST] userId=%s type=%s desc=\"%s\"",
                command.getUserId(),
                command.getType(),
                command.getDescription());

        broadcastLog.add(message);

        // Simulating the "Push" to WebSockets
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    public List<String> getBroadcastLog() {
        return new ArrayList<>(broadcastLog);
    }
}
