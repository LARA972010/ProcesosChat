package UDP;

import java.net.*;

public class ServidorUDP {
    private DatagramSocket socket;
    private byte[] buffer;

    public ServidorUDP(int puerto) {
        try {
            this.socket = new DatagramSocket(puerto);
            this.buffer = new byte[1024];
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String mensaje = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Mensaje recibido: " + mensaje);
                difundirMensaje(mensaje, packet.getPort());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void difundirMensaje(String mensaje, int puertoCliente) {
        try {
            //No creo que esta sea la forma mas óptima
            for (int puerto = 1025; puerto <= 65535; puerto++) {
                if (puerto != puertoCliente) {
                    DatagramPacket packet = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length,
                            InetAddress.getByName("localhost"), puerto);
                    socket.send(packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int puertoServidor = 1234;
        ServidorUDP server = new ServidorUDP(puertoServidor);
        server.startServer();
    }
}
