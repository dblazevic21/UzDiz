package edu.unizg.foi.uzdiz.dblazevic21.lib.csv;

public record AranzmanCsvRecord(
        String oznaka,  
        String naziv,     
        String program,       
        String pocetniDatum,   
        String zavrsniDatum,   
        String vrijemeKretanja, 
        String vrijemePovratka, 
        String cijena,    
        String minBrojPutnika,  
        String maksBrojPutnika,      
        String brojNocenja,          
        String doplataJednokrevetnaSoba,  
        String prijevoz,       
        String brojDorucaka,   
        String brojRuckova, 
        String brojVecera 
		) {
}
