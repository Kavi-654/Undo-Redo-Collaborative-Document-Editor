package com.editor.model;

import com.editor.commands.Command;
import com.editor.observer.DocumentObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentModel {

    private String content;
    private int cursorPosition;
    private Map<Integer,String> formattingMap;

    public DocumentModel()
    {
        this.content="";
        this.cursorPosition=0;
        this.formattingMap=new HashMap<>();
    }

    public Memento saveState()
    {
        return new Memento(content,cursorPosition,new HashMap<>(formattingMap));
    }

    public void restoreState(Memento memento)
    {
        this.content=memento.getContent();
        this.cursorPosition=memento.getCursorPosition();
        this.formattingMap=new HashMap<>(memento.getFormattingMap());

    }

    public void insertText(int position,String text)
    {
        content=content.substring(0,position)+text+content.substring(position);
        cursorPosition=position+text.length();
    }

    public void deleteText(int position,int length)
    {
        content=content.substring(0,position)+content.substring(position+length);
        cursorPosition=position;
    }

    public void applyFormat(int start, int end, String style) {
        for (int i = start; i < end; i++) {
            formattingMap.put(i, style);
        }
    }

    public String getContent() { return content; }
    public int getCursorPosition() { return cursorPosition; }
    public Map<Integer, String> getFormattingMap() { return formattingMap; }

    public void printDocument() {
        System.out.println("\u001B[36m--- Document ---\u001B[0m");
        System.out.println(content.isEmpty() ? "(empty)" : content);
        System.out.println("Cursor at: " + cursorPosition);
        System.out.println("\u001B[36m----------------\u001B[0m");
    }

    private final List<DocumentObserver> observers = new ArrayList<>();

    public void addObserver(DocumentObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DocumentObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Command command) {
        for (DocumentObserver observer : observers) {
            observer.onDocumentChanged(command, this.content);
        }
    }

}
