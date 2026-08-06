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
import java.util.UUID;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractList.ArrayListUniqueZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.system.Syso;
import use.database.sql.generate.SqlUtilZZZ;
import use.database.sql.generate.TextDateiSchreiber;
import use.database.sql.generate.ZeitstempelErzeuger;

public class SqlGeneratorMain_insertForTableByHashMapFromCsv implements IConstantZZZ {
	
	public final static String sDIRECTORY_DEFAULT = "c:\\temp";
	
    private String sTable = null;
    private String sDirectory = null;
    private ArrayListUniqueZZZ<String> listasInsert = null;

    /** Selektiere die Tabelle parstg im SOSPOS System.
     *  Hole dabei die akadmischen Grade für den Studiengang (Abschluss, Studiengang, Vertiefung)
     *  Dies ist die Grundlage für den CSV Export.
     *  
     *  Erstelle das SQL diese akademischen Grade in die HISinOne Tabelle 'academicdegree' per Insert einzufügen.
     *  Dabei wird ein uniquename berechnet. 
     *  Es wird sichergestellt, dass trotz fehlendem Constraint der Datensatz nur 1x erstellt wird.
     *  
     *  Es wird eine objguid berechnet.
     * 
     */
    public SqlGeneratorMain_insertForTableByHashMapFromCsv() {
    }
    
    // Einstiegspunkt des Programms
    public static void main(String[] args) {
    	
    	main:{
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
	        		tabelle = "academicdegree"; //hard coded zum Entwickeln
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
	            //      "11","021","Diplom-Kaufmann                                             ","Diplom-Kauffrau                                             "
	            //		"11","030","Diplom-Ingenieur                                            ","Diplom-Ingenieurin                                          "
	            //		"11","032","Diplom-Chemiker                                             ","Diplom-Chemikerin                                           "
	
	            List<String> listEintrag = new ArrayList<String>();
	            String sEintragOld="";
	            System.out.print("Bitte geben Sie den Eintrag-String ein (kommagetrennt, auch mehrer Zeilen auf einmal, ggfs. mehrfach ENTER druecken)(Leerstring zum Abbrechen): ");
	            while (true) {                
	                String eintrag = reader.readLine();
	
	                //erst beim 2ten "ENTER" die Eingabe beenden
	                if ((eintrag == null || eintrag.trim().isEmpty()) && (sEintragOld == null || sEintragOld.trim().isEmpty())) {
	                    System.out.println("Eingabe beendet.");
	                    break;
	                }else {
	                	 eintrag = eintrag.trim();
	
	                     // ZUERST: Escape vorhandener einfacher Hochkommata → SQL-konform (z. B. O'Reilly → O''Reilly)
	                     eintrag = eintrag.replace("'", "''");
	                                         
	                     String[] saEintrag = eintrag.split("\n");
	                     for(String sEintragTemp : saEintrag) {
	                     	listEintrag.add(sEintragTemp);
	                     }
	                     
	                     sEintragOld = eintrag;
	                }                                       
	            }//end while(true)
	            
	            //++++++++++++++++
	            //Hier als Alternative, das Einlesen der Eintragsliste per Datei
	            //........
	        
	            
		        for(String sEintragTemp : listEintrag) {
		        	if(sEintragTemp != null && sEintragTemp.trim() != "") {
				      	  AcademicDegreeTitle objAcademicDegreeTitle = new AcademicDegreeTitle();
				      	  
				      	  String[] saEntry = parseCsvLine(sEintragTemp);
				      	  
				      	  //Datensatz übernehmen, nur wenn überhaupt ein Wert existiert
				      	  if(saEntry.length>=5) {
				      		  System.out.println(sEintragTemp);
		      			  
				      		  if(!StringZZZ.isEmpty(saEntry[3]) && !StringZZZ.isEmpty(saEntry[4])
				      				  && !saEntry[3].equalsIgnoreCase("null") && !saEntry[4].equalsIgnoreCase("null")) {
				      			
				      			  boolean bSuccess = addStaticCustomValues(objAcademicDegreeTitle, saEntry);
				      			  if(!bSuccess) {
				      				  System.out.println("Fehler: Statische Werte nicht erfolgreich hinzugefügt.");
				      				  break main;
				      			  }
				      			  
						      	  //Schlüssel besteht aus Abschluss | Studiengang | Vertiefunge
						      	  String sKey = saEntry[0] + "|" + saEntry[1] + "|" + saEntry[2];
						      	  objAcademicDegreeTitle.setDefaulttext(saEntry[3]);
						      	  objAcademicDegreeTitle.setDefaulttext_female(saEntry[4]);
						      	  objAcademicDegreeTitle.setLongtext(saEntry[3]);             //Defaulttext = Longtext
						      	  objAcademicDegreeTitle.setLongtext_female(saEntry[4]);      //dito
						      	  hmAcademicDegreeTitle.put(sKey, objAcademicDegreeTitle);				      	        	  						      	  
				      		  }
		        		}
		        	}
		        }//end for ... listEintrag	
	            
		        boolean bTransformed = erzeuger.transformHashMapToDbInsert(hmAcademicDegreeTitle);
	                   
	            List<String> listInsert = erzeuger.getListInsert();
	            Syso.println(SqlUtilZZZ.createSearchPathStmt("hisinone"));
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
	            
	            System.out.println("Verarbeitung beendet.");
	       } catch (IOException e) {
	           System.out.println("Fehler beim Einlesen: " + e.getMessage());
	       } catch (ExceptionZZZ ez){
	    	   System.out.println("Fehler: " + ez.getMessageLast());
	       }
    	}//end main:
       	return;     
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
    		this.listasInsert = new ArrayListUniqueZZZ<String>();
    	}
    	return this.listasInsert;
    }
    
    public void setListInsert(List<String> listasInsert) {
    	this.listasInsert = (ArrayListUniqueZZZ<String>) listasInsert;
    }
    
    
    //######################################
    public static boolean addStaticCustomValues(AcademicDegreeTitle objAcademicDegreeTitle, String[] saValue) throws ExceptionZZZ {
    	boolean bReturn = false;
    	main:{
    		if(objAcademicDegreeTitle==null) {
    			ExceptionZZZ ez = new ExceptionZZZ("objAcademicDegreeTitle", iERROR_PARAMETER_MISSING, SqlGeneratorMain_insertForTableByHashMapFromCsv.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
    		};

    		objAcademicDegreeTitle.setK_language_id(12);
    		objAcademicDegreeTitle.setPosition_of_title(0);
    		    		
    		UUID uuid = UUID.randomUUID();
    		String sObj_guid =  uuid.toString();
    		objAcademicDegreeTitle.setObj_guid(sObj_guid);
    		
    		//nun einen uniquename errechnen.
    		String sValue = saValue[4];
    		int[]iaPartLength= {4,3};
    		String sUniquename = StringZZZ.toShorten(sValue, null, iaPartLength, "x"); //Abkürzung per Default Delimiter erstellen
    		sUniquename = sUniquename.toLowerCase();
    		objAcademicDegreeTitle.setUniquename(sUniquename);
    		bReturn = true;
    	}//end main:
    	return bReturn;
    }
    
    //### Hilfsfunktionen / Komfortfunktionen
    public void addInsert(String sInsert) {
    	this.getListInsert().add(sInsert);
    }
    
    public String erstelleDateinamenDefault() {
    	String sDateTime = ZeitstempelErzeuger.holeAktuellesDatumZeitAlsString();
    	return this.getTable() + sDateTime + ".sql";
    }
    
     
    public boolean transformHashMapToDbInsert(Map<String, AcademicDegreeTitle> mapAcademicDegreeTitle) throws ExceptionZZZ {
    	boolean bReturn = false;
    	main:{
    
    		Set<String> setStg = mapAcademicDegreeTitle.keySet();    	
    		for(String sStg : setStg) {
    			AcademicDegreeTitle objAcademicDegreeTitle = mapAcademicDegreeTitle.get(sStg);
    			if(objAcademicDegreeTitle!=null) {
    				String sInsert = transformAcademicDegreeTitleToDbInsert(objAcademicDegreeTitle);
    				this.addInsert(sInsert);
    				System.out.println(sInsert);
    				System.out.println("sInsert=" + sInsert);
    			}else {
    				System.out.println("Key in Map nicht gefunden. Studiengang '" + sStg + "'");
    			}
    		}    		    	
    	}//end main:
    	return bReturn;
    }
    
    public String transformAcademicDegreeTitleToDbInsert(AcademicDegreeTitle objAcademicDegreeTitle) throws ExceptionZZZ {
    	String sReturn = null;
    	main:{
    		
    		
    		Map<String, String> aliasMap = erzeugeAliasMap(objAcademicDegreeTitle);
    		
	        // Ausgabe zur Kontrolle
	        /*for (Map.Entry<String, String> eintragMap : aliasMap.entrySet()) {
	            System.out.println(eintragMap.getKey() + " => " + eintragMap.getValue());
	        }*/
	
	        String sTable = this.getTable();
	        String sColumns = SqlUtilZZZ.erzeugeColumnsString(aliasMap);
	        String sValues = SqlUtilZZZ.erzeugeValues(aliasMap);
	        
	        System.out.println("Debugzwecke. Hier sind alle beteiligten Werte vorhanden:");	      
	        System.out.println("sColumns='" + sColumns +"'");
	        System.out.println("sValues='" + sValues +"'");
	       
	        String sColumnUnique = "uniquename";
	        String sValueUnique = objAcademicDegreeTitle.getUniquename();
	        sReturn = SqlUtilZZZ.createInsertUnique(sTable, sColumns, sValues, sColumnUnique, sValueUnique);
	        sReturn = SqlUtilZZZ.toStatement(sReturn); //sonst kann postgreSQL die Anweisungszeilen nicht unterscheiden
    	}//end main:
    	return sReturn;
    }

    // Wandelt Überschrift + Eintrag in eine Map um
    public static Map<String, String> erzeugeAliasMap(AcademicDegreeTitle objAcademicDegreeTitle) throws ExceptionZZZ {
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
        String sColumn = null; //das sind die für den Insert später verwendeten Spaltennamen
        String sValue = null; int iValue;
        
        sColumn = "defaulttext";
        sValue = objAcademicDegreeTitle.getDefaulttext();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        sColumn = "defaulttext_female";
        sValue = objAcademicDegreeTitle.getDefaulttext_female();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        //++++++++++++++++++
        sColumn = "longtext";
        sValue = objAcademicDegreeTitle.getLongtext();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        sColumn = "longtext_female";
        sValue = objAcademicDegreeTitle.getLongtext_female();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        //+++++++++++++++++++
        //+++ Statische Werte
        sColumn = "k_language_id";
        iValue = objAcademicDegreeTitle.getK_language_id();
        sValue =  SqlUtilZZZ.toSqlValue(iValue);
        mapReturn.put(sColumn, sValue);
        
        sColumn = "uniquename";
        sValue = objAcademicDegreeTitle.getUniquename();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        
        sColumn = "obj_guid";
        sValue = objAcademicDegreeTitle.getObj_guid();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        //+++++++++++++++++++
        //+++ Statische Werte wg. Constraints
        sColumn = "position_of_title";
        iValue = objAcademicDegreeTitle.getPosition_of_title();
        sValue =  SqlUtilZZZ.toSqlValue(iValue);
        mapReturn.put(sColumn, sValue);
        
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