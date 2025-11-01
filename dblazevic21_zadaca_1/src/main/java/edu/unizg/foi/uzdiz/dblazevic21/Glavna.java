package edu.unizg.foi.uzdiz.dblazevic21;

import edu.unizg.foi.uzdiz.dblazevic21.handleri.CsvUcitajSingleton;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Glavna
{
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Niste unijeli argumente!");
            System.out.println("Primjer:");
            System.out.println("java -jar dblazevic21_zadaca_1.jar --ta DZ_1_aranzmani.csv --rta DZ_1_rezervacije.csv [--rta DZ_1_rezervacije_1.csv]");
            return;
        }

        Map<String, List<String>> argumenti = new HashMap<>();

        provjeriKomandu(args, argumenti);

        List<String> datotekeAranzmani = argumenti.get("--ta");
        List<String> datotekeRezervacije = argumenti.get("--rta");

        System.out.println("\nPokretanje programa...");

        CsvUcitajSingleton csvUcitaj = CsvUcitajSingleton.getInstance();

        ucitajCsvAranzmana(datotekeAranzmani, csvUcitaj);

        TuristickaAgencija.getInstance().setAranzmani(csvUcitaj.getAranzmani());

        ucitajCsvRezervacija(datotekeRezervacije, csvUcitaj);

        pokreniInteraktivniNacin();
    }

    public static void provjeriKomandu(String[] args, Map<String, List<String>> argumenti)
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

    public static void ucitajCsvRezervacija(List<String> datotekeRezervacije, CsvUcitajSingleton csvUcitaj)
    {
        if (datotekeRezervacije != null)
        {
            for (String dat : datotekeRezervacije)
            {
                provjeriDatoteku(dat);
                System.out.println("\nUčitane rezervacije iz " + dat);
                csvUcitaj.ucitajRezervacije(dat);
            }
        }
        else
        {
            System.out.println("Niste naveli nijednu datoteku s rezervacijama (--rta)!");
        }
    }

    public static void ucitajCsvAranzmana(List<String> datotekeAranzmani, CsvUcitajSingleton csvUcitaj)
    {
        if (datotekeAranzmani != null)
        {
            for (String dat : datotekeAranzmani)
            {
                provjeriDatoteku(dat);
                System.out.println("\nUčitani aranžmani iz " + dat);
                csvUcitaj.ucitajAranzmane(dat);
            }
        }
        else
        {
            System.out.println("Niste naveli datoteku aranžmana (--ta)!");
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

    private static void pokreniInteraktivniNacin()
    {
        System.out.println("\nSustav je spreman za interaktivni način rada.");
        System.out.println("Unesite komande (za izlaz upišite 'Q').");

        try (Scanner scanner = new Scanner(System.in))
        {
            while (true)
            {
                System.out.print("> ");
                String komanda = scanner.nextLine().trim();

                if ("Q".equalsIgnoreCase(komanda))
                {
                    System.out.println("Izlaz iz programa...");
                    break;
                }

                TuristickaAgencija.getInstance().izvrsiKomandu(komanda);
            }
        }
    }
}
