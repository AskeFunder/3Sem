package Threads.Print100Times;

public class Main
{

    public static void main(String[] args)
    {
        Runnable printA = new PrintChar('a', 1000000);
        Runnable printB = new PrintChar('b', 1000000);
        Runnable printNum = new PrintNum(1000000);

        Thread thread1 = new Thread(printA);
        Thread thread2 = new Thread(printB);
        Thread thread3 = new Thread(printNum);

        thread3.setPriority(Thread.MAX_PRIORITY);

        thread1.start();
        thread2.start();
        thread3.start();

    }

}
