package edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije;

import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Rezervacije {

    private static volatile Rezervacije INSTANCE;

    private final List<Rezervacija> rezervacije = new ArrayList<>();
    private final DateTimeFormatter hrvatskiFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");

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

    public void dodajRezervaciju(String ime, String prezime, int oznakaAranzmana, String datumVrijeme) 
    {
        if (datumVrijeme == null || datumVrijeme.isEmpty()) 
        {
            return;
        }
        try 
        {
            LocalDateTime datum = LocalDateTime.parse(datumVrijeme, hrvatskiFormat);
            rezervacije.add(new Rezervacija(ime, prezime, oznakaAranzmana, datum));
        } 
        catch (Exception e) 
        {
            System.out.println("Greška pri dodavanju rezervacije: " + datumVrijeme);
        }
    }


    public void obradiRezervacije(int minPutnika, int maxPutnika) 
    {
        Map<Integer, Integer> brojPrijava = new HashMap<>();

        for (Rezervacija rezervacija : rezervacije) 
        {
            int oznaka = rezervacija.getOznakaAranzmana();
            brojPrijava.put(oznaka, brojPrijava.getOrDefault(oznaka, 0) + 1);

            int broj = brojPrijava.get(oznaka);

            if (broj < minPutnika) 
            {
                rezervacija.setStatus(StatusRezervacije.PRIMLJENA);
            } 
            else if (broj <= maxPutnika) 
            {
                rezervacija.setStatus(StatusRezervacije.AKTIVNA);
            } 
            else 
            {
                rezervacija.setStatus(StatusRezervacije.NA_CEKANJU);
            }
        }
    }

//    public void ispisiRezervacije() 
//    {
//        for (Rezervacija rezervacija : rezervacije) 
//        {
//            System.out.println(rezervacija);
//        }
//    }

    private static class Rezervacija 
    {
        private final String ime;
        private final String prezime;
        private final int oznakaAranzmana;
        private final LocalDateTime datumVrijeme;
        private StatusRezervacije status;

        public Rezervacija(String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme) {
            this.ime = ime;
            this.prezime = prezime;
            this.oznakaAranzmana = oznakaAranzmana;
            this.datumVrijeme = datumVrijeme;
        }

        public int getOznakaAranzmana() {
            return oznakaAranzmana;
        }

        public void setStatus(StatusRezervacije status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return String.format("Rezervacija: %s %s, Aranžman: %d, Datum: %s, Status: %s",
                    ime, prezime, oznakaAranzmana, datumVrijeme.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")), status);
        }
    }
}
