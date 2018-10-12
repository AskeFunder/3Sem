package Server.Mandatory.Stuff;

public abstract class Command
{

    protected Command()
    {
    }

    public abstract boolean execute(String commandMessage);
}
