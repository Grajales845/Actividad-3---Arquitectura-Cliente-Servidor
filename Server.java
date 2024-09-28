/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado");

                ClientHandler clientHandler = new ClientHandler(clientSocket, Server::broadcastMessage, Server::removeClient);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para enviar mensajes a todos los clientes conectados
    public static void broadcastMessage(String message, ClientHandler excludeClient) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != excludeClient) {
                clientHandler.sendMessage(message);
            }
        }
    }

    // Eliminar cliente
    public static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Cliente desconectado");
    }
}