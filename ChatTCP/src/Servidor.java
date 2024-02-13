import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    public static void main(String[] args) throws IOException {




        ServerSocket servidor=null;
        Socket sc=null;
        int puerto=5002;
        DataOutputStream out;
        DataInputStream in;


        servidor=new ServerSocket(puerto);
        System.out.println("Servidor iniciado");
        while (true){
            //Hasta que el cliente no inicialice se quedará aquí esperando:
            sc=servidor.accept();
            System.out.println("Cliente conectado.");
            //Recibimos los mensajes del cliente:
            in= new DataInputStream(sc.getInputStream());
            String mensaje=in.readUTF();
            System.out.println(mensaje);
            //Mandamos un mensaje:
            out=new DataOutputStream(sc.getOutputStream());
            out.writeUTF("Hola soy el servidor");
            //cerramos el cliente:
            sc.close();
            System.out.println("Cliente desconectado");


        }


    }
}
