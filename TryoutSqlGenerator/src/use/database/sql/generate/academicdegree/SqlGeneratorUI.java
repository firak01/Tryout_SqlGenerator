package use.database.sql.generate.academicdegree;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.file.csv.stream.FileCsvReaderZZZ;

public class SqlGeneratorUI {
	
	private BufferedReader objReaderConsole = null;
	private String sDirecory = null;
	private String sTablename = null;
	
	//### GETTER / SETTER
	public BufferedReader getReaderForConsole() throws ExceptionZZZ{
		if(this.objReaderConsole==null) {
			this.objReaderConsole = new BufferedReader(new InputStreamReader(System.in));
		}
		return this.objReaderConsole;
	}
	
	//+++++++++++++++++++++++++++
	public String getDirectory() throws ExceptionZZZ {
		if(StringZZZ.isEmptyNull(this.sDirecory)){
			this.sDirecory = this.readDirectory();
		}
		return this.sDirecory;
	}
	
	public void setDirectory(String sDirectory) throws ExceptionZZZ {
		this.sDirecory = sDirectory;
	}
	
	public String readDirectory() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			try {
			BufferedReader reader = this.getReaderForConsole();
		
			//Verzeichnisnamen eingeben
	    	System.out.print("Bitte geben Sie den Namen des Verzeichnisse ein (Leerstring verwendet default '" + SqlGeneratorMain_insertForTableByHashMapFromCsv.sDIRECTORY_DEFAULT + "'):"
	    					 + "\n");
	        String directory = reader.readLine();
	        directory = directory.trim();
	        if (!StringZZZ.isEmpty(directory)) {
	        	sReturn = directory;	            	
	        }else {
	        	sReturn = SqlGeneratorMain_insertForTableByHashMapFromCsv.sDIRECTORY_DEFAULT;
	        }	
			}catch (IOException ioe){
				ExceptionZZZ ez = new ExceptionZZZ(ioe);
				throw ez;
			}
		}//end main:
		return sReturn;
	}
	
	//+++++++++++++++++++++++++++++++
	
	public String getTablename() throws ExceptionZZZ {
		if(StringZZZ.isEmptyNull(this.sTablename)){
			this.sTablename = this.readTablename();
		}
		return this.sTablename;
	}
	
	public void setTablename(String sTablename) throws ExceptionZZZ {
		this.sTablename = sTablename;
	}
	
	public String readTablename() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			try {
			BufferedReader reader = this.getReaderForConsole();
		
			//Verzeichnisnamen eingeben
			System.out.print("Bitte geben Sie den Tabellennamen als String ein (Leerstring zum Abbrechen):"
							+ "\n");
	        String s = reader.readLine();
	        s = s.trim();
	        if (!StringZZZ.isEmpty(s)) {
	        	sReturn = s;	            	
	        }else {
	        	sReturn = SqlGeneratorMain_insertForTableByHashMapFromCsv.sDIRECTORY_DEFAULT;
	        }	
			}catch (IOException ioe){
				ExceptionZZZ ez = new ExceptionZZZ(ioe);
				throw ez;
			}
		}//end main:
		return sReturn;
	}
	
	
	/**Hier ist die Überschrift egal,
         * da diese Spaltennamen aus der sospos ausgangstabelle hat.
         * In HISinone sind das eh andere Tabellenspalten, der Tabellenname wurde oben angegeben. 
        
        Nur wenn wir aus dem Gleichen "System" in das gleiche "System" transferieren würden.
        // Die Überschrift eingeben, 
        System.out.print("Bitte geben Sie die Tabellenspalten als String ein. (Leerstring zum Abbrechen): ");
        ueberschrift = reader.readLine();
        if (ueberschrift == null || ueberschrift.trim().isEmpty()) return;            
      
       
        // Wiederholt Einträge verarbeiten
        // z.B. "11","012","Diplom-Archäologe                                           ","Diplom-Archäologin                                          "
        //      "11","021","Diplom-Kaufmann                                             ","Diplom-Kauffrau                                             "
        //		"11","030","Diplom-Ingenieur                                            ","Diplom-Ingenieurin                                          "
        //		"11","032","Diplom-Chemiker                                             ","Diplom-Chemikerin                                           "

        //Beispiel für die Datei:
        //     "parstg - Werte der Academicdegrees pro Studiengang.csv"
        
	 
	 * @return
	 * @throws ExceptionZZZ
	 */
	public List<String> readCsvAsList()throws ExceptionZZZ{
		List<String> listasReturn = null;
		main:{
			try {
				BufferedReader reader = this.getReaderForConsole();		        
				String sDirectory = this.getDirectory();
		        
				
				listasReturn = new ArrayList<String>();
        
				String eintragOld="";
		        String eintrag="";
		        String sEintrag="";
		        File fileEintrag=null;
        
        
		        boolean bFile = false; boolean bFileChecked=false;
		        System.out.print("Bitte geben Sie einen Dateinamen im Verzeichnis '" + sDirectory + "' an"
		        		       + "\noder die CSV-Strings direkt ein (kommagetrennt, auch mehrer Zeilen auf einmal, ggfs. mehrfach ENTER druecken)(Leerstring zum Abbrechen):"
		        		       + "\n");
		        while (true) {                
		            eintrag = reader.readLine();
		            eintrag = eintrag.trim();
		       	    
		            //erst beim 2ten "ENTER" die Eingabe beenden
		            if (StringZZZ.isEmptyNull(eintrag) && StringZZZ.isEmptyNull(eintragOld)) {
		                break;
		            }else {	                	
		            	 if(!bFileChecked) {
		                	 //Ist der Eintrag ein Dateipfad?
		                	 bFile = FileEasyZZZ.exists(sDirectory, eintrag);
		                	 if(bFile) {
		                		 fileEintrag = new File(sDirectory, eintrag);
		                		 bFile = FileEasyZZZ.isFileExisting(fileEintrag);
		                		 if(bFile) {
		                			sEintrag = eintrag;		                			 		                					                			
		     	                    break;
		                		 }
		                	 }
		                	 bFileChecked=true;	 
		            	 }
		            	 
		            	 
		            	 sEintrag = eintragOld;
		                 eintragOld = eintrag;
		            }                                       
		        }//end while(true)
		        System.out.println("CSV Eingabe beendet.");
		        
		        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		        if(bFile) {
			        //Hier als Alternative, das Einlesen der Eintragsliste per Datei
		        	FileCsvReaderZZZ objReaderCsv = new FileCsvReaderZZZ(fileEintrag,',');
		        	listasReturn = objReaderCsv.getLines(); //Das hat den Vorteil, das es nur Zeilen ohne Kommentar und keine Leerzeilen sind.
		        }else {
		        	//Hier die Verarbeitung der per Konsole eingegebenen CSV Strings
		        	 // ZUERST: Escape vorhandener einfacher Hochkommata → SQL-konform (z. B. O'Reilly → O''Reilly)
		            sEintrag = sEintrag.replace("'", "''");
		                                
		            String[] saEintrag = sEintrag.split("\n");
		            for(String sEintragTemp : saEintrag) {
		            	listasReturn.add(sEintragTemp);
		            }		            
		        }        
			}catch (IOException ioe){
				ExceptionZZZ ez = new ExceptionZZZ(ioe);
				throw ez;
			}
		}//end main:
		return listasReturn;		
	}
}
