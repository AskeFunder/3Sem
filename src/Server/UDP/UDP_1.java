package Server.UDP;
/**
 * Write a description of class UDP_1 here.
 *
 */

import java.io.*;
import java.net.*;

public class UDP_1
{
    public static void main(String args[]) throws Exception
    {
        String sentence;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket receivingSocket = new DatagramSocket(12346);
        DatagramSocket sendingSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
        byte[] data = new byte[1024];

        System.out.println("Please type you message: ");
        sentence = inFromUser.readLine();
        data = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 12345);
        sendingSocket.send(sendPacket);
        sentence = "                  ";
        data = sentence.getBytes();

        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        receivingSocket.receive(receivePacket);
        sentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + sentence);


    }
}