# Actividad-3---Arquitectura-Cliente-Servidor
Vamos a realizar un pequeño ejercicio aplicando la arquitectura cliente-servidor utilizando un chat con el lenguaje JAVA. La idea principal es que el servidor acepte varios clientes, y pues que cada cliente pueda enviar y recibir mensajes haciendo uso del chat
# Codigo del Cliente
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private String hostname;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Cliente(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(hostname, port);
            System.out.println("Conectado al servidor de chat");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread para leer mensajes del servidor
            new Thread(new ReadMessage()).start();

            // Scanner para enviar mensajes al servidor
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ReadMessage implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        Cliente cliente = new Cliente(hostname, port);
        cliente.start();
    }
}
