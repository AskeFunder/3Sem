package Server.Mandatory.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Heartbeatable implements Runnable
{
    Socket clientSocket;

    public Heartbeatable(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try {
                PrintWriter pwrite = new PrintWriter(clientSocket.getOutputStream(), true);
                pwrite.println("IMALIVE");
                Thread.sleep(2500);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
