package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani;

import java.time.LocalDate;
import java.time.LocalTime;

public class AranzmaniDirector
{

    private AranzmaniBuilder builder;

    public AranzmaniDirector() 
    {
        this.builder = new AranzmaniBuilderConcrete();
    }

    public AranzmaniDirector(AranzmaniBuilder builder)
    {
        this.builder = builder;
    }

    public void setBuilder(AranzmaniBuilder builder) 
    {
        this.builder = builder;
    }

    public Aranzmani kreirajOsnovniAranzman(
            int oznaka,
            String naziv,
            String program,
            LocalDate pocetniDatum,
            LocalDate zavrsniDatum,
            LocalTime vrijemeKretanja,
            LocalTime vrijemePovratka,
            float cijena,
            int minBrojPutnika,
            int maksBrojPutnika,
            int brojNocenja,
            float doplataSobe,
            String prijevoz,
            int brojDorucka,
            int brojRucka,
            int brojVecera
    ) {
        builder.kreirajAranzmane(
                oznaka,
                naziv,
                program,
                pocetniDatum,
                zavrsniDatum,
                vrijemeKretanja,
                vrijemePovratka,
                cijena,
                minBrojPutnika,
                maksBrojPutnika,
                brojNocenja,
                doplataSobe,
                prijevoz,
                brojDorucka,
                brojRucka,
                brojVecera
        );
        return builder.getAranzman();
    }

    public Aranzmani kreirajAranzmanBezObroka(
            int oznaka,
            String naziv,
            String program,
            LocalDate pocetniDatum,
            LocalDate zavrsniDatum,
            LocalTime vrijemeKretanja,
            LocalTime vrijemePovratka,
            float cijena,
            int minBrojPutnika,
            int maksBrojPutnika,
            int brojNocenja,
            float doplataSobe,
            String prijevoz
    ) {
        return kreirajOsnovniAranzman(
                oznaka,
                naziv,
                program,
                pocetniDatum,
                zavrsniDatum,
                vrijemeKretanja,
                vrijemePovratka,
                cijena,
                minBrojPutnika,
                maksBrojPutnika,
                brojNocenja,
                doplataSobe,
                prijevoz,
                0,
                0,
                0
        );
    }
}
