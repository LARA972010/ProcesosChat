import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Cliente {
    public static void main(String[] args) throws IOException {
        String host="127.0.0.1";
        int puerto=5002;
        DataOutputStream out;
        DataInputStream in;




        Socket sc=new Socket(host,puerto);
        in= new DataInputStream(sc.getInputStream());
        out=new DataOutputStream(sc.getOutputStream());


        out.writeUTF("Hola soy el cliente.");
        String mensaje=in.readUTF();
        System.out.println(mensaje);
        sc.close();
    }
}

