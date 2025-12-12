package edu.unizg.foi.uzdiz.dblazevic21.app.composite;

import java.util.List;

public interface TuristickiElement 
{
    String getOpis();
    int getBrojOsoba();
    List<TuristickiElement> getDjeca();
    void dodajDijete(TuristickiElement element);
    void ukloniDijete(TuristickiElement element);
    boolean isLeaf();
}
