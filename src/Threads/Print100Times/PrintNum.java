package Threads.Print100Times;

public class PrintNum implements Runnable
{
    int num;
    int times;

    public PrintNum(int num)
    {
        this.num = num;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < num; i++)
        {
            System.out.println(i + 1);
        }
    }
}
