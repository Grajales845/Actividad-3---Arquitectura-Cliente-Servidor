/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.*;
import java.net.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BiConsumer<String, ClientHandler> broadcastMessage;
    private Consumer<ClientHandler> removeClient;

    public ClientHandler(Socket socket, BiConsumer<String, ClientHandler> broadcastMessage, Consumer<ClientHandler> removeClient) {
        this.clientSocket = socket;
        this.broadcastMessage = broadcastMessage;
        this.removeClient = removeClient;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + message);
                broadcastMessage.accept(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            removeClient.accept(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
    

