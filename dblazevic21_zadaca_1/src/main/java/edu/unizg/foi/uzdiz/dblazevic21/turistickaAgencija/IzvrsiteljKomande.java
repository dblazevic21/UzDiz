package edu.unizg.foi.uzdiz.dblazevic21.turistickaAgencija;


import edu.unizg.foi.uzdiz.dblazevic21.komande.Komanda;
import edu.unizg.foi.uzdiz.dblazevic21.komande.KomandaBridge;

public class IzvrsiteljKomande extends KomandaBridge
{
	public IzvrsiteljKomande(Komanda komanda) 
	{
       super(komanda);
    }

    @Override
    public void izvrsiKomandu(String unos)
    {
        komanda.izvrsi(unos);
    }
}
