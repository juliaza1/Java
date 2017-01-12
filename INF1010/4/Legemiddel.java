abstract class Legemiddel {
    
    private String navn;
    private int nummer;
    private double pris;
    
    //konstruktør
    public Legemiddel(String navn, int nummer, double pris) {
        this.navn = navn;
        this.nummer = nummer;
        this.pris = pris;
    }
    
    public String navn() {
        return navn;
    }
    public int nummer() {
        return nummer;
    }
    
    public double pris() {
        return pris;
    }
}



// Type A, B, C
abstract class TypeA extends Legemiddel {
    private int narkoStyrke;
    // konstruktør
    public TypeA(String navn, int nummer, double pris, int narkoStyrke) {
        super(navn, nummer, pris); // finner variablene i superklassen (Legemiddel)
        this.narkoStyrke = narkoStyrke;
    }

    public int narkoStyrke() {
        return narkoStyrke;
    }

}


abstract class TypeB extends Legemiddel {
    private int vanedannende;
    
    public TypeB(String navn, int nummer, double pris, int vanedannende) {
        super(navn, nummer, pris);
        this.vanedannende = vanedannende;
    }
    public int vanedannende() {
        return vanedannende;
    }
}


abstract class TypeC extends Legemiddel {
    public TypeC(String navn, int nummer, double pris) {
        super(navn, nummer, pris);
    }
}





// Type A Liniment, Pille, Injeksjon
class TypeAInjeksjon extends TypeA implements Injeksjon {
    
    private int milligram;
    
    TypeAInjeksjon (String navn, int nummer, double pris, int narkoStyrke, int milligram) {
        super(navn, nummer, pris, narkoStyrke);
        this.milligram = milligram;
    }
            
    public int milligram() {
        return milligram;
    }
}


class TypeALiniment extends TypeA implements Liniment {
    int volum;
    
    TypeALiniment (String navn, int nummer, double pris, int narkoStyrke, int volum) {
        super(navn, nummer, pris, narkoStyrke);
        this.volum = volum;
    }
    
    public int volum() {
        return volum;
    }
}


class TypeAPille extends TypeA implements Pille {
    int antallPiller;
    
    TypeAPille (String navn, int nummer, double pris, int narkoStyrke, int antallPiller) {
        super(navn, nummer, pris, narkoStyrke);
        this.antallPiller = antallPiller;
    }

    public int antallPiller() {
        return antallPiller;
    }
}



// Type B Liniment, Pille, Injeksjon
class TypeBInjeksjon extends TypeB implements Injeksjon {
    int milligram;
    
    TypeBInjeksjon (String navn, int nummer, double pris, int vanedannende, int milligram) {
        super(navn, nummer, pris, vanedannende);
        this.milligram = milligram;
    }
    
    public int milligram() {
        return milligram;
    }
}


class TypeBLiniment extends TypeB implements Liniment {
    int volum;

    TypeBLiniment (String navn, int nummer, double pris, int vanedannende, int volum) {
        super(navn, nummer, pris, vanedannende);
        this.volum = volum;
    }
    
    public int volum() {
        return volum;
    }
}


class TypeBPille extends TypeB implements Pille {
    int antallPiller;
    
    TypeBPille (String navn, int nummer, double pris, int vanedannende, int antallPiller) {
        super(navn, nummer, pris, vanedannende);
        this.antallPiller = antallPiller;
    }
    
    public int antallPiller() {
        return antallPiller;
    }
}



// Type C Liniment, Pille, Injeksjon
class TypeCInjeksjon extends TypeC implements Injeksjon {
    int milligram;

    TypeCInjeksjon(String navn, int nummer, double pris, int milligram) {
        super(navn, nummer, pris);
        this.milligram = milligram;
    }
    
    public int milligram() {
        return milligram;
    }
}


class TypeCLiniment extends TypeC implements Liniment {
    int volum;

    TypeCLiniment(String navn, int nummer, double pris, int volum) {
        super(navn, nummer, pris);
        this.volum = volum;
    }
    
    public int volum() {
        return volum;
    }

}


class TypeCPille extends TypeC implements Pille {
    int antallPiller;
    
    TypeCPille(String navn, int nummer, double pris, int antallPiller) {
        super(navn, nummer, pris);
        this.antallPiller = antallPiller;
    }
    
    public int antallPiller() {
        return antallPiller;
    }
}



// interfaces
interface Injeksjon {
    public int milligram();
}

interface Liniment {
    public int volum();
}

interface Pille {
    public int antallPiller();

}