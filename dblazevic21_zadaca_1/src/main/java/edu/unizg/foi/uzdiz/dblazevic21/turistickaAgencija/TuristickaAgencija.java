package edu.unizg.foi.uzdiz.dblazevic21.turistickaAgencija;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TuristickaAgencija 
{
    private static volatile TuristickaAgencija INSTANCE;

    private Map<Integer, Aranzmani> aranzmani = new HashMap<>();

    private final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final DateTimeFormatter inputDate = new DateTimeFormatterBuilder()
            .parseLenient()
            .appendPattern("d.M.yyyy")
            .optionalStart().appendLiteral('.').optionalEnd()
            .toFormatter(Locale.ROOT);

    private TuristickaAgencija() {}

    public static TuristickaAgencija getInstance()
    {
        if (INSTANCE == null) 
        {
            synchronized (TuristickaAgencija.class) 
            {
                if (INSTANCE == null) 
                {
                    INSTANCE = new TuristickaAgencija();
                }
            }
        }
        return INSTANCE;
    }

    public void setAranzmani(Map<Integer, Aranzmani> aranzmani)
    {
        this.aranzmani = (aranzmani != null) ? aranzmani : new HashMap<>();
    }

    public void izvrsiKomandu(String unos)
    {
        if (unos == null || unos.isBlank())
        {
            System.out.println("Neispravna komanda.");
            return;
        }

        String trimanNaCapsSlova = unos.trim().toUpperCase(Locale.ROOT);

        if (trimanNaCapsSlova.startsWith("ITAK"))
        {
            izvrsiITAKkomandu(unos);
            return;
        }
        if (trimanNaCapsSlova.startsWith("ITAP"))
        {
            izvrsiITAPkomandu(unos);
            return;
        }
        if (trimanNaCapsSlova.startsWith("IRTA"))
		{
			izvrsiIRTAkomandu(unos);
			return;
		}
        if (trimanNaCapsSlova.startsWith("IRO"))
        {
        	izvrsiIROkomandu(unos);
        	return;
        }
        if (trimanNaCapsSlova.startsWith("ORTA"))
        {
        	izvrsiORTAkomandu(unos);
        	return;
        }
        if (trimanNaCapsSlova.startsWith("DRTA"))
        {
        	izvrsiDRTAkomandu(unos);
        	return;
        }

        System.out.println("Nepoznata komanda. Lista komandi: ITAK, ITAP, IRO, IRTA, ORTA, DRTA.");
    }

    private void izvrsiDRTAkomandu(String unos) 
    {
        String odrezano = (unos == null) ? "" : unos.trim();

        Pattern p = Pattern.compile(
                "^DRTA\\s+([\\p{L}\\p{M}]+)\\s+([\\p{L}\\p{M}]+)\\s+(\\d+)\\s+(\\d{1,2}[\\.\\/]\\d{1,2}[\\.\\/]\\d{4}\\.)\\s+(\\d{1,2}:\\d{1,2}(?::\\d{1,2})?)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS
        );

        Matcher m = p.matcher(odrezano);

        if (!m.matches()) 
        {
            System.out.println("Unos komande: " + odrezano);
            System.out.println("Regex pattern: " + p.pattern());
            System.out.println("Matcher matches: " + m.matches());
            System.out.println("Nepoznata komanda. Upotrijebite: DRTA ime prezime oznaka datum vrijeme");
            return;
        }

        String ime = m.group(1).trim();
        String prezime = m.group(2).trim();
        int oznaka = Integer.parseInt(m.group(3).trim());
        String datum = m.group(4).trim();
        String vrijeme = m.group(5).trim();

        LocalDateTime datumVrijeme = normalizirajDatumIVrijeme(datum, vrijeme);
        if (datumVrijeme == null) 
        {
            System.out.println("Greška: neispravan format datuma i vremena. Očekivano: dd.MM.yyyy HH:mm:ss");
            return;
        }
        System.out.println("DATUM: " + datumVrijeme);

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null) 
        {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        try
        {
            Rezervacije.getInstance().dodajRezervaciju(ime, prezime, oznaka, datumVrijeme);
            Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
            System.out.println("Rezervacija za osobu " + ime + " " + prezime + " na aranžman " + oznaka + " je dodana.");
        } 
        catch (Exception e) 
        {
            System.out.println("Greška prilikom dodavanja rezervacije: " + e.getMessage());
        }
    }

    private LocalDateTime normalizirajDatumIVrijeme(String datum, String vrijeme) 
    {
        try 
        {
            if (datum.endsWith(".")) 
            {
                datum = datum.substring(0, datum.length() - 1);
            }

            DateTimeFormatter fleksibilniDatum = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[d.M.yyyy][dd.MM.yyyy][d/M/yyyy][dd/MM/yyyy]")
                    .toFormatter(Locale.ROOT);

            LocalDate parsiranDatum = LocalDate.parse(datum.replace("/", ".").trim(), fleksibilniDatum);

            if (!vrijeme.matches("\\d{1,2}:\\d{1,2}:\\d{1,2}")) 
            {
                vrijeme += ":00";
            }

            DateTimeFormatter fleksibilnoVrijeme = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[H:mm:ss][HH:mm:ss][H:mm][HH:mm]")
                    .toFormatter(Locale.ROOT);

            LocalTime parsiranoVrijeme = LocalTime.parse(vrijeme.trim(), fleksibilnoVrijeme);

            return LocalDateTime.of(parsiranDatum, parsiranoVrijeme);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }
    
	private void izvrsiORTAkomandu(String unos) 
    {
		String odrezano = (unos == null) ? "" : unos.trim();
		
		Pattern p = Pattern.compile("^ORTA\\s+(\\S+)\\s+(.+)\\s+(\\d+)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher m = p.matcher(odrezano);
		
		if (!m.matches())
		{
			System.out.println("Nepoznata komanda. Upotrijebite: ORTA ime prezime oznaka");
			return;
		}
		
		String ime = m.group(1).trim();
		String prezime = m.group(2).trim();
		int oznaka = Integer.parseInt(m.group(3).trim());
		
		Aranzmani a = aranzmani.get(oznaka);
		if (a == null)
		{
			System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
			return;
		}
		
		boolean ok = Rezervacije.getInstance().otkaziRezervaciju(oznaka, ime, prezime, LocalDateTime.now());
		
		if (ok)
		{
			Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
			System.out.println("Rezervacija za osobu " + ime + " " + prezime + " na aranžman " + oznaka + " je otkazana.");
		}
		else
		{
			System.out.println("Ne postoji rezervacija za osobu " + ime + " " + prezime + " na aranžman " + oznaka + ".");
		}
	}

	public void izvrsiIROkomandu(String unos)
    {
        String trimmed = (unos == null) ? "" : unos.trim();
        Pattern p = Pattern.compile("^IRO\\s+(\\S+)\\s+(.+)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(trimmed);
        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: IRO ime prezime");
            return;
        }

        String ime = m.group(1).trim();
        String prezime = m.group(2).trim();

        List<Rezervacija> lista = Rezervacije.getInstance().getZaOsobu(ime, prezime);

        if (lista == null || lista.isEmpty())
        {
            System.out.println("Nema rezervacija za osobu " + ime + " " + prezime + ".");
            return;
        }

        int[] w = {22, 22, 35, 17};
        printajSeperatorTabliceMulti(w);
        printajRedTabliceMulti(w, "Datum i vrijeme", "Oznaka aranžmana", "Naziv aranžmana", "Vrsta");
        printajSeperatorTabliceMulti(w);

        for (Rezervacija r : lista)
        {
            Integer oznakaAranzmana = null;
            try 
            { 
            	oznakaAranzmana = r.getOznakaAranzmana(); 
            }
            catch (Exception ignored) {}

            String oznakaStr = (oznakaAranzmana == null) ? "-" : String.valueOf(oznakaAranzmana);
            Aranzmani a = (oznakaAranzmana == null) ? null : aranzmani.get(oznakaAranzmana);
            String naziv = (a == null) ? "-" : val(a.getNaziv());

            String datumVrijeme = fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw());
            String vrsta = statusOznaka(r.getStatus());

            printajRedTabliceMulti(w, datumVrijeme, oznakaStr, izrezi(naziv, w[2]), vrsta);
        }

        printajSeperatorTabliceMulti(w);
    }

	private void izvrsiITAKkomandu(String unos) {
		String trimmed = unos.trim();

        if (trimmed.equalsIgnoreCase("ITAK")) 
        {
            ispisiAranzmane(null, null);
            return;
        }

        Pattern p = Pattern.compile("^ITAK\\s+(.+)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(trimmed);
        if (m.matches()) 
        {
            String odStr = normalizirajDatum(m.group(1));
            String doStr = normalizirajDatum(m.group(2));

            LocalDate od = parseDatumZaKomandu(odStr);
            LocalDate dO = parseDatumZaKomandu(doStr);

            if (od == null || dO == null) {
                System.out.println("Greška: neispravan format datuma. Očekivano: d.M.yyyy. (npr. 1.10.2025.)");
                return;
            }
            if (dO.isBefore(od)) {
                System.out.println("Greška: završni datum je prije početnog.");
                return;
            }
            ispisiAranzmane(od, dO);
            return;
        }

        System.out.println("Nepoznata komanda. Upotrijebite: ITAK [od do]");
	}
	
	private void izvrsiITAPkomandu(String unos)
    {
        String trimmed = unos.trim();

        if (trimmed.equalsIgnoreCase("ITAP"))
        {
            ispisiAranzmanePoCijeni(null, null);
            return;
        }
        
        Pattern pOne = Pattern.compile("^ITAP\\s+([0-9]+)$", Pattern.CASE_INSENSITIVE);
        Matcher mOne = pOne.matcher(trimmed);
        if (mOne.matches())
        {
            int oznaka = Integer.parseInt(mOne.group(1));
            ispisiAranzmanDetaljno(oznaka);
            return;
        }

        Pattern pRange = Pattern.compile("^ITAP\\s+([0-9]+[\\.,]?[0-9]*)\\s+([0-9]+[\\.,]?[0-9]*)$", Pattern.CASE_INSENSITIVE);
        Matcher mRange = pRange.matcher(trimmed);
        if (mRange.matches())
        {
            Float min = parseCijenu(mRange.group(1));
            Float max = parseCijenu(mRange.group(2));

            if (min == null || max == null)
            {
                System.out.println("Greška: neispravan format cijene. Očekivano: broj (npr. 100 ili 100,50).");
                return;
            }
            if (max < min)
            {
                System.out.println("Greška: maksimalna cijena je manja od minimalne.");
                return;
            }
            ispisiAranzmanePoCijeni(min, max);
            return;
        }

        System.out.println("Nepoznata komanda. Upotrijebite: ITAP [min max] ili ITAP [oznaka]");
    }
	
	private void izvrsiIRTAkomandu(String unos) 
	{
	    String trimmed = unos.trim();
	    Pattern p = Pattern.compile("^IRTA\\s+(\\d+)(?:\\s+([PAČO]+))?$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	    Matcher m = p.matcher(trimmed);
	    if (!m.matches()) 
	    {
	        System.out.println("Nepoznata komanda. Upotrijebite: IRTA [oznaka] [PA|Č|O|PAČO]");
	        return;
	    }

	    int oznaka = Integer.parseInt(m.group(1));
	    String flags = (m.group(2) == null) ? "PA" : m.group(2).toUpperCase(Locale.ROOT);

	    if (!flags.matches("[PAČO]+")) 
	    {
	        System.out.println("Greška: neispravni filteri. Dozvoljeni su samo PA, Č, O.");
	        return;
	    }
	    
	    Aranzmani a = aranzmani.get(oznaka);
	    if (a == null) 
	    {
	        System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
	        return;
	    }

	    List<Rezervacija> ulaz = Rezervacije.getInstance().getZaAranzman(oznaka);
	    if (ulaz.isEmpty()) 
	    {
	        System.out.println("Nema rezervacija za aranžman " + oznaka + ".");
	        return;
	    }

	    Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);

	    boolean dodajPA = flags.contains("PA");
	    boolean dodajČekaj = flags.contains("Č");
	    boolean dodajOtkazan = flags.contains("O");

	    List<Rezervacija> zaIspis = ulaz.stream()
	        .filter(r -> (dodajPA && (r.getStatus() == StatusRezervacije.PRIMLJENA || r.getStatus() == StatusRezervacije.AKTIVNA))
	                || (dodajČekaj && r.getStatus() == StatusRezervacije.NA_CEKANJU)
	                || (dodajOtkazan && r.getStatus() == StatusRezervacije.OTKAZANA))
	        .collect(Collectors.toList());

	    if (zaIspis.isEmpty())
	    {
	        System.out.println("Nema rezervacija koje zadovoljavaju kriterij za aranžman " + oznaka + ".");
	        return;
	    }

	    int[] w = dodajOtkazan ? new int[] {20, 20, 22, 14, 35} : new int[] {20, 20, 22, 14};
	    printajSeperatorTabliceMulti(w);
	    
	    if (dodajOtkazan)
	    {
	    	printajRedTabliceMulti(w, "Ime", "Prezime", "Datum i vrijeme", "Vrsta", "Otkazano");
	    }
	    else
	    {
	    	printajRedTabliceMulti(w, "Ime", "Prezime", "Datum i vrijeme", "Vrsta");
	    }
	    printajSeperatorTabliceMulti(w);
	    
	    for (Rezervacija r : zaIspis) 
	    {
	    	 if (dodajOtkazan) 
	         {
	             printajRedTabliceMulti(w,
	                 val(r.getIme()),
	                 val(r.getPrezime()),
	                 fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw()),
	                 statusOznaka(r.getStatus()),
	                 fmtDatumVrijeme(r.getOtkazanoAt(), null));
	         } 
	         else 
	         {
	             printajRedTabliceMulti(w,
	                 val(r.getIme()),
	                 val(r.getPrezime()),
	                 fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw()),
	                 statusOznaka(r.getStatus()));
	         }
	    }
	    printajSeperatorTabliceMulti(w);
	}

	
	private String statusOznaka(StatusRezervacije s) 
	{
        if (s == null) return "-";
        return switch (s) 
        {
            case PRIMLJENA -> "PRIMLJENA";
            case AKTIVNA -> "AKTIVNA";
            case NA_CEKANJU -> "NA ČEKANJU";
            case OTKAZANA -> "OTKAZANA";
        };
    }

    private String fmtDatumVrijeme(LocalDateTime dt, String raw) 
    {
        if (dt != null) 
        {
            return (dt.toLocalDate().format(datumIspis) + " " + dt.toLocalTime().format(vrijemeIspis)).trim();
        }
        if (raw != null && !raw.isBlank()) return raw.trim();
        return "-";
    }
	
	
	private void ispisiAranzmanePoCijeni(Float min, Float max)
	{
	    List<Aranzmani> lista = new ArrayList<>(aranzmani.values());
	    lista.sort(Comparator
	            .comparing(Aranzmani::getCijena)
	            .thenComparing(Aranzmani::getOznaka));

	    if (min != null && max != null)
	    {
	        lista.removeIf(a -> a == null || a.getCijena() < min || a.getCijena() > max);
	    }

	    String format = "%-7s %-35s %-14s %-14s %-12s %-16s %-10s %-18s %-18s%n";
	    System.out.printf(format,
	            "Oznaka", "Naziv", "Početni datum", "Završni datum",
	            "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika");
	    System.out.println(ponovi('-', 150));

	    for (Aranzmani a : lista)
	    {
	        System.out.printf(format,
	                val(a.getOznaka()),
	                izrezi(a.getNaziv(), 35),
	                fmtDatum(a.getPocetniDatum()),
	                fmtDatum(a.getZavrsniDatum()),
	                fmtVrijeme(a.getVrijemeKretanja()),
	                fmtVrijeme(a.getVrijemePovratka()),
	                fmtCijena(a.getCijena()),
	                val(a.getMinBrojPutnika()),
	                val(a.getMaksBrojPutnika()));
	    }
	}

	private void ispisiAranzmane(LocalDate od, LocalDate dO)
	{
	    List<Aranzmani> lista = new ArrayList<>(aranzmani.values());
	    lista.sort(Comparator
	            .comparing(Aranzmani::getPocetniDatum, Comparator.nullsLast(Comparator.naturalOrder()))
	            .thenComparing(Aranzmani::getOznaka));

	    if (od != null && dO != null)
	    {
	        lista.removeIf(a -> {
	            if (a == null) return true;
	            LocalDate pd = a.getPocetniDatum();
	            return pd == null || pd.isBefore(od) || pd.isAfter(dO);
	        });
	    }

	    int[] w = {7, 35, 14, 14, 12, 16, 10, 18, 18};

	    printajSeperatorTabliceMulti(w);
	    printajRedTabliceMulti(w,
	            "Oznaka", "Naziv", "Početni datum", "Završni datum",
	            "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika"
	    );
	    printajSeperatorTabliceMulti(w);

	    for (Aranzmani a : lista)
	    {
	        if (a == null) continue;
	        printajRedTabliceMulti(w,
	                val(a.getOznaka()),
	                izrezi(a.getNaziv(), w[1]),
	                fmtDatum(a.getPocetniDatum()),
	                fmtDatum(a.getZavrsniDatum()),
	                fmtVrijeme(a.getVrijemeKretanja()),
	                fmtVrijeme(a.getVrijemePovratka()),
	                fmtCijena(a.getCijena()),
	                val(a.getMinBrojPutnika()),
	                val(a.getMaksBrojPutnika())
	        );
	    }
	    printajSeperatorTabliceMulti(w);
	}

    private void ispisiAranzmanDetaljno(int oznaka)
    {
        Aranzmani a = aranzmani.get(oznaka);
        if (a == null)
        {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        final int w1 = 28;
        final int w2 = 90;

        System.out.println("Detalji turističkog aranžmana");
        printajSeperatorTablice(w1, w2);
        System.out.printf("| %-" + w1 + "s | %-" + w2 + "s |%n", "Polje", "Vrijednost");
        printajSeperatorTablice(w1, w2);

        printajRedTablice("Oznaka", String.valueOf(a.getOznaka()), w1, w2);
        printajRedTablice("Naziv", val(a.getNaziv()), w1, w2);
        printajRedTablice("Program", val(a.getProgram()), w1, w2);
        printajRedTablice("Početni datum", fmtDatum(a.getPocetniDatum()), w1, w2);
        printajRedTablice("Završni datum", fmtDatum(a.getZavrsniDatum()), w1, w2);
        printajRedTablice("Vrijeme kretanja", fmtVrijeme(a.getVrijemeKretanja()), w1, w2);
        printajRedTablice("Vrijeme povratka", fmtVrijeme(a.getVrijemePovratka()), w1, w2);
        printajRedTablice("Cijena", fmtCijena(a.getCijena()), w1, w2);
        printajRedTablice("Min broj putnika", String.valueOf(a.getMinBrojPutnika()), w1, w2);
        printajRedTablice("Maks broj putnika", String.valueOf(a.getMaksBrojPutnika()), w1, w2);
        printajRedTablice("Broj noćenja", String.valueOf(a.getBrojNocenja()), w1, w2);
        printajRedTablice("Doplata jednokrevetne", fmtCijena(a.getDoplataSobe()), w1, w2);
        printajRedTablice("Prijevoz", val(a.getPrijevoz()), w1, w2);
        printajRedTablice("Broj doručka", String.valueOf(a.getBrojDorucka()), w1, w2);
        printajRedTablice("Broj ručkova", String.valueOf(a.getBrojRucka()), w1, w2);
        printajRedTablice("Broj večera", String.valueOf(a.getBrojVecera()), w1, w2);

        printajSeperatorTablice(w1, w2);
    }
    

    private String fmtDatum(LocalDate d) 
    {
        return (d == null) ? "-" : d.format(datumIspis);
    }

    private String fmtVrijeme(LocalTime t)
    {
        return (t == null) ? "-" : t.format(vrijemeIspis);
    }

    private String fmtCijena(float c) 
    {
        return String.format(Locale.ROOT, "%.2f", c);
    }

    private String val(Object o) 
    {
        return (o == null) ? "-" : String.valueOf(o);
    }

    private String izrezi(String s, int max) 
    {
        if (s == null) return "-";
        return s.length() <= max ? s : s.substring(0, Math.max(0, max - 1)) + "…";
    }

    private String ponovi(char c, int n) 
    {
        char[] arr = new char[n];
        Arrays.fill(arr, c);
        return new String(arr);
    }

    private LocalDate parseDatumZaKomandu(String s)
    {
        try 
        {
            return LocalDate.parse(s, inputDate);
        }
        catch (Exception ignored)
        {
            return null;
        }
    }

    private String normalizirajDatum(String s) 
    {
        String x = s.trim().replaceAll("\\.+", ".");
        if (!x.endsWith(".")) x += ".";
        return x;
    }
    
    private Float parseCijenu(String s)
    {
        try
        {
            String t = s.trim().replace(',', '.');
            return Float.parseFloat(t);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    private void printajSeperatorTablice(int w1, int w2)
    {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        for (int i = 0; i < w1 + 2; i++) sb.append('-');
        sb.append('+');
        for (int i = 0; i < w2 + 2; i++) sb.append('-');
        sb.append('+');
        System.out.println(sb.toString());
    }

    private void printajRedTablice(String key, String value, int w1, int w2)
    {
        String v = (value == null || value.isBlank()) ? "-" : value;
        v = v.replace("\\n", "\n");

        List<String> lines = wrappajTekst(v, w2);
        String firstKey = (key == null) ? "-" : key;

        for (int i = 0; i < lines.size(); i++)
        {
            String k = (i == 0) ? firstKey : "";
            System.out.printf("| %-" + w1 + "s | %-" + w2 + "s |%n", k, lines.get(i));
        }
    }

    private List<String> wrappajTekst(String text, int width)
    {
        List<String> out = new ArrayList<>();
        if (text == null) { out.add("-"); return out; }

        String[] paragraphs = text.replace("\r", "").split("\n", -1);
        for (String p : paragraphs)
        {
            String s = p.trim();
            if (s.isEmpty()) { out.add(""); continue; }

            String[] words = s.split("\\s+");
            StringBuilder line = new StringBuilder();

            for (String w : words)
            {
                if (w.length() > width)
                {
                    if (line.length() > 0) 
                    { 
                    	out.add(line.toString()); line.setLength(0); 
                    }
                    for (int i = 0; i < w.length(); i += width)
                    {
                        out.add(w.substring(i, Math.min(i + width, w.length())));
                    }
                    continue;
                }
                if (line.length() == 0)
                {
                    line.append(w);
                }
                else if (line.length() + 1 + w.length() <= width)
                {
                    line.append(' ').append(w);
                }
                else
                {
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
    
    private void printajSeperatorTabliceMulti(int[] widths)
    {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        for (int w : widths)
        {
            for (int i = 0; i < w + 2; i++) sb.append('-');
            sb.append('+');
        }
        System.out.println(sb.toString());
    }

    private void printajRedTabliceMulti(int[] widths, String... cells)
    {
        StringBuilder fmt = new StringBuilder();
        fmt.append('|');
        for (int w : widths)
        {
            fmt.append(" %-" + w + "s |");
        }
        fmt.append("%n");

        Object[] out = new Object[widths.length];
        for (int i = 0; i < widths.length; i++)
        {
            String v = (i < cells.length) ? cells[i] : "-";
            out[i] = (v == null || v.isBlank()) ? "-" : v;
        }
        System.out.printf(fmt.toString(), out);
    }
    
}
