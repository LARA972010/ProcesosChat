import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clients = new HashSet<>(); // Conjunto para almacenar flujos de salida de clientes

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // Se crea el socket del servidor y se vincula al puerto especificado
            System.out.println("Servidor iniciado...");
            while (true) { // Bucle infinito para aceptar conexiones de clientes continuamente
                Socket clientSocket = serverSocket.accept(); // Se acepta la conexión de un cliente
                System.out.println("Nuevo cliente conectado: " + clientSocket);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // Se crea un flujo de salida para enviar datos al cliente
                clients.add(out); // Se agrega el flujo de salida del cliente al conjunto

                // Se crea y se inicia un hilo para manejar las interacciones con el cliente
                Thread clientHandler = new Thread(new ClientHandler(clientSocket, out));
                clientHandler.start();
            }
        } catch (IOException e) { // Se captura cualquier excepción de E/S que pueda ocurrir
            e.printStackTrace(); // Se imprime la traza de la excepción
        }
    }

    // Clase interna para manejar las interacciones con cada cliente
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;

        // Constructor que recibe el socket del cliente y el flujo de salida asociado
        public ClientHandler(Socket clientSocket, PrintWriter out) {
            this.clientSocket = clientSocket;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Se crea un lector de entrada para recibir datos del cliente

                String message;
                // Bucle para leer mensajes del cliente continuamente
                while ((message = in.readLine()) != null) {
                    System.out.println("Mensaje recibido: " + message); // Se imprime el mensaje recibido del cliente
                    broadcast(message); // Se envía el mensaje recibido a todos los clientes conectados
                }
            } catch (IOException e) { // Se captura cualquier excepción de E/S que pueda ocurrir
                e.printStackTrace(); // Se imprime la traza de la excepción
            } finally {
                if (out != null) {
                    clients.remove(out); // Se elimina el flujo de salida del cliente del conjunto cuando la conexión se cierra
                }
                try {
                    clientSocket.close(); // Se cierra el socket del cliente
                } catch (IOException e) { // Se captura cualquier excepción de E/S que pueda ocurrir al cerrar el socket
                    e.printStackTrace(); // Se imprime la traza de la excepción
                }
            }
        }
    }

    // Método para enviar un mensaje a todos los clientes conectados
    private static void broadcast(String message) {
        for (PrintWriter client : clients) {
            client.println(message); // Se envía el mensaje a cada cliente a través de su flujo de salida
        }
    }
}
