import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Cliente implements Runnable{
    private int puerto;
    private String mensaje;

    public Cliente(int puerto,String mensaje){
        this.puerto=puerto;
        this.mensaje=mensaje;

    }


    @Override
    public void run() {
        String host="127.0.0.1";

        DataOutputStream out;

        Socket sc= null;
        try {
            sc = new Socket(host,puerto);


        out=new DataOutputStream(sc.getOutputStream());


        out.writeUTF(mensaje);

        sc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

