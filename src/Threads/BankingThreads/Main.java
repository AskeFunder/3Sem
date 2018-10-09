package Threads.BankingThreads;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        BankingThread banking = new BankingThread();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++)
        {
            threads[i] = new Thread(banking);
        }

        /*List<Thread> threads = new ArrayList<>();
        */



        /*for (int i = 0; i < 10000; i++)
        {
            threads.add(new Thread(banking));
        }*/

        for (Thread thread : threads)
        {
            thread.start();
        }

        System.out.println(banking.getBalance());

    }
}
