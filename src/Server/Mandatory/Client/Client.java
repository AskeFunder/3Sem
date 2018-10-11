package Server.Mandatory.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{
    public static void main(String[] args) throws IOException {
        //Initialize variables
        InetAddress IPAdress = null;
        Socket clientSocket = new Socket();
        final int port = 12000;
        String recieveMessage, sendMessage;
        String name = null;

        //Reads from keyboard, keyboard object
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));


        //User enters username and will ask
        //again until username is filled
        while (name == null)
        {
            System.out.println("Enter your name for chat");
            String tempName = inFromUser.readLine();
            if (tempName.contains(" "))
            {
                System.out.println("Spaces are not allowed");
            }else{
                name = tempName;
            }
                try {

                    //Create and starts write and read thread
                    Thread writeThread = new Thread(new ClientWritable(clientSocket, name));
                    writeThread.start();

                    System.out.println("Client is now ready to use, connect using /join");

                }catch (Exception e)
                {

                }
        }


        //Continues until user has entered a valid IP Adress
        // and a connection has successfully been made
    }
}

