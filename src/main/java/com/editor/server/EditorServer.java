package com.editor.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collection;

public class EditorServer extends WebSocketServer {

    public EditorServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("\u001B[32m[SERVER]\u001B[0m New client connected: " + conn.getRemoteSocketAddress());
        conn.send("[SERVER] Welcome to the collaborative editor!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("\u001B[34m[SERVER]\u001B[0m Received: " + message);
        // Broadcast to ALL other clients (not back to sender)
        broadcast(message, conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("\u001B[31m[SERVER]\u001B[0m Client disconnected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("\u001B[31m[SERVER ERROR]\u001B[0m " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("\u001B[32m[SERVER]\u001B[0m Editor server started on port " + getPort());
    }

    // Broadcast to all except the sender
    private void broadcast(String message, WebSocket sender) {
        Collection<WebSocket> connections = getConnections();
        for (WebSocket conn : connections) {
            if (conn != sender) {
                conn.send(message);
            }
        }
    }
}
