package use.database.sql.console;

import java.util.Scanner;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractList.HashMapZZZ;
import basic.zBasic.util.console.thread.AbstractKeyPressThreadCommonZZZ;
import basic.zBasic.util.console.thread.AbstractKeyPressThreadZZZ;
import basic.zBasic.util.console.thread.IConsoleZZZ;
import basic.zBasic.util.console.thread.IKeyPressConstantZZZ;
import basic.zBasic.util.console.thread.IKeyPressThreadConstantZZZ;
import basic.zBasic.util.console.thread.KeyPressUtilZZZ;
import basic.zBasic.util.crypt.thread.KeyPressCryptUtilZZZ;
import basic.zBasic.util.crypt.thread.KeyPressThreadDecryptZZZ;
import basic.zBasic.util.datatype.booleans.BooleanZZZ;
import basic.zBasic.util.datatype.character.CharacterExtendedZZZ;
import basic.zBasic.util.datatype.character.ICharacterExtendedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;

public abstract class AbstractKeyPressThreadSqlGeneratorZZZ extends AbstractKeyPressThreadCommonZZZ implements IKeyPressThreadSqlGeneratorConstantZZZ{
	public AbstractKeyPressThreadSqlGeneratorZZZ(IConsoleZZZ objConsole) {
    	super(objConsole);
    }
    public AbstractKeyPressThreadSqlGeneratorZZZ(IConsoleZZZ objConsole, long lSleepTime) {
    	super(objConsole, lSleepTime);
    }
     
   
	
//	protected void questionNumericKey(HashMapZZZ hmVariable) throws ExceptionZZZ {
//		//######################################################################
//    	//### Frage nach dem Numeric-Key (um den dann die Rotation stattfindet
//    	if(!this.isCurrentInputFinished()) {
//        	String sInput = KeyPressUtilZZZ.makeInputNumericCancel(this.getInputReader(), "Bitte geben Sie den nummerischen Schluessel ein.");				                						                				    	                			                				                					                		
//    		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                		
//    			this.cancelToMenue(hmVariable);				                
//        	}else {
//        		this.isCurrentInputValid(true);	
//        		if(hmVariable!=null) hmVariable.put(IKeyPressThreadConstantZZZ.sINPUT_KEY_NUMERIC, sInput);					
//        	}	
//    	}
//	}
	
//	protected void questionAlphabetKey(HashMapZZZ hmVariable) throws ExceptionZZZ {
//		//######################################################################
//    	//### Frage nach dem Alphabet-Key (um dessen Zahlenwert den dann die Rotation stattfindet
//    	if(!this.isCurrentInputFinished()) {
//        	String sInput = KeyPressUtilZZZ.makeInputAlphabetCancel(this.getInputReader(), "Bitte geben Sie das Schluesselwort bestehend aus Zeichen des Alphabets ein.");				                						                				    	                			                				                					                		
//    		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){					                		
//    			this.cancelToMenue(hmVariable);				                
//        	}else {
//        		this.isCurrentInputValid(true);	
//        		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_KEY_STRING, sInput);					
//        	}	
//    	}
//	}
	
//	protected void questionUseAdditional(HashMapZZZ hmVariable, String sCharacterPool) throws ExceptionZZZ {
//		
//		//#####################################################################
//		//### Frage nach Sonderzeichen, bzw. Zusatzzeichen        	
//    	if(!this.isCurrentInputFinished()) {        		
//    		boolean bCharacterPoolContainsAdditionalOnly=false;//nur nach Sonderzeichen fragen, wenn der characterPool nicht eh aus Sonderzeichen besteht.
//    		if(!StringZZZ.isEmpty(sCharacterPool)) {
//    			bCharacterPoolContainsAdditionalOnly=StringZZZ.containsOnly(sCharacterPool, ICharacterExtendedZZZ.sCHARACTER_ADDITIONAL);
//    		}
//    		        		
//    		if(!bCharacterPoolContainsAdditionalOnly) {
//        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit diesen Standard-Zusatzbuchstaben '" + ICharacterExtendedZZZ.sCHARACTER_ADDITIONAL + "' verwenden?");
//        		this.isCurrentInputValid(true); 
//        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){
//        			this.cancelToMenue(hmVariable);
//        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {		        				                		
//        			System.out.println("Geben Sie den gewuenschten Standard-Zusatzbuchstaben Zeichenvorrat als String ein, oder keine Zusatzbuchstaben.");		                	
//                	sInput = this.getInputReader().nextLine();                        	
//                	if(StringZZZ.isEmpty(sInput)) {
//                		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL, false);
//                		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_CHARACTER_ADDITIONAL, false);
//                		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_CHARACTERPOOL_ADDITIONAL, "");
//                	}else {
//                		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL, true);
//                		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_CHARACTER_ADDITIONAL, true);
//                		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_CHARACTERPOOL_ADDITIONAL, sInput);
//                	}	                        		                        		                                            		        				                			                		                		                			                	              
//            	}else{	            		
//            		//STANDARD-FALL	            		
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL, true);
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_CHARACTER_ADDITIONAL, true);
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_CHARACTERPOOL_ADDITIONAL, CharacterExtendedZZZ.sCHARACTER_ADDITIONAL);        					            			      
//            	}	        		
//    		}//end if(!bCharacterPoolContainsAdditionalOnly) {	
//    	}
//	}
	
//	protected void questionUseBlank(HashMapZZZ hmVariable, String sCharacterPool) throws ExceptionZZZ {
//		//#####################################################################
//		//### Frage nach Zahlen        	
//    	if(!this.isCurrentInputFinished()) {
//
//    		boolean bCharacterPoolContainsNumericOnly=false;//nur nach Zahlen fragen, wenn der characterPool nicht eh aus Zahlen besteht.
//    		if(StringZZZ.isEmpty(sCharacterPool)) {
//    			bCharacterPoolContainsNumericOnly=StringZZZ.containsNumericAndBlankOnly(sCharacterPool);
//    		}
//    		        		
//    		if(!bCharacterPoolContainsNumericOnly) {
//        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit dem 'Leerzeichen' verwenden?");	        		
//        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){
//        			this.cancelToMenue(hmVariable);
//        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//        			this.isCurrentInputValid(true);	
//                	if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_CHARACTER_BLANK, BooleanZZZ.stringToBoolean(sInput));
//            	}else {
//            		this.isCurrentInputValid(true);	
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_CHARACTER_BLANK, BooleanZZZ.stringToBoolean(sInput));
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadDecryptZZZ.sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL, BooleanZZZ.stringToBoolean(sInput));
//            	}	        		
//    		}
//    	}
//	}
	
//	protected void questionUseNumeric(HashMapZZZ hmVariable, String sCharacterPool) throws ExceptionZZZ {
//		//#####################################################################
//		//### Frage nach Zahlen        	
//    	if(!this.isCurrentInputFinished()) {
//
//    		boolean bCharacterPoolContainsNumericOnly=false;//nur nach Zahlen fragen, wenn der characterPool nicht eh aus Zahlen besteht.
//    		if(StringZZZ.isEmpty(sCharacterPool)) {
//    			bCharacterPoolContainsNumericOnly=StringZZZ.containsNumericAndBlankOnly(sCharacterPool);
//    		}
//    		        		
//    		if(!bCharacterPoolContainsNumericOnly) {
//        		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie den Pool ergaenzend mit nummerischen Werte (0-9) verwenden?");	        		
//        		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){
//        			this.cancelToMenue(hmVariable);
//        		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//        			this.isCurrentInputValid(true);	
//                	if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_NUMERIC, BooleanZZZ.stringToBoolean(sInput));
//            	}else {
//            		this.isCurrentInputValid(true);	
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_CHARACTER_NUMERIC, BooleanZZZ.stringToBoolean(sInput));
//            		if(hmVariable!=null) hmVariable.put(KeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL, BooleanZZZ.stringToBoolean(sInput));
//            	}	        		
//    		}
//    	}
//	}
	
//	protected void questionUseStrategy_CaseChange(HashMapZZZ hmVariable, String sCharacterPool) throws ExceptionZZZ {
//		//#####################################################################
//		//### Frage nach der Stragegy "CaseChange"        	
//    	if(!this.isCurrentInputFinished()) {
//    		String sInput = KeyPressUtilZZZ.makeQuestionYesNoCancel(this.getInputReader(), "Wollen Sie als Strategie den Austausch zwischen den Typen Gross-/Kleinbuchstaben und Numerischen Zeichen verwenden (y) oder soll der Austausch innerhalb des Typs bleiben (N)?");	        		
//    		if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyCancel)){
//    			this.cancelToMenue(hmVariable);
//    		}else if(StringZZZ.equalsIgnoreCase(sInput, IKeyPressConstantZZZ.cKeyNo)) {
//    			this.isCurrentInputValid(true);	
//    			if(hmVariable!=null) hmVariable.put(IKeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_USE_STRATEGY_CASECHANGE, BooleanZZZ.stringToBoolean(sInput));
//        	}else {
//        		this.isCurrentInputValid(true);	            		
//        		if(hmVariable!=null) hmVariable.put(IKeyPressThreadSqlGeneratorZZZ.sINPUT_FLAG_USE_STRATEGY_CASECHANGE, BooleanZZZ.stringToBoolean(sInput));
//        		
//        		//###############################################################
//        		this.questionUseBlank(hmVariable,null);
//        	}	        		
//		}
//	}
}
