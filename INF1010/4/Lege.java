import java.lang.Comparable;


interface Avtale {
    public int avtalenr();

}


class Lege implements Comparable<Lege>, Lik, Avtale {
    
    private String navn;
    private int avtalenr;
    EnkelReseptListe resepter;
    
    void skrivUtResept(Resept r) {
        resepter.settInnResept(r);
    }
    
    EnkelReseptListe resepter() {
        return resepter;
    }
    
    // konstruktør
    Lege(String navn, int avtalenr) {
        resepter = new EnkelReseptListe();
        this.navn = navn;
        this.avtalenr = avtalenr;
    }
    
    public String navn() {
        return navn;
    }
    
    public int avtalenr(){
        return avtalenr;
    }
    
    public boolean samme(String navn) {
        if (this.navn.equals(navn)) {
            return true;
        }
        return false;
    }
    
    public int compareTo(Lege annen) {
        return this.navn.compareTo(annen.navn);
    }

}


class Spesialist extends Lege {
    
    // konstruktør
    Spesialist(String navn, int avtalenr) {
        super(navn, avtalenr);

    }
}

// sjekker om en String er lik
interface Lik {
    boolean samme(String navn);
}
