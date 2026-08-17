package use.database.sql.console;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractList.HashMapZZZ;
import basic.zBasic.util.console.multithread.AbstractConsoleUserStartableZZZ;
import basic.zBasic.util.console.multithread.IConsoleZZZ;
import basic.zBasic.util.crypt.code.CryptAlgorithmMaintypeZZZ;
import basic.zBasic.util.crypt.code.ICharacterPoolEnabledZZZ;
import basic.zBasic.util.crypt.code.ICryptZZZ;
import basic.zBasic.util.crypt.thread.KeyPressThreadEncryptZZZ;
import basic.zBasic.util.datatype.character.CharacterExtendedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;

public abstract class AbstractConsoleUserSqlGeneratorZZZ extends AbstractConsoleUserStartableZZZ{
	public AbstractConsoleUserSqlGeneratorZZZ()  throws ExceptionZZZ {
		super();
	}
	public AbstractConsoleUserSqlGeneratorZZZ(IConsoleZZZ objConsole) throws ExceptionZZZ {
		super(objConsole);		
	}
	public AbstractConsoleUserSqlGeneratorZZZ(IConsoleZZZ objConsole,String sFlag) throws ExceptionZZZ {
		super(objConsole, sFlag);		
	}
	public AbstractConsoleUserSqlGeneratorZZZ(IConsoleZZZ objConsole,String[] saFlag) throws ExceptionZZZ {
		super(objConsole, saFlag);	
	}
	
	public boolean preProcessing(ICryptZZZ objCrypt, HashMapZZZ<String,Object>hmVariable) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			bReturn = this.preProcessingObject(objCrypt,hmVariable);
			if(!bReturn) {
				System.out.println("PreProcessing Objecteinstellungen nicht erfolgreich, Abbruch");
				bReturn=false;
				break main;
			}
			
			bReturn = this.preProcessingFlags(objCrypt, hmVariable);
			if(!bReturn) {
				System.out.println("PreProcessing Flags nicht erfolgreich, Abbruch");
				bReturn=false;
				break main;
			}
		}//end main:
		return bReturn;
	}
	
	public boolean preProcessingObject(ICryptZZZ objCrypt, HashMapZZZ<String,Object>hmVariable) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			String sInput=null;
			
			//+++ Zusätzlich gesetzte Argumente				
			if(objCrypt.getSubtype()==CryptAlgorithmMaintypeZZZ.TypeZZZ.ROT.ordinal()) {
				//A) Für ROT-Algorithmen
				//0) NumericKey
				sInput = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_KEY_NUMERIC);
				if(!StringZZZ.isEmpty(sInput)) {
					Integer intCryptKey = new Integer(sInput);
					int iCryptKey = intCryptKey.intValue();
					objCrypt.setCryptNumber(iCryptKey);
				}

			}else if(objCrypt.getSubtype()==CryptAlgorithmMaintypeZZZ.TypeZZZ.VIGENERE.ordinal()) {
				//B) Für Vigenere-Algorithmen
				//0) StringKey
				sInput = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_KEY_STRING);
				if(!StringZZZ.isEmpty(sInput)) {
					String sCryptKey = sInput;						
					objCrypt.setCryptKey(sCryptKey);
				}
									
			}else {
				System.out.println("Der Algorithmus-Subtyp wird nicht behandelt.");
				bReturn = false;
				break main;
			}
			
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	public boolean preProcessingFlags(ICryptZZZ objCrypt, HashMapZZZ<String,Object>hmVariable) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			String sInput=null;
			
			//a) alle Flags setzen und ggfs. auswerten
			String[] saFlags = hmVariable.getKeysAsStringStartingWith(KeyPressThreadEncryptZZZ.sINPUT_FLAG);
			if(saFlags!=null) {
				for(String stemp : saFlags) {
					String sFlagName = StringZZZ.right(stemp,KeyPressThreadEncryptZZZ.sINPUT_FLAG);
					Boolean bValue = (Boolean) hmVariable.get(stemp);
					objCrypt.setFlag(sFlagName, bValue);
				}
			}
			
			//b) CharacterPool setzen, wenn verwendet
			if(objCrypt.getFlag(ICharacterPoolEnabledZZZ.FLAGZ.USESTRATEGY_CHARACTERPOOL.name())) {
				sInput = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_CHARACTERPOOL);
				if(!StringZZZ.isEmpty(sInput)) {
					objCrypt.setCharacterPoolBase(sInput);
					
					//... und dann auch das fehlende Zeichen setzen
					CharacterExtendedZZZ objCharacterMissingReplacement = new CharacterExtendedZZZ('¶');//Mal was anderes verwenden als den "DefaultBuchstaben" aus ICharacterPoolUserConstantZZZ
					objCrypt.setCharacterMissingReplacement(objCharacterMissingReplacement);
				}
				
				//ba) Zusaetzlichen Zeichenpool setzen, wenn verwendet					
				if(objCrypt.getFlag(ICharacterPoolEnabledZZZ.FLAGZ.USEADDITIONALCHARACTER.name())) {
					sInput = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_CHARACTERPOOL_ADDITIONAL);
					if(!StringZZZ.isEmpty(sInput)) {
						objCrypt.setCharacterPoolAdditional(sInput);
					}
				}
			}
			
			bReturn = true;
		}//end main:
		return bReturn;
	}
}
