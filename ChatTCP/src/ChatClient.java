import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends JFrame {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String NombreUsuario;

    private JTextArea chatArea;
    private JTextField inputField;

    public ChatClient(Socket socket, String nombreUsuario) {
        super("Chat de Grupo - " + nombreUsuario);
        this.socket = socket;
        this.NombreUsuario = nombreUsuario;

        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            enviarMensaje(); // Envía el nombre de usuario al servidor
            chatGrupo();
            recibeMensaje(); // Escucha los mensajes del servidor
        } catch (IOException e) {
            cerrarTodo();
        }
    }

    private void chatGrupo() {
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        inputField = new JTextField(50);

        JButton botonEnviar = new JButton("Enviar");
        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MensajeServidor();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(botonEnviar);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void MensajeServidor() {
        try {
            String mensajeEnviar = inputField.getText().trim();
            if (!mensajeEnviar.isEmpty()) {
                bufferedWriter.write(   mensajeEnviar);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                inputField.setText("");
            }
        } catch (IOException e) {
            cerrarTodo();
        }
    }

    private void recibeMensaje() {
        new Thread(() -> {
            String mensajeParaTodos;
            try {
                while ((mensajeParaTodos = bufferedReader.readLine()) != null) {
                    chatArea.append(mensajeParaTodos + "\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                }
            } catch (IOException e) {
                cerrarTodo();
            }
        }).start();
    }

    private void enviarMensaje() {
        try {
            bufferedWriter.write(NombreUsuario);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            cerrarTodo();
        }
    }

    private void cerrarTodo() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Conexión perdida. Cerrando la aplicación.");
        System.exit(1);
    }

    public static void main(String[] args) {
        String nombreUsuario = JOptionPane.showInputDialog("Escribe tu nombre para el grupo del chat");
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            try {
                Socket socket = new Socket("localhost", 1234);
                new ChatClient(socket, nombreUsuario);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al conectar al servidor: " + e.getMessage());
            }
        }
    }
}