package Server.UDP;

import java.io.*;
import java.net.*;

class UDPClient
{
    private static String sentence = "";

    public static void main(String args[]) throws Exception
    {
        while(true) {
            BufferedReader inFromUser =
                    new BufferedReader(new InputStreamReader(System.in));
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("10.111.180.223");
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            System.out.print("Type message: ");
            sentence = inFromUser.readLine();

            if (sentence.equals("/quit"))
            {
                clientSocket.close();
            }

            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 12000);
            clientSocket.send(sendPacket);
            System.out.println("Pakke sendt");
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
        }
    }
}