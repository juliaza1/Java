import easyIO.*;
import java.util.*;

/*
 cd Documents/inf1010/obliger/5
 Eksempel.txt har 28 loesninger
 Hvis du vil, kan du anta at stoerste brett er 16 x 16 ruter.
 1-9, A=10, B=11, C=12, D=13, E=14, F1=15, G=16 osv
 */
class Oblig5 {
    private static String[] savedArgs;
    
    public static String[] getArgs() {
        return savedArgs;
    }
    
    public static void main (String[] args) {
        savedArgs = args;
        if (args.length == 1) { // SKRIVER TIL SKJERM
            Brett brett = new Brett(args[0]);
            brett.lesInn();
            
            SudokuBeholder losninger = new SudokuBeholder();
            brett.los(losninger);
            for(int i = 0; i < losninger.hentAntallLosninger(); i++)
            	losninger.taUt().skrivUt();
            
            System.out.println();
            
            System.out.println("Fant " + losninger.hentAntallLosninger());
        } else if (args.length == 2) { // SKAL SKRIVE TIL FIL NÅR MER ENN 20 LØSNINGER FUNNET
            Brett brett = new Brett(args[0]);
            brett.lesInn();
            
            SudokuBeholder losninger = new SudokuBeholder();
            brett.los(losninger);
            
            System.out.println();
            System.out.println("Fant " + losninger.hentAntallLosninger());
            for(int i = 0; i < losninger.hentAntallLosninger(); i++)
            	losninger.taUt().skrivTilFil();
            
        } else {
            System.out.println("Bruk minst 1 tekstfil");
        }
    }
    
    /*
     
     
     // MATHIAS
     if (args.length < 1) {
     System.out.println("Bruk minst 1 tekstfil");
     // Hvis det oppgis ett filnavn skal løsningen(e) skrives til skjerm.
     } else {
     Brett brett = new Brett(args[0]);
     brett.lesInn();
     
     SudokuBeholder losninger = new SudokuBeholder();
     brett.los(losninger);
     
     System.out.println();
     System.out.println("Fant " + losninger.hentAntallLosninger());
     
     }*/
    
    
}


class SudokuBeholder {
    List<Brett> losninger;
    int antallFunnet;
    
    public void settInn(Brett b) {
        
        if (losninger.size() < 750) {
            losninger.add(b);
        }
        
        antallFunnet++;
    }
    
    public Brett taUt() {
        
        if (losninger.size() > 0) {
            return losninger.remove(0);
        }
        
        return null;
    }
    
    public int hentAntallLosninger() {
        return antallFunnet;
    }
    
    public SudokuBeholder() {
        losninger = new ArrayList<Brett>();
    }
}


abstract class Rute {
    Integer verdi;     // Bruk Integer for å kunne vite om den er definert eller ikke
    Rute neste;
    Boks boks;
    Rad rad;
    Kolonne kolonne;
    int antall = 0;
    
    public void settVerdi(Integer v) {
        verdi = v;
    }
    
    public void settNeste(Rute n) {
        neste = n;
    }
    
    public void settBoks(Boks b) {
        boks = b;
    }
    
    public void settKolonne(Kolonne k) {
        kolonne = k;
    }
    
    public void settRad(Rad r) {
        rad = r;
    }
    
    public Integer hentVerdi() {
        return verdi;
    }
    
    public abstract void fyllUtRestenAvBrettet(SudokuBeholder beholder, Brett brett);
    
    public void fyllUtNeste(SudokuBeholder beholder, Brett brett) {
        if (neste != null) {
            neste.fyllUtRestenAvBrettet(beholder, brett);
        } else {
            // løst
            beholder.settInn(new Brett(brett));
        }
    }
    
    
    public Rute() {
    }
}


class StatiskRute extends Rute {
    // med forhåndsutfylt verdi
    public StatiskRute(int v) {
        settVerdi(v);
    }
    
    public void fyllUtRestenAvBrettet(SudokuBeholder beholder, Brett brett) {
        fyllUtNeste(beholder, brett);
    }
}


class DynamiskRute extends Rute {
    // for å finne en mulig verdi
    public DynamiskRute() {
    }
    
    public void fyllUtRestenAvBrettet(SudokuBeholder beholder, Brett brett) {
        // sett inn forskjellige mulige verdier
        
        for (int i = 1; i <= brett.hentDimensjon(); ++i) {
            if (!kolonne.harVerdi(i) && !rad.harVerdi(i) && !boks.harVerdi(i)) {
                // for hver mulige verdi, gå videre
                settVerdi(i);
                fyllUtNeste(beholder, brett);
            }
        }
        
        // sett verdi til null for neste iterasjon?
        settVerdi(null);
    }
}


class Brett {
    private String tekstfil;
    private Rute[][] ruter;
    private int dimensjon;
    private int rad;
    private int kolonne;
    
    public int hentDimensjon() {
        return dimensjon;
    }
    
    public int hentRader() {
    	return rad;
    }
    
    public int hentKolonner() {
    	return kolonne;
    }
    
    public Rute[][] hentRuter() {
    	return ruter;
    }
    
    // Opprett rutetabellen
    private void lagRuter(int n) {
        ruter = new Rute[n][n];
    }
    
    private void lagStruktur(int n, int boksBredde, int boksHoyde) {
        int bokserX = n / boksBredde;
        int bokserY = n / boksHoyde;
        
        // lag bokser
        for (int x = 0; x < bokserX; ++x) {
            for (int y = 0; y < bokserY; ++y) {
                Boks b = new Boks(boksBredde, boksHoyde);
                
                // Fyll inn boksens ruter
                for (int i = 0; i < boksBredde; ++i) {
                    for (int j = 0; j < boksHoyde; ++j) {
                        Rute r = ruter[y*boksHoyde+j][x*boksBredde+i];
                        b.settRute(i,j,r);
                    }
                }
            }
        }
        
        // lag kolonner
        for (int i = 0; i < n; ++i) {
            Kolonne k = new Kolonne(n);
            
            for (int j = 0; j < n; ++j) {
                Rute r = ruter[j][i];
                k.settRute(j,r);
            }
        }
        
        // lag rader
        for (int i = 0; i < n; ++i) {
            Rad rad = new Rad(n);
            
            for (int j = 0; j < n; ++j) {
                Rute r = ruter[i][j];
                rad.settRute(j,r);
            }
        }
        
        // sett neste
        for (int i = 0; i < n*n-1; ++i) {
            // sparer en del linjer ved bruk av modulo
            ruter[i / n][i % n].settNeste(ruter[(i+1)/n][(i+1)%n]);
        }
    }
    
	public Brett(String fil) {
		tekstfil = fil;
		kolonne = 0;
		rad = 0;
    }
    
    public void lesInn() {
        int linje = 0;
        In fil = new In(tekstfil);  // leser inn fra filen
        
        while(!fil.lastItem()) {
            if(linje == 0) {
                String t = fil.inLine();
                rad = Integer.parseInt(t);
                linje++;
            }
            
            if(linje == 1) {
                String t = fil.inLine();
                kolonne = Integer.parseInt(t);
                linje++;
                
                dimensjon = rad * kolonne;
                lagRuter(dimensjon);
            }
            
            if (linje > 1) {
                String t = fil.inLine();
                
                for(int i = 0; i < dimensjon; i++) {
                    char c = t.charAt(i);
                    
                    if (c == '.') {
                        ruter[linje-2][i] = new DynamiskRute();
                    } else {
                        int a = Character.getNumericValue('A');
                        int val = Character.getNumericValue(c);
                        
                        if (val >= a) {
                            val = 10 + val - a;
                        }
                        
                        ruter[linje-2][i] = new StatiskRute(val);
                    }
                }
            }
            
            linje++;
        }
        
        lagStruktur(dimensjon, kolonne, rad);
        fil.close();
    }
    
    // Lager en kopi av brettets nåværende tilstand med kun statiske ruter
    public Brett(Brett b) {
        dimensjon = b.dimensjon;
        lagRuter(dimensjon);
        
        for (int i = 0; i < dimensjon; ++i) {
            for (int j = 0; j < dimensjon; ++j) {
                ruter[i][j] = new StatiskRute(b.ruter[i][j].hentVerdi());
            }
        }
    }
    
    public void los(SudokuBeholder beholder) {
        ruter[0][0].fyllUtRestenAvBrettet(beholder, this);
    }
    
    // skriver til skjerm
    public void skrivUt() {
        
        System.out.println("");
        System.out.print("Løsning: ");
        
        for (int i = 0; i < dimensjon; ++i) {
            for (int j = 0; j < dimensjon; ++j) {
                System.out.print(ruter[i][j].hentVerdi());
            }
            System.out.print("//");
        }
    }
    
    public void skrivTilFil() {
        
        String[] args = Oblig5.getArgs();
        Out utfil = new Out(args[1], true);
        utfil.out("\nLøsning: ");
        
        for (int i = 0; i < dimensjon; ++i) {
            for (int j = 0; j < dimensjon; ++j) {
                utfil.out(ruter[i][j].hentVerdi());
            }
            utfil.out("//");
        }
        utfil.close();
    }
}



abstract class RuteSamling {
    Rute[] ruter;
    
    public RuteSamling(int n) {
        ruter = new Rute[n];
    }
    
    public void settRute(int i, Rute r) {
        ruter[i] = r;
    }
    
    public boolean harVerdi(int v) {
        for (int i = 0; i < ruter.length; ++i) {
            Integer rv = ruter[i].hentVerdi();
            
            if (rv != null && rv == v) {
                return true;
            }
        }
        
        return false;
    }
}



class Boks extends RuteSamling {
    int bredde;
    int hoyde;
    
    public void settRute(int i, int j, Rute r) {
        settRute(j*bredde + i, r);
        r.settBoks(this);
    }
    
    public Boks(int b, int h) {
        super(b*h);
        bredde = b;
        hoyde = h;
    }
}

class Kolonne extends RuteSamling {
    
    @Override
    public void settRute(int i, Rute r) {
        super.settRute(i, r);
        r.settKolonne(this);
    }
    
    public Kolonne(int n) {
        super(n);
    }
}

class Rad extends RuteSamling {
    @Override
    public void settRute(int i, Rute r) {
        super.settRute(i, r);
        r.settRad(this);
    }
    
    public Rad(int n) {
        super(n);
    }
}