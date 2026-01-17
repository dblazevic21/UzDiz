package edu.unizg.foi.uzdiz.dblazevic21.app.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.unizg.foi.uzdiz.dblazevic21.app.csv.CsvZaAranzmane;
import edu.unizg.foi.uzdiz.dblazevic21.app.csv.CsvZaRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.komande.KomandaConcreteCreator;
import edu.unizg.foi.uzdiz.dblazevic21.app.komande.KomandaCreator;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.strategy.JdrStrategija;
import edu.unizg.foi.uzdiz.dblazevic21.app.strategy.NullStrategija;
import edu.unizg.foi.uzdiz.dblazevic21.app.strategy.VdrStrategija;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.TuristickaFacade;

public class Glavna
{
    public static void main(String[] args)
    {	
        Map<String, List<String>> argumenti = new HashMap<>();
        provjeriKomandu(args, argumenti);

        List<String> datotekeAranzmani = argumenti.get("--ta");
        List<String> datotekeRezervacije = argumenti.get("--rta");

        boolean imaJdr = argumenti.containsKey("--jdr");
        boolean imaVdr = argumenti.containsKey("--vdr");
        
        if (imaJdr && imaVdr)
        {
        	System.out.println("Greška: opcije --jdr i --vdr se ne smiju koristiti istovremeno.");
            return;
        }
        
        pokretanjeStrategije(imaJdr, imaVdr);
        
        System.out.println("\nPokretanje programa...");

        TuristickaFacade facade = TuristickaFacade.getInstance();
        Map<Integer, Aranzmani> aranzmani = new HashMap<>();

        pripremiPodatkeIzCsv(datotekeAranzmani, datotekeRezervacije, facade, aranzmani);

        Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
        
        AranzmanCaretaker caretaker = new AranzmanCaretaker();
        
        KomandaCreator komandaCreator = new KomandaConcreteCreator(aranzmani, caretaker);

        System.out.println("\nUčitano " + aranzmani.size() + " aranžmana.");
        System.out.println("Broj grešaka: " + facade.getBrojGresaka());

        pokreniInteraktivniNacin(komandaCreator);
    }

	public static void pokretanjeStrategije(boolean imaJdr, boolean imaVdr) 
	{
		Rezervacije rez = Rezervacije.getInstance();
        if (imaJdr)
        {
        	rez.setStrategija(new JdrStrategija());
        }
        else if (imaVdr)
        {
        	rez.setStrategija(new VdrStrategija());
        }
        else
        {
        	rez.setStrategija(new NullStrategija());
        }
	}

	public static void pripremiPodatkeIzCsv(List<String> datotekeAranzmani, List<String> datotekeRezervacije,
			TuristickaFacade facade, Map<Integer, Aranzmani> aranzmani) {
		List<List<String>> ucitaniAranzmani = new ArrayList<>();
        if (datotekeAranzmani != null)
        {
            for (String dat : datotekeAranzmani)
            {
                provjeriDatoteku(dat);
                System.out.println("\nUčitani aranžmani iz " + dat);
                ucitaniAranzmani.addAll(facade.ucitajAranzmane(dat));
            }
        }
        else
        {
            System.out.println("Niste naveli datoteku aranžmana (--ta)!");
        }

        CsvZaAranzmane csvA = new CsvZaAranzmane();
        List<List<String>> validAranzmani = csvA.provjeri(ucitaniAranzmani);
        csvA.dodaj(validAranzmani, aranzmani);

        List<List<String>> ucitaneRezervacije = new ArrayList<>();
        if (datotekeRezervacije != null)
        {
            facade.resetirajRezervacije(); 

            ucitaneRezervacije = facade.ucitajSveRezervacije(datotekeRezervacije);

            for (String dat : datotekeRezervacije)
            {
                provjeriDatoteku(dat);
                System.out.println("\nUčitane rezervacije iz " + dat);
            }

            CsvZaRezervacije csvR = new CsvZaRezervacije();
            List<List<String>> validRezervacije = csvR.provjeri(ucitaneRezervacije);
            csvR.dodaj(validRezervacije, aranzmani);
        }
        else
        {
            System.out.println("Niste naveli nijednu datoteku s rezervacijama (--rta)!");
        }
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
