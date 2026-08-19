package use.database.sql.generate.title;

import java.util.ArrayList;
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
import use.database.sql.generate.common.AcademicDegreeTitle;
import use.database.sql.generate.common.SqlGeneratorConsoleUI;
import use.database.sql.generate.common.Title;

/**TODOGOON20260819
 * Ziel ist das Einlesen einer CSV Datei, mit Daten für die Erstellung von Schlüsseltabellen - Einträgen in HISinOne.
 * Die CSV Datei wurde zuvor mit einer SQL Abrfrage in pgAdmin erstellt. (siehe Example/title02/1sql).
 *
 *  Erstelle das SQL diese akademischen Grade in die HISinOne Tabelle 'title' per Insert einzufügen.
 *  Dabei wird ein uniquename berechnet. 
 *  Es wird sichergestellt, dass trotz fehlendem Constraint der Datensatz nur 1x erstellt wird.
 *  
 *  Es wird eine objguid berechnet.
 *  
 * 
 * @author Fritz Lindhauer
 *
 */
public class SqlGeneratorMain_insertForTableByHashMapFromCsv implements IConstantZZZ {
	
	public final static String sDIRECTORY_DEFAULT = "c:\\temp";
	
    private String sTable = null;
    private String sDirectory = null;
    private ArrayListUniqueZZZ<String> listasInsert = null;


    public SqlGeneratorMain_insertForTableByHashMapFromCsv() {
    }
    
    // Einstiegspunkt des Programms
    public static void main(String[] args) {
    	
    	main:{
	    	//TODOGOON20260803: Als eine Variante die Komplette Datei einlesen und nicht Zeileweise über die Eingabe... 
	    	//                  Beim Bauen der Insertstrings dann darauf achten, dass alle Schlüsselbestandteile vorhanden sind.
	    	
	    	//TODOGOON20260803: Im Main die Klasse aufrufen. Das ist dann eine andere Klasse, ohne Klassennamen ...Main ... 
	    	
	        String tabelle = "";
	        SqlGeneratorMain_insertForTableByHashMapFromCsv erzeuger = null;
	
	       try {
	        	if(args!=null && args.length>=1) {
	        		tabelle = args[0];
	        	}else {
	        		tabelle = "title"; //hard coded zum Entwickeln
	        	}
	        		        		        	
	        	SqlGeneratorConsoleUI sqlUi = new SqlGeneratorConsoleUI();	        	
	        	List<String> listEintrag = sqlUi.readCsvAsList();
	        	
	        	erzeuger = new SqlGeneratorMain_insertForTableByHashMapFromCsv();
	        	
	        	//Wir müssen nun eine HashMap mit dem entsprechenden AcademicDegree-Objekt füllen
	        	//ausgehend von der csv-Datei, wichtig: Das Entity AcademicDegreeTitle muss schon zwischen den Klassen identisch sein (package common)
	            Map<String,Title> hmTitle = SqlGeneratorMain_insertForTableByHashMapFromCsv.createMapWithEntityFromCsvEntry(listEintrag);	
	            	          
		        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		        String sTablename;
		        if(StringZZZ.isEmpty(tabelle)) {
		        	sTablename = sqlUi.getTablename();
		        }else {
		        	sTablename = tabelle;
		        }
	        	erzeuger.setTable(sTablename);	 
	        	
		        boolean bTransformed = erzeuger.transformHashMapToDbInsert(hmTitle);
	            if(!bTransformed) {
	            	System.out.println("Keine Transformation zu insert Befehlen. Vermutlich inkorrekte Eingabezeile.");
	            	break main;
	            }
		        
	            List<String> listInsert = erzeuger.getListInsert();	           
	            if(!listInsert.isEmpty()) {
	            	 Syso.println(SqlUtilZZZ.createSearchPathStmt("hisinone"));
			         for(String sInsertTemp : listInsert) {
			        	 System.out.println(sInsertTemp);
			         }   
			            
			            
			         String sDirectory = sqlUi.getDirectory();
			         erzeuger.setDirectory(sDirectory);
			            	                
			         String sDateiname = erzeuger.erstelleDateinamenDefault();
			         boolean bSuccess = TextDateiSchreiber.schreibeTextdatei(erzeuger.getDirectory(), sDateiname, erzeuger.getListInsert());
			         if(bSuccess) {
			        	 System.out.println("Erzeugte Textdatei kann fuer Inserts verwendet werden.");
			         }else{
			        	 System.out.println("Textdatei nicht erzeugt.");
			         }
	            }	            	         
//	       } catch (IOException e) {
//	           System.out.println("Fehler beim Einlesen: " + e.getMessage());
	       } catch (ExceptionZZZ ez){
	    	   System.out.println("Fehler: " + ez.getMessageLast());
	       }
    	}//end main:
    	System.out.println("Verarbeitung beendet.");
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
    public static boolean addStaticCustomValues(Title objTitle, String[] saValue) throws ExceptionZZZ {
    	boolean bReturn = false;
    	main:{
    		if(objTitle==null) {
    			ExceptionZZZ ez = new ExceptionZZZ("objTitle", iERROR_PARAMETER_MISSING, SqlGeneratorMain_insertForTableByHashMapFromCsv.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
    		};

    		objTitle.setK_language_id(12);
    		objTitle.setSortorder(0);
    		    		
    		UUID uuid = UUID.randomUUID();
    		String sObj_guid =  uuid.toString();
    		objTitle.setObj_guid(sObj_guid);
    		
    		//nun einen uniquename errechnen.
    		String sValue = saValue[0]; //Der Text steht hier in der 1. Spalte (der einzigen Spalte)
    		
    		//Entferne links/rechts irgendwelche String-Marker
    		String[]saToStrip= {"\"","'"};
    		String sValue4Key = StringZZZ.stripCharacters(sValue, saToStrip);
    		
    		int[]iaPartLength= {4,4}; //Anders als bei academic_degree, dort ist 4,3
    		String sUniquename = StringZZZ.toShorten(sValue4Key, null, iaPartLength, "x"); //Abkürzung per Default Delimiter erstellen
    		sUniquename = sUniquename.toLowerCase();
    		objTitle.setUniquename(sUniquename);
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
    
     
    public boolean transformHashMapToDbInsert(Map<String,Title> mapTitle) throws ExceptionZZZ {
    	boolean bReturn = false;
    	main:{
    
    		Set<String> setStg = mapTitle.keySet();    	
    		for(String sStg : setStg) {
    			Title objTitle = mapTitle.get(sStg);
    			if(objTitle!=null) {
    				String sInsert = transformTitleToDbInsert(objTitle);
    				this.addInsert(sInsert);
    				System.out.println(sInsert);
    				System.out.println("sInsert=" + sInsert);
    			}else {
    				System.out.println("Key in Map nicht gefunden. Studiengang '" + sStg + "'");
    			}
    		} 
    		bReturn = true;
    	}//end main:
    	return bReturn;
    }
    
    public String transformTitleToDbInsert(Title objTitle) throws ExceptionZZZ {
    	String sReturn = null;
    	main:{
    		
    		
    		Map<String, String> aliasMap = erzeugeAliasMap(objTitle);
    		
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
	        String sValueUnique = objTitle.getUniquename();
	        sReturn = SqlUtilZZZ.createInsertUnique(sTable, sColumns, sValues, sColumnUnique, sValueUnique);
	        sReturn = SqlUtilZZZ.toStatement(sReturn); //sonst kann postgreSQL die Anweisungszeilen nicht unterscheiden
    	}//end main:
    	return sReturn;
    }

    // Wandelt Überschrift + Eintrag in eine Map um
    public static Map<String, String> erzeugeAliasMap(Title objTitle) throws ExceptionZZZ {
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
        sValue = objTitle.getDefaulttext();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        sColumn = "defaulttext_female";
        sValue = objTitle.getDefaulttext_female();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        //++++++++++++++++++
        sColumn = "longtext";
        sValue = objTitle.getLongtext();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        sColumn = "longtext_female";
        sValue = objTitle.getLongtext_female();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        //+++++++++++++++++++
        //+++ Statische Werte
        sColumn = "k_language_id";
        iValue = objTitle.getK_language_id();
        sValue =  SqlUtilZZZ.toSqlValue(iValue);
        mapReturn.put(sColumn, sValue);
        
        sColumn = "uniquename";
        sValue = objTitle.getUniquename();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        
        sColumn = "obj_guid";
        sValue = objTitle.getObj_guid();
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        sColumn = "sortorder";
        iValue = objTitle.getSortorder();
        sValue =  SqlUtilZZZ.toSqlValue(iValue);
        mapReturn.put(sColumn, sValue);
        
        //+++++++++++++++++++
        //+++ Statische Werte wg. Constraints
//        sColumn = "position_of_title";
//        iValue = objTitle.getPosition_of_title();
//        sValue =  SqlUtilZZZ.toSqlValue(iValue);
//        mapReturn.put(sColumn, sValue);
        
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
    
    public static Map<String,Title> createMapWithEntityFromCsvEntry(List<String> listEintrag) throws ExceptionZZZ {
    	Map<String,Title> hmReturn = null;    		    			
    	main:{
    		if(listEintrag==null) {
    			ExceptionZZZ ez = new ExceptionZZZ("List<String> eintrag", iERROR_PARAMETER_MISSING, SqlGeneratorMain_insertForTableByHashMapFromCsv.class, ReflectCodeZZZ.getPositionCurrent());
    			throw ez;
    		}
    		
	      	//Lies die CSV-Datei mit Werten ein, Zeile für Zeile.    		
    		hmReturn = new LinkedHashMap<String,Title>();
	        for(String sEintragTemp : listEintrag) {
	        	if(!StringZZZ.isEmpty(sEintragTemp)) {
			      	  Title objTitle = new Title();

			      	  //TODOGOON20260809: Überprüfe die Anzahl der Spalten in der CSV Datei, vielleicht wurde die falsche Datei angegeben, oder etwas falschen eingefügt.
			      	  String[] saEntry = parseCsvLine(sEintragTemp);
			      	  
			      	  //Datensatz übernehmen, nur wenn überhaupt ein Wert vollwertiger Eintrag existiert
			      	  if(saEntry.length>=1) {  //Ist nur 1 Spalte
			      		  
			      		  if(!StringZZZ.isEmpty(saEntry[0]) 
			      				  && !saEntry[0].equalsIgnoreCase("null")
			      				  && !saEntry[0].equals("''''"))
			      		  {
			      			System.out.println(sEintragTemp);
			      			  
			      			  boolean bSuccess = addStaticCustomValues(objTitle, saEntry);
			      			  if(!bSuccess) {
			      				  ExceptionZZZ ez = new ExceptionZZZ("Fehler: Statische Werte nicht erfolgreich hinzugefügt.", iERROR_RUNTIME, SqlGeneratorMain_insertForTableByHashMapFromCsv.class, ReflectCodeZZZ.getPositionCurrent());
			      				  throw ez;
			      			  }
			      			  
			      			  //Hier wird der Key ausgerechnet aus dem Name			      			  
			      			  String sKey = objTitle.getUniquename();
					      	 
					      	  objTitle.setDefaulttext(saEntry[0]);
					      	  objTitle.setDefaulttext_female(saEntry[0]);
					      	  objTitle.setLongtext(saEntry[0]);             //Defaulttext = Longtext
					      	  objTitle.setLongtext_female(saEntry[0]);      //dito
					      	  hmReturn.put(sKey, objTitle);				      	        	  						      	  
			      		  }
	        		}
	        	}
	        }//end for ... listEintrag	
    	}//end main:
        return hmReturn;
    }
}