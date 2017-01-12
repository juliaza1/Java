import easyIO.*;
import java.util.*;
import java.lang.Math.*;


class Oblig5 {
	public static void main (String [] args) {
	
	// Oppretter et objekt av denne samme klassen for å kunne kalle
    // objektmetoder fra denne klassemetoden vha. pekeren "p".
	Planlegger p = new Planlegger(); 
	
	// leser inn data fra fil og bygger opp registeret
	p.lesFil();
	
	// går i dialog med bruker
	p.lesFraTil();
	}
} //slutt Oblig5


//________________________________________________________________________________________

class Planlegger {
	Out skjerm = new Out();
	In tast = new In();
	HashMap<String,Stasjon> stasjoner = new HashMap<String,Stasjon>();	
	HashMap<String,Linje> linjene = new HashMap<String,Linje>();
		
	//leser inn fila mens den oppretter objektene av klassene Linje og Stasjon
	void lesFil() {
		String filnavn = "TrikkOgTbane.txt";
		In fil = new In(filnavn);
		
		Linje linje = null;
		
		while(fil.hasNext()) {
			String input = fil.inLine();
			
			// Lag en ny linje
			if (input.contains("Linje")) {
				String navn = input.split(" ")[1];
				linje = new Linje(navn);
				linjene.put(navn, linje);
				
			// Dersom strengen ikke inneholder Linje, er det en stasjon vi har lest inn
			} else {
				
				// Opprett et nytt stasjonsobjekt dersom det ikke finnes fra for
				if (!stasjoner.containsKey(input)) {
					stasjoner.put(input, new Stasjon(input));
				}
				
				Stasjon stasjon = stasjoner.get(input);
				linje.nyStasjon(stasjon);
				stasjon.registrerNyLinje(linje);
			}
		}
		
		fil.close();q
	}//slutt lesFil()				
	

	void lesFraTil() {
		// Disse maa fortsette aa spoerre til man faar korrekte stasjonsnavn
		boolean verifiser = false; 
		
		do {
			System.out.println("Hvor skal du reise fra? (Skriv f.eks 'Carl-Berners-plass' eller 'Ullevål-stadion': ");
			String fraSt = tast.inWord();
			System.out.println("Hvor skal du reise til? (Skriv f.eks 'Carl-Berners-plass' eller 'Ullevål-stadion': ");
			String tilSt = tast.inWord();
			System.out.println("-----------------------------------------------------------------------------------");
		
		
			// Verifiser at stasjonne finnes
			if (stasjoner.containsKey(fraSt) && stasjoner.containsKey(tilSt)) {
				Stasjon fra = stasjoner.get(fraSt);
				Stasjon til = stasjoner.get(tilSt);
				beregnRuter(fra,til);
				verifiser = true;
			}
		} while(!verifiser);
			
	
	}//slutt lesFraFil()
	
	
	double beregnLinjereise(Linje l, Stasjon fra, Stasjon til) {
		
		double tid = 0.0;
		// Sjekk om stasjonene ligger paa samme linje
		int retning = l.stasjonsNummer(fra) - l.stasjonsNummer(til);
		tid = (float)Math.abs(retning) ;
		
		if (l.type == 0) {
			tid = tid * 1.8;
		} else {
			tid = tid * 1.4;
		}
		
		System.out.println("Ta linje " + l.linjeNavn + " i retning " + l.endestasjon(retning).navn + " til " + til.navn );
		
		return tid;
	}
	

	void beregnRuter(Stasjon fra, Stasjon til) {

		Linje[] linjer = fra.alleFellesLinjer(til);
		double tid;
		
		for (int i = 0; i < linjer.length; ++i) {
			if (linjer[i] != null) {
				tid = beregnLinjereise(linjer[i], fra,til);
				if (tid > 0.0) {
					System.out.println("Beregnet reisetid er ");
					System.out.printf("%.2f", tid);
					System.out.println(" minutter");
				}
				return;
			}
		}
		
		Stasjon[] mellom = fra.finnOvergangsreiser(til);
		
		Overgang[] overganger = new Overgang[32*12];
		int f = 0;
		
		// Opprett alle mulige overgangsreiser
		for ( int i = 0; i < mellom.length; ++i) {
			if (mellom[i] != null) {
				linjer = fra.alleFellesLinjer(mellom[i]);
				
				for (int x = 0; x < linjer.length; ++x) {
					if (linjer[x] != null) {
						Linje[] bytteTil = mellom[i].alleFellesLinjer(til);
						for (int y = 0; y < bytteTil.length; ++y) {
							if (bytteTil[y] != null) {
								// Lag den nye overgangen
								Overgang ny = new Overgang(this,linjer[x], bytteTil[y],fra,mellom[i],til);
								boolean finnes = false;
								
								// Gaa gjennom alle tidligere overganger, og se om vi har lagt til den naavaerende foer
								for (int t = 0; t < f; ++t) {
									finnes = finnes || overganger[t].identisk(ny);
								}
								
								// Dersom overgangen ikke allerede finnes, legg den til
								if (!finnes) {
									overganger[f++] = ny;
								}
							}
						}
					}
				}				
			}
		}

		double minsteTid = Double.MAX_VALUE;
		Overgang minsteOvergang = null;
		for (int o = 0; o < overganger.length; ++o) {
			if (overganger[o] != null) {
				
 				double midlertidig = overganger[o].beregnOvergang();
  				if (midlertidig < minsteTid) {
   		 			minsteTid = midlertidig;
					minsteOvergang = overganger[o];
				}			
			}	
		}
		if (minsteOvergang != null) {
  			System.out.println("---------------------------------- raskest ----------------------------------------");	
			System.out.println("");
			minsteOvergang.beregnOvergang();
			System.out.println("-----------------------------------------------------------------------------------");

  		}	
		
		//minsteOvergang.beregnOvergang();

	}//slutt beregnRuter
	
} //slutt Planlegger


//________________________________________________________________________________________

class Linje {
    String linjeNavn;
	int type; // 0 for T-bane, 1 for trikk
    int antall;
		
	Stasjon[] stasjoner = new Stasjon[32];
	
    Linje (String l) {
		linjeNavn = l;
		int nummer = Integer.parseInt(l) ;
			
		if (nummer < 10) {
			type = 0;
		} else {
			type =1 ;
		}
				
        antall = 0;
    }
	
	Stasjon endestasjon(int retning) {
		if (retning > 0) {
			return stasjoner[0];
		}
		
			return stasjoner[antall-1];
		}
		
		
		// Rreturner hvilket nummer stasjonen er paa linjen
		int stasjonsNummer (Stasjon s) {
			
			for (int i = 0; i < 32; ++i) {
				if (stasjoner[i] == s) {
					return i;
				}
			}
			
			return -1;
		}
		
		// Sjekker alle stasjonene paa denne linjen, og returner alle som har en felles linje med til-stasjonen i en array
		Stasjon[] finnOverganger (Stasjon til) {
			Stasjon[] funnet = new Stasjon[32];
			int f = 0;
			
			for ( int i = 0; i < 32; ++i) {
				if (stasjoner[i] != null && stasjoner[i].fellesLinje(til) != null) {
					funnet[f++] = stasjoner[i];
				}
			}
			return funnet;
		}
	
		
	void nyStasjon(Stasjon s) {
		stasjoner[antall++] = s;

	}//slutt nyStasjon()
	
 
}//slutt Linje



//________________________________________________________________________________________

class Stasjon {
	
	String navn;
	Linje[] linjene = new Linje[12];
	
	private int antall;
	
	// Hjelpefunksjon for  aa see om en linje gaar gjennom denne stasjonen
	boolean harLinje(Linje linje) {
		String navn = linje.linjeNavn;
		
		for (int i = 0; i < 12; ++i) {
			if (linjene[i] != null && linjene[i].linjeNavn.equals(navn)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	Linje[] alleFellesLinjer(Stasjon st) {
		Linje[] funnet = new Linje[12];
		int f = 0;
		
		for (int i = 0; i < 12; ++i) {
			if (linjene[i] != null && st.harLinje(linjene[i])) {
				funnet[f++] = linjene[i];
			}
		}
		return funnet;
	}
	
	
	Linje fellesLinje(Stasjon st) {
		for (int i = 0; i < 12; ++i) {
			if (linjene[i] != null && st.harLinje(linjene[i])) {
				return linjene[i];
			}
		}
		
		return null;
	}
	
	
	// Finn og returner alle mulige overgangsstasjoner for "til"
	Stasjon[] finnOvergangsreiser(Stasjon til) {
		Stasjon[] funnet = new Stasjon[32];
		int f= 0;
		
		for (int i = 0; i < 12; ++i) {
			if (linjene[i] != null) {
				Stasjon[] o = linjene[i].finnOverganger(til);
				
				for (int j = 0; j < o.length; ++j) {
					if (o[j] != null) {
						funnet[f++] = o[j];
					}
				}
			}
		}
		return funnet;
	}


	Stasjon (String s) {
        navn = s;
    }
	
	void registrerNyLinje(Linje nyLinje) { 
         // registrer at den angitte linja går gjennom denne Stasjonen og
         // at denne stasjonen på denne linja har det oppgitte nummeret
		linjene[antall++] = nyLinje;
	}//slutt registrerNyLinje()
}//slutt Stasjon



//________________________________________________________________________________________

class Overgang {
	// Klasse som representerer en overgangsreise
	Linje l1, l2;
	Stasjon fra,til,bytte;
	Planlegger planlegger;
	
	Overgang(Planlegger p, Linje a, Linje b, Stasjon f, Stasjon by, Stasjon t) {
		l1 = a;
		l2 = b;
		fra = f;
		til = t;
		bytte = by;
		planlegger = p;
		
	}
	
	// Sammenligne to overganger og se om de har identiske pekere
	boolean identisk (Overgang o) {
		return l1 == o.l1 && l2 == o.l2 && fra == o.fra && til == o.til && bytte == o.bytte;
	}
	
	double beregnOvergang() {
		// Beregne tiden overgangsreisen tar. Vi vet at fra og bytte ligger paa samme linje(l1), og at bytte og til ligger paa samme linje (l2).
		double tid = planlegger.beregnLinjereise(l1,fra,bytte);
		tid += 3.0;
		if (l2.type == 0) {
			tid += 7.5;
		} else {
			tid += 5.0;
		}
		tid += planlegger.beregnLinjereise(l2,bytte,til);
	
		
		System.out.print("Beregnet reisetid (inkludert bytte) er ");
		System.out.printf("%.2f", tid );
		System.out.println(" minutter");
		System.out.println();
		return tid;
	}
	
}//slutt Overgang

