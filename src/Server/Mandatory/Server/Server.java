package Server.Mandatory.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    public static List<Socket> connectedSockets = new ArrayList<>();
    public static List<ClientHandler> clients = new ArrayList<>();

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

            connectedSockets.add(clientSocket);
            clientCount++;

            String name = command.split(" ")[1].replace(",", "");

            PrintWriter pwrite = new PrintWriter(clientSocket.getOutputStream(), true);

            Boolean isNameTaken = false;
            for (ClientHandler client : clients)
            {
                System.out.println(client.getName());
                if (client.getName().equals(name))
                {
                    isNameTaken = true;
                    break;
                }
            }

            System.out.println(name + " is taken: " + isNameTaken);

            if (isNameTaken)
            {
                System.out.println("name was taken");
                pwrite.println("J_ER 1:name is already in use");
            }else {
                System.out.println("name was not taken");
                ClientHandler clientHandler = new ClientHandler(clientSocket, serverSocket, name);
                clients.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
                pwrite.println("J_OK");
            }
        }
    }
}
