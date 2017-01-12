import java.lang.Comparable;

class Resept implements Comparable<Resept> {
    
    private int reseptNr;
    Legemiddel lm;
    Lege lege;
    private int pNr;
    private int antallIgjen; // 0 = ugyldig
    private char blaahvit;
    
    // konstruktør
    Resept(int reseptNr, char blaahvit, int pNr, Lege lege, Legemiddel lm, int antallIgjen) {
        this.reseptNr = reseptNr;
        this.blaahvit = blaahvit;
        this.pNr = pNr;
        this.lege = lege;
        this.lm = lm;
        this.antallIgjen = antallIgjen;
        
        lege.skrivUtResept(this);
    }
    
    Lege lege() {
        return lege;
    }
    
    Legemiddel middel() {
        return lm;
    }
    public int reseptNr() {
        return reseptNr;
    }
    
    public int pNr() {
        return pNr;
    }
    
    public int antallIgjen() {
        return antallIgjen;
    }
    
    public boolean reduser() {
        if (antallIgjen <= 0) {
            return false;
        }
        
        antallIgjen--;
        return true;
    }
    
    double pris() {
        return lm.pris();
    }
    
    public char blaahvit() {
        return blaahvit;
    }
    
    public int compareTo(Resept a) {
        return reseptNr() - a.reseptNr();
    }
}