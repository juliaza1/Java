import java.util.*;
import java.io.*;
import easyIO.*;


class oblig3B {
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
         	 
        // lage 2 arrayer med funnet størrelse 
          String[] ord = new String[ordFunnet];  
          int[] antall = new int[ordFunnet];
          
          
        // finne antall ord som er unike  
        fil = new In(tekstfil);
        int i = 0;
        	while (!fil.lastItem()) {
          		String lestord = fil.inWord().toLowerCase();
          		boolean funnet = false;
          		for (int j = 0; j < ord.length; j++) {
          			if (lestord.equals(ord[j])) {
          				funnet = true;
          				antall[j]++;
          			}
          		}
          		
       			// kommentar!!!!!!!!!!!!!!!!!!
       			if (!funnet) {
          			ord[i] = lestord;
          			antall[i] = 1;
          			i++;
          			unikeOrd++;
    			} 	       
    	 	}
    	 	
    		/* looper gjennom hele antall-arrayen, som har antall forekomster av hver rad,
			så sjekker man hvert tall, om det er større enn det forrige registrerte høyeste antallet
			og hvis det er høyere, bytter man flestForekomster til indexen til ordet med flest forekomster */
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
     
			// kommentar !!!!!!!!!!!! og skriver resultatene til filen med angitt bredde 
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




