import easyIO.*;

class Oblig3 {
    public static void main (String[] args) {
		Oblig3 oblig3kj�ring = new Oblig3();
	}

	Oblig3 () {
		String dataFil = "Haihus.data";
		String str�mFil = "Lysregning.data";
		Hybelhus hybh = new Hybelhus(dataFil, str�mFil);
		hybh.meny();

	}
}


class Hybelhus {
	private String dataFil;
	private String str�mFil;

	final int ANT_KOR = 4;
	final int ANT_ROM = 8;
	final String TOM_HYBEL = "TOM HYBEL";

	private int totalFortjeneste;
	private int m�nederTommeHybler;
	private int m�nederTotalt;

	In tast = new In();
	Out skjerm = new Out();




	int gangNrArray = 0;
	int romNr = 0;

	Hybel[][] hyblene = new Hybel[ANT_KOR][ANT_ROM];

	Hybelhus(String dataFil, String str�mFil) { //konstrukt�r, leser inn Haihus.data
		this.dataFil = dataFil;
		this.str�mFil = str�mFil;

		Konvertering konv = new Konvertering();
		skjerm.outln("Leser fra " + dataFil + ":");
		In haiHusData = new In(dataFil);

		for (int i = 0; i < ANT_KOR * (ANT_ROM - 1);i++) {
			int gang = haiHusData.inInt(";");
			skjerm.out(gang);
			char bokstav = haiHusData.inChar(";");
			skjerm.out(bokstav);
			String studentNavn = haiHusData.inWord(";");
			skjerm.out(" " + studentNavn);
			int saldo = haiHusData.inInt(";");
			skjerm.outln(" " + saldo);
			haiHusData.inLine();
			int boksTilTall = konv.bokstavTilTall(bokstav);
			hyblene[gang - 1][boksTilTall] = new Hybel(studentNavn , saldo);

		} //end for
		m�nederTotalt = haiHusData.inInt(";");
		m�nederTommeHybler = haiHusData.inInt(";");
		totalFortjeneste = haiHusData.inInt(";");
		haiHusData.close();
	}//end konstrukt�r

	void meny() {


	int valg = 8;
	while (valg != 0) {

		if (valg != 8) {
			skjerm.outln();
			skjerm.outln("Trykk Enter for � fortsette");
			tast.inLine();
			tast.inLine();
			skjerm.outln();
		}
		skjerm.outln(" --- Haihus hovedmeny --- ");
		skjerm.outln();
		skjerm.outln("0. Avslutt");
		skjerm.outln("1. Skriv liste over ledige hybler");
		skjerm.outln("2. Registrer ny leietager");
		skjerm.outln("3. Registrer frivillig flytting av leietager");
		skjerm.outln("4. M�nedkj�ring av husleie med str�m");
		skjerm.outln("5. Registrer betaling fra leietager");
		skjerm.outln("6. Sjekk om noen leietagere skal kastes ut");
		skjerm.outln("7. Skriv totalrapport");
		skjerm.out("Skriv inn ditt valg (0-7) ");
		valg = tast.inInt();

		switch (valg) {
			case 1:
				ledigeHybler();
			break;
			case 2:
				regNyLeietaker();
			break;
			case 3:
				frivilligFlytting();
			break;
			case 4:
				m�nedsKj�ring();
			break;
				} //end switch
			}//end while
		}//end meny


	void ledigeHybler() {
		Konvertering konv = new Konvertering();

			skjerm.outln("--- Liste over ledige hybler ---");
			skjerm.outln();
			for(int gangNrArray = 0; gangNrArray<4; gangNrArray++)	{
				for (int romNr = 1; romNr<8; romNr++) {
					if(hyblene[gangNrArray][romNr].leietaker.navn.equals(TOM_HYBEL)) {
						char romBokstav = konv.tallTilBokstav(romNr);
						int gangNr = konv.tallArrayTilGangNr(gangNrArray);
						skjerm.outln("Rom nr. " + gangNr + romBokstav + " er ledig" );
					}//end if
				}//end for
			}//end for
	}//end ledigHybel

	void regNyLeietaker() {
		Konvertering konv = new Konvertering();
		skjerm.outln(" --- Legg til ny leietaker ---");
		skjerm.outln();
		skjerm.outln("Hvilket rom skal leies ut?");
		skjerm.out("Skriv inn gangnummer. ");
		int gangNr = tast.inInt();

		skjerm.out("Skriv inn rombokstav. ");
		tast.inLine();
		char romBokstav = tast.inChar();
		int gangNrArray = konv.tallAngittTilArray(gangNr);
		int romNrArray = konv.bokstavTilTall(romBokstav);

		if (gangNrArray > 3 || romNrArray == 0 || romNrArray > 7) { //test om rom finnes
			skjerm.outln("Rommet eksisterer ikke, pr�v igjen!");
		} else {
			if(hyblene[gangNrArray][romNrArray].leietaker.navn.equals("TOM HYBEL")) { //test om rom er ledig
				skjerm.outln("Skriv inn navn p� leietaker.");

				tast.inLine();
				String nyLeietaker = tast.inLine();
				hyblene[gangNrArray][romNrArray].leietaker.navn = nyLeietaker;
				hyblene[gangNrArray][romNrArray].utest�ende = hyblene[gangNrArray][romNrArray].utest�ende + 8000;
				totalFortjeneste = totalFortjeneste + 3000;

				skjerm.outln("Rom " + gangNr + romBokstav + " er blitt utleid til " + nyLeietaker + ".");
			} //end if
			else {
				skjerm.outln("Rommet er ikke ledig, pr�v igjen!");
			} // end else
		} // end else

	} //end nyLeietaker


	void frivilligFlytting() {
		Konvertering konv = new Konvertering();
		skjerm.outln(" --- Frivillig flytting av leietaker --- ");
		skjerm.out("Skriv inn gangnummer ");
		int gangNr = tast.inInt();
		skjerm.out("Skriv inn rombokstav ");
		tast.inLine();
		char romBokstav = tast.inChar();
		int gangNrArray = konv.tallAngittTilArray(gangNr);
		int romNrArray = konv.bokstavTilTall(romBokstav);
		skjerm.out("Skriv inn navn p� leietaker: ");
		tast.inLine();
		String kunde = tast.inLine();
		if (hyblene[gangNrArray][romNrArray].leietaker.navn.equals(kunde)) {
			skjerm.outln("Flytter " + kunde + " i hybel " + gangNr + romBokstav + "?");
			skjerm.outln("1. Ja");
			skjerm.outln("2. Nei");
			int svar = tast.inInt();
			if (svar == 1) {
				hyblene[gangNrArray][romNrArray].leietaker.navn = "TOM HYBEL";
				int studentTilGode = hyblene[gangNrArray][romNrArray].utest�ende - 800;
				skjerm.outln(kunde + " flyttet ut av hybel " + gangNr + romBokstav);
				skjerm.outln(kunde + " har " + studentTilGode + " kr. til gode.");
				hyblene[gangNrArray][romNrArray].utest�ende = 0;
			} // end if
			else {
				skjerm.outln(kunde + " ble ikke registrert flyttet");
			} // end else
		} // end if
	} //end frivilligFlytting

	void m�nedsKj�ring() {
		Konvertering konv = new Konvertering();
		for (int gang = 0; gang < 4; gang++) {
			for (int rom = 1; rom < 7; rom++) {

				if (!hyblene[gang][rom].leietaker.navn.equals("TOM HYBEL")) {

					hyblene[gang][rom].utest�ende = hyblene[gang][rom].utest�ende - 3000; //fast m�nedspris
					totalFortjeneste = totalFortjeneste + 2000; //fortjeneste for utleid hybel
				} else {

					totalFortjeneste = totalFortjeneste - 1000; //tap ikke utleid hybel
				} // end else
			} // end for
		} // end for
		In lysFil = new In("Lysregning.data");
		int kwhTotalt = 0;
		for (int lys = 1; lys < 33; lys++) {
			int husNr = lysFil.inInt(":");
			int gangNr = lysFil.inInt(":");
			char romBoks = lysFil.inChar(":");
			int lysRegning = lysFil.inInt(":\n");
			//lysFil.inLine();
			skjerm.outln(husNr + " og " + gangNr + " og " + romBoks + " og " + lysRegning);
			int gangNrArray = konv.tallAngittTilArray(gangNr);
			int romNrArray = konv.bokstavTilTall(romBoks);
			kwhTotalt = kwhTotalt + lysRegning;
			int nyStr�m = lysRegning * 2;
			int saldo = hyblene[gangNrArray][romNrArray].utest�ende;
			int nySaldo = saldo - nyStr�m;
			hyblene[gangNrArray][romNrArray].utest�ende = nySaldo;
		} // end for
		double str�mInntekter = kwhTotalt * 2;
		double str�mUtgifter = kwhTotalt * 0.8;
		int str�mPenger = (int) (str�mInntekter - str�mUtgifter);
		totalFortjeneste = totalFortjeneste + str�mPenger;
	} // end m�nedkj�ring
}//end klasse


class Konvertering {
	char tallTilBokstav(int romTall) { //konverterer tall i array til bokstav p� skjerm
		char romBokstav = (char) ('A' + romTall);
		return romBokstav;
	}//end tallTilBokstav

	int tallArrayTilGangNr (int gangArray) { //konverterer fra arrayposisjon til gangnummer utskrevet
		int gangNr = 1 + gangArray;
		return gangNr;
	} // end tallArrayTilGangNr

	int bokstavTilTall(char romBokstav) { //konverterer fra bokstav til tall
		int romTall = (int) (romBokstav - 'A');
		return romTall;
	}

	int tallAngittTilArray (int gangNr) { //konverterer fra angitt tall til arrayposisjon
		int gangNrArray = gangNr - 1;
		return gangNrArray;


	} //end tallAngittTilArray


} //end konvertering

class Hybel {
	//String leietaker;
	int utest�ende;
	Student leietaker;

	In tast = new In();
	Out skjerm = new Out();

	Hybel(String studentNavn, int saldo) {
		//leietaker = studentNavn;
		utest�ende = saldo;
		if (!studentNavn.equals("TOM HYBEL")) {
			Student stud = new Student(studentNavn, utest�ende);
			leietaker = stud;
		}

	}
}


class Student
{
	int saldo;
	String navn;
	Student (String n, int saldo) {
		navn = n;
		this.saldo = saldo;
	}


}
