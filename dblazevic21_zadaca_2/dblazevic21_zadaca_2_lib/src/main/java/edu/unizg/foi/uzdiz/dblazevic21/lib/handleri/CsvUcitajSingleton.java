package edu.unizg.foi.uzdiz.dblazevic21.lib.handleri;

import edu.unizg.foi.uzdiz.dblazevic21.lib.csv.AranzmanCsvRecord;
import edu.unizg.foi.uzdiz.dblazevic21.lib.csv.RezervacijaCsvRecord;
import edu.unizg.foi.uzdiz.dblazevic21.lib.ispis.IspisiGresku;
import edu.unizg.foi.uzdiz.dblazevic21.lib.util.CsvParser;
import edu.unizg.foi.uzdiz.dblazevic21.lib.util.GramatikaIJezik;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUcitajSingleton 
{

    private static volatile CsvUcitajSingleton INSTANCE;

    public static int brojGreske = 0;

    private CsvUcitajSingleton() { }

    public static CsvUcitajSingleton getInstance() 
    {
        if (INSTANCE == null) 
        {
            synchronized (CsvUcitajSingleton.class) 
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new CsvUcitajSingleton();
                }
            }
        }
        return INSTANCE;
    }

    
    public List<AranzmanCsvRecord> ucitajAranzmaneKaoRecorde(String putanja) 
    {
        List<AranzmanCsvRecord> rezultat = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8)) 
        {
            String linija = br.readLine();
            if (linija == null) 
            {
                return rezultat;
            }

            int brojLinije = 1;

            while ((linija = br.readLine()) != null) 
            {
                brojLinije++;

                String raw = linija;
                linija = linija.trim();
                if (linija.isEmpty()) {
                    continue;
                }

                List<String> stupci = CsvParser.parseCsvLiniju(linija);
                List<String> razloziGreske = new ArrayList<>();

                IspisiGresku.provjeriStupceAranzmana(stupci, razloziGreske);

                if (!razloziGreske.isEmpty()) 
                {
                    ++brojGreske;
                    IspisiGresku.ispisiGresku(brojGreske, brojLinije, raw, razloziGreske, putanja);
                    continue;
                }

                while (stupci.size() < 16) {
                    stupci.add("");
                }

                String oznaka = stupci.get(0);
                String naziv = GramatikaIJezik.makniNavodnike(stupci.get(1));
                String program = GramatikaIJezik.makniNavodnike(stupci.get(2));
                String pocetniDatum = stupci.get(3);
                String zavrsniDatum = stupci.get(4);
                String vrijemeKretanja = stupci.get(5);
                String vrijemePovratka = stupci.get(6);
                String cijena = stupci.get(7);
                String minBrojPutnika = stupci.get(8);
                String maksBrojPutnika = stupci.get(9);
                String brojNocenja = stupci.get(10);
                String doplataSobe = stupci.get(11);
                String prijevoz = GramatikaIJezik.makniNavodnike(stupci.get(12));
                String brojDorucka = stupci.get(13);
                String brojRucka = stupci.get(14);
                String brojVecera = stupci.get(15);

                AranzmanCsvRecord record = new AranzmanCsvRecord(
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

                rezultat.add(record);
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke aranžmana: " + putanja + " (" + e.getMessage() + ")");
        }

        return rezultat;
    }

   
    public List<RezervacijaCsvRecord> ucitajRezervacijeKaoRecorde(String putanja) 
    {
        List<RezervacijaCsvRecord> rezultat = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8)) 
        {
            String linija = br.readLine();
            if (linija == null) 
            {
                return rezultat;
            }

            int brojLinije = 1;

            while ((linija = br.readLine()) != null) {
                brojLinije++;

                String raw = linija;
                linija = linija.trim();
                if (linija.isEmpty()) 
                {
                    continue;
                }

                List<String> stupci = CsvParser.parseCsvLiniju(linija);
                List<String> razloziGreske = new ArrayList<>();

                IspisiGresku.provjeriStupceRezervacije(stupci, razloziGreske);

                if (!razloziGreske.isEmpty()) 
                {
                    ++brojGreske;
                    IspisiGresku.ispisiGresku(brojGreske, brojLinije, raw, razloziGreske, putanja);
                    continue;
                }

                while (stupci.size() < 4) 
                {
                    stupci.add("");
                }

                String ime = GramatikaIJezik.makniNavodnike(stupci.get(0));
                String prezime = GramatikaIJezik.makniNavodnike(stupci.get(1));
                String oznakaAranzmana = stupci.get(2);
                String datumVrijemeRaw = GramatikaIJezik.makniNavodnike(stupci.get(3));

                RezervacijaCsvRecord record = new RezervacijaCsvRecord(
                        ime,
                        prezime,
                        oznakaAranzmana,
                        datumVrijemeRaw
                );

                rezultat.add(record);
            }
        }
        catch (IOException e)
        {
            System.out.println("Greška pri čitanju datoteke rezervacija: " + putanja + " (" + e.getMessage() + ")");
        }

        return rezultat;
    }
}
