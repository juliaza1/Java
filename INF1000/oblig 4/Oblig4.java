import easyIO.*;
import java.io.*;

class Oblig4 {
	public static void main(String[] args) {
	HybelHus u = new HybelHus();
	u.menyLoekke();
    }
}

//________________________________________________________________________________________

class Student {
	String navn;
	int saldo;
	
	Student(String n, int s){
		navn = n;
		saldo = s;
	}
}

//________________________________________________________________________________________

class Hybel {
	Student leietaker;
	boolean toppEtasje;
	int etasje, pris;
	String romnr;

}

//________________________________________________________________________________________

class HybelHus {
		
	Hybel[] hyblene = new Hybel[18];
	
	int maaned, aar, antallMnd, leiepris, leietopp, utgift, totalFortjeneste, gebyr;
	
	In tast = new In();
	Out skjerm = new Out();
	String DATAFIL = "Hybeldata.txt";
	
	HybelHus(){
		//innlesning fra fil
		In innfil = new In(DATAFIL); 
		
		int linje = 0;
		while(!innfil.lastItem()){
			String t = innfil.readLine();
			if(linje == 0) {
			
				//verdiene fra 1. linje lagres i egen array
				
				String[] okonomi = t.split(";");
				maaned =  Integer.parseInt(okonomi[0]);
				aar = Integer.parseInt(okonomi[1]);
				totalFortjeneste = Integer.parseInt(okonomi[2]);
				antallMnd = Integer.parseInt(okonomi[3]);
				leiepris = Integer.parseInt(okonomi[4].trim());
				leietopp = Integer.parseInt(okonomi[5].trim());
			}
			if(linje > 0) {
			
				//detaljene fra hybel lagres i hyblene[]
				
				String[] hybelInfo = t.split(";"); 
				hyblene[linje-1] = new Hybel();
				hyblene[linje-1].etasje = Integer.parseInt(hybelInfo[0]);
				hyblene[linje-1].romnr = hybelInfo[1];
				hyblene[linje-1].leietaker = new Student(hybelInfo[3],Integer.parseInt(hybelInfo[2]));
				
				//setter leieprisene for 1./2. og 3. etasje
				if (hyblene[linje-1].etasje == 3) {
					hyblene[linje-1].toppEtasje = true;
					hyblene[linje-1].pris = leietopp;
				} else {
					hyblene[linje-1].toppEtasje = false;
					hyblene[linje-1].pris = leiepris;
				}
								
            }
				linje++;
        }
		} 
		
	void menyLoekke() {
	
	int valg;
	do {
	    valg= skrivMeny();
		switch (valg) {
		case 1: skrivOversikt(); break;
			case 2: registrerNyLeietaker(); break;
    		case 3: registrerBetaling(); break;
   			case 4: registrerFrivilligUtflytting(); break;
    		case 5: maanedskjoering(); break;
    	  	case 6: kastUtLeietakere(); break;
     	 	case 7: oekHusleie(); break;  
    		case 8: save(); break;
        	default: System.out.println("Gi et tall mellom 1 og 8"); break;      
        	}
	    }while(valg!=8);
    } //slutt kommandoloekke
	
	
	int skrivMeny()
    {
	int valg;
		skjerm.outln();
        skjerm.outln("Hovedmeny");
	 	skjerm.outln("-------------------------------------------");
	 	skjerm.outln("1. Skriv oversikt");
	 	skjerm.outln("2. Registrer ny leietaker");
	 	skjerm.outln("3. Registrer betaling fra leietaker");
	 	skjerm.outln("4. Registrer frivillig utflytting");
	 	skjerm.outln("5. Månedskjøring av husleie");
	 	skjerm.outln("6. Kast ut leietakere");
	 	skjerm.outln("7. Øk husleien");
	 	skjerm.outln("8. Avslutt");
		valg = Integer.parseInt(tast.inLine());

	return valg;
    } //slutt skrivMeny()

	
	
	
	
	//------------------------------------------------case 1
	
	void skrivOversikt() {
		skjerm.outln("Skriv oversikt");	
		skjerm.outln();
		int BREDDE1 = 10;
		int BREDDE2 = 20;
		int BREDDE3 = 25;
		
		skjerm.out("Hybel", BREDDE1);
		skjerm.out("Leietaker", BREDDE2);
		skjerm.outln("Saldo", BREDDE1);
		
		skjerm.out("__________");
		skjerm.out("____________________");
		skjerm.outln("__________");
		
		skjerm.outln();
		
		//går gjennom arrayen og skriver ut til skjerm
		
		for(int i = 0; i<hyblene.length; i++) {
			skjerm.out(hyblene[i].etasje + hyblene[i].romnr,BREDDE1);
			if(hyblene[i].leietaker.navn.equals("TOM HYBEL")) {
				skjerm.out("( Ledig )", BREDDE2);
			} else {
			skjerm.out(hyblene[i].leietaker.navn,BREDDE2);}
			skjerm.outln(hyblene[i].leietaker.saldo,BREDDE1);	
		}
		skjerm.out("Måned/år og driftstid: ", BREDDE3);
		skjerm.outln(maaned + "/" + aar + ",  " + antallMnd + " mnd. i drift.");
		
		skjerm.out("Totalfortjeneste: ", BREDDE3);
		skjerm.out(totalFortjeneste);
		skjerm.outln(" kr");
		skjerm.outln();
	} // slutt skrivOversikt
	
	
	
	
	//------------------------------------------------case 2
	
	void registrerNyLeietaker() {
		skjerm.outln("Registrere ny leietaker");
		skjerm.outln();
		skjerm.outln("Ledige hybler:");
		
		//finner ledige hybler og lar brukeren velge hybel til innflytning
		//valget er mest brukervennlig ved å taste inn et nummer, istedenfor "1D", "3A",..
		
		for(int i = 0; i<hyblene.length; i++) {
			if(hyblene[i].leietaker.navn.equals("TOM HYBEL")) {
				skjerm.outln((i+1) + ": " + hyblene[i].etasje + hyblene[i].romnr);
			}
		}
		
		skjerm.outln("Vennligst velg hybel (oppgi hybelnummeret)");
		int hybelvalg = tast.inInt();
		hybelvalg--;
		if ((hybelvalg)< hyblene.length) {
			if(hyblene[hybelvalg].leietaker.navn.equals("TOM HYBEL")) {
				skjerm.outln("Oppgi navn");
				String navn = tast.inLine();
				
				//lager et nytt Studentobjekt med navn og saldo
				
				hyblene[hybelvalg].leietaker = new Student(navn, 15000-hyblene[hybelvalg].pris);	
				
				skjerm.outln(navn + " har flyttet inn i hybel" + ": " + hyblene[hybelvalg].etasje + hyblene[hybelvalg].romnr);
				skjerm.outln("Gjenværende saldo: " + hyblene[hybelvalg].leietaker.saldo); 
							
			} else { 
				skjerm.outln(hybelvalg + " er ikke en ledig hybel.");
			}
		} else {
			skjerm.outln(hybelvalg + " er ikke en ledig hybel.");
		} 	
	}
	
	
	
	
	//------------------------------------------------case 3
	
	void registrerBetaling() {
		skjerm.outln("Registrere betaling fra leietaker");
		skjerm.outln();
		
		//skriver ut en liste med hyblene som er bebodde
		
		for(int i = 0; i<hyblene.length; i++) {
			if(!hyblene[i].leietaker.navn.equals("TOM HYBEL")) {
				skjerm.outln((i+1) + ": " + hyblene[i].etasje + hyblene[i].romnr + "  " + hyblene[i].leietaker.navn);
			}
		}
		skjerm.outln("Hvem vil du registrere innbetaling på? (oppgi nummeret)");
		int leietakerValg = tast.inInt();
		leietakerValg--;
		if (leietakerValg < hyblene.length) {
			if(!hyblene[leietakerValg].leietaker.navn.equals("TOM HYBEL")) {
				skjerm.outln("Oppgi beløpet");
				int beloep = tast.inInt();
				
				hyblene[leietakerValg].leietaker.saldo += beloep;	
				
				skjerm.outln("Gjenværende saldo: " + hyblene[leietakerValg].leietaker.saldo); 
				
				
			} else { 
				skjerm.outln(leietakerValg + " er ikke et gyldig valg");
			}
		} else {
			skjerm.outln(leietakerValg + " er ikke et gyldig valg");
		} 		
	}
	
	
	
	
	//------------------------------------------------case 4	
	
	void registrerFrivilligUtflytting() {
	
		boolean funnet = false;
		skjerm.outln("Registrere frivillig utflytting");
		skjerm.outln();
		
		for(int i = 0; i<hyblene.length; i++) {
			if(!hyblene[i].leietaker.navn.equals("TOM HYBEL")) {
				skjerm.outln(hyblene[i].etasje + hyblene[i].romnr + "  " + hyblene[i].leietaker.navn);
			}
		}
		skjerm.outln("Hvem skal flytte ut? (Skriv navnet)");
		String navnUt = tast.inLine();
		
		//går gjennom løkka og når oppgitt navn ble funnet, setter den hybelnavn på TOM HYBEL og saldoen på 0
		
		for(int i = 0; i<hyblene.length; i++) {
			if(hyblene[i].leietaker.navn.equals(navnUt)) {
				hyblene[i].leietaker.navn = "TOM HYBEL"	;
				hyblene[i].leietaker.saldo = 0;	
				funnet = true;	
				skjerm.outln(navnUt + " har nå flyttet ut og hybel " + hyblene[i].etasje + hyblene[i].romnr + " er nå ledig.");	
			} 
		}if (!funnet) {
				skjerm.outln("Den du leter etter, bor ikke her");
			}
	} // slutt frivillig utflytting



	//------------------------------------------------case 5
        
	void maanedskjoering() {
		skjerm.outln("Månedskjøring av husleie");
		skjerm.outln();
		utgift = (1200 * 18) + (1700 * 3);
		int totalFortjenesteFOER = totalFortjeneste; 

		if (maaned == 12) {
			skjerm.outln("Ønsker du å utføre månedskjøring for måned 1/" + (aar + 1) + "? [j/n]: ");
			}else{ 
			skjerm.outln("Ønsker du å utføre månedskjøring for måned " + (maaned +1) + "/" + aar + "[j/n]: ");
			}
		String svar = tast.inWord();
			if (svar.equalsIgnoreCase("n") || (!svar.equalsIgnoreCase("n") && !svar.equalsIgnoreCase("j"))){
					return;	}
			if(svar.equalsIgnoreCase("j")) {
        		for (int i = 0; i < hyblene.length; i++) {
					if (!hyblene[i].leietaker.navn.equals("TOM HYBEL")) {
					  	if (hyblene[i].leietaker.saldo > 0) {
							if (hyblene[i].leietaker.saldo - hyblene[i].pris < 0) {
       	 						totalFortjeneste = totalFortjeneste + hyblene[i].leietaker.saldo;
      						}
      						else {
        						totalFortjeneste = totalFortjeneste + hyblene[i].pris;
      						}
    					}hyblene[i].leietaker.saldo = hyblene[i].leietaker.saldo - hyblene[i].pris;
    					
  					}
				}
			}	
				antallMnd++; // totalt antall måneder systemet har vært i bruk oppdateres
     			int forrigeMaaned = maaned; // Måned for gjeldende månedskjøring
    			int forrigeAar = aar; // Årstall for gjeldende månedskjøring
    			boolean aarSkifte = false;
    			if (maaned == 12) { // Hvis det er den siste måneden i året, settes måned til 1 og år oppdateres
    	   			maaned = 1;
           			aar++;
           			aarSkifte = true;
           			
           			if(aarSkifte) {	
           				 
                    	totalFortjeneste = totalFortjeneste - utgift; 
                       
                       // Utskrift  
                        skjerm.outln();                            
                        skjerm.outln("a) Månedskjøring er gjennomført for: " + maaned + "/" + aar + " driftstid: " + antallMnd + " måneder.");
                        skjerm.outln("b) Husleiesatsene: 1. og 2. etasje " + leiepris + " kr, 3. etasje: " + leietopp + " kr");
                        skjerm.outln("c) Månedens fortjeneste: " + (totalFortjeneste - totalFortjenesteFOER) + " kr");
                        skjerm.outln("d) Totalfortjeneste: " + totalFortjeneste);
                        skjerm.outln("e) Gjennomsnittlig månedsfortjeneste: "+ (totalFortjeneste/antallMnd));  
                             
                    }   
           			
    			 } else { 
					maaned++;
                    totalFortjeneste = totalFortjeneste - utgift; // Kostnader for vedlikehold trekkes fra totalfortjeneste
                       
                       // Utskrift  
                        skjerm.outln();                            
                        skjerm.outln("a) Månedskjøring er gjennomført for: " + maaned + "/" + forrigeAar + " driftstid: " + antallMnd + " måneder.");
                        skjerm.outln("b) Husleiesatsene: 1. og 2. etasje " + leiepris + " kr, 3. etasje: " + leietopp + " kr");
                        skjerm.outln("c) Månedens fortjeneste: " + (totalFortjeneste - totalFortjenesteFOER) + " kr");
                        skjerm.outln("d) Totalfortjeneste: " + totalFortjeneste);
                        skjerm.outln("e) Gjennomsnittlig månedsfortjeneste: "+ (totalFortjeneste/antallMnd));         
                       
                        }
							
	}//slutt maanedskjoering
		
	
	
	//------------------------------------------------case 6
	
	void kastUtLeietakere() {
        gebyr = 3000;
		boolean noenKastetUt = false;
        for (int i = 0; i < hyblene.length; i++) {
                if(hyblene[i].leietaker.saldo < -hyblene[i].pris) {  
					tilkallHole(hyblene[i].etasje, hyblene[i].romnr, hyblene[i].leietaker.navn, ((hyblene[i].leietaker.saldo * -1) + gebyr));
					totalFortjeneste = totalFortjeneste + (hyblene[i].leietaker.saldo * -1) + gebyr;
					hyblene[i].leietaker.navn = "TOM HYBEL";
					hyblene[i].leietaker.saldo = 0;
					noenKastetUt = true;
					}
        }      	
		if (!noenKastetUt)
			skjerm.outln("Ingen skal kastes ut!");          
        }//slutt kastUtLeietakere
	
	//--------------------------------------
	
	void tilkallHole(int etasje, String romnr, String navn, int krav) { 

        Out torpedo = new Out("torpedo.txt", true); 
        skjerm.outln("Hybel: " + etasje + romnr + "\nLeietaker: " + navn + "\nPengekrav: " + krav); 

		torpedo.outln();
        torpedo.outln("Hybel: " + etasje + romnr);
        torpedo.outln("Leietaker: " + navn);
        torpedo.outln("Pengekrav: " + krav);
        torpedo.close();
        
    } //slutt tilkallHole
    
    
	
	//------------------------------------------------case 7
	
	void oekHusleie() {
		int BREDDE4 = 42;
		skjerm.outln();
		skjerm.outln("Øk husleien");
		skjerm.outln();
		skjerm.outln("Gjeldene husleiesatser: " + leiepris + " kr for 1. og 2. etasje og " + leietopp + " kr for 3. etasje.");
		skjerm.out("Oppgi ny leiepris for etasje 1 og 2: (kr) ", BREDDE4);
		
		int nyNede = tast.inInt();
		skjerm.out("Oppgi ny leiepris for 3. etasje: (kr) ", BREDDE4);
		int nyOppe= tast.inInt();
		skjerm.outln();
		leiepris = nyNede; 
		leietopp = nyOppe;
		
		//utfører save() sånn at de nye prisene er lagret uten at man må avslutte programmet
	
		save();
	}//slutt oekHusleie
	
	
	
	//------------------------------------------------case 8
	
	void save() {
	 
        Out utfil = new Out("hybeldata.txt"); 
        
		//layout for hybeldata.txt
        utfil.out(maaned + ";");
        utfil.out(aar + ";");
        utfil.out(totalFortjeneste + ";");
        utfil.out(antallMnd + ";");
       	utfil.out(leiepris + ";");
        utfil.outln(leietopp);
        

        // Skriver ut en linje for hver bolig
        for (int i = 0; i < hyblene.length; i++) {
            utfil.out(hyblene[i].etasje + ";");
            utfil.out(hyblene[i].romnr + ";");
            utfil.out(hyblene[i].leietaker.saldo + ";");
            utfil.outln(hyblene[i].leietaker.navn);
        }
        utfil.close();
    }//slutt save
	
} // slutt HybelHus

	