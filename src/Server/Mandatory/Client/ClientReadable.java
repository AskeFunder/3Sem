package Server.Mandatory.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReadable implements Runnable
{
    private Socket clientSocket;

    public ClientReadable(Socket clientSocket, String name)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        InputStream istream = null;
        try {
            istream = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader recieveRead = new BufferedReader(new InputStreamReader(istream));

        try {
            String joinMessage = recieveRead.readLine();
            if (joinMessage.equals("J_OK"));
            {
                System.out.println("J_OK recieved");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            if (clientSocket.isConnected()) {
                try {
                    String message = recieveRead.readLine();
                    if (message != null) {
                        System.out.println(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
