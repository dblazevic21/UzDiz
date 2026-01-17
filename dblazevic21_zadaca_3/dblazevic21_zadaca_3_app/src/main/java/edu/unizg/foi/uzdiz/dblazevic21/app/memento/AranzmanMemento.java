package edu.unizg.foi.uzdiz.dblazevic21.app.memento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class AranzmanMemento 
{
    public final int oznaka;
    public final String naziv;
    public final String program;
    public final LocalDate pocetniDatum;
    public final LocalDate zavrsniDatum;
    public final LocalTime vrijemeKretanja;
    public final LocalTime vrijemePovratka;
    public final float cijena;
    public final int minBrojPutnika;
    public final int maksBrojPutnika;
    public final int brojNocenja;
    public final float doplataSobe;
    public final String prijevoz;
    public final int brojDorucka;
    public final int brojRucka;
    public final int brojVecera;

    public final String statusKljuc;

    public final List<RezervacijaSnapshot> rezervacije;

    public AranzmanMemento(
            int oznaka, String naziv, String program,
            LocalDate pocetniDatum, LocalDate zavrsniDatum,
            LocalTime vrijemeKretanja, LocalTime vrijemePovratka,
            float cijena, int minBrojPutnika, int maksBrojPutnika,
            int brojNocenja, float doplataSobe, String prijevoz,
            int brojDorucka, int brojRucka, int brojVecera,
            String statusKljuc,
            List<RezervacijaSnapshot> rezervacije
    ) {
        this.oznaka = oznaka;
        this.naziv = naziv;
        this.program = program;
        this.pocetniDatum = pocetniDatum;
        this.zavrsniDatum = zavrsniDatum;
        this.vrijemeKretanja = vrijemeKretanja;
        this.vrijemePovratka = vrijemePovratka;
        this.cijena = cijena;
        this.minBrojPutnika = minBrojPutnika;
        this.maksBrojPutnika = maksBrojPutnika;
        this.brojNocenja = brojNocenja;
        this.doplataSobe = doplataSobe;
        this.prijevoz = prijevoz;
        this.brojDorucka = brojDorucka;
        this.brojRucka = brojRucka;
        this.brojVecera = brojVecera;
        this.statusKljuc = statusKljuc;
        this.rezervacije = List.copyOf(rezervacije);
    }

    public static final class RezervacijaSnapshot 
    {
        public final long redniBroj;
        public final String ime;
        public final String prezime;
        public final int oznakaAranzmana;
        public final LocalDateTime datumVrijeme;
        public final String statusKljuc;
        public final LocalDateTime otkazanoAt;

        public RezervacijaSnapshot(
                long redniBroj, String ime, String prezime,
                int oznakaAranzmana, LocalDateTime datumVrijeme,
                String statusKljuc, LocalDateTime otkazanoAt
        ) {
            this.redniBroj = redniBroj;
            this.ime = ime;
            this.prezime = prezime;
            this.oznakaAranzmana = oznakaAranzmana;
            this.datumVrijeme = datumVrijeme;
            this.statusKljuc = statusKljuc;
            this.otkazanoAt = otkazanoAt;
        }
    }
}
