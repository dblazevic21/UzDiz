package edu.unizg.foi.uzdiz.dblazevic21.lib.facade;

import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.lib.handleri.CsvUcitajSingleton;
import edu.unizg.foi.uzdiz.dblazevic21
import edu.unizg.foi.uzdiz.dblazevic21
import edu.unizg.foi.uzdiz.dblazevic21

public class TuristickaFacade {

    private static TuristickaFacade instance;

    private final CsvUcitajSingleton csvLoader;
    private final TuristickaAgencija agencija;

    private TuristickaFacade() {
        this.csvLoader = CsvUcitajSingleton.getInstance();
        this.agencija = TuristickaAgencija.getInstance();
    }

    public static TuristickaFacade getInstance() {
        if (instance == null) {
            instance = new TuristickaFacade();
        }
        return instance;
    }

    // \- High level operations

    public void ucitajAranzmane(List<String> datotekeAranzmana) {
        if (datotekeAranzmana == null) {
            System.out.println("Niste naveli datoteku aranžmana \`--ta\`!");
            return;
        }

        for (String dat : datotekeAranzmana) {
            System.out.println("\nUčitani aranžmani iz " + dat);
            csvLoader.ucitajAranzmane(dat);
        }

        agencija.setAranzmani(csvLoader.getAranzmani());
    }

    public void ucitajRezervacije(List<String> datotekeRezervacije) {
        if (datotekeRezervacije == null) {
            System.out.println("Niste naveli nijednu datoteku s rezervacijama \`--rta\`!");
            return;
        }

        for (String dat : datotekeRezervacije) {
            System.out.println("\nUčitane rezervacije iz " + dat);
            csvLoader.ucitajRezervacije(dat);
        }
    }

    public void izvrsiKomandu(String komanda) {
        agencija.izvrsiKomandu(komanda);
    }

    // \- Accessors

    public Map<Integer, Aranzmani> getAranzmani() {
        return csvLoader.getAranzmani();
    }

    public Rezervacije getRezervacije() {
        return Rezervacije.getInstance();
    }
}
