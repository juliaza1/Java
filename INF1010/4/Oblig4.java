import java.util.Iterator;
import java.util.Scanner;
import easyIO.*;

class Oblig4 {
    static Tabell<Person> personer;
    static Tabell<Legemiddel> legemidler;
    static SortertEnkelListe<String, Lege> leger;
    static int forrigePersonnummer;
    static int forrigeMiddelnummer;
    static int forrigeReseptnummer;
    
    // oppretter nytt legemiddel. sjekker først om det er pille, liniment eller injeksjon, så type
    static void addLegemiddel(int nr, String type, String name, String form, int mengde, Double pris, int styrke) {
        Legemiddel middel;
        if (form.equals("pille")) {
            if (type.equals("a")) {
                middel = new TypeAPille(name,nr,pris,styrke,mengde);
            } else if (type.equals("b")) {
                middel = new TypeBPille(name,nr,pris,styrke,mengde);
            } else {
                middel = new TypeCPille(name,nr,pris,mengde);
            }
        } else if (form.equals("liniment")) {
            if (type.equals("a")) {
                middel = new TypeALiniment(name,nr,pris,styrke,mengde);
            } else if (type.equals("b")) {
                middel = new TypeBLiniment(name,nr,pris,styrke,mengde);
            } else {
                middel = new TypeCLiniment(name,nr,pris,mengde);
            }
        } else {
            if (type.equals("a")) {
                middel = new TypeAInjeksjon(name,nr,pris,styrke,mengde);
            } else if (type.equals("b")) {
                middel = new TypeBInjeksjon(name,nr,pris,styrke,mengde);
            } else {
                middel = new TypeCInjeksjon(name,nr,pris,mengde);
            }
        }
        
        legemidler.settInn(middel,nr);
    }
    
    // setter inn ny person
    static void addPerson(String name, int nr, char sex) {
        personer.settInn(new Person(name, nr, sex), nr);
    }
    
    // legger til et nytt resept.
    static void addResept(int nr, int pnr, int lnr, String lnavn, char hb, int reit) {
        Lege legen = leger.finn(lnavn);
        Legemiddel middel = legemidler.finn(lnr);
        Person p = personer.finn(pnr);
        Resept res = new Resept(nr, hb, pnr, legen, middel, reit);
        p.faaResept(res);
    }
    
    // legger til lege
    static void addLege(String navn, int spesialist, int avtalenr) {
        Lege lege;
        if (spesialist == 0) {
            lege = new Lege(navn, avtalenr);
        } else {
            lege = new Spesialist(navn, avtalenr);
        }
        
        leger.settInnSortert(lege.navn(), lege);
    }
    
    // leser fra fil og legger det den "finner" inn i riktig liste
    static void lesFraFil() {
        personer = new Tabell<Person>(100);
        legemidler = new Tabell<Legemiddel> (100);
        leger = new SortertEnkelListe<String, Lege>();
        
        int p_nr = 0;
        String name;
        char sex;
        In fil = new In("data.txt");
        fil.inLine();
        
        // Personer
        while (!fil.endOfFile()) {
            
            String linje = fil.inLine();
            String[] deler = linje.split(",");
            
            if (linje.charAt(0) == '#') {
                break;
            }
            
            p_nr = Integer.parseInt(deler[0].trim());
            name = deler[1].trim();
            sex = deler[2].trim().charAt(0);
    
            forrigePersonnummer = p_nr;
            addPerson(name, p_nr, sex);
        }
        
        int nr, mengde, styrke;
        double pris;
        String type,form;
        
        // Legemidler
        while (!fil.endOfFile()) {
            
            String linje = fil.inLine();
            String[] deler = linje.split(",");
            if (linje.charAt(0) == '#') {
                break;
            }
            
            nr = Integer.parseInt(deler[0].trim());
            name = deler[1].trim();
            form = deler[2].trim();
            type = deler[3].trim();
            pris = Double.parseDouble(deler[4].trim());
            mengde = Integer.parseInt(deler[5].trim());
            styrke = 0;
            
            if (!type.equals("c")) {
                styrke = Integer.parseInt(deler[6].trim());
            }
            
            forrigeMiddelnummer = nr;
            addLegemiddel(nr, type, name, form, mengde, pris, styrke);
        }
        
        // lege
        while (!fil.endOfFile()) {
            
            String linje = fil.inLine();
            String[] deler = linje.split(",");
            
            if (linje.charAt(0) == '#') {
                break;
            }
            
            String navn = deler[0].trim();
            int spesialist = Integer.parseInt(deler[1].trim());
            int avtalenr = Integer.parseInt(deler[2].trim());
            addLege(navn, spesialist, avtalenr);
        }
        
        // Resepter
        while (!fil.endOfFile()) {
            
            String linje = fil.inLine();
            String[] deler = linje.split(",");
            
            if (linje.charAt(0) == '#') {
                break;
            }
            
            nr = Integer.parseInt(deler[0].trim());
            char reseptType = deler[1].trim().charAt(0);
            int persNr = Integer.parseInt(deler[2].trim());
            String legeNavn = deler[3].trim();
            int middelNummer = Integer.parseInt(deler[4].trim());
            int reit = Integer.parseInt(deler[5].trim());
            
            forrigeReseptnummer = nr;
            addResept(nr, persNr, middelNummer, legeNavn, reseptType, reit);
        }
        
        fil.close();
    }
    
    // // skriver ut alt (bortsett fra resepter)
    // static void skrivUtAlt() {
        
    //     System.out.println("Leger:");
    //     // sjekker om en lege har avtalenr eller ikke og skriver riktig ut
    //     for (Lege l : leger) {
    //         if (l.avtalenr() != 0) {
    //             System.out.println(l.navn() + " - avtalenr: " + Integer.toString(l.avtalenr()) );
    //         } else {
    //             System.out.println(l.navn());
    //         }

    //     }
        
    //     System.out.println("Legemidler:");
        
    //     for (Legemiddel m : legemidler) {
    //         if (m != null) {
    //             System.out.println(m.navn() + " -  nummer: " + Integer.toString(m.nummer())
    //                            + ", pris:" + Double.toString(m.pris()));
    //         }
    //     }
        
    //     System.out.println("Personer:");
        
    //     for (Person p : personer) {
    //         if (p != null) {
    //             System.out.println(p.navn());
    //         }
    //     }
    // }
    
    
    
    
    
    
    
    
    
    
    // /* Hente legemiddelet på en resept basert på nummeret til personen som skal ha resepten og reseptens nummer. Siden vi i denne oppgaven ikke har noe data om mengden av legemiddel på lager, betyr dette at vi bare teller ned antallet ganger resepten kan brukes (reit). Om antallet blir null, betyr dette at resepten er ugyldig. Prisen som skal betales skrives ut. Skriv også ut legens navn, personens navn og all dataene du har om legemiddelet på resepten (inkludert antall piller i en eske, hvor stort volum en tube har eller hvor mye virkemiddel det er i en dose).
    //  */
    
    // static void hentResept() {
    //     System.out.println("Angi personnr:");
    //     Scanner scanner = new Scanner(System.in);
    //     int personNr = scanner.nextInt();
    //     System.out.println("Angi reseptnr:");
    //     int reseptNr = scanner.nextInt();
        
    //     Person p = personer.finn(personNr);
    //     Resept r = p.hentResept(reseptNr);
        
    //     if (r != null) {
    //         if (r.reduser()) {
    //             System.out.println("Hentet resept, venligst betal " + Double.toString(r.pris()));
    //             System.out.println("Skrevet ut av " + r.lege().navn());
            
        
    //             if (r.middel() instanceof Pille) {
    //                 Pille p = r.middel();
    //                 System.out.println(p.antall());
    //             }
            
    //             if (r.middel() instanceof Liniment) {
    //                 Liniment li = r.middel();
    //                 System.out.println(li.volum());
    //             }
            
    //             if (r.middel() instanceof Injeksjon) {
    //                 Injeksjon inje = r.middel();
    //                 System.out.println(inje.milligram());
    //             }
        
    //         }
    //     }
    // }
    
    // // skriver ut alle blåe resepter og antall igjen for en person
    // static void skrivUtBlaa() {
    //     Scanner scanner = new Scanner(System.in);
    //     System.out.println("Angi personnummer:");
    //     int personNr = scanner.nextInt();

    //     Person p = personer.finn(personNr);
        
    //     for (Resept r : p.resepter()) {
    //         if (r.blaahvit() == 'b' && r.antallIgjen() > 0) {
    //             System.out.println("Blå resept: " + r.middel().navn() + " antall igjen: " + Integer.toString(r.antallIgjen()));
    //         }
    //     }
    // }
    
    // // finner leger med avtalnr. og skriver ut antall narkotiske legemidler skrevet ut av legen
    // static void skrivUtNarkoavtaler() {
    //     for (Lege l : leger) {
    //         if (l.avtalenr() > 0) {
    //             System.out.println("Lege med avtale: " + l.navn());
                
    //             int c = 0;
    //             for (Resept r : l.resepter()) {
    //                 if (r.middel() instanceof TypeA) {
    //                     c++;
    //                 }
    //             }
                
    //             System.out.println("Antall utskrevne narkotiske legemidler: " + Integer.toString(c));
    //         }
    //     }
    // }
    
    // // skriver ut hvor mange gyldige resepter som er skrevet ut på vanedannende legemidler
    // static void skrivUtVanedannende() {
    //     int k = 0;
    //     int m = 0;
        
    //     for (Person p : personer) {
            
    //         if (p == null) {
    //             continue;
    //         }
            
    //         System.out.println(p.navn());
    //         int c = 0;
            
    //         for (Resept r : p.resepter()) {

    //             if (r.middel() instanceof TypeB && r.antallIgjen() > 0) {
    //                 c++;
    //                 // teller kvinner og nenn
    //                 if (p.sex() == 'm') {
    //                     m++;
    //                 } else {
    //                     k++;
    //                 }
    //             }
    //         }
            
    //         System.out.println("Antall vanedannende resepter:" + Integer.toString(c));
    //     }
        
    //     System.out.println("Totalt vanedannende resepter: " + Integer.toString(m+k));
    //     System.out.println("Herav til menn: " + Integer.toString(m));
    //     System.out.println("Herav til kvinner: " + Integer.toString(k));
    // }
    
    // // legger til legemiddel
    // static void leggTilLegemiddel() {
    //     Scanner scanner = new Scanner(System.in);
    //     int nr = ++forrigeMiddelnummer;
    //     System.out.println("Angi type(a,b eller c):");
    //     String type = scanner.nextLine();
    //     System.out.println("Angi navn:");
    //     String navn = scanner.nextLine();
    //     System.out.println("Angi form (pille,liniment, injeksjon):");
    //     String form = scanner.nextLine();
    //     System.out.println("Angi pris:");
    //     Double pris = scanner.nextDouble();
    //     System.out.println("Angi mengde:");
    //     int mengde = scanner.nextInt();
        
    //     int styrke = 0;
        
    //     if (!type.equals("c")) {
    //         System.out.println("Angi styrke:");
    //         styrke = scanner.nextInt();
    //     }
        
    //     System.out.println("Nytt nummer lagt til:" + Integer.toString(forrigeMiddelnummer));
    //     addLegemiddel(nr, type, navn, form, mengde, pris, styrke);
    // }
    
    // // legger til person
    // static void leggTilPerson () {
    //     Scanner scanner = new Scanner(System.in);
    //     int nr = ++forrigePersonnummer;
        
    //     System.out.println("Angi navn:");
    //     String name = scanner.nextLine();
        
    //     System.out.println("Angi kjønn:");
    //     char sex = scanner.nextLine().charAt(0);
        
    //     System.out.println("Nytt nummer lagt til:" + Integer.toString(forrigePersonnummer));
    //     addPerson(name, nr, sex);
        
    // }

    // // legger til resept
    // static void leggTilResept() {
    //     int nr = ++forrigeReseptnummer;
    //     Scanner scanner = new Scanner(System.in);
    //     System.out.println("Angi personnummer:");
    //     int pnr = scanner.nextInt();
        
    //     System.out.println("Angi legemiddelnummer:");
    //     int lnr = scanner.nextInt(); scanner.nextLine();
        
    //     System.out.println("Angi legenavn:");
    //     String lnavn = scanner.nextLine();
        
    //     System.out.println("Angi h/b:");
    //     char hb = scanner.nextLine().charAt(0);
        
    //     System.out.println("Angi reit:");
    //     int reit = scanner.nextInt();
        
    //     addResept(++forrigeReseptnummer, pnr, lnr, lnavn, hb, reit);
    // }
    
    // // legger til lege
    // static void leggTilLege() {
    //     Scanner scanner = new Scanner(System.in);
        
    //     System.out.println("Angi legenavn:");
    //     String lnavn = scanner.nextLine();
        
    //     System.out.println("Angi spesialiststatus 1/0");
    //     int spes = scanner.nextInt();
        
    //     System.out.println("Angi avtalenr, 0 for ingen avtale");
    //     int avtalenr = scanner.nextInt();
        
    //     addLege(lnavn, spes, avtalenr);
    // }
    
    public static void main (String[] args) {
       // System.out.println("enkelListe: " + (TestLister.enkelListe() ? "true" : "false"));
       // System.out.println("tabell:" + (TestLister.tabell() ? "true" : "false"));
        
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        lesFraFil();
        
        // Meny
        while (running) {
            System.out.println("1: Opprett legemiddel");
            System.out.println("2: Opprett ny lege");
            System.out.println("3: Opprett ny person");
            System.out.println("4: Opprett ny resept");
            System.out.println("5: Hent resept for person");
            System.out.println("6: Skriv ut alt");
            System.out.println("7: Skriv ut gyldige blå resepter for person");
            System.out.println("8: Skriv ut avtalelege + narkotiske resepter");
            System.out.println("9: Skriv ut personer med vanedannende informasjon");
            System.out.println("10: Avslutt");
            int cmd = scanner.nextInt();
            
            switch (cmd) {
                case 1:
                    leggTilLegemiddel();
                    break;
                case 2:
                    leggTilLege();
                    break;
                case 3:
                    leggTilPerson();
                    break;
                case 4:
                    leggTilResept();
                    break;
                case 5:
                    hentResept();
                    break;
                case 6:
                    skrivUtAlt();
                    break;
                case 7:
                    skrivUtBlaa();
                    break;
                case 8:
                    skrivUtNarkoavtaler();
                    break;
                case 9:
                    skrivUtVanedannende();
                    break;
                case 10:
                    running = false;
                    break;
            }
        }
    }
}

/*
 Oppgave 6
 
 class TestLister {
    public static boolean tabell() {
        int length = 100;
        boolean ret = true;
        Tabell<String> tab = new Tabell(length);
        
        for (int i = 0; i < length; ++i) {
            tab.settInn(Integer.toString(i), i);
        }
        
        for (int i = 0; i < length; ++i) {
            if (!tab.finn(i).equals(Integer.toString(i))) {
                ret = false;
            }
        }
        
        return ret;
    }
    
    public static boolean enkelListe() {
        int count = 10;
        SortertEnkelListe<Integer, String> l = new SortertEnkelListe<Integer,String>();
        int[] expected = new int[count];
        String[] values = new String[count];
        boolean ret = true;
        
        for (int i = 0; i < count; ++i) {
            expected[i] = i;
            values[i] = Integer.toString(i);
        }
        
        for (int i = count-1; i >= 0; i--) {
            l.settInnSortert(expected[i], values[i]);
        }
        
        int i = 0;
        for (String s: l) {
            if (!values[i++].equals(s)) {
                ret = false;
            }
        }
        return ret;
    }
}*/