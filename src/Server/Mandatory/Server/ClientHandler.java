package Server.Mandatory.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable
{
    //Variables
    private final Socket clientSocket;
    private final ServerSocket serverSocket;
    private OutputStream ostream;
    private InputStream istream;
    private BufferedReader recieveRead;
    private List<Socket> connectedSockets;
    private String name;




    public ClientHandler(Socket clientSocket, ServerSocket serverSocket, String name) throws IOException {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;

        //Send to client
        this.ostream = clientSocket.getOutputStream();

        //recieving from client
        this.istream = clientSocket.getInputStream();
        this.recieveRead = new BufferedReader(new InputStreamReader(istream));

        this.connectedSockets = Server.connectedSockets;

        this.name = name;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try {
                String message = this.recieveRead.readLine();

                if (message != null)
                {
                    for (Socket socket : connectedSockets)
                    {
                        PrintWriter pwrite = new PrintWriter(socket.getOutputStream(), true);

                        pwrite.println(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
