import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClienteHandler implements Runnable {
    private static ArrayList<ClienteHandler> clienteHandler = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String NombreCliente;

    public ClienteHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.NombreCliente = bufferedReader.readLine();
            clienteHandler.add(this);
            DifundirMensaje("server: " + NombreCliente + " ha entrado al chat!");
        } catch (IOException e) {
            CerrarTodo();
        }
    }

    @Override
    public void run() {
        String MensajeCliente;
        try {
            while ((MensajeCliente = bufferedReader.readLine()) != null) {
                DifundirMensaje(NombreCliente + ": " + MensajeCliente);
            }
        } catch (IOException e) {
            CerrarTodo();
        }
    }

    public static void DifundirMensaje(String MensajeEnviar) {
        for (ClienteHandler cliente : clienteHandler) {
            try {
                cliente.bufferedWriter.write(MensajeEnviar);
                cliente.bufferedWriter.newLine();
                cliente.bufferedWriter.flush();
            } catch (IOException e) {
                cliente.CerrarTodo();
            }
        }
    }

    public void salidaCliente() {
        clienteHandler.remove(this);
        DifundirMensaje("Server " + NombreCliente + " ha salido del chat.");
    }

    public void CerrarTodo() {
        salidaCliente();
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