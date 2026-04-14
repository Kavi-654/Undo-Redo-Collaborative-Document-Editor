package com.editor.observer;

import com.editor.commands.Command;

public class UIRenderer implements DocumentObserver {

    private final String clientId;

    public UIRenderer(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void onDocumentChanged(Command command, String documentSnapshot) {
        System.out.println("\u001B[35m[UI:" + clientId + "]\u001B[0m Re-rendering after: "
                + command.getDescription());
        System.out.println("  Current content: \"" + documentSnapshot + "\"");
    }
}