package edu.unizg.foi.uzdiz.dblazevic21.app.cor;

public class ExecuteHandlerCoR extends BaseHandlerCoR 
{
	@Override
	public void handle(KomandaContext context)
	{
		try
		{
			context.komanda.izvrsi(context.unos);
		}
		catch (Exception e)
		{
			System.out.println("Greška tijekom izvršavanja komande.");
		}
		
		sljedeci(context);
	}
}
