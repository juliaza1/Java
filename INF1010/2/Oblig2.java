/*
TEST
*/

class Oblig2 {
    public static void main (String[] args) {
		Testklasse tk = new Testklasse();
		tk.testMetode();
    }
}



//________________________________________________________________________________________


class Person {
    
   	private String navn;
	private Person[] kjenner;
    private Person[] likerikke;
 
    private Bok[] boker;
    private int antallBoker;
    private Plate[] plater;
    private int antallPlater;
    private String interessertI;
    private int interessertIEldreEnn;
    
    private Person forelsketi;
    private Person sammenmed;
    
    Person(String navn, int lengde) { 
		this.navn = navn;
		kjenner = new Person[lengde];
		likerikke = new Person[lengde];
      
    }
    
    public void samlerAv(String smlt, int ant) {
        if (smlt.equals("bøker")) {
            boker = new Bok[ant];
        } else if (smlt.equals("plater")) {
            plater = new Plate[ant];
        }
    }
    
    public void megetInteressertI (String artist) {
        interessertI = artist;
    }
    
    public void megetInteressertI (int alder) {
        interessertIEldreEnn = alder;
    }
    
    public String hentNavn() { 
        return navn;
    }
    
    public Bok vilDuHaGaven (Bok boka) {
        
        boolean likerGodt = liker(boka);
        
        if (boker == null || boker.length == 0) {
            return boka;
        }
        
        if (antallBoker > boker.length / 2 && !likerGodt) {
            return boka;
        }
        
        for (Bok b : boker) {
            if (b != null) {
                if (b.hentForfatter().equals(boka.hentForfatter()) && b.hentTittel().equals(boka.hentTittel())) {
                    return boka;
                }
            }
        }
        
        if (antallBoker >= boker.length) {
            if (likerGodt) {
                for (int i = 0; i < boker.length; i++) {
                    if (!liker(boker[i])) {
                        Bok ret = boker[i];
                        boker[i] = boka;
                        return ret;
                    }
                }
            }
            return boka;
        }

        
        boker[antallBoker++] = boka;
        return null;
    }
    
    boolean liker(Bok boka) {
        if (boka.hentAar() <= interessertIEldreEnn) {
            return true;
        }
        return false;
    }
    
    boolean liker(Plate plata) {
        if (plata.hentArtist().equals(interessertI)) {
            return true;
        }
        return false;
    }
    
    public Plate vilDuHaGaven (Plate plata) {
        
        boolean likerGodt = liker(plata);
        
        if (plater == null || plater.length == 0) {
            return plata;
        }
        
        if (antallPlater > plater.length / 2 && !likerGodt) {
            return plata;
        }
        
        for (Plate p : plater) {
            if (p != null) {
                if (p.hentArtist().equals(plata.hentArtist()) && p.hentNavn().equals(plata.hentNavn())) {
                    return plata;
                }
            }
        }
        
        if (antallPlater >= plater.length) {
            if (likerGodt) {
                for (int i = 0; i < plater.length; i++) {
                    if (!liker(plater[i])) {
                        Plate ret = plater[i];
                        plater[i] = plata;
                        return ret;
                    }
                }
            }
            return plata;
        }
        
        plater[antallPlater++] = plata;
        return null;
    }
    
    public boolean erKjentMed(Person p){ 
    	for (int i = 0; i < kjenner.length; ++i) {
    		if (kjenner[i] == p) {
    			return true;
    		}
    	}
    	
		return false;	
	}

    public void blirKjentMed (Person p){
	// Dana blir kjent med p, bortsett fra hvis p peker paa Dana (Dana kan ikke vaere kjent med seg selv).
		if(p != this) {
			for (int i = 0; i < kjenner.length; i++) {
				if(kjenner[i] == null){
					kjenner[i] = p;
					break;
	    		}
    		}
    	}
    }
    
    public void blirForelsketI (Person p){
    // Dana blir forelsket i p, bortsett fra hvis p peker pa Dana
		if(p != this){
	    	if(forelsketi == null) {
			forelsketi = p;
	    	}
		}	
	}	
    
    public void blirUvennMed(Person p){
    // Dana blir uvenn med p, bortsett fra hvis p peker pa Dana
		if(p != this) {
			for (int i = 0; i < likerikke.length; ++i) {
	    		if(likerikke[i] == null){
					likerikke[i] = p;
					break;
	    		}
	    	}
		}	
    }
    
    public boolean erVennMed(Person p){
	// returnerer sann hvis Dana kjenner p og ikke er uvenner med p
		boolean sann = false;
		for(int i = 0; i < kjenner.length; i++){
	    	if (kjenner[i] == p){
				sann = true;
				for(int k = 0; k < likerikke.length; k++){
		    		if( likerikke[k] == p){
						sann = false;
		    		}
				}
	    	} 
		}
		return sann;
    }
    
    public void blirVennMed(Person p){
    // samme virkning som blirKjentMed(p), men hvis Dana ikke 
    // liker p dvs. (likerikke[i] == p) for en gitt i
    // blir likerikke[i] satt til null.
		if(p != this){
			for (int i = 0; i < kjenner.length; ++i) {
	    		if(kjenner[i] == null){
					kjenner[i] = p;
	    		}
	    		for(int k = 0; k < likerikke.length; k++){
					if( likerikke[k] == p){
				    	likerikke[k] = null;
				    }
				}
		    }
		}
    }
	
    public void skrivUtVenner (){
		for (Person p: kjenner){
	    	if ( p!=null && erVennMed(p) == true) System.out.println(p.hentNavn() + " ");
	    	else System.out.println ("EPIC FAIL");
		}			
    }
    // skriver ut navnet pa dem Dana kjenner, unntatt dem hun ikke liker.
	
    public Person hentBestevenn() {
		return kjenner[0];
	}	
    
    public Person[] hentVenner() {
    // returnerer en array som peker pa alle Danas kjente
	// Arrayen skal vaere akkurat sa lang at lengden er lik antallet venner, 
	// og rekkefoelgen skal vaere den samme som i kjenner-arrayen.
		int lengde = 0;
		for (int i = 0; i < kjenner.length; i++){
	    	if (kjenner[i] != null) lengde++;
		}
		Person[] hVenner = new Person[lengde];

		for (int i = 0; i < hVenner.length; i++){
	    	if (kjenner[i] != null) hVenner[i] = kjenner[i];
		}	
    	return hVenner;
 	}

	public int antVenner() {
		return hentVenner().length;
	}
	
    public void skrivUtKjenninger () { 
		for (Person p: kjenner)
	    	if ( p!=null) System.out.print(p.hentNavn() + " "); 
				System.out.println("");
    }	
    
    public void skrivUtLikerIkke () {
		for (Person p: likerikke )
	    	if (p!=null) System.out.print(p.hentNavn() + " ");
				System.out. println("");
    }
    
    public void skrivUtAltOmMeg() { 
		System.out.print(navn + " kjenner: "); 
		skrivUtKjenninger ();
		if (forelsketi != null) {
	    	System.out.println(navn +" er forelsket i " + forelsketi.hentNavn()); 
	    	System.out.print(navn + " liker ikke "); 
	    	skrivUtLikerIkke ();
		}
        
        System.out.println(navn + " sine bøker:");
        
        if (boker != null) {
            for (Bok b : boker) {
                if (b != null) {
                    System.out.println(b.hentForfatter() + ": " + b.hentTittel() + " - " + b.hentAar());
                }
            }
        }
        
        System.out.println(navn + " sine plater:");
        
        if (plater != null) {
            for (Plate p : plater) {
                if (p != null) {
                    System.out.println(p.hentArtist() + ":" + p.hentNavn() + ", " + p.hentAntall() + " spor");
                }
            }
        }
    }
    
}//slutt class Person


class Bok {

    private String forfatter;
    private String tittel;
    private int aar;
    
    public Bok (String forfatter, String tittel, int aar) {
        this.forfatter = forfatter;
        this.tittel = tittel;
        this.aar = aar;
    }
    
    public String hentForfatter() {
        return forfatter;
    }
    
    public String hentTittel() {
        return tittel;
    }
    
    public int hentAar() {
        return aar;
    }
}

class Plate {
    
    private String navn;
    private String artist;
    private int antall;
    
    public int hentAntall() {
        return antall;
    }
    
    public String hentArtist() {
        return artist;
    }
    
    public String hentNavn() {
        return navn;
    }
    
    public Plate (String artist, String navn, int antall) {
        this.navn = navn;
        this.artist = artist;
        this.antall = antall;
    }
}


class Testklasse {
	public void testMetode() {
		Person julvor = new Person("Julvor", 9);
		Person ask = new Person("Ask", 9);
		Person dana = new Person("Dana", 9);
		Person tom = new Person("Tom", 9);
        Person truls = new Person("Truls", 9);
        Person ola = new Person("Ola", 9);
        Person petter = new Person("Petter", 9);
        Person hanna = new Person("Hanna", 9);
        Person karla = new Person("Karla", 9);
        
        karla.blirKjentMed(julvor);
        karla.blirKjentMed(ask);
        karla.blirKjentMed(tom);
        karla.blirKjentMed(dana);
        karla.blirKjentMed(ola);
        karla.blirKjentMed(petter);
        karla.samlerAv("plater", 5);
        karla.megetInteressertI("Queen");

        hanna.blirKjentMed(petter);
        hanna.blirKjentMed(ola);
        hanna.blirKjentMed(tom);
        hanna.samlerAv("plater", 5);
        hanna.megetInteressertI("Iron Maiden");
        
        
		julvor.blirKjentMed(ask);
		julvor.blirKjentMed(dana);
		julvor.blirKjentMed(tom);
		julvor.samlerAv("bøker", 5);
        julvor.samlerAv("plater", 5);
        
		ask.blirKjentMed(julvor);
		ask.blirKjentMed(dana);
		ask.blirKjentMed(tom);
        ask.samlerAv("bøker", 5);
        ask.samlerAv("plater", 5);
        ask.megetInteressertI("Silya Nymoen");
		ask.blirForelsketI(julvor);
		
		ask.blirUvennMed(dana);
		ask.blirUvennMed(tom);
			
		dana.blirKjentMed(ask);
		dana.blirKjentMed(tom);
		dana.blirKjentMed(julvor);
        dana.samlerAv("bøker", 5);
        dana.samlerAv("plater", 5);
		dana.blirForelsketI(tom);
		
		dana.blirUvennMed(julvor);
		
		tom.blirKjentMed(julvor);
		tom.blirKjentMed(dana);
		tom.blirKjentMed(ask);
        tom.samlerAv("bøker", 5);
        tom.samlerAv("plater", 5);
        tom.megetInteressertI("Bob Dylan");
		tom.blirUvennMed(julvor);
		tom.blirUvennMed(ask);
			
		tom.blirForelsketI(dana);
        
        truls.blirKjentMed(ola);
        truls.samlerAv("plater", 5);
        
        ola.blirKjentMed(petter);
        ola.samlerAv("bøker", 5);
        
        Person[] personer = new Person[7];
        personer[0] = julvor;
        personer[1] = ask;
        personer[2] = dana;
        personer[3] = tom;
        personer[4] = truls;
        personer[5] = ola;
        personer[6] = petter;
        
        Bok[] boker = new Bok[20];
        boker[0] = new Bok("Jo Nesbø", "Hodejegerne", 1999);
        boker[1] = new Bok("JRR Tolkien", "The Hobbit", 1937);
        boker[2] = new Bok("Jules Verne", "En reise til jordens indre", 1870);
        boker[3] = new Bok("J.C.", "The Bible", 1899);
        boker[4] = new Bok("Someone", "Java for Dummies", 1994);
        boker[5] = new Bok("Jo Nesbø", "Politi", 2013);
        boker[6] = new Bok("JRR Tolkien", "The Lord og the Rings", 1939);
        boker[7] = new Bok("Jonas Jonasson", "Hundreåringen som klatret ut gjennom vinduet og forsvant", 1870);
        boker[8] = new Bok("JC", "The Bible", 0);
        boker[9] = new Bok("Sarah Schenker", "5:2-DIETTENS KOKEBOK SPIS DET DU PLEIER", 1994);
        boker[10] = new Bok("Patti Smith", "Just Kids", 1999);
        boker[11] = new Bok("Leif Ryvarden", "Norges Nasjonalparker", 1998);
        boker[12] = new Bok("Jules Verne", "En reise til jordens indre", 1870);
        boker[13] = new Bok("JC", "The Bible", 0);
        boker[14] = new Bok("someone", "Java for Dummies", 1994);
        boker[15] = new Bok("Jo Nesbø", "Hodejegerne", 1825);
        boker[16] = new Bok("JRR Tolkien", "The Hobbit", 1937);
        boker[17] = new Bok("Jules Verne", "En reise til jordens indre", 1870);
        boker[18] = new Bok("JC", "The Bible", 0);
        boker[19] = new Bok("someone", "Java for Dummies", 1876);
        
        Plate[] plater = new Plate[20];
        plater[0] = new Plate("Eric Clapton", "Unplugged", 12);
        plater[1] = new Plate("Bob Dylan", "Greatest Hits", 14);
        plater[2] = new Plate("Iron Maiden", "Unplugged", 22);
        plater[3] = new Plate("Rammstein", "Reise Reise", 78);
        plater[4] = new Plate("Queen", "Hot Space", 11);
        plater[5] = new Plate("Queen", "Made in Heaven", 13);
        plater[6] = new Plate("Eric Clapton", "Unplugged", 12);
        plater[7] = new Plate("Bob Dylan", "Greatest Hits", 1);
        plater[8] = new Plate("Iron Maiden", "Unplugged", 42);
        plater[9] = new Plate("Rammstein", "Reise Reise", 78);
        plater[10] = new Plate("Rammstein", "Mutter", 11);
        plater[11] = new Plate("Bob Dylan", "Greatest Hits", 1);
        plater[12] = new Plate("Iron Maiden", "Unplugged", 42);
        plater[13] = new Plate("Rammstein", "Sonne", 8);
        plater[14] = new Plate("Queen", "Yet another song", 1);
        plater[15] = new Plate("Queen", "A Day at the Races", 10);
        plater[16] = new Plate("Eric Clapton", "Rush", 10);
        plater[17] = new Plate("Bob Dylan", "Not so Greatest Hits", 821);
        plater[18] = new Plate("Iron Maiden", "Plugged", 12);
        plater[19] = new Plate("Rammstein", "Live @ somewhere", 24);
        
        for (int b = 0; b < boker.length; b++) {
            
            for (Person p: personer) {
                boker[b] = p.vilDuHaGaven(boker[b]);
                
                if (boker[b] == null) {
                    break;
                }
            }
            
            if (boker[b] != null) {
                System.out.println ("Klarte ikke å gi bort boka '" + boker[b].hentTittel() + "'");
                System.out.println();

            }
        }
        
        for (int p = 0; p < plater.length; p++) {
            
            for (Person person : personer) {
                plater[p] = person.vilDuHaGaven(plater[p]);
                
                if (plater[p] == null) {
                    break;
                }
            }
            
            if (plater[p] != null) {
                System.out.println ("Klarte ikke å gi bort plata '" + plater[p].hentNavn() + "'");
                System.out.println();

            }
        }
        
        for (Person p : personer) {
            p.skrivUtAltOmMeg();
            System.out.println();
        }
	}	
}
