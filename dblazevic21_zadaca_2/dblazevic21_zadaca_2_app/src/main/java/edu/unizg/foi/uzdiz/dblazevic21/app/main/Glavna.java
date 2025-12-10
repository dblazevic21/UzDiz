package edu.unizg.foi.uzdiz.dblazevic21.app.main;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.unizg.foi.uzdiz.dblazevic21.app.komande.KomandaConcreteCreator;
import edu.unizg.foi.uzdiz.dblazevic21.app.komande.KomandaCreator;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.turistickaAgencija.TuristickaAgencija;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.Facade;

public class Glavna 
{
    public static void main(String[] args) 
    {
        if (args.length < 2) 
        {
            System.out.println("Potrebno je navesti putanje do CSV datoteka.");
            return;
        }

        String putanjaAranzmani = args[0];
        String putanjaRezervacije = args[1];

        Facade facade = Facade.getInstance();

        List<Object[]> aranzmanPodaci = facade.ucitajAranzmane(putanjaAranzmani);
        List<Object[]> rezervacijaPodaci = facade.ucitajRezervacije(putanjaRezervacije);

        TuristickaAgencija ta = TuristickaAgencija.getInstance();
        ta.ucitajAranzmane(aranzmanPodaci);
        ta.ucitajRezervacije(rezervacijaPodaci);

        Map<Integer, Aranzmani> aranzmani = ta.getAranzmani();
        KomandaCreator komanda = new KomandaConcreteCreator(aranzmani);

        System.out.println("Učitano " + aranzmani.size() + " aranžmana.");
        System.out.println("Broj grešaka: " + facade.getBrojGresaka());

        try (Scanner scanner = new Scanner(System.in)) 
        {
            while (true) 
            {
                System.out.print("> ");
                String unos = scanner.nextLine();

                if (unos == null || unos.trim().equalsIgnoreCase("Q")) 
                {
                    break;
                }

                komanda.izvrsiKomandu(unos);
            }
        }
    }
}
