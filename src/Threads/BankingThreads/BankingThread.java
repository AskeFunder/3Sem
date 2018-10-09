package Threads.BankingThreads;

public class BankingThread implements Runnable

{
    private int balance = 0;

    @Override
    public synchronized void run()
    {
        incrementBalance();
    }

    private synchronized void incrementBalance()
    {
        for (int i = 0; i < 10; i++) {
            balance++;
        }
    }

    public int getBalance() {
        return balance;
    }
}
