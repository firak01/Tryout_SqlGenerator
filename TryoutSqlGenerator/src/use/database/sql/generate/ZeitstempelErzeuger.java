package use.database.sql.generate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZeitstempelErzeuger {

    public static String holeAktuellesDatumZeitAlsString() {
        // Erlaubtes Format: Jahr-Monat-Tag_Stunde-Minute-Sekunde
        SimpleDateFormat formatierer = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date jetzt = new Date();
        return formatierer.format(jetzt);
    }
}

