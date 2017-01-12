public interface Gave {
  String kategori();
    // Returnerer en tekststreng som gjør det mulig å vite hva slags gave
    // dette er, f.eks. «bok», «plate», «vin», «sko», «sjokolade», «bil»...

  String gaveId();
    // Returnerer en tekststreng som identifiserer gaven. To gaver med
    // lik {\em kategori} og {\em gaveId} er samme gave (gjenstand).
}
