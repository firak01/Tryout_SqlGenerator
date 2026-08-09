package use.database.sql.generate.courseOfStudy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import use.database.sql.generate.academicdegree.AcademicDegreeTitle_ALTE_VERSION;
import use.database.sql.generate.academicdegree.SqlGeneratorMain_insertForTableByHashMapFromCsv;
import use.database.sql.genrate.common.AcademicDegreeTitle;
import use.database.sql.genrate.common.CourseOfStudy_with_AcademicDegree;
import use.database.sql.genrate.common.SqlGeneratorUI;

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
	        	
	        	SqlGeneratorUI sqlUi = new SqlGeneratorUI();	        	
	        	List<String> listEintrag = sqlUi.readCsvAsList();
	        	
	        	
	        	
	        	erzeuger = new SqlGeneratorMain_updateForTableByHashMapFromCsv();

	        	
	        	//Wir müssen nun eine HashMap mit dem entsprechenden AcademicDegree-Objekt füllen
	            //ausgehend von der csv-Datei, wichtig: Das Entity AcademicDegreeTitle muss schon zwischen den Klassen identisch sein (package common)
	            Map<String,AcademicDegreeTitle> hmAcademicDegreeTitle = SqlGeneratorMain_insertForTableByHashMapFromCsv.createMapWithEntityFromCsvEntry(listEintrag);	
	            
	        	
//	        	//++++++++++++++++++++++++++++++++++++++++++++++
//	        	//TODOGOON20260809: Das ist identisch zu dem Code aus SqlGeneratorMain_insertForTableByHashMapFromCsv
//	        	//                  vermeide diese Redundanz, mache also in der genannten Klasse eine MEthode und rufe die hier auf.
//	        	//Wir müssen nun eine HashMap mit dem entsprechenden AcademicDegree-Objekt füllen
//	            //ausgehend von der csv-Datei
//	            Map<String,AcademicDegreeTitle> hmAcademicDegreeTitle = new LinkedHashMap<String,AcademicDegreeTitle>();
//		        for(String sEintragTemp : listEintrag) {
//		        	if(!StringZZZ.isEmpty(sEintragTemp)) {
//				      	  AcademicDegreeTitle objAcademicDegreeTitle = new AcademicDegreeTitle();
//				
//				      	//TODOGOON20260809: Überprüfe die Anzahl der Spalten in der CSV Datei, vielleicht wurde die falsche Datei angegeben, oder etwas falschen eingefügt.
//				      	  String[] saEntry = parseCsvLine(sEintragTemp);
//				      	  
//				      	  //Datensatz übernehmen, nur wenn überhaupt ein Wert existiert
//				      	  if(saEntry.length>=5) {
//				      		  System.out.println(sEintragTemp);
//		      			  
//				      		  if(!StringZZZ.isEmpty(saEntry[3]) && !StringZZZ.isEmpty(saEntry[4])
//				      				  && !saEntry[3].equalsIgnoreCase("null") && !saEntry[4].equalsIgnoreCase("null")) {
//				      			
//				      			  boolean bSuccess = addStaticCustomValues(objAcademicDegreeTitle, saEntry);
//				      			  if(!bSuccess) {
//				      				  System.out.println("Fehler: Statische Werte nicht erfolgreich hinzugefügt.");
//				      				  break main;
//				      			  }
//				      			  
//						      	  //Schlüssel besteht aus Abschluss | Studiengang | Vertiefunge
//						      	  String sKey = saEntry[0] + "|" + saEntry[1] + "|" + saEntry[2];
//						      	  objAcademicDegreeTitle.setAbschluss(saEntry[0]);
//						      	  objAcademicDegreeTitle.setStudiengang(saEntry[1]);
//						      	  objAcademicDegreeTitle.setVertiefung(saEntry[2]);
//						      	  
//						      	  objAcademicDegreeTitle.setDefaulttext(saEntry[3]);
//						      	  objAcademicDegreeTitle.setDefaulttext_female(saEntry[4]);
//						      	  objAcademicDegreeTitle.setLongtext(saEntry[3]);             //Defaulttext = Longtext
//						      	  objAcademicDegreeTitle.setLongtext_female(saEntry[4]);      //dito
//						      	  hmAcademicDegreeTitle.put(sKey, objAcademicDegreeTitle);
//				      	        	  
//						      	  //Nein: Jetzt müssen die Studiengänge selectiert werden.
//						      	  //boolean bTransformed = erzeuger.transformHashMapToDbInsert(hmAcademicDegreeTitle);
//				      		  }
//		        		}
//		        	}
//		        }//end for ... listEintrag	
//	            //++++++++++++++++++++++++++++++++++++++++++
//		        
		        
		        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		        String sTablename;
		        if(StringZZZ.isEmpty(tabelle)) {
		        	sTablename = sqlUi.getTablename();
		        }else {
		        	sTablename = tabelle;
		        }
	        	erzeuger.setTable(sTablename);	 
	        	
		        
		        
		        //###########################################################################
		        //Erstelle nun eine HashMap mit course_of_study Objekten.
		        //Die Course_of_study Objekte werden mit daten aus dem objAcademicDegreeTitle Objekt
		        //und der Schlüsseltabelle hmAcademicDegreeTitle (hier der Key, aufgeteilt in Abschluss, Studiengang, Verteifung) gefüllt.
	            Map<String,CourseOfStudy_with_AcademicDegree> hmCourseOfStudy = new LinkedHashMap<String,CourseOfStudy_with_AcademicDegree>();

		        
		        //Das ist wirklich bewusst so wie oben für AcademicDegreeTitle.
		        //Wir beziehen uns auf die gleich CSV-Datei als Ursprung.
		        //Diese gekapselte Struktur erst einmal beibehalten... würde vom Prinzip her ja auch so bleiben, wenn man auf irgendwann mal existierende "Generator-Objekte" und deren Ergebnisse zugreifen würde.
		        for(String sEintragTemp : listEintrag) {
		        	if(!StringZZZ.isEmpty(sEintragTemp)) {
			        	CourseOfStudy_with_AcademicDegree objCourseOfStudy = new CourseOfStudy_with_AcademicDegree();
			        	
			        	System.out.println("Verarbeite Zeile: " + sEintragTemp);
			        	String[] saEntry = parseCsvLine(sEintragTemp);		        	
			        	String sKey = saEntry[0] + "|" + saEntry[1] + "|" + saEntry[2];
			        	
			        	AcademicDegreeTitle objAcademicDegreeTitle = hmAcademicDegreeTitle.get(sKey);
			        	if(objAcademicDegreeTitle==null) {
			        		System.out.println("Kein AcademicDegree-Objekt vorhanden für Key: '" + sKey + "'");
			        	}else {
				        	objCourseOfStudy.setAcademicDegreeObject(objAcademicDegreeTitle);
				        	
				        	hmCourseOfStudy.put(sKey, objCourseOfStudy);
				        	
				        	 //Jetzt müssen die Studiengänge selectiert werden.
					      	 //boolean bTransformed = erzeuger.transformHashMapToDbUpdate(hmCourseOfStudy);			        		
				        	boolean bTransformed = erzeuger.transformToDbUpdate(objCourseOfStudy);
			        	}
		        	}
		        }//end for ... listEintrag
		       
//		        CourseOfStudy_with_AcademicDegree objCourseOfStudy = hmCourseOfStudy.get("11|032|   ");
//		        String sAbschl = objCourseOfStudy.getAbschluss();
//		        String sStg = objCourseOfStudy.getStudiengang();
//		        String sVert = objCourseOfStudy.getVertiefung();
//		        
//		        String sSigntureTemplateForSearch = objCourseOfStudy.getSignatureTemplateForSearch();
//		        String sUniquename = CourseOfStudy_with_AcademicDegree.createUniquenameForSearch(sAbschl, sStg, sVert, sSigntureTemplateForSearch);
//		        System.out.println("Studiengang uniquename='" + sUniquename + "'");
//		        
//		        String sAcademicDegreeUniquename = objCourseOfStudy.getAcademicDegreeUniquename();
//		        System.out.println("AcademicDegree uniquename='" + sAcademicDegreeUniquename + "'");
			        
		        
	            
		        //###########################################################################
	            //Auf Update abgeänderter Code für die Ausgabe
	            List<String> listUpdate = erzeuger.getListUpdate();
	            if(!erzeuger.getListUpdate().isEmpty()) {
	            	Syso.println(SqlUtilZZZ.createSearchPathStmt("hisinone"));
	 	            for(String sUpdateTemp : listUpdate) {
	 	                  	System.out.println(sUpdateTemp);
	 	            }    
	 	            
	 	            String sDirectory = sqlUi.getDirectory();
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
    public static boolean addStaticCustomValues(AcademicDegreeTitle_ALTE_VERSION objAcademicDegreeTitle, String[] saValue) throws ExceptionZZZ {
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
    
     
    //public boolean transformHashMapToDbUpdate(Map<String, CourseOfStudy_with_AcademicDegree> mapCourseOfStudy) throws ExceptionZZZ {
    public boolean transformToDbUpdate(CourseOfStudy_with_AcademicDegree objCourseOfStudy) throws ExceptionZZZ {
    	boolean bReturn = false;
    	main:{
    		/*Anders als bei academicDegree hier nicht die ganze Map durchsehen
    		Set<String> setStg = mapCourseOfStudy.keySet();    	
    		for(String sStg : setStg) {
    			CourseOfStudy_with_AcademicDegree objCourseOfStudy = mapCourseOfStudy.get(sStg);
    			if(objCourseOfStudy!=null) {
    				String sUpdate = transformCourseOfStudyToDbUpdate(objCourseOfStudy);
    				this.addUpdate(sUpdate);
    				System.out.println("sUpdate=" + sUpdate);
    			}else {
    				System.out.println("Key in Map nicht gefunden. Studiengang '" + sStg + "'");
    			}
    		}    	
    		*/	    	
    		
    		String sUpdate = transformCourseOfStudyToDbUpdate(objCourseOfStudy);
    		if(StringZZZ.isEmpty(sUpdate)) break main;
    		
			this.addUpdate(sUpdate);
			System.out.println("sUpdate=" + sUpdate);
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

	        
	        
	        //ZIEL: 
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
	        
	        //                                    (String sTable, String sColumn, String sSingleValue, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
	        sReturn = SqlUtilZZZ.createUpdateConditioned_LIKE(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue);
	        sReturn = SqlUtilZZZ.toStatement(sReturn); //Sonst kann postgre die Anweisungszeilen Zeilen wohl nicht unterscheiden
    	}//end main:
    	return sReturn;
    }

    // Wandelt Überschrift + Eintrag in eine Map um
    public static Map<String, String> erzeugeAliasMap(AcademicDegreeTitle_ALTE_VERSION objAcademicDegreeTitle) throws ExceptionZZZ {
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
        String sColumn = null; //das sind die für den Update mit Suche später verwendebare Spaltennamen
        String sValue = null; int iValue;
        
        
        
        //++++
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
}