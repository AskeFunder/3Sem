package Server.Mandatory.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    public static List<Socket> connectedSockets = new ArrayList<>();

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


            System.out.println("Client " + clientCount + " has connected");
            ClientHandler clientHandler = new ClientHandler(clientSocket, serverSocket, name);
            Thread thread = new Thread(clientHandler);
            thread.start();

            try{
                for (Socket socket : connectedSockets)
                {
                    PrintWriter pwrite = new PrintWriter(socket.getOutputStream(), true);

                    pwrite.println(" has joined the channel");
                }
            }finally{}
        }
    }
}
