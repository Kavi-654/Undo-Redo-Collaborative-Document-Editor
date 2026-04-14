package com.editor.history;

import java.util.List;

public class HistoryIterator {

    private final List<HistoryEntry> entries;
    private int currentIndex;

    public HistoryIterator(List<HistoryEntry> entries) {
        this.entries = entries;
        this.currentIndex = entries.size() - 1; // start at most recent
    }

    public boolean hasNext() { return currentIndex < entries.size() - 1; }
    public boolean hasPrev() { return currentIndex > 0; }

    public HistoryEntry next() {
        if (hasNext()) currentIndex++;
        return entries.get(currentIndex);
    }

    public HistoryEntry prev() {
        if (hasPrev()) currentIndex--;
        return entries.get(currentIndex);
    }

    public HistoryEntry current() { return entries.get(currentIndex); }

    public void printAll() {
        System.out.println("\u001B[36m--- Edit History ---\u001B[0m");
        for (int i = 0; i < entries.size(); i++) {
            String marker = (i == currentIndex) ? " \u001B[32m<< current\u001B[0m" : "";
            System.out.println("  [" + i + "] " + entries.get(i).getLabel() + marker);
        }
        System.out.println("\u001B[36m--------------------\u001B[0m");
    }
}