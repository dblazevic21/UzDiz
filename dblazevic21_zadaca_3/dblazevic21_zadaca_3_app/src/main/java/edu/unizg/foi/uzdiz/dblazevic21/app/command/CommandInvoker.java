package edu.unizg.foi.uzdiz.dblazevic21.app.command;

public class CommandInvoker 
{
    public void run(CommandAranzmani command) 
    {
        if (command == null) return;
        
        command.execute();
    }
}
