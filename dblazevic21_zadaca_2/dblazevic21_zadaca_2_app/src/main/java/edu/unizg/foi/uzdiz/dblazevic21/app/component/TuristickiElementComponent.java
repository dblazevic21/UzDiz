package edu.unizg.foi.uzdiz.dblazevic21.app.component;

import java.util.List;

public interface TuristickiElementComponent 
{
    String getOpis();
    int getBrojOsoba();
    List<TuristickiElementComponent> getDjeca();
    void dodajDijete(TuristickiElementComponent element);
    void ukloniDijete(TuristickiElementComponent element);
    boolean isLeaf();
}
