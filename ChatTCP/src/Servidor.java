import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;


public class Servidor extends Observable implements Runnable {

    private int puerto;
    public Servidor(int puerto){
        this.puerto=puerto;

    }

    @Override
    public void run() {

        ServerSocket servidor=null;
        Socket sc=null;


        DataInputStream in;


        try {
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

            this.setChanged();
            //El mensaje es lo que nosotros queremos notificar
            this.notifyObservers(mensaje);
            this.clearChanged();

            //cerramos el cliente:
            sc.close();
            System.out.println("Cliente desconectado");


        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
