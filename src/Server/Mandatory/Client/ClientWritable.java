package Server.Mandatory.Client;

import Server.Mandatory.Stuff.Command;

import javax.sound.sampled.Port;
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
    private boolean isRunning = true;

    public ClientWritable(Socket clientSocket, String name)
    {
        this.clientSocket = clientSocket;
        this.name = name;
    }

    @Override
    public void run()
    {
        try {
            BufferedReader keyBoard = new BufferedReader(new InputStreamReader(System.in));

            do {
                String message = keyBoard.readLine();
                if (message != null)
                {
                    if (!isClientCommand(message))
                    {
                        if (clientSocket != null && clientSocket.isConnected())
                        {
                            PrintWriter pwrite = new PrintWriter(clientSocket.getOutputStream(), true);

                            pwrite.println("DATA " + name + ": " + message);
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

        //checks if the first word is a command if so
        //it will execute the command
        if (!commandMap.containsKey(message.split(" ")[0]))
        {
            return false;
        }else{
            commandMap.get(message.split(" ")[0]).execute(message);
        }
        return true;
    }

    private Map<String, Command> initializeCommands()
    {
        Map<String, Command> commandMap = new HashMap<>();

        commandMap.put("/quit", new Command() {
            @Override
            public boolean execute(String commandMessage) {
                try
                {
                    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    printWriter.println("QUIT");

                    clientSocket.close();
                    isRunning = false;
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        commandMap.put("/join", new Command() {
            @Override
            public boolean execute(String commandMessage) {
                //Grabbing the first element after the "/join " message
                String ipInfo = commandMessage.split(" ")[1];

                clientSocket = connectSocket(ipInfo);

                if (clientSocket.isConnected()) {

                    try {
                        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                        printWriter.println("JOIN " +name +", "+clientSocket.getInetAddress().getHostName() +":"+clientSocket.getPort());

                        startReadable(clientSocket);

                        return true;
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                return false;
            }
        });

        commandMap.put("/name", new Command() {
            @Override
            public boolean execute(String commandMessage)
            {
                if (commandMessage.length() > 1)
                {
                    setName(commandMessage.split(" ")[1]);
                    return true;
                }
                return false;
            }
        });

        commandMap.put("/ping", new Command() {
            @Override
            public boolean execute(String commandMessage) {
                System.out.println("pong");
                return true;
            }
        });



        return commandMap;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Socket connectSocket(String ipInfo)
    {
        Socket socket = null;

        try {
            if (ipInfo.equals("localhost")) {

                return new Socket(InetAddress.getByName(ipInfo), PORT);

            } else {

                //If the ipInfo sent from the command does not contain :
                //Program will use default port 12000
                if (!ipInfo.contains(":")) {
                    return new Socket(InetAddress.getByName(ipInfo), PORT);
                } else {
                    //User specified port
                    //splits string on : and uses [0] and [1]
                    return new Socket(InetAddress.getByName(ipInfo.split(":")[0]), Integer.parseInt(ipInfo.split(":")[1]));
                }
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    public void startReadable(Socket socket)
    {
        Thread readThread = new Thread(new ClientReadable(clientSocket, name));
        readThread.start();
    }
}
























































