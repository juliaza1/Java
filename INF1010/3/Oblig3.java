class Oblig3 {
    public static void main (String[] args) {
		Testklasse tk = new Testklasse();
		tk.testMetode();
    }
}



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
    private String samlerInfo;
    
    private Gave[] mineGaver;
    
    Person neste;
    
    Person(String navn) {
		this.navn = navn;
		kjenner = new Person[100];
		likerikke = new Person[10];
        
    }
    
    
    
    
    public void blirSammenMed(Person p) {
        if(p != this) {
            sammenmed = p;
            if(p.hentSammenMed()!= this) {  // veldig viktig, ellers kommer vi ingen vei
                p.blirSammenMed(this);
                
            }
        }
    }
    
    public Person hentSammenMed() {
        return sammenmed;
    }
    
    
    public void samlerAv(String smlt, int ant) {
        mineGaver = new Gave[ant];
        samlerInfo = smlt;
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
    
   
    public Gave vilDuHaGaven(Gave gaven) {
        return vilDuHaGaven(gaven, true);
    }
    
    public Gave vilDuHaGaven(Gave gaven, boolean gividere) {
        if (liker(gaven) && mineGaver != null) {
            for (int i = 0; i < mineGaver.length; ++i) {
                if (mineGaver[i] == null) {
                    mineGaver[i] = gaven;
                    gaven = null;
                    break;
                }
            }
        }
        
        // 1. prøver å gi gaven til personen som pekes på av sammenmed.
        if (gaven != null && gividere) {
            if (sammenmed != null) {
                gaven = sammenmed.vilDuHaGaven(gaven, false);
        
            }

            // 2. prøver å gi gaven til personen som pekes på av forelsketi.
            if (gaven != null && forelsketi != null) {
                gaven = forelsketi.vilDuHaGaven(gaven, false);

            }
            // 3. prøver å gi gaven til en venn.
            int i = 0;
            while (gaven != null && i < kjenner.length) {
                if (kjenner[i] != null) {
                    gaven = kjenner[i].vilDuHaGaven(gaven, false);
                }
                i++;
                    
            }
        }
        return gaven; //returner gaven hvis ingen vil ha den.
    
    }
        
    
    boolean liker(Gave gaven) {
        if (gaven.kategori().equals(samlerInfo)) {
            return true;
        }
        return false;
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
                for (int i = 0; i < likerikke.length; ++i) {
                    if (likerikke[i] != null && likerikke[i] == p)
                        System.out.println("Det er ikke mulig å bli forelsket i noen man ikke liker.");
                        break;
                }
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
        boolean empty = true;
        
        // skriver foerst kjenninger, så forelsket i, liker ikke, sammen med og så gavene (1 pr linje)
        // sjekker om pekeren er null og skriver ut hvis den ikke er det
            // i tillegg: array: går gjennom arrayen og skriver ut hvis ikke null (vha. boolesk verdi)
		if (kjenner != null) {
            System.out.print(navn + " kjenner: ");
            skrivUtKjenninger();
        }

		if (forelsketi != null) {
	    	System.out.println(navn + " er forelsket i: " + forelsketi.hentNavn());
        }
        
        if (likerikke != null) {
            for (int i = 0; i < likerikke.length; ++i) {
                if (likerikke[i] != null) {
                    empty = false;
                }
            }
            if (empty == false){
                System.out.print(navn + " liker ikke: ");
                skrivUtLikerIkke();
            }
		}
        
        if(sammenmed != null) {
            System.out.println(navn + " er sammen med: " + sammenmed.hentNavn());
        }
        
        empty = true;
        if (mineGaver != null) {
            for (int j = 0; j < mineGaver.length; j++) {
                if (mineGaver[j] != null) {
                    empty = false;
                }
            }
            
            if (empty == false) {
                System.out.println("Jeg har disse gavene: ");
                for (int i = 0; i < mineGaver.length; ++i ){
                    if (mineGaver[i] != null) {
                        System.out.println("- " + mineGaver[i].gaveId());
                    }
                }
            }
        }

    }
    
}//slutt class Person


class Testklasse {
	public void testMetode() {
        Personer register = new Personer();
        
        
        GaveLager gavelager = new GaveLager();
        ListeAvPersoner liste = new ListeAvPersoner();
        Person ny = register.hentPerson();
        
        while (ny != null) {
            liste.settInnSist(ny);
            ny = register.hentPerson();
        }
        
        String[] navn = register.hentPersonnavn();

        for (Gave g = gavelager.hentGave(); g!= null; g=gavelager.hentGave()) {
            Gave gibort = g;
            
            for (int i = 0; i < navn.length && gibort != null; ++i) {
                gibort = liste.finnPerson(navn[i]).vilDuHaGaven(gibort);
            }
        }
        
        liste.skrivAlle();
        

    }
}
