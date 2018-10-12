package Server.Mandatory.Server;

import Server.Mandatory.Client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server
{
    public static Map<String, Socket> clients = new HashMap<>();

    public static void main(String[] args) throws IOException
    {
        String recieveMessage, sendMessage;
        int clientCount = 0;

        ServerSocket serverSocket = new ServerSocket(12000);
        System.out.println("Server ready for chatting");

        while(true)
        {
            //Server is now ready to accept connections
            Socket clientSocket = serverSocket.accept();

            //so it can communicate
            InputStream istream = clientSocket.getInputStream();
            BufferedReader recieveRead = new BufferedReader(new InputStreamReader(istream));

            //recieves join statement from client
            String command = recieveRead.readLine();
            //special regex for the join message from
            //the client to get name
            String name = command.split(" ")[1].replace(",", "");

            PrintWriter pwrite = new PrintWriter(clientSocket.getOutputStream(), true);

            Boolean isNameTaken = clients.containsKey(name);


            if (isNameTaken)
            {
                pwrite.println("J_ER 1: Name is taken");
            }else{
                ClientHandler clientHandler = new ClientHandler(clientSocket, serverSocket, name);
                Thread thread = new Thread(clientHandler);
                thread.start();

                clients.put(name, clientSocket);
                pwrite.println("J_OK");
            }
        }
    }
}
