import java.io.*;
import java.net.*;

public class Servidor {
    private ServerSocket serverSocket;

    public Servidor(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado");
                ClienteHandler clienteHandler = new ClienteHandler(socket);

                Thread thread = new Thread(clienteHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        } finally {
            cerrarSocket();
        }
    }

    public void cerrarSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor cerrado.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Servidor server = new Servidor(serverSocket);
            server.startServer();
        } catch (IOException e) {
            System.err.println("Error al crear el socket del servidor: " + e.getMessage());
        }
    }
}
