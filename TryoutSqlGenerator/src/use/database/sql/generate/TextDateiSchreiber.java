package use.database.sql.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextDateiSchreiber {

    public static boolean schreibeTextdatei(String pfad, String dateiname, ArrayList<String> zeilen) {
    	boolean bReturn = false;
    	main:{
	        BufferedWriter writer = null;
	        try {
	            // Erstelle das Verzeichnis, falls es nicht existiert
	            File verzeichnis = new File(pfad);
	            if (!verzeichnis.exists()) {
	                verzeichnis.mkdirs();
	            }
	
	            // Erstelle die Datei
	            File datei = new File(verzeichnis, dateiname);
	            writer = new BufferedWriter(new FileWriter(datei));
	
	            // Schreibe jede Zeile
	            for (String zeile : zeilen) {
	                writer.write(zeile);
	                writer.newLine();
	            }
	
	            System.out.println("Datei erfolgreich geschrieben: " + datei.getAbsolutePath());
	        } catch (IOException e) {
	            System.err.println("Fehler beim Schreiben der Datei: " + e.getMessage());
	        } finally {
	            try {
	                if (writer != null) writer.close();
	            } catch (IOException e) {
	                System.err.println("Fehler beim Schließen des Writers: " + e.getMessage());
	            }
	        }
        	bReturn = true;
    	}//end main
    	return bReturn;
    }
}
