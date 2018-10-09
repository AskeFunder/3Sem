package Server.Mandatory.Client;

import java.io.BufferedReader;
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
        while (!clientSocket.isClosed()) {
            try {
                InputStream istream = clientSocket.getInputStream();
                BufferedReader recieveRead = new BufferedReader(new InputStreamReader(istream));

                String message = recieveRead.readLine();
                if (message != null) {
                    System.out.println(message);
                }


            } catch (Exception e) {

            }
        }
    }
}
