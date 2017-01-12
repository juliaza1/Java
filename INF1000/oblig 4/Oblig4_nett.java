import easyIO.*;

class Oblig4_nett {
	 public static void main(String[] args) {

        Hybelhus hh = new Hybelhus(); //Klassen klargjøres med pekeren hh
        hh.lesFil(); // Leser inn fra tekstfil 
        hh.meny(); // Skriver ut meny
        hh.utskrift(); // Skriver til fil hvis programmet avsluttes
        System.out.println("\n * * * Programmet avsluttes * * * \n");
    }
}

// ____________________________________________________________________________________________________________________________________________

class Hybelhus {

    Hybel[] hybel = new Hybel[18]; // Klargjør array for objekter av klassen Hybel
    In tast = new In(); // Klargjør for inntast
    Out skjerm = new Out(); // Klargjør for utskrift

    int måned; // Gjeldende måned for utleiesystemet
    int år; // Gjeldende år
    int totalfortjeneste; // Den totale fortjenesten er inntekter minus utgifter for totalt antall måneder
    int totaltAntallMåneder; // Antall måneder i drift
    int etasje1_2 = 7500; // Pris på de nedre etasjene
    int etasje3 = 9000; // Pris på toppetasjen
    int vedlikehold = (1200 * 18) + (1700 * 3); // Månedlige avgifter for vedlikehold. 1400 kr pr. hybel, 2100 kr pr. etasje
    int gebyr = 3000; // Gebyr ved utkastelse


    // ................................................................................................................................................   


    void lesFil() { // Leser inn fra tekstfil

        In hdt = new In("hybeldata.txt"); 	// Velger tekstfil
        String[] hd = new String[4]; 		// Array med verdier fra hver linje i tekstfil
        int lnr = 0; 						// Teller for innlesing av linjer i tekstfil
        while (!hdt.endOfFile()) { 			// Leser til filens slutt
            String s = hdt.readLine(); 
            if (lnr > 0) { 					// Hopper over den første linjen
                int j = lnr-1; 				// lnr-1 brukes for å fylle arrayet fra [0])
                hd = s.split(";");
			    hybel[j] = new Hybel(); 	// Lager hybelobjekt
                hybel[j].etasje = Integer.parseInt(hd[0]);
                hybel[j].bokstav = hd[1];
                hybel[j].saldo = Integer.parseInt(hd[2]); 
                hybel[j].leietager = hd[3];

                // Lagrer pris for 3 etasje

                if (hybel[j].etasje == 3) {
                    hybel[j].pris = etasje3; 
                    hybel[j].topp = true;
                } else {

                    // Lagrer pris for 1. og 2. etasje

                    hybel[j].pris = etasje1_2;
                    hybel[j].topp = false;
                }
            } else { 

                // Lagrer informasjon for drift av systemet

                hd = s.split(";");
				måned = Integer.parseInt(hd[0]);
                år = Integer.parseInt(hd[1]);
                totalfortjeneste = Integer.parseInt(hd[2]);
                totaltAntallMåneder = Integer.parseInt(hd[3]);
            }
            lnr++;
        }
    }    

// ................................................................................................................................................
 
 
    void meny() {
        int ordre = 0;
        while (ordre != 7) { // 7 avslutter programmet
            System.out.print("\n========== UTSYN  EIENDOM =========="
                             +"\n1. Skriv oversikt"
                             +"\n2. Registrer ny leietager"
                             +"\n3. Registrer betaling fra leietager"
                             +"\n4. Registrer frivillig utflytting"
                             +"\n5. Månedskjøring av husleie"
                             +"\n6. Kast ut leietagere"
                             +"\n7. Øk husleien"
                             +"\n8. Avslutt"
                             +"\n====================================\n");
            System.out.print("\nVelg alternativ: ");
            ordre = tast.inInt();

            if ((ordre < 1) || (ordre > 7)) // Sjekker at valgt alternativ er fra menyen

                System.out.println("\nVelg mellom alternativ 1-7");            
            
            switch (ordre) {
            case 1: skrivOversikt(); break;
            case 2: registrerNyLeietager(); break;
            case 3: registrerBetalingFraLeietager(); break;
            case 4: registrerFrivilligUtflytting(); break;
            case 5: månedskjøringAvHusleie(); break;
            case 6: kastUtLeietagere(); break;
        //    case 7: oekHusleie(); break;  //MANGLER
            default: break;     
            }
        }
    }

// ................................................................................................................................................    

    void skrivOversikt() {

        // Layout - Definerer bredde på kolonnene

        final int BREDDE1 = 7;
        final int BREDDE2 = 21;  
        final int BREDDE3 = 8;

        skjerm.outln(); // Layout
        skjerm.out("Hybel", BREDDE1);
        skjerm.out("Leietager", BREDDE2);
        skjerm.outln("Saldo", BREDDE3, Out.RIGHT);
        skjerm.outln("------ -------------------- --------");

        for (int i = 0; i < hybel.length; i++) { // Går gjennom hybelobjektene

            skjerm.out(hybel[i].etasje + hybel[i].bokstav, BREDDE1); // Skriver ut etasje og bokstav
            if (hybel[i].leietager.equals("TOM HYBEL")) {
                skjerm.out("( LEDIG )", BREDDE2);  // Ser etter TOM HYBEL og skriver eventuelt ut ( LEDIG )
                skjerm.outln(hybel[i].saldo, BREDDE3, Out.RIGHT); // Skriver ut leietagers saldo
            } else { 
                skjerm.out(hybel[i].leietager, BREDDE2); // Skriver ut navn på leietager
                skjerm.outln(hybel[i].saldo, BREDDE3, Out.RIGHT); // Skriver ut leietagers saldo
            }
        }
        skjerm.outln("------ -------------------- --------"); // Skriver ut verdier for drift
        skjerm.outln("Måned/år: " + måned + "/" + år);
        skjerm.outln("Antall måneder i drift: " + totaltAntallMåneder);
        skjerm.outln("Totalfortjeneste: " + totalfortjeneste);
    }    

 // ................................................................................................................................................   

    void registrerNyLeietager() {

        boolean b = false; // Blir true hvis det finnes ledig bolig
        boolean b2 = false; // Blir true hvis valgt bolig finnes
        boolean b3 = false; // Blir true hvis boligen er opptatt

        String leietager; // Navn på leietager fra inntast
        String bolig; // Navn på bolig fra inntast
        String bokstav; // Bokstav fra bolig.substring

        int etasje; // Etasje fra bolig.substring
        int nr = -1; // Brukes til arraynummer for valgt bolig
        int depositum = 10000; // Fast depositum ved registrering av ny leietager

        System.out.println(); // Layout

        for (int i = 0; i < hybel.length; i++) { // Går gjennom hybelarray

            if (hybel[i].leietager.equals("TOM HYBEL")) { // Ser etter TOM HYBEL
                b = true; // true for ledig bolig              
                System.out.println("Bolig " + hybel[i].etasje + hybel[i].bokstav + " er ledig"); 
            }
        }

        System.out.println(); // Layout

        if (b == true) { // Hvis det finnes ledige boliger kan boligen velges fra inntast
            System.out.print("Velg bolig for utleie: ");
            bolig = tast.inLine();
            etasje = Integer.parseInt(bolig.substring(0, 1)); // Konverterer String til int
            bokstav = bolig.substring(1); // Henter bokstav fra inntastet bolignavn

            for (int i = 0; i < hybel.length; i++) { 

                if ((etasje == hybel[i].etasje) && (bokstav.equalsIgnoreCase(hybel[i].bokstav))) { // Sjekker om bolig fra inntast finnes i Hybel[]
                    b2 = true; // true hvis bolig finnes
                    nr = i; // Boligens arraynummer
				}
            }

            if (b2 == true) { // true hvis valgt bolig finnes
                
                if ((hybel[nr].leietager.equals("TOM HYBEL"))) // Sjekker om valgt bolig er ledig

                    b3 = true; // true hvis valgt bolig er ledig
            } else 

                System.out.println("\nBolig " + bolig + " finnes ikke");

            if (b3 == true) { // true hvis valgt bolig er ledig
                
                System.out.print("\nNavn på leietager: "); 
                leietager = tast.inLine(); // Navn på leietager fra inntast
                hybel[nr].leietager = leietager; // Navn fra inntast lagres i objekt
                hybel[nr].saldo = depositum - hybel[nr].pris; // Depositum minus månedsleie lagres i objekt
                totalfortjeneste = totalfortjeneste + hybel[nr].pris; // Månedsleie legges til totalfortjeneste

                System.out.println("\nBolig " + bolig.toUpperCase() + " er leid ut til " + hybel[nr].leietager 
                                   + ". Saldo for " + hybel[nr].leietager + " er: " + hybel[nr].saldo);     
            }
            
            if (b3 == false && b2 == true) // Hvis bolig er opptatt, skrives det ut feilmelding
                System.out.println("\nBolig " + bolig.toUpperCase() + " er opptatt");
        }
    }

// ................................................................................................................................................        

    void registrerBetalingFraLeietager() {

        boolean b = false; // true hvis inntastet bolignavn finnes
        boolean b2 = false; // true hvis valgt bolig er opptatt

        String bolig; // Navn på bolig fra inntast
        String bokstav; // Bokstav fra bolig.substring

        int etasje; // Etasje fra bolig.substring      
        int beløp; // Ønsket beløp fra inntast som skal legges til saldo
        int nr = -1; // Her lagres arraynummer for valgt bolig

        System.out.print("\nBolig: ");
        bolig = tast.inLine(); // Bolignavn fra inntast

        // bolig deles i etasje og bokstav

        etasje = Integer.parseInt(bolig.substring(0, 1)); 
        bokstav = bolig.substring(1); 

        for (int i = 0; i < hybel.length; i++) { 

            if ((etasje == hybel[i].etasje) && (bokstav.equalsIgnoreCase(hybel[i].bokstav))) { // Ser etter inntastet bolignavn i Hybel[]
                b = true; // true hvis bolignavn finnes
                nr = i;
			}
        }
    
        if (b == true) { // true hvis boligen finnes
            if ((hybel[nr].leietager.equals("TOM HYBEL"))) // Skriver ut feilmelding hvis bolig er tom
                System.out.println("\nBolig " + bolig.toUpperCase() + " har ingen leietager");
            else 
                b2 = true; // true hvis inntastet bolig har leietager
        }

        else 
            System.out.println("\nBolig " + bolig + " finnes ikke");

        if (b2 == true) { // true hvis inntastet bolig har leietager
            System.out.print("\nBeløp: ");
            beløp = tast.inInt(); // Beløp som skal settes inn fra inntast

            // Hvis saldo etter innbetaling er i minus, legges innbetalt beløp til totalfortjeneste og saldo oppdateres
            if (hybel[nr].saldo + beløp < 0) { 

                totalfortjeneste = totalfortjeneste + beløp;
                hybel[nr].saldo = hybel[nr].saldo + beløp;
            }

            // Hvis saldo er i minus, men i pluss etter innbetaling, legges det som leietager skyldte til totalfortjeneste, og saldo oppdateres
            else if (hybel[nr].saldo < 0 && hybel[nr].saldo + beløp > 0) {

                totalfortjeneste = totalfortjeneste + hybel[nr].saldo * -1;
                hybel[nr].saldo = hybel[nr].saldo + beløp;
            }

            else
                hybel[nr].saldo = hybel[nr].saldo + beløp;         
            System.out.println("\nInnbetaling: " + beløp + "\nBolig: " + bolig.toUpperCase() 
                               + "\nLeietager: " + hybel[nr].leietager + "\nSaldo etter innbetaling: " + hybel[nr].saldo);
        }
    }

 // ................................................................................................................................................   

    void registrerFrivilligUtflytting() {

        boolean b = false; // true hvis inntastet navn finnes   
        int nr = -1; // Lagrer arraynummer for inntastet leietager

        String navn; // Navn på leietager fra inntast
        System.out.print("\nLeietager: "); 
        navn = tast.inLine(); // Lagrer navn fra terminal

        for (int i = 0; i < hybel.length; i++) { 

            if (navn.equalsIgnoreCase(hybel[i].leietager)) { // Ser etter inntastet navn i hybelobjekter
                nr = i; // Arraynummer for inntastet leietager
                b = true; // true hvis navnet blir funnet
            }
        }

        if (b == false) // false hvis navnet ikke finnes, feilmelding skrives i så fall ut

            System.out.println("\nFinner ingen leietagere med navnet " + navn); // Kjører hvis navnet ikke finnes
        else if (hybel[nr].saldo < 0) // Hvis saldo er i minus, avbrytes utflytting
            System.out.println("\nSaldo er i minus. Flytting kan ikke gjennomføres");
        else {  // Hvis  saldo er i pluss, gjennomføres flyttingen
            String s = hybel[nr].leietager; // Lagrer navn på leietager i midlertidig streng
            hybel[nr].leietager = "TOM HYBEL"; // Boligen settes til ledig
            hybel[nr].saldo = 0; // Saldo nullstilles
            System.out.println("\n" + s + " er fjernet som leietager. Bolig " + hybel[nr].etasje + hybel[nr].bokstav + " er ledig");
        }
    }   

 // ................................................................................................................................................   

    void månedskjøringAvHusleie() {

        if (måned == 12) // Hvis det er måned 12, skrives første måned for neste år ut 
            System.out.print("\nØnsker du å utføre månedskjøring for 1/" + (år + 1) + "? (J/n): ");
        else 
            System.out.print("\nØnsker du å utføre månedskjøring for " + (måned + 1) + "/" + år + "? (J/n): ");
        String s = tast.inLine(); // Ja eller nei fra inntast
       
        if (s.equalsIgnoreCase("j")) { // Kjører hvis bruker har valgt ja
            int gammeltotalfortjeneste = totalfortjeneste; // Fortjeneste før månedskjøring
            for (int i = 0; i < hybel.length; i++) {

                if (!hybel[i].leietager.equals("TOM HYBEL")) { // Tomme hybler ignoreres

                    if (hybel[i].saldo > 0) { // Sjekker at saldoen er i pluss

                        if (hybel[i].saldo - hybel[i].pris < 0) // Hvis saldo er i minus etter månedskjøring, legges saldo til totalfortjeneste
                            totalfortjeneste = totalfortjeneste + hybel[i].saldo;

                        else { // Hvis saldo er i pluss etter månedskjøring, legges pris til totalfortjeneste
                            totalfortjeneste = totalfortjeneste + hybel[i].pris;
                        }
                    }
                    hybel[i].saldo = hybel[i].saldo - hybel[i].pris; // Saldo trekkes fra
                }
            }

            totaltAntallMåneder = totaltAntallMåneder + 1; // Oppdaterer totalt antall måneder systemet har vært i bruk
            int gammelmåned = måned; // Måned for gjeldende månedskjøring
            int gammeltår = år; // Årstall for gjeldende månedskjøring
            if (måned == 12) { // Hvis det er den siste måneden i året, settes måned til 1 og år oppdateres
                måned = 1;
                år = år + 1;
            }

            else // Hvis måned ikke er 12 legges 1 til måned
                måned = måned + 1;

            totalfortjeneste = totalfortjeneste - vedlikehold; // Kostnader for vedlikehold trekkes fra totalfortjeneste

            // Utskrift   
            skjerm.outln();
            skjerm.out("Månedskjøring er gjennomført for: ");
            skjerm.outln(gammelmåned + "/" + gammeltår);
            skjerm.out("Måned/år: ");
            skjerm.outln(måned + "/" + år);
            skjerm.out("Antall måneder system har vært i bruk: ");
            skjerm.outln(totaltAntallMåneder);
            skjerm.out("Månedens fortjeneste: ");
            skjerm.outln((totalfortjeneste - gammeltotalfortjeneste));
            skjerm.out("Totalfortjeneste: ");
            skjerm.outln(totalfortjeneste);
            skjerm.out("Gjennomsnittlig månedsfortjeneste: ");
            skjerm.outln(totalfortjeneste/totaltAntallMåneder);
        }
        
        else // Hvis brukes velger "n", returnerer programmet til hovedmeny
            return;
    }

// ................................................................................................................................................    

    void kastUtLeietagere() { 

        for (int i = 0; i < hybel.length; i++) {

            if (hybel[i].saldo < -hybel[i].pris) { // Eksekverer hvis saldo er lavere enn minus en måneds husleie 
                tilkallHole(hybel[i].etasje, hybel[i].bokstav, hybel[i].leietager, ((hybel[i].saldo * -1) + gebyr)); // Gir Hole informasjon om bolig, navn og krav
                totalfortjeneste = totalfortjeneste + (hybel[i].saldo * -1) + gebyr; // Legger til det leietager har i minus på saldo + gebyr til totalfortjeneste
                hybel[i].leietager = "TOM HYBEL"; // Setter hybelen som ledig
                hybel[i].saldo = 0; // Nullstiller saldo
            }
        }
    }

// ................................................................................................................................................    

    void tilkallHole(int etasje, String bokstav, String navn, int krav) { 

        Out torpedo = new Out("torpedo.txt", true); // Gjør klar for utskrift til fil
        System.out.println("\nEtasje: " + etasje + bokstav + "\nLeietager: " + navn + "\nPengekrav: " + krav); 

        // Skriver bolignavn, navn på leietager og pengekrav  til tekstfil

        torpedo.outln(etasje + bokstav);
        torpedo.outln(navn);
        torpedo.outln(krav + "\n");
        torpedo.close();
    }

// ................................................................................................................................................    

    void utskrift() { // Skriver til tekstfil

        Out utfil = new Out("hybeldata.txt"); // Klargjør for utskrift til fil

        // Skriver første linje, som er informasjon ang. drift av systemet
        utfil.out(måned + ";");
        utfil.out(år + ";");
        utfil.out(totalfortjeneste + ";");
        utfil.outln(totaltAntallMåneder);

        // Skriver ut en linje for hver bolig
        for (int i = 0; i < hybel.length; i++) {
            utfil.out(hybel[i].etasje + ";");
            utfil.out(hybel[i].bokstav + ";");
            utfil.out(hybel[i].saldo + ";");
            utfil.outln(hybel[i].leietager);
        }
        utfil.close();
    }
}


// ____________________________________________________________________________________________________________________________________________
 

class Hybel {

    boolean topp; // true hvis bolig ligger i etasje 3
    int pris; // Pris for en måneds husleie
    int etasje; // Boligens etasjenummer
    int saldo; // Leietagers saldo

    String bokstav; // Boligens bokstav
    String leietager; // Navn på boligens leietager

}
