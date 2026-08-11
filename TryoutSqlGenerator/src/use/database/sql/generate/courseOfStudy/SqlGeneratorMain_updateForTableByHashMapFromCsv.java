package use.database.sql.generate.courseOfStudy;

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
import use.database.sql.generate.academicdegree.SqlGeneratorMain_insertForTableByHashMapFromCsv;
import use.database.sql.generate.common.AcademicDegreeTitle;
import use.database.sql.generate.common.SqlGeneratorConsoleUI;

/**Ziel ist das aktualisieren von Studiengängen im HISinOne-System mit den zugehörigen "akademischen Graden".
 * Die "akademischen Grade" wurden zuvor aus sospos exportiert (Tabelle parstg).
 * Insert-Anweisungen wurden erstellt und damit stehen sie in HISinOne zur Verfügung.
 * Dabei wurde ein "uniquename" berechnet.
 * 
 * Für die Erstellung der UPDATE-Anweisungen der HISinOne Tabelle course_of_Study:
 * - Ausgangslage ist die gleiche CSV-Datei wie für die Erstellung der INSERT-Anweisungen
 * 
 * - Es gibt als Verbindung zwischen dem akademischen Grad der Datei und des jetzt in HISinOne vorhandenen Schlüsseltabellendatensatzes
 *   den (wieder neu auszurechnenden) "uniquename".
 *   
 * - Die Infos für den Studiengang stammen ebenfalls aus der Datei (abschl, stg, vert)
 * 
 * Also:
 * - 1. Im UPDATE muss die Selektion des Schlüsseltabellendatensatzes über den uniquename vorhanden sein.
 *      und davon dann der Wert "id".
 *      
 * - 2. Das UPDATE soll dann die gefundene id in course_of_study.academicdegree_id eintragen
 * 
 * PROGRAMMIERUNG:
 * Aus der CSV Datei wird wieder ein Object von AcademicDegreeTitle gefüllt. (s. Package use.database.sql.generate.academicdegree )
 * Danach wird ein Object von CourseOfStudy_with_AcademicDegree gefüllt.
 * Das ist dann die Grundlage für die Erstellung der UPDATE Anweisung
 *  
 * 
 * 
 * @author fl86kyvo
 *
 */
public class SqlGeneratorMain_updateForTableByHashMapFromCsv implements IConstantZZZ {
	
	public final static String sDIRECTORY_DEFAULT = "c:\\temp";
	
    private String sTable = null;
    private String sDirectory = null;
    private ArrayListUniqueZZZ<String> listasInsert = null;
    private ArrayListUniqueZZZ<String> listasUpdate = null;
    
    public SqlGeneratorMain_updateForTableByHashMapFromCsv() {
    }
    
    // Einstiegspunkt des Programms
    public static void main(String[] args) {
    	
    	main:{
	    	//TODOGOON20260803: Als eine Variante die Komplette Datei einlesen und nicht Zeileweise über die Eingabe... 
	    	//                  Beim Bauen der Insertstrings dann darauf achten, dass alle Schlüsselbestandteile vorhanden sind.
	    	
	    	//TODOGOON20260803: Im Main die Klasse aufrufen. Das ist dann eine andere Klasse, ohne Klassennamen ...Main ... 
	    	
	        String tabelle = "";	        	    
	        SqlGeneratorMain_updateForTableByHashMapFromCsv erzeuger = null;
	
	       try {
	        	if(args!=null && args.length>=1) {
	        		tabelle = args[0];
	        	}else {
	        		tabelle = "course_of_study"; //hard coded zum Entwickeln
	        	}
	        	
	        	SqlGeneratorConsoleUI sqlConsole = new SqlGeneratorConsoleUI();	        	
	        	List<String> listEintrag = sqlConsole.readCsvAsList(); //entweder Zeilen aus der Konsole oder aus einer Datei.
	        		        	
	        	//Wir müssen nun eine HashMap mit dem entsprechenden AcademicDegree-Objekt füllen, ausgehend von der csv-Datei. 
	        	//Wichtig: Das Entity AcademicDegreeTitle muss identisch sein (package common) zu dem, das in Course_of_study... verwendet wird.
	            Map<String,AcademicDegreeTitle> hmAcademicDegreeTitle = SqlGeneratorMain_insertForTableByHashMapFromCsv.createMapWithEntityFromCsvEntry(listEintrag);	
	           		        
		        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	            //Hole aus der Map der AcademicDegreeTitle eben den AcademicDegreeTitle und ordne diesen dem neuen CourseOfStudy_with_AcademicDegree zu 
	        	Map<String,CourseOfStudy_with_AcademicDegree> hmCourseOfStudy = SqlGeneratorMain_updateForTableByHashMapFromCsv.createMapWithEntityFromCsvEntry(hmAcademicDegreeTitle);
		       
	        	 //###########################################################################
		        //Definiere das erzeuger - Objekt, mit dem die HashMap-Werte in UPDATE - Befehlszeilen umgewandelt werden.
	        	String sTablename;
		        if(StringZZZ.isEmpty(tabelle)) {
		        	sTablename = sqlConsole.getTablename();
		        }else {
		        	sTablename = tabelle;
		        }
		        	
		        erzeuger = new SqlGeneratorMain_updateForTableByHashMapFromCsv();
	        	erzeuger.setTable(sTablename);	 
		        boolean bTransformed = erzeuger.transformHashMapToDbUpdate(hmCourseOfStudy);
	        	if(!bTransformed) {
	        		System.out.println("Keine Transformation zu insert Befehlen. Vermutlich inkorrekte Eingabezeile.");
	     	     	break main;
	     	    }
	        			
		        //###########################################################################
	            //Auf Update abgeänderter Code für die Ausgabe
	            List<String> listUpdate = erzeuger.getListUpdate();
	            if(!erzeuger.getListUpdate().isEmpty()) {
	            	Syso.println(SqlUtilZZZ.createSearchPathStmt("hisinone"));
	 	            for(String sUpdateTemp : listUpdate) {
	 	                  	System.out.println(sUpdateTemp);
	 	            }    
	 	            
	 	            String sDirectory = sqlConsole.getDirectory();
			        erzeuger.setDirectory(sDirectory);
	            	
	            	String sDateiname = erzeuger.erstelleDateinamenDefault();
	            	boolean bSuccess = TextDateiSchreiber.schreibeTextdatei(erzeuger.getDirectory(), sDateiname, erzeuger.getListUpdate());
	            	if(bSuccess) {
	            		System.out.println("Erzeugte Textdatei kann fuer Updates verwendet werden.");
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
    public SqlGeneratorMain_updateForTableByHashMapFromCsv(String sDirectory, String sTable) {
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
    
    public ArrayList<String> getListUpdate(){
    	if(this.listasUpdate==null) {
    		this.listasUpdate = new ArrayListUniqueZZZ<String>();
    	}
    	return this.listasUpdate;
    }
    
    public void setListUpdate(List<String> listasUpdate) {
    	this.listasUpdate = (ArrayListUniqueZZZ<String>) listasUpdate;
    }
    
    
    //######################################
    public static boolean addStaticCustomValues(AcademicDegreeTitle objAcademicDegreeTitle, String[] saValue) throws ExceptionZZZ {
    	boolean bReturn = false;
    	main:{
    		if(objAcademicDegreeTitle==null) {
    			ExceptionZZZ ez = new ExceptionZZZ("objAcademicDegreeTitle", iERROR_PARAMETER_MISSING, SqlGeneratorMain_updateForTableByHashMapFromCsv.class, ReflectCodeZZZ.getPositionCurrent());
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
    
    public void addUpdate(String sUpdate) {
    	this.getListUpdate().add(sUpdate);
    }
    
    public String erstelleDateinamenDefault() {
    	String sDateTime = ZeitstempelErzeuger.holeAktuellesDatumZeitAlsString();
    	return this.getTable() + sDateTime + ".sql";
    }
    
        
    public boolean transformHashMapToDbUpdate(Map<String,CourseOfStudy_with_AcademicDegree> mapCourseOfStudy)  throws ExceptionZZZ {
    	boolean bReturn = false;
    	main:{    	    		
			Set<String> setStgAbschlVert = mapCourseOfStudy.keySet();    	
    		for(String sKeyTemp : setStgAbschlVert) {
    			CourseOfStudy_with_AcademicDegree objCourseOfStudy = mapCourseOfStudy.get(sKeyTemp);
    			if(objCourseOfStudy!=null) {
    				String sUpdate = transformCourseOfStudyToDbUpdate(objCourseOfStudy);
    				if(!StringZZZ.isEmptyNull(sUpdate)) {
	    				this.addUpdate(sUpdate);
	    				System.out.println("sUpdate=" + sUpdate);
	    			}
    			}else {
    				System.out.println("Key in Map nicht gefunden. Studiengang '" + sKeyTemp + "'");
    			}
    		} 
    		bReturn = true;
    	}//end main:    	
    	return bReturn;
    }
    
    public String transformCourseOfStudyToDbUpdate(CourseOfStudy_with_AcademicDegree objCourseOfStudy) throws ExceptionZZZ {
    	String sReturn = null;
    	main:{
    		
    		
    		Map<String, String> aliasMap = erzeugeAliasMap(objCourseOfStudy);
    		if(aliasMap==null) break main;
    		
	        // Ausgabe zur Kontrolle
	        /*for (Map.Entry<String, String> eintragMap : aliasMap.entrySet()) {
	            System.out.println(eintragMap.getKey() + " => " + eintragMap.getValue());
	        }*/
	
	       
	        String sColumns = SqlUtilZZZ.erzeugeColumnsString(aliasMap);
	        String sValues = SqlUtilZZZ.erzeugeValues(aliasMap);
	        System.out.println("Debugzwecke. Hier sind zwar alle beteiligten Werte vorhanden, aber nicht der Wert der Upzudatenden ID");
	        System.out.println("Die Upzudatende ID wird per SQL Select zur Laufzeit errechnet.");
	        System.out.println("sColumns='" + sColumns +"'");
	        System.out.println("sValues='" + sValues +"'");

	        
	        
	        //ZIEL, erstelle SQL wie: 
	        //update course_of_study set academicdegree_id = (select id from academicdegree where uniquename in ('diplxing'))
	        //where uniquename LIKE '11|032|-|-|H|%|0390|P|V|%|'
	        AcademicDegreeTitle objAcademicDegreeTitle = objCourseOfStudy.getAcademicDegreeObject();
	        String sSelectColumn = "id";
	        
	        String sWhereColumn = "uniquename";
	        String sWhereSingleValue = objAcademicDegreeTitle.getUniquename();
	        String sWhereTable = "academicdegree";
	        String sCondition = SqlUtilZZZ.createSelectConditioned(sSelectColumn, sWhereTable, sWhereColumn, sWhereSingleValue);
	        
	        String sTable = this.getTable();
	        String sColumn = "academicdegree_id";
	        String sSingleValue = "(" + sCondition + ") "; 
	        sWhereColumn = "uniquename";
	        sWhereSingleValue = objCourseOfStudy.createUniquenameForSearch();
	        
	        sReturn = SqlUtilZZZ.createUpdateConditioned_LIKE(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue);
	        sReturn = SqlUtilZZZ.toStatement(sReturn); //Sonst kann postgre die Anweisungszeilen Zeilen wohl nicht unterscheiden
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
    
    // Wandelt Überschrift + Eintrag in eine Map um
    public static Map<String, String> erzeugeAliasMap(CourseOfStudy_with_AcademicDegree objCourseOfStudy) throws ExceptionZZZ {
        Map<String, String> mapReturn = null;
        main:{

        String sColumn = null; //das sind die für den Update mit Suche später verwendebare Spaltennamen
        String sValue = null;        
        
        
        //++++ Der Uniquename enthält Studiengang, Abschluss und Vertiefung
        sColumn = "uniquename";
        
        String sAbschl = objCourseOfStudy.getAbschluss();
        String sStg = objCourseOfStudy.getStudiengang();
        
        //Abschluss und Studiengang sind zwingend notwendig. Aber keinen Fehler werfen
        if(StringZZZ.isEmpty(sAbschl) && StringZZZ.isEmpty(sStg)) {
        	Syso.println("Abschluss und Studiengang sind leer, überspringe Datensatz.");
        	break main;
    	}
        
        mapReturn =  new LinkedHashMap<String, String>(); // Reihenfolge bewahren
        
        
        String sVert = objCourseOfStudy.getVertiefung();
        
        String sSigntureTemplateForSearch = objCourseOfStudy.getSignatureTemplateForSearch();
        String sUniquename = CourseOfStudy_with_AcademicDegree.createUniquenameForSearch(sAbschl, sStg, sVert, sSigntureTemplateForSearch);
       
        sValue = sUniquename;
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        //+++ Folgende Spalte gibt es nicht in course_of_study, wird aber für die Suche verwendet
        sColumn = "myAcademicDegreeUniquename";        
        String sAcademicDegreeUniquename = objCourseOfStudy.getAcademicDegreeUniquename();
        sValue = sAcademicDegreeUniquename;
        sValue = SqlUtilZZZ.toSqlValue(sValue);
        mapReturn.put(sColumn, sValue);
        
        
        //+++++++++++++++++++
        //+++ Statische Werte
        
        //+++++++++++++++++++
        //+++ Statische Werte wg. Constraints
      
    	}//end main:
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
    
    public static Map<String,CourseOfStudy_with_AcademicDegree> createMapWithEntityFromCsvEntry(Map<String,AcademicDegreeTitle> hmAcademicDegreeTitle) throws ExceptionZZZ {
    	Map<String,CourseOfStudy_with_AcademicDegree> hmReturn = null;    		    			
    	main:{
    		if(hmAcademicDegreeTitle==null) {
    			ExceptionZZZ ez = new ExceptionZZZ("Map<String,AcademicDegreeTitle> hmAcademicDegreeTitle", iERROR_PARAMETER_MISSING, SqlGeneratorMain_insertForTableByHashMapFromCsv.class, ReflectCodeZZZ.getPositionCurrent());
    			throw ez;
    		}
    		
	      	//Lies die CSV-Datei mit Werten ein, Zeile für Zeile.    		
    		hmReturn = new LinkedHashMap<String,CourseOfStudy_with_AcademicDegree>();	
    		
    		Set<String> setKey = hmAcademicDegreeTitle.keySet();
	        for(String sKeyTemp : setKey) {
	        	if(!StringZZZ.isEmpty(sKeyTemp)) {
	        		CourseOfStudy_with_AcademicDegree objCourseOfStudy = new CourseOfStudy_with_AcademicDegree();

			      	//TODOGOON20260809: Überprüfe die Anzahl der Spalten in der CSV Datei, vielleicht wurde die falsche Datei angegeben, oder etwas falschen eingefügt.
	        		System.out.println("Verarbeite Key: " + sKeyTemp);
		        	     	 
					AcademicDegreeTitle objAcademicDegreeTitle = hmAcademicDegreeTitle.get(sKeyTemp);
					if(objAcademicDegreeTitle==null) {
			     		System.out.println("Kein AcademicDegree-Objekt vorhanden für Key: '" + sKeyTemp + "'");
			       	}else {
			        	objCourseOfStudy.setAcademicDegreeObject(objAcademicDegreeTitle);
			        	
			        	hmReturn.put(sKeyTemp, objCourseOfStudy);						        			        					        
			       	}			    
	        	}//end if(!StringZZZ.isEmpty(sEintragTemp)) {
	        }//end for ... listEintrag	
    	}//end main:
        return hmReturn;
    }
}