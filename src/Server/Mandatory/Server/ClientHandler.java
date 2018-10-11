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


    public String getName() {
        return name;
    }

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
            String message = null;

            //Recieves message from this client
            try {
                message = this.recieveRead.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (message != null)
            {
                for (Socket socket : connectedSockets)
                {
                    try
                    {
                        PrintWriter pwrite = new PrintWriter(socket.getOutputStream(), true);
                        pwrite.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            /*



                if (message != null)
                {
                    System.out.println("Message is not null");
                    for (Socket socket : connectedSockets)
                    {
                        System.out.println("We start printing");
                        PrintWriter pwrite = null;
                        try {
                            pwrite = new PrintWriter(socket.getOutputStream(), true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        pwrite.println(message);
                    }
                }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}
