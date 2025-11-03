package edu.unizg.foi.uzdiz.dblazevic21.komande;

public abstract class KomandaBridge 
{
	protected Komanda komanda;

    public KomandaBridge(Komanda komanda) 
    {
        this.komanda = komanda;
    }

    public abstract void izvrsiKomandu(String unos);
}
