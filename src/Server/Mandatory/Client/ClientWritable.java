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
    private Thread readThread;
    private Thread heartbeat;

    public ClientWritable(Socket clientSocket, String name)
    {
        this.clientSocket = clientSocket;
        this.name = name;
    }

    @Override
    public void run()
    {
        try {
            //Ready for user input
            BufferedReader keyBoard = new BufferedReader(new InputStreamReader(System.in));

            do {
                //reads userinput
                System.out.println("Ready to read");
                String message = keyBoard.readLine();
                if (message != null)
                {
                    //if message was not a client command, it will be sent to the server as a text message
                    //if it is a command it will be executed and isClientCommand will return false
                    if (!isClientCommand(message))
                    {
                        if (clientSocket != null && clientSocket.isConnected())
                        {
                            PrintWriter pwrite = new PrintWriter(clientSocket.getOutputStream(), true);

                            pwrite.println("DATA " + name + ": " + message);
                        }
                    }
                }
            }while (isRunning);

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

    //starts up all commands
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

                    isRunning = false;
                    System.out.println("Exiting the program");

                    readThread.interrupt();
                    heartbeat.interrupt();
                    Thread.currentThread().interrupt();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
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
                        //Creates printwrite and sends the join protocol to the server
                        PrintWriter pwrite = new PrintWriter(clientSocket.getOutputStream(), true);
                        pwrite.println("JOIN " +name +", "+clientSocket.getInetAddress().getHostName() +":"+clientSocket.getPort());

                        //starts heartbeat and readable thread
                        readThread =  new Thread(new ClientReadable(clientSocket, name));
                        readThread.start();

                        heartbeat = new Thread(new Heartbeatable(clientSocket));
                        heartbeat.start();


                        return true;
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                return false;
            }
        });

        //changes name
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


        //request the server to print a list of every connected user
        commandMap.put("/list", new Command() {
            @Override
            public boolean execute(String commandMessage)
            {
                if (clientSocket.isConnected())
                {
                    try {
                        PrintWriter pwrite = new PrintWriter(clientSocket.getOutputStream(), true);
                        pwrite.println("LIST");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });




        return commandMap;
    }

    public void setName(String name) {
        this.name = name;
    }


    //@Param ipInfo is info about the connection sent to the method
    //from this info, this message creates and returns a socket
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


}
























































