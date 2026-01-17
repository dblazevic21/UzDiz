package edu.unizg.foi.uzdiz.dblazevic21.app.command;

public class PstarCommand implements CommandAranzmani
{
	private final PstarReceiver receiver;
    private final int oznaka;

    public PstarCommand(PstarReceiver receiver, int oznaka) 
    {
        this.receiver = receiver;
        this.oznaka = oznaka;
    }
    
    

    @Override
    public void execute() 
    {
        System.out.println(receiver.spremi(oznaka));
    }
}
