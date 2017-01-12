import easyIO.*;


public class oblig3C {
	public static void main (String [] args) {
		if (args.length < 1) {
			System.out.println("Bruk: > java Oblig3A <tekstfil>");
                } else {
                     OrdAnalyseA oa = new OrdAnalyseA(args[0]);
                     oa.analyser();
			}
		}
}

class OrdAnalyseA {
	private String tekstfil;

	public OrdAnalyseA(String fil) {
		tekstfil = fil;
	}

	// finne ut hvilken index ord har i ord-arrayen
	int finnOrdIndex(String[] array, String ord) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(ord)) {
				return i;
			}
		}
		return -1;
	}
	
	void analyser () {
		In fil = new In(tekstfil); // leser inn filen
		Out utfil = new Out("oppsummering.txt");
		int ordFunnet = 0;
		int unikeOrd = 0;
		final int BREDDE1 = 15;
		final int BREDDE2 = 4;
		final int BREDDE3 = 10;

		// finne antall ord
		while (!fil.lastItem()) {
			ordFunnet++;
			fil.inWord();
		 }
		 
        // lage to arrayer med funnet størrelse
		String[] ord = new String[ordFunnet];
		String[] tekst = new String[ordFunnet];
		int[] antall = new int[ordFunnet];
			 
        // finne antall ord som er unike
        int i = 0;
		fil = new In(tekstfil);
		int totaltFunnet = 0;
		 
		while (!fil.lastItem()) {
			String lestord = fil.inWord().toLowerCase();
			tekst[totaltFunnet] = lestord;
			totaltFunnet++;
			boolean funnet = false;

			for (int j = 0; j < ord.length; j++) {
				if (lestord.equals(ord[j])) {
					funnet = true;
					antall[j]++;
				}
			}
			
			if (!funnet) {
				ord[i] = lestord;
				antall[i] = 1;
				i++;
				unikeOrd++;
			}
		}
		
		// lager en 2d-array som inneholder forekomster av sekvensen i,j
		// det foerste ordet er alltid i, det andre er alltid j
		int[][] antallPar = new int[unikeOrd][unikeOrd];
		for (int par = 1; par < tekst.length; par++) {
			
			int indexA = finnOrdIndex(ord, tekst[par-1]);
			int indexB = finnOrdIndex(ord, tekst[par]);
			
			// Oek antall ganger sekvensen  
			antallPar[indexA][indexB] ++;
		}

						
			// TEST Skriver ut alle ord som etterfoelger "alice"
			int aliceIndex = finnOrdIndex(ord,"alice");
			for (int j = 0; j < unikeOrd; j++) {
				if (antallPar[aliceIndex][j] > 0) {
					System.out.println(ord[j] + " etterfoelger Alice " + antallPar[aliceIndex][j] + " ganger.");
				}
			}

    		/*
    		looper gjennom hele antall-arrayen, som har antall forekomster av hver rad,
			saa sjekker man hvert tall, om det er stoerre enn det forrige registrerte hoeyeste antallet
			og hvis det er hoeyere, bytter man flestForekomster til indeksen til ordet med flest forekomster
			*/
    	 	int flestForekomster = 0;
			for (int j = 0; j < ord.length; j++) {
				if (antall[j] > antall[flestForekomster]) {
					flestForekomster = j;
				}
			}

			for (int j = 0; j < antall.length; j++) {
				if (antall[j] > antall[flestForekomster]) {
					flestForekomster = j;
				}
			}

    		// skriver resultatene til filen "oppsummering.txt"
     		utfil.outln("Antall ord lest: " + ordFunnet + " og antall unike ord: " + unikeOrd);
     		utfil.outln();

			// skriver resultatene til filen med angitt bredde
     		for (int j = 0; j < unikeOrd; j++) {
				utfil.out(ord[j], BREDDE1);
     			utfil.outln(antall[j], BREDDE2);
     		}

     		// UTSKRIFT B
     		utfil.outln();
     		utfil.out("Vanlige ord: ");
			utfil.out(ord[flestForekomster], BREDDE3);
			utfil.out("(");
			utfil.out(antall[flestForekomster], BREDDE2);
			utfil.outln(")");

			for (int j = 0; j < ord.length; j++) {
				if (antall[j] > antall[flestForekomster]*0.10) { //10%
					utfil.out("Vanlige ord: ");
					utfil.out(ord[j], BREDDE3);
					utfil.out("(");
					utfil.out(antall[j], BREDDE2);
					utfil.outln(")");
				}
			}

     	   utfil.close();
    	   fil.close();

      }   //slutt analyser
} //slutt class OrdAnalyseA




