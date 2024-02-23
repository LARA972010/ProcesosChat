package UDP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClientUDP extends JFrame {
    private DatagramSocket socket;
    private InetAddress direccionServidor;
    private int puertoServidor;
    private String nombreUsuario;

    private JTextArea chatArea;
    private JTextField inputField;

    public ChatClientUDP(String direccion, int puerto, String nombreUsuario) {
        super("Chat de Grupo - " + nombreUsuario);
        try {
            this.socket = new DatagramSocket();
            this.direccionServidor = InetAddress.getByName(direccion);
            this.puertoServidor = puerto;
            this.nombreUsuario = nombreUsuario;
        } catch (UnknownHostException | SocketException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el socket del cliente: " + e.getMessage());
        }
        createUI();
        new Thread(this::recibeMensajes).start();
    }

    private void createUI() {
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        inputField = new JTextField(50);

        JButton botonEnviar = new JButton("Enviar");
        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
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

    private void enviarMensaje() {
        try {
            String mensaje = inputField.getText().trim();
            if (!mensaje.isEmpty()) {
                mensaje = nombreUsuario + ": " + mensaje;
                byte[] buffer = mensaje.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, direccionServidor, puertoServidor);
                socket.send(packet);
                inputField.setText("");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al enviar el mensaje: " + e.getMessage());
        }
    }

    private void recibeMensajes() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String mensaje = new String(packet.getData(), 0, packet.getLength());
               //En este punto me escribe el mensaje dos veces
                chatArea.append(mensaje + "\n");

            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al recibir mensajes: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String direccionServidor = "localhost"; // Dirección IP del servidor
        int puertoServidor = 1234; // Puerto en el que escucha el servidor
        String nombreUsuario = JOptionPane.showInputDialog("Escribe tu nombre para el grupo del chat");
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            SwingUtilities.invokeLater(() -> new ChatClientUDP(direccionServidor, puertoServidor, nombreUsuario));
        }
    }
}
