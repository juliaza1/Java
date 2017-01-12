//larsereb g-12 o-3 k-inf1000/h07

/**
* Oblig3 av Lars-Erik Bruce
* ================================
* Jeg erklærer å ha lest "Krav til innleverte oppgaver ved Institutt for Informatikk
* (http://www.ifi.uio.no/studinf/skjemaer/erklaring.pdf) og fulgt denne til punkt og
* prikke. Har benyttet meg av skjelletet på oppgavearket som mal.
*
* API av programmet kan finnes på
* http://folk.uio.no/larsereb/semester1/inf1000/oblig3/api/
****************************/

import easyIO.*;
import java.io.*;
import java.util.Random; // Så jeg kan lage en strømregning med random kwt


/**
* Denne klassen inneholder main() og oppretter ett objekt av klassen HybelHus.
* Resten av programmet kjøres fra klassen HybelHus, med objekter av Student og Hybel.
****************************/
class Oblig3 {
    public static void main (String[] args) {
	HybelHus utsyn = new HybelHus("HaiHus.data");
	utsyn.kommandoLøkke();
	System.out.println("Ha en riktig god dag!");
    } //Slutt metode main
} //Slutt class Oblig3

/**
* Dette er den mest omfattende klassen. Oppretter objekter av Student og Hybel, og kjører
* forskjellige metoder avhengig av brukervalg i en switch-meny.
****************************/
class HybelHus {
    Out skjerm = new Out(); //For å skrive til skjerm
    In tastatur = new In(); //For å lese fra tastatur

    //Økonomidata her
    final double HUSLEIE = 4500; // en fast sum for husleien
	final double KWTKRSTUDENT = 2; // en fast sum for kwt for studenter
	final double KWTGG = 0.8; // en fast sum for GGs kwt
	final double LEILIGHETUTGIFTER = 1000; // Utgifter pr leilighet for GG
	final double TORPEDOGEBYR = 1000; // Hva torpedo forlanger i betaling

	final int GANGNUMMER = 4;
	final int HYBELBOKSTAV = 7;
	final int TELLGANG = GANGNUMMER -1;
	final int TELLBOKSTAV = HYBELBOKSTAV - 1;

    double fortjeneste = 0; // GGs fortjeneste i antall kroner
    int ledigeHyblerMåned = 0; // Antall ledige hybler hver måned legges til denne variabel
    int måned = 1; // Hvilken måned det er. 1 = november 2007 etc ...

    Hybel [][] hyblene = new Hybel[GANGNUMMER][HYBELBOKSTAV];


/**
* Oppretter hybelobjektene og fyller de opp ved behov. Først opprettes alle nødvendige hybel-
* objekter og pekere til dette. Finnes det en HaiHus.data fylles data fra denne inn i hybel. Da
* opprettes det også student-objekter der hybelen er bebodd.
****************************/
    HybelHus(String filnavn) { // Konstruktør
	//Her lager jeg pekere til alle hyblene som finnes
	for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) {
	    for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {
		hyblene[tellHus][tellHybel] = new Hybel();
	    }
	}

	if (new File(filnavn).exists()) { // Hvis filen eksisterer; her bruker jeg metode i java.io.*
	    //Her leser jeg inn filen! Finnes den ikke, blir den lagd når programmet avslutter.
	    In innfil = new In(filnavn);
	    for (int telle = 0; telle < 28; telle++) { // Teller de 28 første linjene
		int hus = innfil.inInt(";");
		int hybel = innfil.inInt(";");
		String studentNavn = innfil.inWord(";");
		double saldo = innfil.inDouble(";");
		if (!studentNavn.equals("TOM HYBEL")) { // Hvis hybelen ikke er tom, registrerer jeg verdiene
		    hyblene[hus][hybel].leietager = new Student(studentNavn);
		    hyblene[hus][hybel].opptatt = true;
		    hyblene[hus][hybel].leietager.saldo = saldo;
		    }
	    } //slutt løkke while
	    //Resten kan leses inn enkelt og greit:
	    måned = innfil.inInt(";");
	    ledigeHyblerMåned = innfil.inInt(";");
	    fortjeneste = innfil.inDouble(";");

	} // Slutt if (filnavn).exists
    } //Slutt konstruktør


/**
* Kjører menyfunksjonen.
*
****************************/
    void kommandoLøkke() {
	int ordre = 9; // For valg i menyen
	while (ordre != 0) {
	    skrivMeny(); //Printer ut menyen i skrivMeny-metoden
	    ordre = tastatur.inInt(); // Får inn ordrenummer bra terminalbruker
	    switch (ordre) {
	    case 0: avslutt();	       	    break;
	    case 1: ledigeHybler();    	    break;
	    case 2: registrerLeietager();   break;
	    case 3: flyttUt(); 		 	    break;
	    case 4: nyMåned(); 			    break;
	    case 5: betaling(); 		    break;
	    case 6: kastUt(); 			    break;
	    case 7: rapport();			    break;
	    case 8: lagFil();               break;
	    case 9: printListe();			break;
	    default: skjerm.outln("Velg et tall mellom 0 og 7");        break;

	    } //Slutt switch
	} //Slutt while løkken, når man gir signal 8
    } //Slutt metode kommandoløkke


/**
* Skriver ut menyen.
*
****************************/
    void skrivMeny() {
	skjerm.outln("0. Avslutt program");
	skjerm.outln("1. Skriv liste over ledige hybler");
	skjerm.outln("2. Registrer ny leietager");
	skjerm.outln("3. Registrer frivillig flytting av leietager");
	skjerm.outln("4. Måneds-kjøring av husleie med strøm");
	skjerm.outln("5. Registrer betaling fra leietager");
	skjerm.outln("6. Sjekk om noen leietagere skal kastes ut");
	skjerm.outln("7. Skriv økonomirapport");
	skjerm.outln("8: Hent lysregningen");
	skjerm.outln("9: Liste over personer med saldo");

    }

/**
* Denne metoden avslutter programmet.
* Lagrer hybler, studentnavn og studentsaldo til HaiHus.data.
* Hvis hybel ikke er bebodd, skrives "TOM HYBEL" til fil og strøm siste måned som saldo (utestående).
****************************/
    void avslutt() {
	skjerm.outln("Vennligst vent mens programmet avslutter.");
	Out utfil = new Out("HaiHus.data");
	for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) { 		// Teller igjennom husene
	    for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {  	// Teller igjennom hyblene
		if (hyblene[tellHus][tellHybel].opptatt == true) {	// Sjekker om hybelen er opptatt
		    utfil.out(tellHus + ";" + tellHybel + ";");
		    utfil.out(hyblene[tellHus][tellHybel].leietager.navn + ";");
		    utfil.outln(hyblene[tellHus][tellHybel].leietager.saldo + ";");
		} else {
		    utfil.outln(tellHus + ";" + tellHybel + ";TOM HYBEL;" + hyblene[tellHus][tellHybel].utestående + ";");
		} // Slutt if/else
	    } //Slutt for tellHybel
	} //Slutt for tellHus
	utfil.out(måned + ";" + ledigeHyblerMåned + ";" + fortjeneste);
	utfil.close();
    } //Slutt metode avslutt

/**
* Printer ut hyblene som er ledig. Er alle hyblene i en gang opptatt, printes ut en tekst
* om dette i stedet.
****************************/
    void ledigeHybler() {
        boolean ledig; // For å printe en fin linje om alle husene er opptatt
	skjerm.outln("\n"); //Linjeskift etter meny, for penere utskrift, dette fortsetter uten kommentarer.
	skjerm.outln("Ledige hybler:");
	for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) { 	        // Teller igjennom husene
	    ledig = true; // Negerer ledig hver gang tellHybel-løkke er ferdig
	    skjerm.outln("Gang nummer " + (tellHus + 1));               // + 1 så det ser riktig ut for brukeren
	    for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {  	// Teller igjennom hyblene
		if (hyblene[tellHus][tellHybel].opptatt == false) {		// Sjekker om hybelen er ledig
		    char hybel = (char) ('B' + tellHybel); 		// Nummer 0 = B, etc ...
		    skjerm.out(hybel + " ");
		    ledig = false;
		} // end if
	    } //end for tellHybel
	    if (ledig == true) {skjerm.out("Ingen hybler er ledig i denne gangen");}
	    skjerm.outln("");
	} // end for tellHus
	skjerm.outln("\n"); //Linjeskift før meny, for penere utskrift, dette fortsetter uten kommentarer..
    } //Slutt metode ledigeHybler

/**
* Registrerer ny leietager. Oppretter studentobjekt i hybel, og setter hybelobjekt som opptatt.
* Trekker studenten for husleie og strøm fra tidligere student.
****************************/
    void registrerLeietager() {
	skjerm.outln("\n");
	skjerm.outln("Registrer ny leietager:");
	skjerm.out("Navnet på leietageren: ");
	String studentNavn = tastatur.inLine();
	int gang = gang(); int hybelnr = hybel(); // Så jeg slipper å skrive dette hver gang jeg skal ha tak i leilighet.

	//Nå har jeg samlet informasjon om studentNavn, gangnummer og hybelnummer (0 - 6)
	if (gang < 0 || gang > TELLGANG || hybelnr < 0 || hybelnr > TELLBOKSTAV) { // Sjekker om hybelen faktisk eksisterer.
	    skjerm.outln("Ugyldig gang eller hybel!");
	} else if (hyblene[gang][hybelnr].opptatt == true) { //Sjekker om eksisterende hybel er opptatt
	    skjerm.outln("Denne hybelen er opptatt!");
	} else  { // registrer leietager
	    hyblene[gang][hybelnr].leietager = new Student(studentNavn); // Her setter jeg studentNavn som verdi 'navn' i Student-objekt via konstruktør i Student
	    hyblene[gang][hybelnr].opptatt = true; // Hybelen er endelig opptatt \o/
	    hyblene[gang][hybelnr].leietager.saldo -= HUSLEIE; fortjeneste += HUSLEIE; // Trekk husleie fra student, og sett inn hos GG
		hyblene[gang][hybelnr].leietager.saldo -= hyblene[gang][hybelnr].utestående; // Trekk fra siste strømforbruk fra siste leietager
		fortjeneste += hyblene[gang][hybelnr].utestående; // Legg dette til fortjeneste, utestående settes til null når studenten flytter ut frivillig eller kastes ut.

	    skjerm.out(hyblene[gang][hybelnr].leietager.navn); // Her leser jeg verdien navn i Studentobjektet til hybelobjektet
	    skjerm.outln(" har nå flyttet inn i vårt leilighetskompleks og har betalt forrige lysregning på kr " + hyblene[gang][hybelnr].utestående );
	} // Slutt registrer leietager
	skjerm.outln("\n");
    }// Slutt metode registrerLeietager


/**
* Sletter student fra systemet, og setter hybel som ledig.
* Sjekker om studenten har råd til å flytte, og ber om betaling hvis student ikke har råd.
* Tar også et gebyr på 800,- fra student og setter inn på GGs fortjeneste.
*
****************************/
    void flyttUt() {
	skjerm.outln("\n");
	skjerm.outln("Flytting fra leilighet:");
	skjerm.out("Studentens navn: ");

	//tastatur.skipWhite();
	String navn = tastatur.inLine();

	int gang = gang(); int hybelnr = hybel();

	if (gang < 0 || gang > TELLGANG || hybelnr < 0 || hybelnr > TELLBOKSTAV) { // Sjekker om hybelen faktisk eksisterer.
	    skjerm.outln("Ingen kan da bo her!");
	} else if (hyblene[gang][hybelnr].opptatt == false) {
	    skjerm.outln("Her er det ingen som bor.");
	} else if (hyblene[gang][hybelnr].leietager.navn.equals(navn))  { //Sjekker om studenten har oppgitt riktig navn

		if ((hyblene[gang][hybelnr].leietager.saldo - 800) >= 0) {
		    skjerm.outln(hyblene[gang][hybelnr].leietager.navn + " flyttes nå ut av vårt leilighetskompleks.");
		    hyblene[gang][hybelnr].leietager.saldo -= 800; // Ekspedisjonsgebyr
		    fortjeneste += 800;
		    skjerm.out("Saldo til leietager er ");
		    skjerm.out(hyblene[gang][hybelnr].leietager.saldo, 2);
		    skjerm.outln(" kroner.");
		    hyblene[gang][hybelnr].utestående = 0; // Når neste strømregning kommer, blir dette registrert på utestående.
		    hyblene[gang][hybelnr].opptatt = false; //Hybelen er nå ledig
		    hyblene[gang][hybelnr].leietager = null; //Ingen peker til Student i dette hybel-objektet (altså slettes Student-objektet)
		    skjerm.outln("Leietager er slettet fra systemet.");
		} else {

			skjerm.outln(hyblene[gang][hybelnr].leietager.navn + " har ikke råd til ekspedisjonsgebyret.");
			skjerm.outln("Studenten må først betale kroner " + ((hyblene[gang][hybelnr].leietager.saldo - 800) * (-1)));
		}

	    skjerm.outln("\n");
	} else {
		skjerm.outln("Navn og leilighetsnummer stemmer ikke overens.");
	}


	skjerm.outln("\n");
    } // Slutt metode flyttUt






/**
* Kjører ny måned, dette er den mest kompliserte metoden i mitt program.
* Derfor kjører jeg litt kraftig kommentering, mest for min egen skyld. <br />
* Den sjekker om vi er i riktig måned med GG (terminalbruker), så tar vi betalt
* for husregning og strøm.
*
****************************/
    void nyMåned() {
	skjerm.outln("\n");
	skjerm.out("Hvilken måned er vi i nå? ");

	int denneMåned = tastatur.inInt();
	if (denneMåned == måned) {

	skjerm.out("Riktig! Kjører neste måned ..");

	måned += 1; // Vi er nå i neste måned
	skjerm.outln(" velkommen til måned " + måned + "!");


	// Økonomi-variabler for metode nyMåned
	double leieinntekter = 0;
	double totalStrøm = 0;
	double strøminntekter = 0;
	double totalKwt = 0; // Regner ut totalt KWT
	double strømutgifterGG = 0;
	int antallBeboere = 0;



	// Utregning for selve husleien
	for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) {                // Teller igjennom husene
	    for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {      // Teller igjennom hyblene
			if (hyblene[tellHus][tellHybel].opptatt == true) {      // Sjekker om hybelen er opptatt
			    hyblene[tellHus][tellHybel].leietager.saldo -= HUSLEIE; leieinntekter += (HUSLEIE - LEILIGHETUTGIFTER); // Trekk husleie fra student, sett i husleie
			    antallBeboere += 1;
			} else {
			    // Utgifter til leilighet, når ingen leier der.
			    leieinntekter -= LEILIGHETUTGIFTER;
			}
	    }
	}
	// Slutt utregning for selve husleien



	// Utregning for strøm
	//

/**
* Utregning for strøm.
* Her leses strømregning, finnes den ikke er dette første måned og strømregning
* har enda ikke kommet (alltid en måneds forsinkelse, hvor neste student betaler
* siste måned for forrige student, da vil nødvendigvis første student tjene på det).
*****************************************************************************************/
	if (new File("Lysregning.data").exists()) { // Hvis filen eksisterer; her bruker jeg metode i java.io.*
	    In innfil = new In("Lysregning.data");
		while (!innfil.endOfFile()) { // Når jeg IKKE har nådd slutten av filen.
			int hus = innfil.inInt(":");
			int hybel = innfil.inInt(":");
			double kwt = innfil.inInt(":");
			double studentRegning = kwt * KWTKRSTUDENT; // Hva må studenten betale?
			totalKwt += kwt; // Hvor mye strøm har blitt brukt til sammen?

/**
* Belaster enten saldo til student eller utestående med strømregning.
* Hvis studenten har flyttet, sendes regning til neste som flytter inn
* Det blir bare lagt til GGs fortjeneste når noen bor i leiligheten
* Utestående blir lagt til fortjeneste når noen flytter til, i metode registrerLeietager()
*****************************************************************************************/
			if (hyblene[hus][hybel].leietager == null) {
				hyblene[hus][hybel].utestående = studentRegning;
			} else {
				hyblene[hus][hybel].leietager.saldo -= studentRegning; // Strøm blir trukket fra leietagers konto
				fortjeneste += studentRegning; // Regningen legges til GGs fortjeneste
				totalStrøm += studentRegning; // Hvor mye blir disse inntektene til sammen?
			}
		} // Slutt mens filen ikke er slutt
	} // Slutt hvis filen eksisterer

	// Utregning generelt

	ledigeHyblerMåned += 28 - antallBeboere; // Setter ledige hybler denne måned inn i total.
	strømutgifterGG = totalKwt * KWTGG; // Hvor mye må GG betale Lys Meg Opp?
	strøminntekter = totalStrøm - strømutgifterGG; // Hvor mye tjener GG på strømmen?

	fortjeneste += leieinntekter + strøminntekter;
	skjerm.outln("Din fortjeneste denne måned: " + (leieinntekter + strøminntekter) + ",-");


} else if (denneMåned < måned) { // Hvis brukeren skriver lavere måned enn det er
	skjerm.outln("Akk, disse forgagne tider");
} else {
	skjerm.out("Rolig nå, kompis! Fremtiden er lenger unna enn du tror!");
	}
	skjerm.outln("\n");

    } // Slutt metode nyMåned

/**
* Registrerer innbetaling fra student, slik at hans saldo aldri blir tom.
*
****************************/
    void betaling() {
	skjerm.outln("\n");
	skjerm.outln("Registrer betaling fra leietager.");
	int gang = gang(); int hybelnr = hybel();
	if (gang < 0 || gang > TELLGANG || hybelnr < 0 || hybelnr > TELLBOKSTAV) { // Sjekker om hybelen faktisk eksisterer.
	    skjerm.outln("Ingen kan da bo her!");
	} else if (hyblene[gang][hybelnr].opptatt == false) {
	    skjerm.outln("Her er det ingen som bor.");
	} else {
	skjerm.outln("Hvor mye har " + hyblene[gang][hybelnr].leietager.navn + " til oss denne gangen?");
	int betalt = tastatur.inInt();
	hyblene[gang][hybelnr].leietager.saldo += betalt;
	}
	skjerm.outln("\n");
    } // Slutt metode betaling()



/**
* Denne metoden sjekker om noen skal kastes ut.
* Hvis det ligger en fil Torpedo.txt på samme område som programmet, vil intet bli gjort.
* Tanken er at GG f.eks. skal sende filen til torpedo, altså flytte/slette fila.
*
* GGs totalfortjeneste (fortjeneste) tar bare med seg torpedogebyr. fortjeneste er allerede
* oppdatert med husleien.
*************************************************/
    void kastUt() {
	skjerm.outln("\n");
	// Sjekker først om forrige fil ligger der. Da har ikke Gullbrand sendt filen til sin torpedo!
	if (new File("Torpedo.txt").exists()) {
	    skjerm.outln("Forrige fil er enda ikke sendt til torpedo Sleipe Leif!");
	} else {
	    for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) {                // Teller igjennom husene
		for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {      // Teller igjennom hyblene
		    if (hyblene[tellHus][tellHybel].opptatt == true) {      // Sjekker om hybelen er opptatt
			if (hyblene[tellHus][tellHybel].leietager.saldo < 0) { // Hvis saldo er lavere enn null
			    // Regn ut det som skal sendes
			    char hybel = (char) ('B' + tellHybel); 		// Nummer 0 = B, etc ...
			    String hybelNavn = (tellHus + 1) + "" + hybel;
			    String studentNavn = hyblene[tellHus][tellHybel].leietager.navn;
			    double krav = hyblene[tellHus][tellHybel].leietager.saldo - LEILIGHETUTGIFTER;

			    // Send til torpedo Sleipe Leif
			    tilkallTorpedo(hybelNavn, studentNavn, krav);

			    // Ta ut fortjeneste og spark ut leietager
			    fortjeneste += TORPEDOGEBYR; // Kun torpedogebyr, betalign for husleie blir registrert ved månedskjøring - de er registrert allerede.
			    hyblene[tellHus][tellHybel].utestående = 0; // Når neste strømregning kommer, blir dette registrert på utestående.
			    hyblene[tellHus][tellHybel].opptatt = false; //Hybelen er nå ledig
			    hyblene[tellHus][tellHybel].leietager = null; //Ingen peker til Student i dette hybel-objektet (altså slettes Student-objektet)
			    skjerm.outln("Leietager er slettet fra systemet.");
			}
		    }
		} // Slutt tellHybel
	    } // Slutt tellHus
	} // Slutt hvis fil eller ikke.
	skjerm.out("\n");
    } // Slutt metode kastUt

/**
* Her kommer forskjellige økonomiske data ut på skjerm.
*
****************************/
    void rapport() {
	// Her kommer utregning
	double totalSaldo = 0;
	double gjennomsnittSaldo = 0;
	int antallBeboere = 0;
	int gjennomsnittLedigMåned = 0;
	for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) {                // Teller igjennom husene
            for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {      // Teller igjennom hyblene
                if (hyblene[tellHus][tellHybel].opptatt == true) {      // Sjekker om hybelen er opptatt
		    totalSaldo += hyblene[tellHus][tellHybel].leietager.saldo;
		    antallBeboere += 1;
			}
	    }
	}
	// Så programmet mitt ikke prøver å dele med null, som blir tull.
	if (totalSaldo != 0) gjennomsnittSaldo = totalSaldo / antallBeboere;
	if (ledigeHyblerMåned > 0) gjennomsnittLedigMåned = ledigeHyblerMåned / måned;

	// Her kommer utskrift
	final int BREDDE1 = 50;
	final int BREDDE2 = 15;
	skjerm.outln("\n");
	skjerm.outln("Økonomirapport: \n" );
	skjerm.out("Måneder i virksomhet: ", BREDDE1);
	skjerm.outln(måned, BREDDE2);
	skjerm.out("Antall beboere akkuratt nå: ", BREDDE1);
	skjerm.outln(antallBeboere, BREDDE2);
	skjerm.out("Total saldo hos beboere: ", BREDDE1);
	skjerm.outln(totalSaldo, 2, BREDDE2);
	skjerm.out("Gjennomsnittelig saldo hos beboere: ", BREDDE1);
	skjerm.outln(gjennomsnittSaldo, 2, BREDDE2);
	skjerm.out("Antall hybelmåneder med ledig hybel: ", BREDDE1);
	skjerm.outln(ledigeHyblerMåned, BREDDE2);
	skjerm.out("Gjennomsnittelig ledige hybler hver måned: ", BREDDE1);
	skjerm.outln(gjennomsnittLedigMåned, BREDDE2);
	skjerm.out("Sum totalfortjeneste: ", BREDDE1);
	skjerm.outln(fortjeneste, 2, BREDDE2);
	skjerm.outln("\n");
}

/**
* Sender informasjon til Sleipe Leif.
*
****************************/
    void tilkallTorpedo(String hybelNavn, String student, double krav) {
	skjerm.outln("Sender informasjon til torpedo Sleipe Leif.");
	skjerm.outln(hybelNavn + ":" + student + ":" + krav);

	Out utfil = new Out("Torpedo.txt", true); //Lager fil "Torpedo.txt" om den ikke eksisterer, samt oppretter peker til fil (utfil)
	utfil.outln(hybelNavn + ":" + student + ":" + krav);
	utfil.close();
    }

/**
* Denne genererer en ny Lysregning.data. Den blir kjørt fra menyen (skjult meny nr 8).
*
****************************/
    void lagFil() {

	skjerm.outln("\n");
	skjerm.outln("Henter ny strømfil fra Lys meg opp!");
	skjerm.outln("\n");

	double kwtd = 0;
	int kwt;
	Out utfil = new Out("Lysregning.data");
	for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) {                // Teller igjennom husene
            for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {      // Teller igjennom hyblene
                if (hyblene[tellHus][tellHybel].opptatt == true) {      // Sjekker om hybelen er opptatt/bruker strøm
		    Random generator = new Random();
		    kwtd = generator.nextDouble() * 30.0 + 40.0; // Lager et tilfeldig tall mellom 40 og 70
		    kwt = (int)kwtd; // Gjør denne verdien om til et heltall som kan brukes i fil
		    utfil.outln(tellHus + ":" + tellHybel + ":" + kwt);
		} else {
		    utfil.outln(tellHus + ":" + tellHybel + ":" + 0);
		}
	    }
	}
	utfil.close();

    } // Slutt metode lagFil()

/**
* Printer ut en liste på skjerm med studentnavn og saldo.
*
****************************/
	void printListe() {
		for (int tellHus = 0; tellHus <= TELLGANG; tellHus++) {                // Teller igjennom husene
			skjerm.outln("Gang nummer " + (tellHus + 1));
            for (int tellHybel = 0; tellHybel <= TELLBOKSTAV; tellHybel++) {      // Teller igjennom hyblene
				if (hyblene[tellHus][tellHybel].opptatt == true) {      // Sjekker om hybelen er bebodd
					char hybel = (char) ('B' + tellHybel); 		// Nummer 0 = B, etc ...
					skjerm.out("Hybel " + hybel + ": ");

					skjerm.out(hyblene[tellHus][tellHybel].leietager.navn + ", ");
					skjerm.outln(hyblene[tellHus][tellHybel].leietager.saldo, 2);
				}
			}
			skjerm.outln("");
		}

	} // Slutt metode printListe()


/**
* En metode for å hente gangnummer, siden dette gjøre i mer enn ett sted i programmet.
*
****************************/
    int gang() {
	skjerm.outln("Skriv inn leietagers gangnummer og hybelbokstav");
	skjerm.out("Gangnummer: ");
	int gang = tastatur.inInt();
	gang -= 1;
	return gang;

}

/**
* En metode for å hente hybelbokstav, siden dette gjøre i mer enn ett sted i programmet.
*
****************************/
    int hybel() {
	skjerm.out("Hybelbokstav: ");
        tastatur.skipWhite();
	char bokstav = tastatur.inChar();
	int hybelnr = (int) (bokstav - 'B'); // Gang B = 0, C = 1 etc ...
	return hybelnr;
}

} //Slutt class hybelhus

/**
* Denne klassen beskriver student-objektene. Hva de har betalt til GG eller skylder ham (saldo)
* og hva de heter (navn), Konstruktøren starter med å sette saldo til 9000, siden de må betale
* dette for å komme inn i en leilighet.
****************************/
class Student {
    double saldo;
    String navn;

    Student(String studentnavn) {
	navn = studentnavn;
	saldo = 9000; //Studenten må betale kr 9000 for å få seg hybel.
    }
}//Slutt klasse student

/**
* Denne klassen beskriver hybel-objektene. Pekeren leietager viser videre til sitt student-objekt.
* Utestående holder rede på strømregningen for siste måned tidligere student bodde der. Neste student
* betaler dette, men betaler igjen ikke neste regning igjen og så videre. opptatt viser om hybelen er
* opptatt eller ikke, så jeg hendig kan sjekke dette når anledningen byr seg.
****************************/
class Hybel {
    Student leietager;
    double utestående = 0;
    boolean opptatt = false;
} // Slutt på klasse Hybel


/*

hyblene[gang][hybelnr].leietager = new Student(studentNavn); // Her setter jeg studentNavn som verdi 'navn' i Student-objekt via konstruktør i Student

*/
