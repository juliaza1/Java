import java.util.*;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedReader;

class oblig2222 {
    public static void main (String[] args) throws FileNotFoundException {
	Meny minmeny = new Meny();
	minmeny.kommandoLoekke();
    }
}

class Meny {

    Scanner tast = new Scanner(System.in);
    java.io.File fil = new java.io.File("fugler.txt");

    void kommandoLoekke()
    {	
	int valg;
	do {
	    valg= skrivMeny();
		switch (valg) {
		case 1: registrer();break;
		case 2: filtrer("Angi fugletype", 0);break;
		case 3: filtrer("Angi sted", 2);break;
		case 4: System.out.println("Ha en fin dag!");break;
		default: System.out.println("Gi et tall mellom 1 og 4");
		}
	    }while(valg!=4);
    } //slutt kommandoloekke
    
  
    void filtrer(String melding, int sokIndeks)
    {
	System.out.println(melding);
	String input = tast.nextLine().trim();
	skrivFugletype(input,sokIndeks);
    }   // funksjon for case 2 og 3

    
    int skrivMeny()
    {
	int valg;
        System.out.println("Tast 1 for å legge til en observasjon");
	System.out.println("Tast 2 for å se alle observasjonene for en fugletype");
        System.out.println("Tast 3 for å se alle observasjonene på ett bestemt sted");
       	System.out.println("Tast 4 for å avslutte programmet");
	valg = Integer.parseInt(tast.nextLine());

	return valg;
    } //slutt skrivMeny()


    void registrer()       
    {
     try {
       PrintWriter fugler = 
	   new PrintWriter(new FileWriter("fugler.txt", true));
       
	System.out.println("Registrer observasjon");
   
	System.out.println("Fugletype:");
       	String type = tast.nextLine();
	    
        System.out.println("Kjønn:");
        String kjoenn = tast.nextLine();
	    
        System.out.println("Sted:");
       	String sted = tast.nextLine();

       	System.out.println("Dato:");
       	String dato = tast.nextLine();

       	fugler.println(type +"," + kjoenn +"," + sted +","+ dato);

       	fugler.close();

     } catch (IOException e) {
       System.out.println("Skriving til fil mislyktes");
       }
	       
    } //slutt registrer


    public void skrivFugletype(String sammenlign, int sjekkIndeks) {
	try { 
	    FileReader fil = new FileReader("fugler.txt"); 
	    BufferedReader reader = new BufferedReader(fil); 
	    String tekst = ""; 
	    String linje = reader.readLine(); 
	    while (linje != null) { 
		String[] observasjonsInfo = linje.split(","); 
		if (observasjonsInfo[sjekkIndeks].trim().equals(sammenlign)) { 
		    for (int i = 0; i < observasjonsInfo.length; i++) {
			if (i != sjekkIndeks) { 
			    tekst += observasjonsInfo[i].trim()+", "; 
			    if (i == observasjonsInfo.length-1)
				tekst = tekst.substring(0, tekst.length() - 2)+"\n";
			} 
		    } 
		} 
		linje= reader.readLine(); } System.out.println(tekst); 
	}	catch (java.io.FileNotFoundException e) {
	    System.out.println("Fant ikke filen " + e);
	} catch (IOException e) {
	    System.out.println("IOEXception fanget");
	}
    } //slutt skrivFugletype



} //slutt class oblig2 	  


	  












