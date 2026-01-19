package edu.unizg.foi.uzdiz.dblazevic21.app.cor;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class SnapshotHandlerCoR extends BaseHandlerCoR
{
	@Override
	public void handle(KomandaContext context)
	{
		Aranzmani aranzman = context.aranzmani.get(context.oznakaAranzmana);
		
		if (context.komanda != null && "VSTAR".equalsIgnoreCase(context.komanda.getNaziv())) 
		{
		    sljedeci(context);
		    return;
		}
		
		if (aranzman == null)
		{
			System.out.println("Aranžman s oznakom " + context.oznakaAranzmana + " ne postoji.");
			return;
		}
		
		context.caretaker.store(context.oznakaAranzmana, aranzman.createMemento());
		
		sljedeci(context);
	}
}
