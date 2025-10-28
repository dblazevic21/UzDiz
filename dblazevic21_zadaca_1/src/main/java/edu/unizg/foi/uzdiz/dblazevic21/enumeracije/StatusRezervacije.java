package edu.unizg.foi.uzdiz.dblazevic21.enumeracije;
/*
• primljena (kada broj prijava nije dostigao minimalni broj putnika na turističkom
aranžmanu)
• aktivna (kada je broj prijava dostigao minimalni broj putnika i nije prešao maksimalni
broj putnika na turističkom aranžmanu)
• na čekanju (kada je broj prijava prešao maksimalni broj putnika na turističkom
aranžmanu).
*/

public enum StatusRezervacije {
	PRIMLJENA,
	AKTIVNA,
	NA_CEKANJU
}
