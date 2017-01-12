

// 2014.02.06: Gitt nytt navn til metoden settInnIStarten...

public class ListeAvPersoner {
    private Person personliste, sistePerson;
    private int antall;
    
    ListeAvPersoner(){
        // skal etablere datastrukturen for tom liste:
        Person lh = new Person("LISTEHODE!!");
        personliste = lh;
        sistePerson = lh;
        antall = 0;
    }
    
    /*
     Invariante tilstandspåstander (skal gjelde før og etter alle metodekall):
     
     - personliste peker på listehodet
     - første person i lista er første person etter
     listehodet, dvs. personliste.neste
     - sisteperson peker på siste person i lista, dvs.
     - sisteperson.neste er alltid null
     - ingen andre personobjekter har neste som er null
     
     - Når lista er tom skal (tilstanden etableres av konstruktør):
     - antall innholde tallet 0
     - personliste peke på listehodet
     - sistePerson peke på listehodet
     - personliste.neste være null
     */
    
    
    
    public boolean erILista (Person p) {
        // sjekker om et personobjekt ligger i lista eller ikke
        //bruker forhåndsdefinert metode finnPerson
        if (finnPerson(p.hentNavn()) == p){
            return true;
        }
        return false;
    }
    
   
    
    public void settInnIStarten(Person nypers){
        if (!erILista(nypers)) { // sjekker at personer som ligger i lista ikke kan legges inn
            nypers.neste = personliste.neste;
            personliste.neste = nypers;
            if (sistePerson.neste == nypers) // nyperson er ny siste
                sistePerson = nypers;
            antall++;
        }
    }
    
    public void settInnSist(Person inn){
        if (!erILista(inn)) {
            sistePerson.neste = inn;
            sistePerson = inn;
            antall++;
        }
    }
    
    
    
    public void settInnEtter(Person denne, Person nypers) {
        if(nypers != denne) {
            //sjekker at parameteren 'denne' virkelig peker på et objekt i lenkelista
            if (!erILista(nypers) && (erILista(denne))) {
            nypers.neste = denne.neste;
            denne.neste = nypers;
            if (sistePerson.neste == nypers)  // nyperson er ny siste
                sistePerson = nypers;
            antall++;
            }
        }
    }
    
    public Person finnPerson(String s) {
        Person p = personliste.neste;
        for (int i = antall; i>0; i--) {
            if (p.hentNavn().equals(s)) return p;
            else p = p.neste;
        }
        return null;
    }
    
    
    public void skrivAlle() {
        Person p = personliste.neste;
        for (int i = antall; i>0; i--) {
            System.out.println("------");
     	    System.out.print(antall - i +1 + ": ");
            p.skrivUtAltOmMeg();
            p = p.neste;

        }
        System.out.println("=======");
    }
}



