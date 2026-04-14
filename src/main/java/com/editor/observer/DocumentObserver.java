package com.editor.observer;

import com.editor.commands.Command;

public interface DocumentObserver {
    void onDocumentChanged(Command command, String documentSnapshot);
}