import easyIO.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* 
 * En Runnable som fletter to sorterte lister.
 * 
 * 
 */
class Fletter implements Runnable {
	String[] dataA, dataB;
	SorteringsHjelper hjelper;
	
	public Fletter(String[] a, String[] b, SorteringsHjelper h) {
		dataA = a;
		dataB = b;
		hjelper = h;
	}
	
	public static String[] flett(String[] dataA, String[] dataB) {
		// Sett av nok plass for den kombinerte arrayen
		String[] resultat = new String[dataA.length + dataB.length];
		int a = 0;
		int b = 0;
		
		for (int i = 0; i < resultat.length; ++i) {
			// Dersom vi har toemt A, setter vi inn elementene fra B
			if (a == dataA.length) {
				resultat[i] = dataB[b++];
			// Det samme gjelder dersom B er toemt
			} else if (b == dataB.length) {
				resultat[i] = dataA[a++];
			// Dersom begge listene fortsatt har elementer, setter vi inn det laveste av de to.
			} else if (dataA[a].compareTo(dataB[b]) <= 0) {
				resultat[i] = dataA[a++];
			} else {
				resultat[i] = dataB[b++];
			}
		}
		
		return resultat;
		
	}
	
	public void run() {
		hjelper.settInnLosning(flett(dataA, dataB));
	}
}

/*
 * Runnable som gjoer den initielle sorteringen av listene ved hjelp av merge sort.
 * 
 */
class Sorterer implements Runnable {
	String[] data;
	SorteringsHjelper hjelper;
	
	public Sorterer(String[] d, SorteringsHjelper h) {
		data = d;
		hjelper = h;
	}
	
	/*
	 * Sorter dataen i dette objektet, og si fra til hjelperen om resultatet
	 */
	public String[] sorter(String[] liste) {
		
		// En liste med lengde 1 er sortert
		if (liste.length == 1) {
			return liste;
		}
		
		int midt = liste.length / 2;
		String[] venstre = Arrays.copyOfRange(liste, 0, midt);
		String[] hoyre = Arrays.copyOfRange(liste, midt, liste.length);
		
		// Rekursivt sorter de to listene
		venstre = sorter(venstre);
		hoyre = sorter(hoyre);
		
		// Bruk fletteren vi allerede har for aa merge listene
		return Fletter.flett(venstre, hoyre);
	}
	
	public void run() {
		hjelper.settInnLosning(sorter(data));
	}
}

/*
 * HJelpeklasse som holder styr paa aktive arbeidere, og resultatene fra disse.
 */
class SorteringsHjelper {
	List<String[]> arrayer;
	
	int aktive = 0;	// Aktive traader denne hjelperen hjelper

	private void frigiArbeider(int antall) {
		aktive -= antall;
	}
	
	synchronized boolean ferdig() {
		return aktive == 0;
	}
	
	/*
	 * Konstruer en hjeper. Initialiser antall arbeidere til maksimalt antall
	 */
	public SorteringsHjelper(int a) {
		aktive = a;
		arrayer = Collections.synchronizedList(new ArrayList<String[]>());
	}
	
	/*
	 * Legg inn en sortert/flettet array i losningssettet.
	 * Dersom 2 eller flere sett er i den interne listen, start en fletter
	 * som fletter sammen de 2.
	 * 
	 * Naar det kun er 1 liste og 1 aktiv arbeider er jobben ferdig, og den siste arbeideren frigis.
	 */
	public synchronized void settInnLosning(String[] l) {
		arrayer.add(l);
		
		if (arrayer.size() >= 2) {
			// Flett disse 2
			String[] a = arrayer.remove(0);
			String[] b = arrayer.remove(0);
			
			// Frigi 1 arbeider, siden det starter en ny rett etter
			frigiArbeider(1);
			
			// Lag en fletter som fletter de to oeverste arrayene
			Thread fletter = new Thread(new Fletter(a,b,this));
			fletter.start();
		} else if (arrayer.size() == 1 && aktive == 1) {
			// Naar det er ett element og 1 aktiv arbeider igjen, er vi ferdige og kan frigi den siste.
			frigiArbeider(1);
		}
	}
	
	public String[] hentLosning() {
		while (!ferdig()) {
		}
		
		return arrayer.get(0);
	}
}

class Sort {
	private static int threadCnt = 0;
	private static int wordCnt = 0;
	private static String innfil, utfil;
	private static String[] ord;
	
	
	
	public static void main (String[] param ) {	
//		maa sjekke om 1. er INT
		if (param.length == 3 ) {
			try {
				threadCnt = Integer.parseInt(param[0]);
			} catch (NumberFormatException e) {
				feil("feil: traadantallet er ikke et heltall");
			}
			innfil = param[1];
			utfil = param[2];
			
			lesInn();
			String[] resultat = sorter(ord, threadCnt);
			
			Out fil = new Out(utfil);
			
			for (int i = 0; i < resultat.length; ++i) {
				fil.outln(resultat[i]);
			}
			
			fil.close();
			
			
			/*
			 * Tidsmaaling:
			 * while (x < 10000) {
				lesInn();

				long startTime = System.currentTimeMillis();
				String[] resultat = sorter(ord, threadCnt);
			
				long endTime = System.currentTimeMillis();

				long duration = endTime - startTime;
				gjennom += duration;
				x++;
			}

			gjennom = gjennom / 10000;
			System.out.println(gjennom);
			*/
			
        } else {
            System.out.println("Bruk 3 parametrer [Antall traader, Innfil, Utfil]");
        }
	}	 

	
	/*
	 * Start traader for aa sortere og sammenflette alle ordene
	 */
	static String[] sorter(String[] liste, int trader) {
		int vindu = liste.length / trader;
		// Finn ut hvor mange traader som maa kjoere med ett ekstra element
		int ekstra = liste.length % trader;
		SorteringsHjelper hjelper = new SorteringsHjelper(threadCnt);
		
		int start = 0;
		
		for (int i = 0; i < threadCnt; ++i) {
			// Kalkuler start og sluttindekser traaden skal sortere
			int slutt = start + vindu;
			
			// Saa lenge ekstra ikke er 0 maa vi kjoere N+1 elementer i denne traaden
			if (ekstra > 0) {
				slutt = slutt + 1;
				ekstra--;
			}
			
			Thread arbeider = new Thread(new Sorterer(Arrays.copyOfRange(liste, start, slutt), hjelper));
			// Lag en arbeidertraad som sorterer sitt vindu
			arbeider.start();
			start = slutt;
		
		}
		
		if (start != ord.length) {
			feil("Start != length etter kjoering");
		}
		
		String[] resultat = hjelper.hentLosning();
		
		if (resultat.length != wordCnt) {
			feil("Antall leste ord matcher ikke antall sorterte ord");
		}
		
		if (resultat[resultat.length-1] == null) {
			feil("Siste element i den sorterte listen er en nullpeker");
		}
		
		return resultat;
	}
	
	public static void lesInn() {
		 int linje = 0;
	     In fil = new In(innfil);  
	     
	     while(!fil.lastItem()) {
	    	if(linje == 0) {
	        	String t = fil.inLine();
	            wordCnt = Integer.parseInt(t);
	            ord = new String[wordCnt];
	            linje++;
            } else if (linje <= wordCnt) {
            	// linje-1 er ord-indeksen. Det betyr at verdien av linje skal gaa opp til wordCnt
	    		ord[linje-1] = fil.inLine();
	            linje++;
	        } else {
	        	feil("Feil i inputfilen: for mange ord i filen");
	        }
	     }
	     
	     // Om linje ikke er storre enn wordCnt her betyr det at filen hadde for faa ord
	     if (linje <= wordCnt) {
	    	 feil("Feil i inputfilen: for faa ord i filen");
	     }
	     
	     fil.close();
	}
	
	static void feil(String melding) {
		System.out.println(melding);
   	 	System.exit(1);
	}
} // slutt Sort






