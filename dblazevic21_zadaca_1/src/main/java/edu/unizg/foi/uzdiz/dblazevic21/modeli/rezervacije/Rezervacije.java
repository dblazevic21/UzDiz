package edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije;

import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

public class Rezervacije 
{
    private static volatile Rezervacije INSTANCE;
    
    private final List<Rezervacija> sveRezervacije = new ArrayList<>();
    private long counter = 0;

    private Rezervacije() {}

    public static Rezervacije getInstance() 
    {
        if (INSTANCE == null) 
        {
            synchronized (Rezervacije.class) 
            {
                if (INSTANCE == null) 
                {
                    INSTANCE = new Rezervacije();
                }
            }
        }
        return INSTANCE;
    }

    public void dodajRezervaciju(String ime, String prezime, int oznaka, LocalDateTime dt)
    {
    	sveRezervacije.add(new Rezervacija(++counter, ime, prezime, oznaka, dt));
    }

    public void dodajRezervaciju(String ime, String prezime, int oznaka, String dtRaw) 
    {
    	sveRezervacije.add(new Rezervacija(++counter, ime, prezime, oznaka, dtRaw));
    }
    
    public List<Rezervacija> getSveRezervacije() {
        return new ArrayList<>(sveRezervacije);
    }

    public List<Rezervacija> getZaAranzman(int oznaka) 
    {
        return sveRezervacije.stream()
                .filter(r -> r.getOznakaAranzmana() == oznaka)
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());
    }
    
    public void azurirajStatuseRezervacija(Map<Integer, Aranzmani> aranzmani) 
    {
        Map<Integer, List<Rezervacija>> rezervacijePoAranzmanu = new HashMap<>();

        for (Rezervacija rezervacija : sveRezervacije) 
        {
            rezervacijePoAranzmanu
                .computeIfAbsent(rezervacija.getOznakaAranzmana(), k -> new ArrayList<>())
                .add(rezervacija);
        }

        for (Map.Entry<Integer, List<Rezervacija>> entry : rezervacijePoAranzmanu.entrySet())
        {
            int oznakaAranzmana = entry.getKey();
            List<Rezervacija> rezervacijeZaAranzman = entry.getValue();
            Aranzmani aranzman = aranzmani.get(oznakaAranzmana);

            rezervacijeZaAranzman.sort(Comparator
                    .comparing(Rezervacija::getDatumVrijeme, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingLong(Rezervacija::getRedniBroj));

            int brojRezervacija = rezervacijeZaAranzman.size();
            int minBrojPutnika = aranzman.getMinBrojPutnika();
            int maksBrojPutnika = aranzman.getMaksBrojPutnika();

            if (brojRezervacija < minBrojPutnika) 
            {
                for (Rezervacija rezervacija : rezervacijeZaAranzman) 
                {
                    rezervacija.setStatus(StatusRezervacije.PRIMLJENA);
                }
            } 
            else if (brojRezervacija <= maksBrojPutnika) 
            {
                for (Rezervacija rezervacija : rezervacijeZaAranzman) 
                {
                    rezervacija.setStatus(StatusRezervacije.AKTIVNA);
                }
            } 
            else 
            {
                int aktivniCount = 0;
                for (Rezervacija rezervacija : rezervacijeZaAranzman) 
                {
                    if (aktivniCount < maksBrojPutnika) 
                    {
                        rezervacija.setStatus(StatusRezervacije.AKTIVNA);
                        aktivniCount++;
                    } 
                    else 
                    {
                        rezervacija.setStatus(StatusRezervacije.NA_CEKANJU);
                    }
                }
            }
        }
    }



    public boolean otkaziRezervaciju(int oznakaAranzmana, String ime, String prezime, LocalDateTime when) 
    {
        for (int i = sveRezervacije.size() - 1; i >= 0; i--) {
            Rezervacija r = sveRezervacije.get(i);
            if (r.getOznakaAranzmana() == oznakaAranzmana
                    && equalsIgnorirajCase(r.getIme(), ime)
                    && equalsIgnorirajCase(r.getPrezime(), prezime)
                    && r.getStatus() != StatusRezervacije.OTKAZANA) {
                r.setStatus(StatusRezervacije.OTKAZANA);
                r.setOtkazanoAt(when != null ? when : LocalDateTime.now());
                return true;
            }
        }
        return false;
    }

    private boolean equalsIgnorirajCase(String a, String b) 
    {
        if (a == null) return b == null;
        
        return a.equalsIgnoreCase(b == null ? "" : b);
    }
}
