# Actividad-3---Arquitectura-Cliente-Servidor
Vamos a realizar un pequeño ejercicio aplicando la arquitectura cliente-servidor utilizando un chat con el lenguaje JAVA. La idea principal es que el servidor acepte varios clientes, y pues que cada cliente pueda enviar y recibir mensajes haciendo uso del chat
# Codigo del Cliente
```javascript
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
```
- El cliente se concecta al servidor mediante un Socket
- Creandose un hilo que puede leer los mensajes del servidor.
- El cliente una vez conectado podra enviar mensajes desde la consola, ejemplo:

  El cliente envia un mensaje:
  
  ![image](https://github.com/user-attachments/assets/8f108e0b-8862-49f5-a6fc-a1fc8346f2c3)


  El servidor recibe el mensaje:

  ![image](https://github.com/user-attachments/assets/85fa37ff-889b-412c-83d5-8cd178b1a5fb)




# Codigo del Servidor

```java
import java.io.*;
import java.net.*;
import java.util.*;
import server.ClientHandler;

public class Server {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + port);

            // Crear un hilo para que el servidor envíe mensajes a los clientes
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String serverMessage = scanner.nextLine();
                    broadcastMessage("Servidor: " + serverMessage, null);
                }
            }).start();

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
```

- El servidor crea un ServerSocket que escuha en un puerto (12345) especifico.
- Cada vez que un cliente se conecta se crea un hilo, permitiendo que varios clientes se puedan conectar al servidor

El servidor puede enviar mensajes a los clientes

![image](https://github.com/user-attachments/assets/ced2930b-3d14-4668-84bb-a0745a8695b5)

Y el cliente los recibe:

![Uploading image.png…]()


















