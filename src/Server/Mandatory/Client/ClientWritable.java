package Server.Mandatory.Client;

import Server.Mandatory.Stuff.Command;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class ClientWritable implements Runnable
{
    private Socket clientSocket;
    private Map<String, Command> commandMap = initializeCommands();
    private final int PORT = 12000;
    private String name;

    public ClientWritable(Socket clientSocket, String name)
    {
        this.clientSocket = clientSocket;
        this.name = name;
    }

    @Override
    public void run()
    {
        try {
            OutputStream oStream = null;
            BufferedReader keyBoard = new BufferedReader(new InputStreamReader(System.in));

            do {
                String message = keyBoard.readLine();
                if (message != null)
                {
                    if (!isClientCommand(message))
                    {
                        if (clientSocket != null && clientSocket.isConnected()) {
                            PrintWriter pwrite = new PrintWriter(oStream, true);

                            pwrite.println(message);
                        }
                    }
                }
            }while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isClientCommand(String message)
    {
        String[] commandMessage = message.split(" ");

        if (!commandMap.containsKey(commandMessage[0]))
        {
            return false;
        }else{
            commandMap.get(commandMessage[0]).execute(commandMessage);
        }
        return true;
    }

    private Map<String, Command> initializeCommands()
    {
        Map<String, Command> commandMap = new HashMap<>();

        commandMap.put("/quit", new Command() {
            @Override
            public boolean execute(String[] message) {
                try
                {
                    clientSocket.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        commandMap.put("/join", new Command() {
            @Override
            public boolean execute(String[] commandMessage)
            {
                if (!(commandMessage.length == 2))
                {
                    if (commandMessage.length > 2)
                    {
                        System.out.println("Too many argument");
                        return false;
                    }else if (commandMessage.length < 2)
                    {
                        System.out.println("Too few arguments");
                        return false;
                    }
                }

                InetAddress ipAdress = null;
                int port = 0;

                try {
                    String IPAndPort = commandMessage[1];
                    String[] IPAndPortArray = commandMessage[1].split(":");

                    ipAdress = InetAddress.getByName(IPAndPortArray[0]);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                if (!(ipAdress == null) && port != 0 || !(ipAdress == null) && ipAdress.toString().equals("localhost/127.0.0.1"))
                {
                    try{
                        clientSocket = new Socket(ipAdress, PORT);
                        System.out.println(clientSocket.isConnected());
                        if (clientSocket.isConnected())
                        {
                            OutputStream oStream = clientSocket.getOutputStream();
                            System.out.println("Successfully connected to " + clientSocket.toString());

                            try(PrintWriter printWriter = new PrintWriter(oStream))
                            {
                                printWriter.println("JOIN <<" + name + ">>, <<" + ipAdress.toString() + ">>:<<" + PORT + ">>");
                            }
                            return true;
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        return commandMap;
    }
}
