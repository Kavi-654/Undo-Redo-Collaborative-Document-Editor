package com.editor.history;

import com.editor.commands.Command;
import com.editor.model.DocumentModel;
import com.editor.model.Memento;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private final List<HistoryEntry> entries = new ArrayList<>();

    // Adds a new snapshot-action pair to the timeline
    public void record(Command command, Memento memento) {
        entries.add(new HistoryEntry(command, memento));
    }

    // Factory method for the Iterator pattern
    public HistoryIterator getIterator() {
        return new HistoryIterator(new ArrayList<>(entries)); // Passing a copy for thread safety
    }

    // Time-traveling: Restore document to a specific point in time
    public void jumpTo(int index, DocumentModel doc) {
        if (index >= 0 && index < entries.size()) {
            HistoryEntry entry = entries.get(index);
            doc.restoreState(entry.getMemento());

            System.out.println("\u001B[33m[HistoryManager]\u001B[0m Jumped to index " + index +
                    ": " + entry.getLabel());
        } else {
            System.err.println("Invalid history index: " + index);
        }
    }

    public int getHistorySize() {
        return entries.size();
    }
}