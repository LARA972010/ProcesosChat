import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClienteHandler implements Runnable {
    private static ArrayList<ClienteHandler> clienteHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nombreCliente;

    public ClienteHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nombreCliente = bufferedReader.readLine();
            clienteHandlers.add(this);
            difundirMensaje("server: " + nombreCliente + " ha entrado al chat!");
        } catch (IOException e) {
            cerrarTodo();
        }
    }

    @Override
    public void run() {
        try {
            String mensajeCliente;
            while ((mensajeCliente = bufferedReader.readLine()) != null) {
                difundirMensaje(nombreCliente + ": " + mensajeCliente);
            }
        } catch (IOException e) {
            // Cliente desconectado
        } finally {
            clienteHandlers.remove(this);
            difundirMensaje("Server: " + nombreCliente + " se ha desconectado.");
            cerrarTodo();
        }
    }

    public static void difundirMensaje(String mensaje) {
        for (ClienteHandler cliente : clienteHandlers) {
            try {
                cliente.bufferedWriter.write(mensaje);
                cliente.bufferedWriter.newLine();
                cliente.bufferedWriter.flush();
            } catch (IOException e) {
                cliente.cerrarTodo();
            }
        }
    }

    public void cerrarTodo() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}