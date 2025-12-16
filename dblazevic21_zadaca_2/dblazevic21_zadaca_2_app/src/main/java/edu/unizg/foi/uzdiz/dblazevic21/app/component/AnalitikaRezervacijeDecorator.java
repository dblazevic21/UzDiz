package edu.unizg.foi.uzdiz.dblazevic21.app.component;

import java.util.List;

public abstract class AnalitikaRezervacijeDecorator implements TuristickiElementComponent 
{
    protected final TuristickiElementComponent component;

    public AnalitikaRezervacijeDecorator(TuristickiElementComponent component) 
    {
        this.component = component;
    }

    @Override
    public String getOpis() 
    {
        return component.getOpis();
    }

    @Override
    public int getBrojOsoba() 
    {
        return component.getBrojOsoba();
    }

    @Override
    public List<TuristickiElementComponent> getDjeca() 
    {
        return component.getDjeca();
    }

    @Override
    public void dodajDijete(TuristickiElementComponent element) 
    {
        component.dodajDijete(element);
    }

    @Override
    public void ukloniDijete(TuristickiElementComponent element) 
    {
        component.ukloniDijete(element);
    }

    @Override
    public boolean isLeaf() 
    {
        return component.isLeaf();
    }
}
