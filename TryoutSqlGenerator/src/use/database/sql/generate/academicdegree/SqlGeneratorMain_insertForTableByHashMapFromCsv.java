package use.database.sql.generate.academicdegree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import use.database.sql.generate.TextDateiSchreiber;
import use.database.sql.generate.ZeitstempelErzeuger;

public class SqlGeneratorMain_insertForTableByHashMapFromCsv {
	
	public final static String sDIRECTORY_DEFAULT = "c:\\temp";
	
    private String sTable = null;
    private String sDirectory = null;
    private ArrayList<String> listasInsert = null;

    public SqlGeneratorMain_insertForTableByHashMapFromCsv() {
    }
    
    // Einstiegspunkt des Programms
    public static void main(String[] args) {
    	
    	//TODOGOON20260803: Es müssen hier noch die VERTIEFUNGEN rein in die CSV Datei, dazu natürlich auch das SQL mit der SOSPOS Abfrage erweitern.
    	//                  Diese kommen dann in den Schlüssel.
    	//TODOGOON20260803: Als eine Variante die Komplette Datei einlesen und nicht Zeileweise über die Eingabe... 
    	//                  Beim Bauen der Insertstrings dann darauf achten, dass alle Schlüsselbestandteile vorhanden sind.
    	
    	//TODOGOON20260803: Im Main die Klasse aufrufen. Das ist dann eine andere Klasse, ohne Klassennamen ...Main ... 
    	
        String ueberschrift = "";
        String tabelle = "";
        String directory = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SqlGeneratorMain_insertForTableByHashMapFromCsv erzeuger = null;

        try {
        	if(args!=null && args.length>=1) {
        		tabelle = args[0];
        	}else {
        		tabelle = "accademicdegree"; //hard coded zum Entwickeln
        	}
        	
        	erzeuger = new SqlGeneratorMain_insertForTableByHashMapFromCsv();
        	
        	//Verzeichnisnamen eingeben
        	System.out.print("Bitte geben Sie den Namen des Verzeichnisse ein (Leerstring verwendet default '" + SqlGeneratorMain_insertForTableByHashMapFromCsv.sDIRECTORY_DEFAULT + "'): ");
            directory = reader.readLine();
            if (directory != null && !directory.trim().isEmpty()) {
            	erzeuger.setDirectory(directory);
            }else {
            	erzeuger.setDirectory(SqlGeneratorMain_insertForTableByHashMapFromCsv.sDIRECTORY_DEFAULT);
            }

        	
            // Tabellennamen ggfs. eingeben
            if(tabelle==null || tabelle.trim().isEmpty()) {
            	System.out.print("Bitte geben Sie den Tabellennamen als String ein (Leerstring zum Abbrechen): ");
            	tabelle = reader.readLine();
            	if (tabelle == null || tabelle.trim().isEmpty()) return;
            }
        	erzeuger.setTable(tabelle);
           
        	//Wir müssen nun eine HashMap mit dem entsprechenden AcademicDegree-Objekt füllen
            //ausgehend von der csv-Datei
            Map<String,AcademicDegreeTitle> hmAcademicDegreeTitle = new LinkedHashMap<String,AcademicDegreeTitle>();

            /* Hier ist die Überschrift egal,
             * da diese Spaltennamen aus der sospos ausgangstabelle hat.
             * In HISinone sind das eh andere Tabellenspalten, der Tabellenname wurde oben angegeben. 
             */
            /* Nur wenn wir aus dem Gleichen "System" in das gleiche "System" transferieren würden.
            // Die Überschrift eingeben, 
            System.out.print("Bitte geben Sie die Tabellenspalten als String ein. (Leerstring zum Abbrechen): ");
            ueberschrift = reader.readLine();
            if (ueberschrift == null || ueberschrift.trim().isEmpty()) return;            
            */
           
            // Wiederholt Einträge verarbeiten
            // z.B. "11","012","Diplom-Archäologe                                           ","Diplom-Archäologin                                          "
            //      "11","032","Diplom-Chemiker                                             ","Diplom-Chemikerin                                           "
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
                
	              String[] saEintrag = eintrag.split("\n");
	              for(String sEintrag : saEintrag) {
	            	  AcademicDegreeTitle objAcademicDegreeTitle = new AcademicDegreeTitle();
	            	  String[] saEntry = parseCsvLine(sEintrag);
	            	  
	            	  String sKey = saEntry[0] + "|" + saEntry[1];
	            	  objAcademicDegreeTitle.setDefaulttext(saEntry[2]);
	            	  objAcademicDegreeTitle.setDefaulttext_female(saEntry[3]);
	            	  objAcademicDegreeTitle.setLongtext(saEntry[2]);             //Defaulttext = Longtext
	            	  objAcademicDegreeTitle.setLongtext_female(saEntry[3]);      //dito
	            	  hmAcademicDegreeTitle.put(sKey, objAcademicDegreeTitle);
	            	  
	            	  
	              	   boolean bTransformed = erzeuger.transformHashMapToDbInsert(hmAcademicDegreeTitle);
	              	
	              	   
	              }          
            }//end while(true)
            
            List<String> listInsert = erzeuger.getListInsert();
            for(String sInsertTemp : listInsert) {
                  	System.out.println(sInsertTemp);
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
    public SqlGeneratorMain_insertForTableByHashMapFromCsv(String sDirectory, String sTable) {
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
    
     
    public boolean transformHashMapToDbInsert(Map<String, AcademicDegreeTitle> mapAcademicDegreeTitle) {
    	boolean bReturn = false;
    	main:{
    
    		Set<String> setStg = mapAcademicDegreeTitle.keySet();    	
    		for(String sStg : setStg) {
    			AcademicDegreeTitle objAcademicDegreeTitle = mapAcademicDegreeTitle.get(sStg);
    			if(objAcademicDegreeTitle!=null) {
    				String sInsert = transformAcademicDegreeTitleToDbInsert(objAcademicDegreeTitle);
    				this.addInsert(sInsert);
    				System.out.println(sInsert);
    			}else {
    				System.out.println("Key in Map nicht gefunden. Studiengang '" + sStg + "'");
    			}
    		}    		    	
    	}//end main:
    	return bReturn;
    }
    
    public String transformAcademicDegreeTitleToDbInsert(AcademicDegreeTitle objAcademicDegreeTitle) {
    	String sReturn = null;
    	main:{
    		
    		
    		Map<String, String> aliasMap = erzeugeAliasMap(objAcademicDegreeTitle);
    		
	        // Ausgabe zur Kontrolle
	        /*for (Map.Entry<String, String> eintragMap : aliasMap.entrySet()) {
	            System.out.println(eintragMap.getKey() + " => " + eintragMap.getValue());
	        }*/
	
	        String sTable = this.getTable();
	        String sColumns = erzeugeColumnsString(aliasMap);
	        String sValues = erzeugeValuesString(aliasMap);
	       
	        sReturn = "INSERT INTO " + sTable + " (" + sColumns + ") VALUES (" + sValues + ") ON CONFLICT DO NOTHING;";
		
    	}//end main:
    	return sReturn;
    }

    // Erzeugt den Spaltenstring, ignoriere die ggfs. vorhandene ID Spalte
    public String erzeugeColumnsString(Map<String, String> aliasMap) {
        StringBuilder sb = new StringBuilder();
        for (String column : aliasMap.keySet()) {
        	if(!column.equalsIgnoreCase("\"id\"")) {
        		if (sb.length() > 0) sb.append(", ");
        		sb.append(column);
        	}
        }
        return sb.toString();
    }

    // Erzeugt den Wertstring mit SQL-konformen Hochkommata, ignoriere die ggfs. vorhandene ID Spalte
    public String erzeugeValuesString(Map<String, String> aliasMap) {
        StringBuilder sb = new StringBuilder();
        for (String column : aliasMap.keySet()) {
        	if(!column.equalsIgnoreCase("\"id\"")) {
	            String value = aliasMap.get(column);
	            if (sb.length() > 0) sb.append(", ");
	
	            // Wenn Wert schon in einfache Hochkommata eingeschlossen ist, nicht doppelt verpacken
	            if (value.startsWith("'") && value.endsWith("'")) {
	                sb.append(value);
	            } else {
	            	if(value.equalsIgnoreCase("NULL")) {
	            		sb.append(value);
	            	}else {
	            		sb.append("'").append(value).append("'");
	            	}
	            }
        	}
        }
        return sb.toString();
    }

    // Wandelt Überschrift + Eintrag in eine Map um
    public static Map<String, String> erzeugeAliasMap(AcademicDegreeTitle objAcademicDegreeTitle) {
        Map<String, String> mapReturn = new LinkedHashMap<String, String>(); // Reihenfolge bewahren

        //TODOGOON20260803 - Hier wird dann aus dem Objekt 
        //Der Name des Getters als ueberschrift genommen
        //Der Wert des Getters als value
        
        //Das Parsen aus einer CSV Datei passiert hier in dem Fall vorher, beim Aufbauen der HashMap
        //also nicht:
//      String[] keys = ueberschrift.split(",");                       
//      String[] values = parseCsvLine(eintrag);
//      
//      int laenge = Math.min(keys.length, values.length);
//
//      for (int i = 0; i < laenge; i++) {
//          String key = keys[i].trim();
//          String value = values[i].trim();
//          map.put(key, value);
//      }

        //sondern ...
        String sKey = null; String sValue = null;
        
        sKey = "defaulttext";
        sValue = objAcademicDegreeTitle.getDefaulttext();
        mapReturn.put(sKey, sValue);
        
        sKey = "defaulttext_female";
        sValue = objAcademicDegreeTitle.getDefaulttext_female();
        mapReturn.put(sKey, sValue);
        
        //++++++++++++++++++
        sKey = "longtext";
        sValue = objAcademicDegreeTitle.getLongtext();
        mapReturn.put(sKey, sValue);
        
        sKey = "longtext_female";
        sValue = objAcademicDegreeTitle.getLongtext_female();
        mapReturn.put(sKey, sValue);

        return mapReturn;
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