class Person {
    private String navn;
    private int pnr;
    private char kjoenn;
    
    EnkelReseptListe resepter;
    
    void faaResept(Resept r) {
        resepter.settInnResept(r);
    }
    
    public Resept hentResept(int nr) {
        return resepter.finn(nr);
    }
    
    public EnkelReseptListe resepter() {
        return resepter;
    }
    
    public String navn() {
        return navn;
    }
    
    public char sex() {
        return kjoenn;
    }
    
    // konstruktør
    Person(String navn, int pnr, char kjoenn) {
        this.navn = navn;
        this.pnr = pnr;
        this.kjoenn = kjoenn;
        resepter = new EnkelReseptListe();
    }
    
    
    public int pnr() {
        return pnr;
    }
}