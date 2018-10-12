package Server.Mandatory.Server;

import Server.Mandatory.Stuff.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class ClientHandler implements Runnable
{
    //Variables
    private final Socket clientSocket;
    private final ServerSocket serverSocket;
    private OutputStream ostream;
    private InputStream istream;
    private BufferedReader recieveRead;
    private String name;
    private HashMap<String, Command> serverCommands = new HashMap<>();
    boolean isAlive;

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public String getName() {
        return name;
    }

    public ClientHandler(Socket clientSocket, ServerSocket serverSocket, String name) throws IOException {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;

        //initializes server command
        serverCommands = initializeServerCommandList();

        //Send to client
        this.ostream = clientSocket.getOutputStream();

        //recieving from client
        this.istream = clientSocket.getInputStream();
        this.recieveRead = new BufferedReader(new InputStreamReader(istream));



        this.name = name;
    }

    @Override
    public void run()
    {
        isAlive = true;
        while (isAlive)
        {
            String message = null;

            //Recieves message from this client
            try {
                System.out.println("ready to read");
                message = this.recieveRead.readLine();
                System.out.println("message: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!isServerCommand(message))
            {
                System.out.println("not command");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isServerCommand(String commandMsg)
    {
        if (commandMsg != null)
        {
            System.out.println("Command msg was not null");
            if (commandMsg.split(" ").length > 0) {
                if (serverCommands.containsKey(commandMsg.split(" ")[0])) {
                    if (serverCommands.get(commandMsg.split(" ")[0]).execute(commandMsg)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    private HashMap<String,Command> initializeServerCommandList()
    {
        serverCommands.put("DATA", new Command() {
            @Override
            public boolean execute(String commandMessage) {
                if (commandMessage.length() > 2)
                {
                    try {
                        for (Socket socket : Server.clients.values())
                        {
                            PrintWriter pwrite = new PrintWriter(socket.getOutputStream(), true);
                            pwrite.println(name + ": " + commandMessage.split(" ")[2]);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        serverCommands.put("QUIT", new Command() {
            @Override
            public boolean execute(String commandMessage)
            {
                Server.clients.remove(name);

                for (Socket socket : Server.clients.values())
                {
                    try {
                        PrintWriter pwrite = new PrintWriter(socket.getOutputStream(), true);
                        pwrite.println(name + " has left the server");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                return true;
            }
        });
        return serverCommands;
    }
}
