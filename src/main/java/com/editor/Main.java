package com.editor;

import com.editor.client.EditorClient;
import com.editor.server.EditorServer;

import java.net.URI;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  java Main server");
            System.out.println("  java Main client <userId>");
            return;
        }

        if (args[0].equals("server")) {
            EditorServer server = new EditorServer(8080);
            server.start();

        } else if (args[0].equals("client")) {
            String userId = args.length > 1 ? args[1] : "User1";
            EditorClient client = new EditorClient(
                    new URI("ws://localhost:8080"), userId
            );
            client.connectBlocking();
            client.startConsoleLoop();
        }
    }
}