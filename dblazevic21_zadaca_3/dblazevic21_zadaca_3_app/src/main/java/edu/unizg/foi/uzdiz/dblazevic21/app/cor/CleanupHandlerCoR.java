package edu.unizg.foi.uzdiz.dblazevic21.app.cor;

public class CleanupHandlerCoR extends BaseHandlerCoR
{
	@Override
	public void handle(KomandaContext context)
	{
		context.caretaker.remove(context.oznakaAranzmana);
	}
}
