package Threads.Print100Times;

public class PrintChar implements Runnable
{
    char c;
    int times;

    public PrintChar(char c, int times)
    {
        this.c = c;
        this.times = times;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < times; i++)
        {
            System.out.println(c);
        }
    }
}
