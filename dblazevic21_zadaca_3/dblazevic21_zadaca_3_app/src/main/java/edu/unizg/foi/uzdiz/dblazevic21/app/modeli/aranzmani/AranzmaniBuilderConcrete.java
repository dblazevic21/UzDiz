package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani;

import java.time.LocalDate;
import java.time.LocalTime;

public class AranzmaniBuilderConcrete implements AranzmaniBuilder 
{

    protected Aranzmani aranzman;

    public AranzmaniBuilderConcrete()
    {
        this.aranzman = new Aranzmani();
    }

    @Override
    public AranzmaniBuilder kreirajAranzmane(
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
        this.aranzman = new Aranzmani();
        this.aranzman.setOznaka(oznaka);
        this.aranzman.setNaziv(naziv);
        this.aranzman.setProgram(program);
        this.aranzman.setPocetniDatum(pocetniDatum);
        this.aranzman.setZavrsniDatum(zavrsniDatum);
        this.aranzman.setVrijemeKretanja(vrijemeKretanja);
        this.aranzman.setVrijemePovratka(vrijemePovratka);
        this.aranzman.setCijena(cijena);
        this.aranzman.setMinBrojPutnika(minBrojPutnika);
        this.aranzman.setMaksBrojPutnika(maksBrojPutnika);
        this.aranzman.setBrojNocenja(brojNocenja);
        this.aranzman.setDoplataSobe(doplataSobe);
        this.aranzman.setPrijevoz(prijevoz);
        this.aranzman.setBrojDorucka(brojDorucka);
        this.aranzman.setBrojRucka(brojRucka);
        this.aranzman.setBrojVecera(brojVecera);
        return this;
    }

    @Override
    public AranzmaniBuilder setOznaka(int oznaka) 
    {
        this.aranzman.setOznaka(oznaka);
        return this;
    }

    @Override
    public AranzmaniBuilder setNaziv(String naziv) 
    {
        this.aranzman.setNaziv(naziv);
        return this;
    }

    @Override
    public AranzmaniBuilder setBrojDorucka(int brojDorucka) 
    {
        this.aranzman.setBrojDorucka(brojDorucka);
        return this;
    }

    @Override
    public AranzmaniBuilder setBrojNocenja(int brojNocenja)
    {
        this.aranzman.setBrojNocenja(brojNocenja);
        return this;
    }

    @Override
    public AranzmaniBuilder setBrojRucka(int brojRucka)
    {
        this.aranzman.setBrojRucka(brojRucka);
        return this;
    }

    @Override
    public AranzmaniBuilder setBrojVecera(int brojVecera) 
    {
        this.aranzman.setBrojVecera(brojVecera);
        return this;
    }

    @Override
    public AranzmaniBuilder setCijena(float cijena) 
    {
        this.aranzman.setCijena(cijena);
        return this;
    }

    @Override
    public AranzmaniBuilder setDoplataSobe(float doplataSobe)
    {
        this.aranzman.setDoplataSobe(doplataSobe);
        return this;
    }

    @Override
    public AranzmaniBuilder setMaksBrojPutnika(int maksBrojPutnika)
    {
        this.aranzman.setMaksBrojPutnika(maksBrojPutnika);
        return this;
    }

    @Override
    public AranzmaniBuilder setMinBrojPutnika(int minBrojPutnika) 
    {
        this.aranzman.setMinBrojPutnika(minBrojPutnika);
        return this;
    }

    @Override
    public AranzmaniBuilder setPocetniDatum(LocalDate pocetniDatum) 
    {
        this.aranzman.setPocetniDatum(pocetniDatum);
        return this;
    }

    @Override
    public AranzmaniBuilder setPrijevoz(String prijevoz) 
    {
        this.aranzman.setPrijevoz(prijevoz);
        return this;
    }

    @Override
    public AranzmaniBuilder setProgram(String program)
    {
        this.aranzman.setProgram(program);
        return this;
    }

    @Override
    public AranzmaniBuilder setZavrsniDatum(LocalDate zavrsniDatum)
    {
        this.aranzman.setZavrsniDatum(zavrsniDatum);
        return this;
    }

    @Override
    public AranzmaniBuilder setVrijemeKretanja(LocalTime vrijemeKretanja) 
    {
        this.aranzman.setVrijemeKretanja(vrijemeKretanja);
        return this;
    }

    @Override
    public AranzmaniBuilder setVrijemePovratka(LocalTime vrijemePovratka) 
    {
        this.aranzman.setVrijemePovratka(vrijemePovratka);
        return this;
    }

    @Override
    public Aranzmani getAranzman() 
    {
        return this.aranzman;
    }
}
