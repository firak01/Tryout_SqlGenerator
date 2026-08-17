package use.database.sql.console;

import java.util.Scanner;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractList.HashMapZZZ;
import basic.zBasic.util.console.multithread.AbstractKeyPressThreadZZZ;
import basic.zBasic.util.console.multithread.IConsoleZZZ;
import basic.zBasic.util.console.multithread.IKeyPressConstantZZZ;
import basic.zBasic.util.console.multithread.IKeyPressThreadConstantZZZ;
import basic.zBasic.util.console.multithread.IKeyPressThreadZZZ;
import basic.zBasic.util.console.multithread.KeyPressUtilZZZ;
import basic.zBasic.util.crypt.code.CryptAlgorithmMappedValueZZZ;
import basic.zBasic.util.crypt.code.ICharacterPoolEnabledZZZ;
import basic.zBasic.util.crypt.code.ROTnnZZZ;
import basic.zBasic.util.crypt.thread.KeyPressCryptUtilZZZ;
import basic.zBasic.util.crypt.thread.KeyPressThreadDecryptZZZ;
import basic.zBasic.util.crypt.thread.KeyPressThreadEncryptZZZ;
import basic.zBasic.util.datatype.booleans.BooleanZZZ;
import basic.zBasic.util.datatype.character.CharZZZ;
import basic.zBasic.util.datatype.character.CharacterExtendedZZZ;
import basic.zBasic.util.datatype.character.ICharacterExtendedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import use.database.sql.generate.SqlUtilZZZ;


	 
	public class KeyPressThreadSqlGeneratorZZZ extends AbstractKeyPressThreadSqlGeneratorZZZ{	
        //Method that gets called when the object is instantiated
		public KeyPressThreadSqlGeneratorZZZ(IConsoleZZZ objConsole) {
        	super(objConsole);
        }
        public KeyPressThreadSqlGeneratorZZZ(IConsoleZZZ objConsole, long lSleepTime) {
        	super(objConsole, lSleepTime);
        }
		@Override
		public void makeMenueMain() throws InterruptedException {
			System.out.println();//Leerzeile zum ggfs. vorherigen Consolentext
			System.out.println("#######################################################################################################");
			System.out.println("# TOOL ZUM ARBEITEN MIT SQL & ZUM VERSCHLUESSELN MIT EINFACHEN ALGORITHMEN");
			System.out.println("# ");
			System.out.println("# Eingaben: + - zur Console-Threadgeschwindigkeit | Q zum Abbruch | A für die Ausgabe der ASCII-Tabelle");
			System.out.println("# Bitte wählen Sie den Algorithmus:");
			System.out.println("# 1a: CRYPT: Rot13, Verschlüssen");
			System.out.println("# 1b: CRYPT: Rot13, Entschlüsseln");
			System.out.println("# 2: Rotascii");
			System.out.println("# 3: RotNumeric");
			System.out.println("# 4: RotNn");
			System.out.println("# 5: VigenereNn");
			System.out.println("# 6: SQL Generator: Erstelle eine Object_GUID");
			System.out.println("#####################################################################################################");
         	System.out.println("Warte auf Eingabe SQLGenerator...");                 	
			Thread.sleep(this.getSleepTime()); 
		}
		
		@Override
		public boolean processMenueMainArgumentInput(String sInput, HashMapZZZ hmVariable) throws ExceptionZZZ {
			boolean bReturn = true;
			main:{
				IKeyPressThreadZZZ objKeyPressThreadUsed = null; //Damit kann man auch andere Thread - Klassen nutzen.
				
				//In the JDK 7 release, you can use a String object in the expression of a switch statement:
	            //Das keine lowercase Methode oder eine Fallunterscheidung in den CASE eingebaut werden kann, 
	            //vorher lowercase
	            this.isCurrentMenue(true);
	            String input = sInput.toLowerCase();			                
	            switch(input) {
	            case "+":
	            	this.isCurrentInputValid(true);					                	
	            	this.setSleepTime(this.getSleepTime()+100);
	            	this.getConsole().setSleepTime(this.getSleepTime());			                	
	            	break;
	            case "-":
	            	this.isCurrentInputValid(true);
	            	this.setSleepTime(this.getSleepTime()-100);
	            	this.getConsole().setSleepTime(this.getSleepTime());			                	
	            	break;
	            case "q":
	            	this.quit();
	            	bReturn=false;
	            	break main; 
	            case "a":
	            	this.isCurrentInputValid(true);            	            	
	            	this.printTableASCII(hmVariable);//Mache eine einfache Print-Ausgabe der ASCII Tabelle
	            	break;
	            case "1a":
	            	this.isCurrentInputValid(true);
	            	objKeyPressThreadUsed = new KeyPressThreadEncryptZZZ(this.getConsole());
	            	this.setKeyPressThreadUsed(objKeyPressThreadUsed);
	            	this.setMethodForThreadUsed("processROT13");           
	            	objKeyPressThreadUsed.initit(hmVariable);           	            						                						                						                					                		              
	            	break;
	            case "1b":	            	
	            	this.isCurrentInputValid(true);
	            	objKeyPressThreadUsed = new KeyPressThreadDecryptZZZ(this.getConsole());
	            	this.setKeyPressThreadUsed(objKeyPressThreadUsed);
	            	this.setMethodForThreadUsed("processROT13");           
	            	objKeyPressThreadUsed.initit(hmVariable);           	            						                						                						                					                		              
	            	break;
	            case "2":
	            	this.isCurrentInputValid(true);
	//            	this.processROTascii_(hmVariable);     
	            	break;
	            case "3":
	            	this.isCurrentInputValid(true);
	//            	this.processROTnumeric_(hmVariable);     
	            	break;
	            case "4":
	            	this.isCurrentInputValid(true);
	//            	this.processROTnn_(hmVariable);        					                	
	            	break;
	            case "5":
	            	this.isCurrentInputValid(true);
	//            	this.processVigenereNn_(hmVariable);
	            	break;
	            case "6":
	            	this.isCurrentInputValid(true);
	            	objKeyPressThreadUsed = this;
	            	this.setKeyPressThreadUsed(objKeyPressThreadUsed);
	            	this.setMethodForThreadUsed("processObjGuid");           
	            	objKeyPressThreadUsed.initit(hmVariable);
	            	//this.processObjGuid_(hmVariable);
	            	break;
	            default:
	            	System.out.println(ReflectCodeZZZ.getPositionCurrent() + " - default Zweig: sInput = '"+sInput+"'");
	            	System.out.println("ungueltige Eingabe");
	            	this.isCurrentMenue(false);//Neue Eingabe OHNE erneut das Menue aufzubauen.
	            	this.isCurrentInputValid(false);					                	
	            	break;
	            }		 		
		}//end main:
		return bReturn;
	}
				
		@Override
		public boolean processMenuePostArgumentInput(HashMapZZZ hmVariable) throws ExceptionZZZ {
			boolean bReturn = true;
			main:{

				//BISHER gibt es hier noch keine Input-Argumente
				
				
//        		//######################################################################
//	        	//### Eingabe des zu verschluesselnden Textes
//	        	//Beispieltexte zum Rauskopieren, enthalten alles relevante...
//	        	//abcdefgHIJK1234abcdefg
//	        	//Das ist das 4711 Haus der Riesenmaus 0815
//	        	
//        		
//        		//Merke Fehler abfangen, wie z.B.: Exception in thread "Thread-1" java.lang.IllegalArgumentException: Illegal character 'ß'
//				//Das passiert beim Aufruf der Verschlüsselung selbst.
//	        	System.out.println("Geben Sie den zu verschluesselnden Text als String ein");
//            	String sInput = this.getInputReader().nextLine();
//            	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_TEXT_UNCRYPTED, sInput);
//            	if(StringZZZ.isEmpty(sInput)) {
//            		this.cancelToMenue(hmVariable);
//            	}
				
			}//end main:
			return bReturn;
		}
		
//		private void processROT13_(HashMapZZZ hmVariable) throws ExceptionZZZ{
//			if(hmVariable!=null) {
//        		String sCipher = CryptAlgorithmMappedValueZZZ.CipherTypeZZZ.ROT13.getAbbreviation();
//        		hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CIPHER, sCipher);
//        	}
//		}
//		
//		private void processROTascii_(HashMapZZZ hmVariable) throws ExceptionZZZ{
//			if(hmVariable!=null) {
//        		String sCipher = CryptAlgorithmMappedValueZZZ.CipherTypeZZZ.ROTascii.getAbbreviation();
//        		hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CIPHER, sCipher);
//        		
//        		//#############################################################
//        		this.questionNumericKey(hmVariable);
//        	}
//		}
//		
//		private void processROTnumeric_(HashMapZZZ hmVariable) throws ExceptionZZZ{
//			if(hmVariable!=null) {
//        		String sCipher = CryptAlgorithmMappedValueZZZ.CipherTypeZZZ.ROTnumeric.getAbbreviation();
//        		hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CIPHER, sCipher);
//        		
//        		//###############################################################
//        		this.questionNumericKey(hmVariable);        		
//        		//###############################################################        		
//        		this.questionUseStrategy_CaseChange(hmVariable, null);         		
//        	}
//		}
//		
//		private void processROTnn_(HashMapZZZ hmVariable) throws ExceptionZZZ{
//						
//			if(hmVariable!=null) {
//        		String sCipher = CryptAlgorithmMappedValueZZZ.CipherTypeZZZ.ROTnn.getAbbreviation();
//        		hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CIPHER, sCipher);
//        	}	
//        	
//			
//			//######################################################################
//        	this.questionNumericKey(hmVariable);
//        	
//        	//######################################################################
//        	//### Frage nach Characterpool
//        	if(!this.isCurrentInputFinished()) {
//        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Standard-Zeichenvorrat '" + CharacterExtendedZZZ.sCHARACTER_POOL_DEFAULT + "' als Ausgangsstring verwenden?");				                						                				    	                			                				                					                		
//        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                		
//        			this.cancelToMenue(hmVariable);
//        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//        			this.isCurrentInputValid(true);	
//        			System.out.println("Geben Sie den Zeichenvorrat als String ein.");		                	
//                	sInput = this.getInputReader().nextLine();
//                	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL, sInput);
//                	if(StringZZZ.isEmpty(sInput)) {
//                		this.isCurrentInputValid(false); //wieder zurück zum Menue
//                		this.isCurrentMenue(true);
//                		this.isCurrentInputFinished(true);	
//                	}							                								                	
//            	}else {
//            		this.isCurrentInputValid(true);	
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL, CharacterExtendedZZZ.sCHARACTER_POOL_DEFAULT);			
//            	}
//        		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL, true);
//        	}
//        	String sCharacterPool = (String) hmVariable.get(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL);
//    		
//    		//#####################################################################
//    		//### Frage nach Grossbuchstaben
//        	if(!this.isCurrentInputFinished()) {
//        		if(!StringZZZ.containsUppercaseAndBlankOnly(sCharacterPool)) {
//	        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit Grossbuchstaben verwenden?");				                						                				    	                			                				                					                		
//	        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                		
//	        			this.cancelToMenue(hmVariable);
//	        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//	        			this.isCurrentInputValid(true);	
//	                	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_UPPERCASE, BooleanZZZ.stringToBoolean(sInput));
//	            	}else {
//	            		this.isCurrentInputValid(true);	
//	            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_UPPERCASE, BooleanZZZ.stringToBoolean(sInput));
//	
//	            	}
//        		}
//        	}
//    		
//    		//#####################################################################
//    		//### Frage nach Kleinbuchstaben
//        	if(!this.isCurrentInputFinished()) {
//        		if(!StringZZZ.containsLowercaseAndBlankOnly(sCharacterPool)) {
//	        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit Kleinbuchstaben verwenden?");				                						                				    	                			                				                					                		
//	        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                			
//	        			this.cancelToMenue(hmVariable);
//	        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//	        			this.isCurrentInputValid(true);	
//	                	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_LOWERCASE, BooleanZZZ.stringToBoolean(sInput));
//	            	}else {
//	            		this.isCurrentInputValid(true);	
//	            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_LOWERCASE, BooleanZZZ.stringToBoolean(sInput));			
//	            	}
//	        	}
//        	}
//    			
//        	//#######################################################################
//    		this.questionUseNumeric(hmVariable, sCharacterPool);
//        	
//    		//#######################################################################
//    		this.questionUseAdditional(hmVariable, sCharacterPool);
//        	    		
//        	//#####################################################################
//    		//### Frage nach Leerzeichen
//        	if(!this.isCurrentInputFinished()) {
//        		if(!StringZZZ.containsBlankAny(sCharacterPool)) {
//	        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit dem Leerzeichen ' ' verwenden?");				                						                				    	                			                				                					                		
//	        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){
//	        			this.cancelToMenue(hmVariable);
//	        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//	        			this.isCurrentInputValid(true);	
//	                	//nix weiter
//	            	}else {
//	            		this.isCurrentInputValid(true);
//	            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL, (" " + sCharacterPool));	            				
//	            	}
//        		}
//        	}	
//		}
//		
//		private void processVigenereNn_(HashMapZZZ hmVariable) throws ExceptionZZZ{
//			
//			if(hmVariable!=null) {
//        		String sCipher = CryptAlgorithmMappedValueZZZ.CipherTypeZZZ.VIGENEREnn.getAbbreviation();
//        		hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CIPHER, sCipher);
//        	}	
//        	
//			//######################################################################
//        	this.questionAlphabetKey(hmVariable);
//        	
//        	//######################################################################
//        	//### Frage nach Haupt-Characterpool
//        	if(!this.isCurrentInputFinished()) {
//        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Zeichenvorrat '" + CharacterExtendedZZZ.sCHARACTER_POOL_DEFAULT + "' als Ausgangsstring verwenden (er kann ggfs. noch in folgenden Schritten ergaenzt werden)?");				                						                				    	                			                				                					                		
//        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                		
//        			this.cancelToMenue(hmVariable);
//        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//        			this.isCurrentInputValid(true);	
//        			System.out.println("Geben Sie den Zeichenvorrat als String ein.");		                	
//                	sInput = this.getInputReader().nextLine();
//                	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL, sInput);
//                	if(StringZZZ.isEmpty(sInput)) {
//                		this.isCurrentInputValid(false); //wieder zurück zum Menue
//                		this.isCurrentMenue(true);
//                		this.isCurrentInputFinished(true);	
//                	}							                								                	
//            	}else {
//            		this.isCurrentInputValid(true);	
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL, CharacterExtendedZZZ.sCHARACTER_POOL_DEFAULT);			
//            	}
//        		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL, true);
//        	}
//        	String sCharacterPool = (String) hmVariable.get(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL);
//    		
//        	
//    		//#####################################################################
//    		//### Frage nach Grossbuchstaben
//        	if(!this.isCurrentInputFinished()) {
//        		if(!StringZZZ.containsUppercaseAndBlankOnly(sCharacterPool)) {
//	        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit Grossbuchstaben verwenden?");				                						                				    	                			                				                					                		
//	        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                		
//	        			this.cancelToMenue(hmVariable);
//	        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//	        			this.isCurrentInputValid(true);	
//	                	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_UPPERCASE, BooleanZZZ.stringToBoolean(sInput));
//	            	}else {
//	            		this.isCurrentInputValid(true);	
//	            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_UPPERCASE, BooleanZZZ.stringToBoolean(sInput));
//	
//	            	}
//        		}
//        	}
//    		
//    		//#####################################################################
//    		//### Frage nach Kleinbuchstaben
//        	if(!this.isCurrentInputFinished()) {
//        		if(!StringZZZ.containsLowercaseAndBlankOnly(sCharacterPool)) {
//	        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit Kleinbuchstaben verwenden?");				                						                				    	                			                				                					                		
//	        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                			
//	        			this.cancelToMenue(hmVariable);
//	        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//	        			this.isCurrentInputValid(true);	
//	                	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_LOWERCASE, BooleanZZZ.stringToBoolean(sInput));
//	            	}else {
//	            		this.isCurrentInputValid(true);	
//	            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_LOWERCASE, BooleanZZZ.stringToBoolean(sInput));			
//	            	}
//	        	}
//        	}
//    			
//        	//#######################################################################        	
//    		this.questionUseNumeric(hmVariable, sCharacterPool);
//    		
//    		//#######################################################################
//    		this.questionUseAdditional(hmVariable, sCharacterPool);
//        	    		
//        	//#####################################################################
//    		//### Frage nach Leerzeichen
//        	if(!this.isCurrentInputFinished()) {
//        		if(!StringZZZ.containsBlankAny(sCharacterPool)) {
//	        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit dem Leerzeichen ' ' verwenden?");				                						                				    	                			                				                					                		
//	        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){
//	        			this.cancelToMenue(hmVariable);
//	        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//	        			this.isCurrentInputValid(true);	
//	                	//nix weiter
//	            	}else {
//	            		this.isCurrentInputValid(true);
//	            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_CHARACTERPOOL, (" " + sCharacterPool));	            				
//	            	}
//        		}
//        	}	
//		}
		
	
	
	@Override
	public boolean initit(HashMapZZZ hmVariable) throws ExceptionZZZ {
		boolean bReturn = true;
		main:{
			
			//Die Hier übergebene Methode wird in ConsoleUserSqlGenerator.startit() ausgelesen.
			//Plus alle anderen INPUT - Variablen.
			
			
		}//end main:
		return bReturn;
	}
}


    