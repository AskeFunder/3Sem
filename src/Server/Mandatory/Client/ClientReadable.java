package Server.Mandatory.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReadable implements Runnable
{
    private Socket clientSocket;
    private boolean isLoggedIn;

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
            if (joinMessage.equals("J_OK"))
            {
                isLoggedIn = true;
                System.out.println("Is logged in: " + isLoggedIn);
            }else{
                if (joinMessage.equals("J_ER 1: Name is taken"))
                {
                    System.out.println("Name is already taken, please change name with /name <new name>");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (isLoggedIn) {
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
