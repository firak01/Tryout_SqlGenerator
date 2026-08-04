package use.database.sql.generate.keytableAny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import use.database.sql.generate.SqlUtilZZZ;
import use.database.sql.generate.TextDateiSchreiber;
import use.database.sql.generate.ZeitstempelErzeuger;

public class SqlGeneratorMain_insertForTableByCsv {
	
	public final static String sDIRECTORY_DEFAULT = "c:\\Temp";
	
    private String sTable = null;
    private String sDirectory = null;
    private ArrayList<String> listasInsert = null;

    public SqlGeneratorMain_insertForTableByCsv() {
    }
    
    // Einstiegspunkt des Programms
    public static void main(String[] args) {
        String ueberschrift = "";
        String tabelle = "";
        String directory = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SqlGeneratorMain_insertForTableByCsv erzeuger = null;

        try {
        	tabelle = args[0];
        	
        	
        	erzeuger = new SqlGeneratorMain_insertForTableByCsv();
        	
        	//Verzeichnisnamen eingeben
        	System.out.print("Bitte geben Sie den Namen des Verzeichnisse ein (Leerstring verwendet default '" + SqlGeneratorMain_insertForTableByCsv.sDIRECTORY_DEFAULT + "'): ");
            directory = reader.readLine();
            if (directory != null && !directory.trim().isEmpty()) {
            	erzeuger.setDirectory(directory);
            }else {
            	erzeuger.setDirectory(SqlGeneratorMain_insertForTableByCsv.sDIRECTORY_DEFAULT);
            }

        	
            // Tabellennamen ggfs. eingeben
            if(tabelle==null || tabelle.trim().isEmpty()) {
            	System.out.print("Bitte geben Sie den Tabellennamen als String ein (Leerstring zum Abbrechen): ");
            	tabelle = reader.readLine();
            	if (tabelle == null || tabelle.trim().isEmpty()) return;
            }
        	erzeuger.setTable(tabelle);
            
            /* Da wir wir aus dem Gleichen "System" in das gleiche "System" transferieren. */
            // Die Überschrift eingeben,
            System.out.print("Bitte geben Sie die Tabellenspalten als String ein. (Leerstring zum Abbrechen): ");
            ueberschrift = reader.readLine();
            if (ueberschrift == null || ueberschrift.trim().isEmpty()) return;            
            
            
  
            // Wiederholt Einträge verarbeiten
            String sInsert=null;
            System.out.print("Bitte geben Sie den Eintrag-String ein (kommagetrennt, auch mehrer Zeilen auf einmal, ggfs. mehrfach ENTER druecken)(Leerstring zum Abbrechen): ");
            while (true) {                
                String eintrag = reader.readLine();

                if (eintrag == null || eintrag.trim().isEmpty()) {
                    System.out.println("Eingabe beendet.");
                    break;
                }

                eintrag = eintrag.trim();

                // ZUERST: Escape vorhandener einfacher Hochkommata → SQL-konform (z. B. O'Reilly → O''Reilly)
                eintrag = eintrag.replace("'", "''");

                // DANACH: Doppelte Hochkommata durch einfache ersetzen (z. B. "Max" → 'Max')
                // Nein, der CSV Zeilen-Parser braucht doppelte Hochkommata, also nicht:      eintrag = eintrag.replace("\"", "'");

                String[] saEintrag = eintrag.split("\n");
                for(String sEintrag : saEintrag) {
                	sInsert = erzeuger.transformCsvStringToDbInsert(ueberschrift.trim(), sEintrag);
                	erzeuger.addInsert(sInsert);
                	System.out.println(sInsert);
                }
            }
            
            if(!erzeuger.getListInsert().isEmpty()) {
            	String sDateiname = erzeuger.erstelleDateinamenDefault();
            	boolean bSuccess = TextDateiSchreiber.schreibeTextdatei(erzeuger.getDirectory(), sDateiname, erzeuger.getListInsert());
            	if(bSuccess) {
            		System.out.println("Erzeugte Textdatei kann fuer Inserts verwendet werden.");
            	}else{
            		System.out.println("Textdatei nicht erzeugt.");
            	}
            }
            return;
        } catch (IOException e) {
            System.out.println("Fehler beim Einlesen: " + e.getMessage());
        }
    }
    
    
    //### GETTER / SETTER
    public SqlGeneratorMain_insertForTableByCsv(String sDirectory, String sTable) {
        this.sTable = sTable;
        this.sDirectory = sDirectory;
    }

    public String getTable() {
        return this.sTable;
    }

    public void setTable(String sTable) {
        this.sTable = sTable;
    }
    
    public String getDirectory() {
        return this.sDirectory;
    }

    public void setDirectory(String sDirectory) {
        this.sDirectory = sDirectory;
    }
    
    public ArrayList<String> getListInsert(){
    	if(this.listasInsert==null) {
    		this.listasInsert = new ArrayList<String>();
    	}
    	return this.listasInsert;
    }
    
    public void setListInsert(ArrayList<String> listasInsert) {
    	this.listasInsert = listasInsert;
    }
    
    
    //### Hilfsfunktionen / Komfortfunktionen
    public void addInsert(String sInsert) {
    	this.getListInsert().add(sInsert);
    }
    
    public String erstelleDateinamenDefault() {
    	String sDateTime = ZeitstempelErzeuger.holeAktuellesDatumZeitAlsString();
    	return this.getTable() + sDateTime + ".sql";
    }
    
    

    // Hauptfunktion zur Verarbeitung von Überschrift + Eintrag 
    public String transformCsvStringToDbInsert(String ueberschrift, String eintrag) {
    	String sReturn = null;
    	main:{
	        Map<String, String> aliasMap = erzeugeAliasMap(ueberschrift, eintrag);
	
	        // Ausgabe zur Kontrolle
	        /*for (Map.Entry<String, String> eintragMap : aliasMap.entrySet()) {
	            System.out.println(eintragMap.getKey() + " => " + eintragMap.getValue());
	        }*/
	
	        String sTable = this.getTable();
	        String sColumns = SqlUtilZZZ.erzeugeColumnsString(aliasMap);
	        String sValues = SqlUtilZZZ.erzeugeValuesString(aliasMap);
	       
	        sReturn = "INSERT INTO " + sTable + " (" + sColumns + ") VALUES (" + sValues + ") ON CONFLICT DO NOTHING;";

    	}//end main:
    	return sReturn;
    }

    
   

    // Wandelt Überschrift + Eintrag in eine Map um
    public static Map<String, String> erzeugeAliasMap(String ueberschrift, String eintrag) {
        Map<String, String> map = new LinkedHashMap<String, String>(); // Reihenfolge bewahren

        String[] keys = ueberschrift.split(",");
        //String[] values = eintrag.split(","); //TODOGOON;In den Texten können auch Kommata stehen. Darum diese Aufteilung verbessern.                
        String[] values = parseCsvLine(eintrag);
        
        int laenge = Math.min(keys.length, values.length);

        for (int i = 0; i < laenge; i++) {
            String key = keys[i].trim();
            String value = values[i].trim();
            map.put(key, value);
        }

        return map;
    }
    
    public static List<String> parseCsvLineAsList(String line) {
        List<String> result = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Doppelte Hochkommas im String ("") → ein Hochkomma
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++; // nächste Anführungszeichen überspringen
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0); // zurücksetzen
            } else {
                current.append(c);
            }
        }
        result.add(current.toString()); // letztes Element hinzufügen

        return result;
    }
    
    public static String[] parseCsvLine(String line) {
        List<String> result = parseCsvLineAsList(line);

        // Rückgabe als Array
        return result.toArray(new String[result.size()]);
    }
}