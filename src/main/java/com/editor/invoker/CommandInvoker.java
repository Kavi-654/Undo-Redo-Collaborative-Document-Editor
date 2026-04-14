package com.editor.invoker;

import com.editor.commands.Command;
import com.editor.model.CareTaker;
import com.editor.model.DocumentModel;

import java.util.Stack;

public class CommandInvoker {

    private final Stack<Command> undoStack=new Stack<>();
    private final Stack<Command> redoStack=new Stack<>();
    private final DocumentModel document;
    private final CareTaker careTaker;

    public CommandInvoker(DocumentModel document,CareTaker careTaker)
    {
        this.careTaker=careTaker;
        this.document=document;
    }

    public void execute(Command command)
    {
        careTaker.save(document.saveState());

        command.execute();
        undoStack.push(command);
        redoStack.clear();
        System.out.println("\u001B[32m[DONE]\u001B[0m " + command.getDescription());
    }

    public void undo()
    {
        if(undoStack.isEmpty())
        {
            System.out.println("\u001B[33m[INFO]\u001B[0m Nothing to undo.");
            return;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        document.notifyObservers(command);
        System.out.println("\u001B[33m[UNDO]\u001B[0m " + command.getDescription());
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("\u001B[33m[INFO]\u001B[0m Nothing to redo.");
            return;
        }
        Command command = redoStack.pop();
        careTaker.save(document.saveState());
        command.execute();
        undoStack.push(command);
        document.notifyObservers(command);
        System.out.println("\u001B[32m[REDO]\u001B[0m " + command.getDescription());
    }

    public Stack<Command> getUndoStack() { return undoStack; }
    public Stack<Command> getRedoStack() { return redoStack; }
}
