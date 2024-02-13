import java.io.*;
import java.net.*;

public class Cliente {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
                // Se establece la conexión con el servidor
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                // Se crea un lector de entrada para leer datos del servidor
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Se crea un escritor de salida para enviar datos al servidor
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // Se crea un lector de entrada para leer la entrada del usuario desde la consola
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // Se imprime un mensaje para indicar que se ha establecido la conexión con el servidor
            System.out.println("Conectado al servidor. Puedes empezar a enviar mensajes:");

            String input;
            // Se lee la entrada del usuario desde la consola y se envía al servidor
            while ((input = consoleInput.readLine()) != null) {
                out.println(input);
            }
        } catch (IOException e) {
            // Se imprime la traza de la excepción en caso de que ocurra un error de E/S
            e.printStackTrace();
        }
    }
}
