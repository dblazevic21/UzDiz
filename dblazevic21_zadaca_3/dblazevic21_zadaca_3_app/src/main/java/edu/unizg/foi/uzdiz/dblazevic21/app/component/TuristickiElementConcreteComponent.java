package edu.unizg.foi.uzdiz.dblazevic21.app.component;

import java.util.ArrayList;
import java.util.List;

public class TuristickiElementConcreteComponent implements TuristickiElementComponent
{
    private final String opis;
    private final List<TuristickiElementComponent> djeca = new ArrayList<>();

    public TuristickiElementConcreteComponent(String opis)
    {
        this.opis = opis;
    }

    @Override
    public String getOpis()
    {
        return opis;
    }

    @Override
    public int getBrojOsoba()
    {
        return 0;
    }

    @Override
    public List<TuristickiElementComponent> getDjeca()
    {
        return djeca;
    }

    @Override
    public void dodajDijete(TuristickiElementComponent element)
    {
        djeca.add(element);
    }

    @Override
    public void ukloniDijete(TuristickiElementComponent element)
    {
        djeca.remove(element);
    }

    @Override
    public boolean isLeaf()
    {
        return djeca.isEmpty();
    }
    
    @Override
    public void accept(TuristickiVisitor visitor)
    {
    	
    }
}
