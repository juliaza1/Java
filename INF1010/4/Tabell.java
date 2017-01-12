import java.util.Iterator;
import java.lang.Comparable;

class ArrayIterator<T> implements Iterator<T> {
    
    int current;
    Object[] data;
    
    // konstruktør
    public ArrayIterator(Object[] arr) {
        current = 0;
        data = arr;
    }
    
    public void remove() {
    }
    
    // itererer over arrayen
    public T next() {
        @SuppressWarnings("unchecked")
        final T r = (T)data[current++];
        return r;
    }
    
    // sjekker om det er et element til i arrayen
    public boolean hasNext() {
        return current < data.length;
    }
}

class Tabell<T> implements AbstraktTabell<T>{

    private int indeks;
    private Object[] data;

    public Tabell(int lengde) {
        data = new Object[lengde];
    }
    
    public boolean settInn (T obj, int indeks) {
        //returnerer true eller false avhengig om operasjonen gikk bra eller ikke
        if (indeks < 0 || indeks >= data.length) {
            return false;
        }
        
        data[indeks] = obj;
        
        return true;
    }
    
    //finne et objekt basert på indeks
    public T finn(int indeks) {
        // sjekker om angitt indeks er lovlig
        if (indeks < 0 || indeks >= data.length) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        final T ret = (T)data[indeks];
        
        return ret;
    }
    
    // metode som returner en Iterator over listen
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(data);
    }
    
}


class Element<K extends Comparable<K>, T extends Comparable> {
    T data;
    K key;
    public Element<K,T> neste, forrige;
    
    // Lag et element, sett det inn etter forrige med data d
    Element(K k, T d, Element<K,T> f) {
        data = d;
        key = k;
        forrige = f;
        
        if (f != null) {
            neste = f.neste;
            f.neste = this;
        }
        
        if (neste != null) {
            neste.forrige = this;
        }
    }
}

class ListeIterator<K extends Comparable<K>, T extends Comparable> implements Iterator<T> {
    
    Element<K,T> current;
    
    ListeIterator(Element<K,T> l) {
        current = l;
    }
    
    public void remove() {
    }
    
    public boolean hasNext() {
        return current.neste != null;
    }
    
    public T next() {
        current = current.neste;
        return current.data;
    }
}

class SortertEnkelListe <K extends Comparable<K>, T extends Comparable> implements AbstraktSortertEnkelListe<K, T> {
    Element<K,T> liste, end;
    
    public Element<K,T> start() {
        return liste.neste;
    }
    
    public Element<K,T> end() {
        return end;
    }
    
    SortertEnkelListe() {
        liste = new Element<K,T>(null, null, null);
        end = liste;
    }
    
    
    int sammenlign(T a, T b) {
        @SuppressWarnings("unchecked")
        final int r = a.compareTo(b);
        return r;
    }
    
    Element<K,T> finnForgjenger(T obj) {
        Element<K,T> itr = liste;
        
        while (itr != null) {
            if (itr.data != null) {
                if (sammenlign(itr.data,obj) >= 0) {
                    itr = itr.forrige;
                    break;
                }
            }
            if (itr.neste == null) {
                break;
            }
            itr = itr.neste;
        }
        
        return itr;
    }
    
    public boolean settInnSortert(K key, T obj) {
        
        if (finn(key) != null) {
            return false;
        }
        
        Element<K,T> itr = finnForgjenger(obj);
        Element<K,T> inserted = new Element<K,T>(key, obj, itr);
        
        if (inserted.neste == null) {
            end = inserted;
        }
        
        return true;
    }
    
    public T finn(K n) {
        Element<K,T> itr = start();
        
        while (itr != null) {
            if (itr.key.compareTo(n) == 0) {
                return itr.data;
            }
            
            itr = itr.neste;
        }
        
        return null;
    }

    // returnerer en Iterator slik at innholdet kan listes opp sortert, minste foerst
    public Iterator<T> iterator() {
        return new ListeIterator<K,T>(liste);
    }
}

class EnkelReseptListe extends SortertEnkelListe<Integer, Resept> {
    
    public boolean settInnResept(Resept r) {
        return settInnSortert(r.reseptNr(), r);
    }
    
    int sammenlign(Resept a, Resept b) {
        return a.compareTo(b);
    }
}

class YngsteForstReseptListe extends EnkelReseptListe {
}

class EldsteForstReseptListe extends EnkelReseptListe {
    int sammenlign(Resept a, Resept b) {
        return b.compareTo(a);
    }
}

interface AbstraktTabell<T> extends Iterable<T> {
    boolean settInn(T obj, int indeks);
    T finn(int indeks);
    Iterator<T> iterator();
}

interface AbstraktSortertEnkelListe<K extends Comparable<K>, T extends Comparable> extends Iterable<T> {
    boolean settInnSortert(K key, T obj);

    T finn(K s);
    
    Iterator<T> iterator();
}










