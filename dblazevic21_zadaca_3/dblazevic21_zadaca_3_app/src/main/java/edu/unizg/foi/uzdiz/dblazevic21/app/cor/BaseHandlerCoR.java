package edu.unizg.foi.uzdiz.dblazevic21.app.cor;

public abstract class BaseHandlerCoR implements HandlerZaCoR 
{
	protected HandlerZaCoR sljedeciHandler;

	// Successor
	@Override
	public void setSljedeci(HandlerZaCoR sljedeci)
	{
		this.sljedeciHandler = sljedeci;
	}
	
	protected void sljedeci(KomandaContext context)
	{
		if (sljedeciHandler != null)
		{
			sljedeciHandler.handle(context);
		}
	}
}
