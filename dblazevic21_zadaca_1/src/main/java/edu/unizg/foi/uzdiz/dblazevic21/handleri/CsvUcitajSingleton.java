package edu.unizg.foi.uzdiz.dblazevic21.handleri;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.ispis.IspisiGresku;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.AranzmaniDirector;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.util.CsvParser;
import edu.unizg.foi.uzdiz.dblazevic21.util.GramatikaIJezik;

public class CsvUcitajSingleton 
{

    private static volatile CsvUcitajSingleton INSTANCE;

    private final Map<Integer, Aranzmani> aranzmani = new HashMap<>();
    
    public static int brojGreske = 0;
    
    private final AranzmaniDirector aranzmaniDirector = new AranzmaniDirector();

    private CsvUcitajSingleton() {}

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

    public Map<Integer, Aranzmani> getAranzmani() {
        return aranzmani;
    }

    public void ucitajAranzmane(String putanja)
    {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8))
        {
            String linija = br.readLine();
            if (linija == null) return;
            
            int brojLinije = 1;

            while ((linija = br.readLine()) != null) 
            {
            	brojLinije++;
            	
            	String raw = linija;
            	
                linija = linija.trim();
                if (linija.isEmpty()) continue;

                List<String> stupci = CsvParser.parseCsvLiniju(linija);
                
                List<String> razloziGreske = new ArrayList<>();
                
                IspisiGresku.provjeriStupceAranzmana(stupci, razloziGreske);
                
                if (!razloziGreske.isEmpty())
                {
                    ++brojGreske;
                    IspisiGresku.ispisiGresku(brojGreske, brojLinije, raw, razloziGreske, putanja);
                    continue;
                }
                

                int oznaka = CsvParser.uInt(stupci.get(0));
                String naziv = GramatikaIJezik.makniNavodnike(stupci.get(1));
                String program = GramatikaIJezik.makniNavodnike(stupci.get(2));
                LocalDate pocetni = CsvParser.uDatum(stupci.get(3));
                LocalDate zavrsni = CsvParser.uDatum(stupci.get(4));
                LocalTime vrijemeKretanja = CsvParser.uVrijeme(stupci.get(5));
                LocalTime vrijemePovratka = CsvParser.uVrijeme(stupci.get(6));
                float cijena = CsvParser.uFloat(stupci.get(7));
                int min = CsvParser.uInt(stupci.get(8));
                int max = CsvParser.uInt(stupci.get(9));
                int nocenja = CsvParser.uInt(stupci.get(10));
                float doplata = CsvParser.uFloat(stupci.get(11));
                String prijevoz = GramatikaIJezik.makniNavodnike(stupci.get(12));
                int dor = CsvParser.uInt(stupci.get(13));
                int ruc = CsvParser.uInt(stupci.get(14));
                int vec = CsvParser.uInt(stupci.get(15));

                Aranzmani a = aranzmaniDirector.kreirajOsnovniAranzman(
                        oznaka,
                        naziv,
                        program,
                        pocetni,
                        zavrsni,
                        vrijemeKretanja,
                        vrijemePovratka,
                        cijena,
                        min,
                        max,
                        nocenja,
                        doplata,
                        prijevoz,
                        dor,
                        ruc,
                        vec
                );

                if (a != null) 
                {
                    aranzmani.put(oznaka, a);
                }
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke aranžmana: " + putanja + " (" + e.getMessage() + ")");
        }
    }

//	public AranzmaniBuilder kreirajAranzmanSBuilderom(int oznaka, String naziv, String program, LocalDate pocetni,
//			LocalDate zavrsni, LocalTime vrijemeKretanja, LocalTime vrijemePovratka, float cijena, int min, int max,
//			int nocenja, float doplata, String prijevoz, int dor, int ruc, int vec) 
//	{
//		AranzmaniBuilder builder = new AranzmaniBuilderConcrete()
//			    .setOznaka(oznaka)
//			    .setNaziv(naziv)
//			    .setProgram(program)
//			    .setPocetniDatum(pocetni)
//			    .setZavrsniDatum(zavrsni)
//			    .setVrijemeKretanja(vrijemeKretanja)
//			    .setVrijemePovratka(vrijemePovratka)
//			    .setCijena(cijena)
//			    .setMinBrojPutnika(min)
//			    .setMaksBrojPutnika(max)
//			    .setBrojNocenja(nocenja)
//			    .setDoplataSobe(doplata)
//			    .setPrijevoz(prijevoz)
//			    .setBrojDorucka(dor)
//			    .setBrojRucka(ruc)
//			    .setBrojVecera(vec);
//		return builder;
//	}
//
//	

    public void ucitajRezervacije(String putanja)
    {
        Rezervacije rez = Rezervacije.getInstance();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8)) 
        {
            String linija = br.readLine();
            if (linija == null) return;
            
            int brojLinije = 1;
            
            while ((linija = br.readLine()) != null) 
            {
				brojLinije++;
				
				String raw = linija;
				
                linija = linija.trim();
                if (linija.isEmpty()) continue;

                List<String> stupci = CsvParser.parseCsvLiniju(linija);
                
                List<String> razloziGreske = new ArrayList<>();
                
                IspisiGresku.provjeriStupceRezervacije(stupci, razloziGreske);
                
                if (!razloziGreske.isEmpty())
                {
                    ++brojGreske;
                    IspisiGresku.ispisiGresku(brojGreske, brojLinije, raw, razloziGreske, putanja);
                    continue;
                }

                try
                {
                	String ime = GramatikaIJezik.makniNavodnike(stupci.get(0));
                    String prezime = GramatikaIJezik.makniNavodnike(stupci.get(1));
                    int oznaka = CsvParser.uInt(stupci.get(2));
                    String dtRaw = GramatikaIJezik.makniNavodnike(stupci.get(3));
                    LocalDateTime dt = CsvParser.uDatumVrijeme(dtRaw);
                    
                    if (!aranzmani.containsKey(oznaka)) 
                    {
                        continue;
                    }

                    if (dt == null) 
                    {
                        rez.dodajRezervaciju(ime, prezime, oznaka, dtRaw);
                    } 
                    else 
                    {
                        rez.dodajRezervaciju(ime, prezime, oznaka, dt);
                    }
                }
                catch (Exception e) 
                {
                    System.out.println("Greška u retku " + brojLinije + ": " + e.getMessage() + ". Redak preskočen.");
                }
            }
            
            rez.azurirajStatuseRezervacija(aranzmani);
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke rezervacija: " + putanja + " (" + e.getMessage() + ")");
        }
    }

	
}
