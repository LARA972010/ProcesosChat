import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.*;

public class Servidor {
    private ServerSocket serverSocket;

    public Servidor(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){

        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado");
                ClienteHandler clienteHandler=new ClienteHandler(socket);

                Thread thread= new Thread(clienteHandler);
                thread.start();

            }


        }catch(IOException E){
        }

    }
    public void closeServerSocket(){
    try{
        if(serverSocket!=null){
            serverSocket.close();

        }

    }catch(IOException E){
    }



}

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(1234);
        Servidor server = new Servidor(serverSocket);
        server.startServer();

    }
}
