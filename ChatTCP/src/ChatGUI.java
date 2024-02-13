import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatGUI extends Application {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private TextArea chatArea; // Área de texto para mostrar los mensajes del chat
    private TextField inputField; // Campo de texto para escribir mensajes

    private Socket socket; // Socket para la conexión con el servidor
    private PrintWriter out; // Escritor para enviar mensajes al servidor

    private List<PrintWriter> clientWriters = new ArrayList<>(); // Lista de escritores de clientes

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat");

        BorderPane root = new BorderPane();

        chatArea = new TextArea();
        chatArea.setEditable(false);
        root.setCenter(chatArea);

        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));

        inputField = new TextField();
        inputField.setPromptText("Type your message here");
        inputField.setOnAction(e -> sendMessage()); // Acción para enviar mensajes cuando se presiona Enter

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage()); // Acción para enviar mensajes cuando se hace clic en el botón

        inputBox.getChildren().addAll(inputField, sendButton);
        root.setBottom(inputBox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Conexión al servidor
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Se establece la conexión con el servidor
            out = new PrintWriter(socket.getOutputStream(), true); // Se crea un escritor para enviar mensajes al servidor
            clientWriters.add(out); // Se agrega el escritor a la lista de escritores de clientes

            new Thread(new ServerListener()).start(); // Se inicia un hilo para escuchar los mensajes del servidor
        } catch (IOException e) {
            e.printStackTrace(); // Se imprime la traza de la excepción en caso de error
        }
    }

    private void sendMessage() {
        String message = inputField.getText(); // Se obtiene el mensaje del campo de texto
        if (!message.isEmpty()) {
            out.println(message); // Se envía el mensaje al servidor
            inputField.clear(); // Se limpia el campo de texto después de enviar el mensaje
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Se crea un lector para recibir mensajes del servidor

                String mensaje;
                while ((mensaje = in.readLine()) != null) { // Bucle para recibir y mostrar mensajes del servidor
                    String finalMensaje = mensaje;
                    Platform.runLater(() -> {
                        chatArea.appendText(finalMensaje + "\n"); // Se muestra el mensaje en el área de chat
                    });
                }
            } catch (IOException e) {
                e.printStackTrace(); // Se imprime la traza de la excepción en caso de error
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (socket != null) {
            socket.close(); // Se cierra el socket al detener la aplicación
        }
    }

    public static void main(String[] args) {
        launch(args); // Se inicia la aplicación JavaFX
    }
}
