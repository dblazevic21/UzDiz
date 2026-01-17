package edu.unizg.foi.uzdiz.dblazevic21.app.command;

public class VstarCommand implements CommandAranzmani 
{
	private final VstarReceiver receiver;
    private final int oznaka;

    public VstarCommand(VstarReceiver receiver, int oznaka)
    {
        this.receiver = receiver;
        this.oznaka = oznaka;
    }

    @Override
    public void execute()
    {
        System.out.println(receiver.vrati(oznaka));
    }
}
