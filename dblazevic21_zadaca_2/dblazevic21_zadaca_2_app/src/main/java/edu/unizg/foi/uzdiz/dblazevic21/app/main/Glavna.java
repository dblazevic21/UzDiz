package edu.unizg.foi.uzdiz.dblazevic21.app.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
        if (args.length == 0)
        {
            System.out.println("Niste unijeli argumente!");
            System.out.println("Primjer:");
            System.out.println("java -jar dblazevic21_zadaca_2.jar --ta DZ_2_aranzmani.csv --rta DZ_2_rezervacije.csv");
            return;
        }

        Map<String, List<String>> argumenti = new HashMap<>();
        provjeriKomandu(args, argumenti);

        List<String> datotekeAranzmani = argumenti.get("--ta");
        List<String> datotekeRezervacije = argumenti.get("--rta");

        System.out.println("\nPokretanje programa...");

        Facade facade = Facade.getInstance();
        TuristickaAgencija ta = TuristickaAgencija.getInstance();

        ucitajAranzmane(datotekeAranzmani, facade, ta);
        ucitajRezervacije(datotekeRezervacije, facade, ta);

        Map<Integer, Aranzmani> aranzmani = ta.getAranzmani();
        KomandaCreator komandaCreator = new KomandaConcreteCreator(aranzmani);

        System.out.println("Učitano " + aranzmani.size() + " aranžmana.");
        System.out.println("Broj grešaka: " + facade.getBrojGresaka());

        pokreniInteraktivniNacin(komandaCreator);
    }

    private static void provjeriKomandu(String[] args, Map<String, List<String>> argumenti)
    {
        String currentFlag = null;

        for (String arg : args)
        {
            if (arg.startsWith("--"))
            {
                currentFlag = arg;
                argumenti.putIfAbsent(currentFlag, new ArrayList<>());
            }
            else if (currentFlag != null)
            {
                argumenti.get(currentFlag).add(arg);
            }
        }
    }

    private static void ucitajAranzmane(List<String> datoteke, Facade facade, TuristickaAgencija ta)
    {
        if (datoteke != null)
        {
            for (String dat : datoteke)
            {
                provjeriDatoteku(dat);
                System.out.println("\nUčitani aranžmani iz " + dat);
                List<List<String>> podaci = facade.ucitajAranzmane(dat);
                ta.ucitajAranzmane(podaci);
            }
        }
        else
        {
            System.out.println("Niste naveli datoteku aranžmana (--ta)!");
        }
    }

    private static void ucitajRezervacije(List<String> datoteke, Facade facade, TuristickaAgencija ta)
    {
        if (datoteke != null)
        {
            for (String dat : datoteke)
            {
                provjeriDatoteku(dat);
                System.out.println("\nUčitane rezervacije iz " + dat);
                List<List<String>> podaci = facade.ucitajRezervacije(dat);
                ta.ucitajRezervacije(podaci);
            }
        }
        else
        {
            System.out.println("Niste naveli nijednu datoteku s rezervacijama (--rta)!");
        }
    }

    private static void provjeriDatoteku(String naziv)
    {
        File f = new File(naziv);
        if (!f.exists())
        {
            System.out.println("Datoteka \"" + naziv + "\" ne postoji u trenutnom direktoriju!");
        }
    }

    private static void pokreniInteraktivniNacin(KomandaCreator komandaCreator)
    {
        System.out.println("\nSustav je spreman za interaktivni način rada.");
        System.out.println("Unesite komande (za izlaz upišite 'Q').");

        try (Scanner scanner = new Scanner(System.in))
        {
            while (true)
            {
                System.out.print("> ");
                String unos = scanner.nextLine();

                if (unos == null || unos.trim().equalsIgnoreCase("Q"))
                {
                    System.out.println("Izlaz iz programa...");
                    break;
                }

                komandaCreator.izvrsiKomandu(unos);
            }
        }
    }
}
