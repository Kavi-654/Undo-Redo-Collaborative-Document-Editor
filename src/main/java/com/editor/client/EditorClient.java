package com.editor.client;

import com.editor.commands.*;
import com.editor.history.HistoryManager;
import com.editor.invoker.CommandInvoker;
import com.editor.model.CareTaker;
import com.editor.model.DocumentModel;
import com.editor.observer.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;

public class EditorClient extends WebSocketClient {

    private final String userId;
    private final DocumentModel document;
    private final CommandInvoker invoker;
    private final HistoryManager historyManager;

    public EditorClient(URI serverUri, String userId) throws Exception {
        super(serverUri);
        this.userId = userId;
        this.document = new DocumentModel();
        CareTaker careTaker = new CareTaker();
        this.invoker = new CommandInvoker(document, careTaker);
        this.historyManager = new HistoryManager();

        // Register all observers
        document.addObserver(new UIRenderer(userId));
        document.addObserver(new CollaboratorCursorTracker());
        document.addObserver(new ChangeBroadcaster());
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("\u001B[32m[CONNECTED]\u001B[0m You are: " + userId);
        printHelp();
    }

    @Override
    public void onMessage(String message) {
        // Incoming change from another collaborator
        System.out.println("\u001B[35m[SYNC]\u001B[0m " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("\u001B[31m[DISCONNECTED]\u001B[0m");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("\u001B[31m[ERROR]\u001B[0m " + ex.getMessage());
    }

    public void startConsoleLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            handleInput(input);
            System.out.print("> ");
        }
    }

    private void handleInput(String input) {
        String[] parts = input.split(" ", 3);
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
            case "INSERT" -> {
                // Usage: INSERT <position> <text>
                int pos = Integer.parseInt(parts[1]);
                String text = parts[2];
                Command command = new InsertTextCommand(document, pos, text, userId);
                invoker.execute(command);
                send("INSERT|" + userId + "|" + pos + "|" + text);
                document.printDocument();
            }
            case "DELETE" -> {
                // Usage: DELETE <position> <length>
                int pos = Integer.parseInt(parts[1]);
                int len = Integer.parseInt(parts[2]);
                Command command = new DeleteTextCommand(document, pos, len, userId);
                invoker.execute(command);
                send("DELETE|" + userId + "|" + pos + "|" + len);
                document.printDocument();
            }
            case "UNDO" -> {
                invoker.undo();
                document.printDocument();
            }
            case "REDO" -> {
                invoker.redo();
                document.printDocument();
            }
            case "HISTORY" -> historyManager.getIterator().printAll();
            case "SHOW" -> document.printDocument();
            case "HELP" -> printHelp();
            default -> System.out.println("Unknown command. Type HELP.");
        }
    }

    private void printHelp() {
        System.out.println("\u001B[36m Commands: INSERT <pos> <text> | DELETE <pos> <len> | UNDO | REDO | HISTORY | SHOW | HELP \u001B[0m");
    }
}
