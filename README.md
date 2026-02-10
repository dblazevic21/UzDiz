# UzDiz
### UzDiz zadaća 1.
- Komanda IRTA -> Pokazuje sve rezervacije aranžmana
- Komanda IRO -> Pokazuje specifično informacije jedne rezervacije
- Komanda ITAK -> Pokazuje informacije aranžmana
- Komanda DRTA -> Dodaje novu rezervaciju
- Komanda ORTA -> Otkazuje rezervaciju
- Komanda ITAP -> Detaljne informacije o aranžmanu

### UzDiz zadaća 2.
##### Upgraded
- Komanda ITAK -> Pokazuje statuse sada za aranžmane
- Komanda ITAP -> Pokazuje statuse sada za aranžmane
- Komanda IRTA -> Pokazuje nove statuse dodane

##### New
- Komanda OTA -> Otkazuje aranžman i stavlja sve njene rezervacije na otkazano
- Komanda IP -> Postavlja ispis na kronološki ili obrnuto kronološki slijed
- Komanda BP -> Briše sve rezervacije ili aranžmane
- Komanda UP -> Dodaje rezervacije ili aranžmane iz CSV-a
- Komanda ITAS -> Pokazuje ukupan broj aktivnih, odgođenih, cijenu...
- Komanda STATS -> Pokazuje postotak prisutnosti različitih statusa

### UzDiz zadaća 3.
##### New

- --jdr launch option - "jedna osoba u jednom trenutku može imati samo jednu aktivnu rezervaciju turističkog aranžmana odnosno jedna osoba ne može imati aktivne rezervacije za dva ili više turistička aranžmana koji se preklapaju u svojim datumima i vremenima. U tom slučaju kronološki najranija rezervacija ima status aktivne rezervacije, a sve njene ostale rezervacije koje su u presjeku datuma i vremena mijenjaju status u odgođenu rezervaciju"
- --vdr launch option
- Komanda PPTAR A/R riječ -> pretražuje i ispisuje aranžman/rezervaciju s tom rječju
- PSTAR oznaka -> Sprema aranžman s oznakom i njegove rezervacije
- VSTAR oznaka -> Vraća iz spremnika aranžman s oznakom i njegove rezervacije
- PTAR Ime Prezime oznaka -> Pretplati se osoba za bilo kakve promjene aranžmana rezervacija
- UPTAR Ime Prezime oznaka -> Ukidanje pretplate osobe na tom aranžmanu
- UPTAR oznaka -> Ukidanje svih pretplata na tom aranžmanu

- Chain of Responsibility za komande, spremanje, ...
