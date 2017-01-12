//halvorbu, Halvor Holhjem Bugge, 9
//juliaza, Julia Zack, 9


class Oblig1 {
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
    // = uvenner 
    // venner blir da alle personer som pekes paa av kjenner 
    // unntatt personen(e) som pekes paa av likerikke
    
    private Person forelsketi;
    private Person sammenmed;
    
    Person(String navn, int lengde) { 
		this.navn = navn;
		kjenner = new Person[lengde];
		likerikke = new Person[lengde];
    }
    
    public String hentNavn() { 
	return navn; 
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
    }
    
}//slutt class Person

class Testklasse{
	public void testMetode() {
		Person julvor = new Person("Julvor", 3);
		Person ask = new Person("Ask", 3);
		Person dana = new Person("Dana", 3);
		Person tom = new Person("Tom", 3);
	
		julvor.blirKjentMed(ask);
		julvor.blirKjentMed(dana);
		julvor.blirKjentMed(tom);
		
		ask.blirKjentMed(julvor);
		ask.blirKjentMed(dana);
		ask.blirKjentMed(tom);
			
		ask.blirForelsketI(julvor);
		
		ask.blirUvennMed(dana);
		ask.blirUvennMed(tom);
			
		dana.blirKjentMed(ask);
		dana.blirKjentMed(tom);
		dana.blirKjentMed(julvor);
	
		dana.blirForelsketI(tom);
		
		dana.blirUvennMed(julvor);
		
		tom.blirKjentMed(julvor);
		tom.blirKjentMed(dana);
		tom.blirKjentMed(ask);
			
		tom.blirUvennMed(julvor);
		tom.blirUvennMed(ask);
			
		tom.blirForelsketI(dana);
			
		julvor.skrivUtAltOmMeg();
		ask.skrivUtAltOmMeg();
		dana.skrivUtAltOmMeg();
		tom.skrivUtAltOmMeg();	
	}	
}
