import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClienteHandler implements Runnable{

    public static  ArrayList<ClienteHandler>clienteHandler=new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClienteHandler(Socket socket){
        try{

            this.socket=socket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername=bufferedReader.readLine();
            clienteHandler.add(this);
            broadcastMessage("server: "+clientUsername+" has entrado al chat!");
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }



    }
    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()){
            try{
                messageFromClient=bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);

            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for(ClienteHandler clienteHandler:clienteHandler){
            try{
                if(!clienteHandler.clientUsername.equals(clientUsername)){
                    clienteHandler.bufferedWriter.write(messageToSend);
                    clienteHandler.bufferedWriter.newLine();
                    clienteHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);

            }

            }
        }

        public void removeClientHandler(){
            clienteHandler.remove(this);
            broadcastMessage("Server "+clientUsername+" ha salido del chat.");

        }
        public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removeClientHandler();
        try{

            if(bufferedReader!=null){

                bufferedReader.close();
            }
            if(bufferedWriter!=null){

                bufferedWriter.close();
            }
            if(socket !=null){

                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        }

    }


