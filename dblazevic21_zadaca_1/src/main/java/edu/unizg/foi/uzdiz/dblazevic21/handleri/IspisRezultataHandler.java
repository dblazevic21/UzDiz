package edu.unizg.foi.uzdiz.dblazevic21.handleri;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class IspisRezultataHandler {

    private static volatile IspisRezultataHandler INSTANCE;

    public static IspisRezultataHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (IspisRezultataHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IspisRezultataHandler();
                }
            }
        }
        return INSTANCE;
    }

    private final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final int[] W_LIST = {7, 35, 14, 14, 12, 16, 10, 18, 18};
    private static final int W_D_KEY = 28;
    private static final int W_D_VAL = 90;

    private IspisRezultataHandler() {}


    public void printItakList(List<Aranzmani> lista) {
        Printer<List<Aranzmani>> p = Factory.create(ViewType.ITAK_LIST);
        p.print(lista);
    }

    public void printItapPriceList(List<Aranzmani> lista) {
        Printer<List<Aranzmani>> p = Factory.create(ViewType.ITAP_PRICE_LIST);
        p.print(lista);
    }

    public void printItapDetail(Aranzmani a) {
        if (a == null) {
            System.out.println("Aranžman nije pronađen.");
            return;
        }
        Printer<Aranzmani> p = Factory.create(ViewType.ITAP_DETAIL);
        p.print(a);
    }


    private enum ViewType 
    { 
    	ITAK_LIST, ITAP_PRICE_LIST, ITAP_DETAIL 
	}

    private interface Printer<T> {
        void print(T data);
    }

    private static class Factory {
        static Printer create(ViewType view) {
            switch (view) {
                case ITAK_LIST:        return new ItakListPrinter();
                case ITAP_PRICE_LIST:  return new ItapPriceListPrinter();
                case ITAP_DETAIL:      return new ItapDetailPrinter();
                default: throw new IllegalArgumentException("Nepoznat view: " + view);
            }
        }
    }

    private static class ItakListPrinter implements Printer<List<Aranzmani>> {
        @Override
        public void print(List<Aranzmani> lista) {
            IspisRezultataHandler h = IspisRezultataHandler.getInstance();
            h.printSepMulti(W_LIST);
            h.printRowMulti(W_LIST,
                    "Oznaka", "Naziv", "Početni datum", "Završni datum",
                    "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika");
            h.printSepMulti(W_LIST);

            for (Aranzmani a : lista) {
                if (a == null) continue;
                h.printRowMulti(W_LIST,
                        h.val(a.getOznaka()),
                        h.crop(a.getNaziv(), W_LIST[1]),
                        h.fmtDatum(a.getPocetniDatum()),
                        h.fmtDatum(a.getZavrsniDatum()),
                        h.fmtVrijeme(a.getVrijemeKretanja()),
                        h.fmtVrijeme(a.getVrijemePovratka()),
                        h.fmtCijena(a.getCijena()),
                        h.val(a.getMinBrojPutnika()),
                        h.val(a.getMaksBrojPutnika()));
            }
            h.printSepMulti(W_LIST);
        }
    }

    private static class ItapPriceListPrinter implements Printer<List<Aranzmani>> {
        @Override
        public void print(List<Aranzmani> lista) {
            IspisRezultataHandler h = IspisRezultataHandler.getInstance();
            h.printSepMulti(W_LIST);
            h.printRowMulti(W_LIST,
                    "Oznaka", "Naziv", "Početni datum", "Završni datum",
                    "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika");
            h.printSepMulti(W_LIST);

            for (Aranzmani a : lista) {
                if (a == null) continue;
                h.printRowMulti(W_LIST,
                        h.val(a.getOznaka()),
                        h.crop(a.getNaziv(), W_LIST[1]),
                        h.fmtDatum(a.getPocetniDatum()),
                        h.fmtDatum(a.getZavrsniDatum()),
                        h.fmtVrijeme(a.getVrijemeKretanja()),
                        h.fmtVrijeme(a.getVrijemePovratka()),
                        h.fmtCijena(a.getCijena()),
                        h.val(a.getMinBrojPutnika()),
                        h.val(a.getMaksBrojPutnika()));
            }
            h.printSepMulti(W_LIST);
        }
    }

    private static class ItapDetailPrinter implements Printer<Aranzmani> {
        @Override
        public void print(Aranzmani a) {
            IspisRezultataHandler h = IspisRezultataHandler.getInstance();

            System.out.println("Detalji turističkog aranžmana");
            h.printSep(W_D_KEY, W_D_VAL);
            System.out.printf("| %-" + W_D_KEY + "s | %-" + W_D_VAL + "s |%n", "Polje", "Vrijednost");
            h.printSep(W_D_KEY, W_D_VAL);

            h.printRow("Oznaka", String.valueOf(a.getOznaka()), W_D_KEY, W_D_VAL);
            h.printRow("Naziv", h.val(a.getNaziv()), W_D_KEY, W_D_VAL);
            h.printRow("Program", h.val(a.getProgram()), W_D_KEY, W_D_VAL);
            h.printRow("Početni datum", h.fmtDatum(a.getPocetniDatum()), W_D_KEY, W_D_VAL);
            h.printRow("Završni datum", h.fmtDatum(a.getZavrsniDatum()), W_D_KEY, W_D_VAL);
            h.printRow("Vrijeme kretanja", h.fmtVrijeme(a.getVrijemeKretanja()), W_D_KEY, W_D_VAL);
            h.printRow("Vrijeme povratka", h.fmtVrijeme(a.getVrijemePovratka()), W_D_KEY, W_D_VAL);
            h.printRow("Cijena", h.fmtCijena(a.getCijena()), W_D_KEY, W_D_VAL);
            h.printRow("Min broj putnika", String.valueOf(a.getMinBrojPutnika()), W_D_KEY, W_D_VAL);
            h.printRow("Maks broj putnika", String.valueOf(a.getMaksBrojPutnika()), W_D_KEY, W_D_VAL);
            h.printRow("Broj noćenja", String.valueOf(a.getBrojNocenja()), W_D_KEY, W_D_VAL);
            h.printRow("Doplata jednokrevetne", h.fmtCijena(a.getDoplataSobe()), W_D_KEY, W_D_VAL);
            h.printRow("Prijevoz", h.val(a.getPrijevoz()), W_D_KEY, W_D_VAL);
            h.printRow("Broj doručka", String.valueOf(a.getBrojDorucka()), W_D_KEY, W_D_VAL);
            h.printRow("Broj ručkova", String.valueOf(a.getBrojRucka()), W_D_KEY, W_D_VAL);
            h.printRow("Broj večera", String.valueOf(a.getBrojVecera()), W_D_KEY, W_D_VAL);

            h.printSep(W_D_KEY, W_D_VAL);
        }
    }

    // ----- Low-level helpers (formatting, layout) -----

    private String fmtDatum(LocalDate d) {
        return (d == null) ? "-" : d.format(datumIspis);
    }

    private String fmtVrijeme(LocalTime t) {
        return (t == null) ? "-" : t.format(vrijemeIspis);
    }

    private String fmtCijena(float c) {
        return String.format(Locale.ROOT, "%.2f", c);
    }

    private String val(Object o) {
        return (o == null) ? "-" : String.valueOf(o);
    }

    private String crop(String s, int max) {
        if (s == null) return "-";
        return s.length() <= max ? s : s.substring(0, Math.max(0, max - 1)) + "…";
    }

    private void printSep(int w1, int w2) {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        for (int i = 0; i < w1 + 2; i++) sb.append('-');
        sb.append('+');
        for (int i = 0; i < w2 + 2; i++) sb.append('-');
        sb.append('+');
        System.out.println(sb.toString());
    }

    private void printRow(String key, String value, int w1, int w2) {
        String v = (value == null || value.isBlank()) ? "-" : value;
        v = v.replace("\\n", "\n");

        List<String> lines = wrap(v, w2);
        String firstKey = (key == null) ? "-" : key;

        for (int i = 0; i < lines.size(); i++) {
            String k = (i == 0) ? firstKey : "";
            System.out.printf("| %-" + w1 + "s | %-" + w2 + "s |%n", k, lines.get(i));
        }
    }

    private List<String> wrap(String text, int width) {
        List<String> out = new ArrayList<>();
        if (text == null) { out.add("-"); return out; }

        String[] paragraphs = text.replace("\r", "").split("\n", -1);
        for (String p : paragraphs) {
            String s = p.trim();
            if (s.isEmpty()) { out.add(""); continue; }

            String[] words = s.split("\\s+");
            StringBuilder line = new StringBuilder();

            for (String w : words) {
                if (w.length() > width) {
                    if (line.length() > 0) { out.add(line.toString()); line.setLength(0); }
                    for (int i = 0; i < w.length(); i += width) {
                        out.add(w.substring(i, Math.min(i + width, w.length())));
                    }
                    continue;
                }
                if (line.length() == 0) {
                    line.append(w);
                } else if (line.length() + 1 + w.length() <= width) {
                    line.append(' ').append(w);
                } else {
                    out.add(line.toString());
                    line.setLength(0);
                    line.append(w);
                }
            }
            if (line.length() > 0) out.add(line.toString());
        }
        if (out.isEmpty()) out.add("-");
        return out;
    }

    private void printSepMulti(int[] widths) {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        for (int w : widths) {
            for (int i = 0; i < w + 2; i++) sb.append('-');
            sb.append('+');
        }
        System.out.println(sb.toString());
    }

    private void printRowMulti(int[] widths, String... cells) {
        StringBuilder fmt = new StringBuilder();
        fmt.append('|');
        for (int w : widths) fmt.append(" %-" + w + "s |");
        fmt.append("%n");

        Object[] out = new Object[widths.length];
        for (int i = 0; i < widths.length; i++) {
            String v = (i < cells.length) ? cells[i] : "-";
            out[i] = (v == null || v.isBlank()) ? "-" : v;
        }
        System.out.printf(fmt.toString(), out);
    }
}