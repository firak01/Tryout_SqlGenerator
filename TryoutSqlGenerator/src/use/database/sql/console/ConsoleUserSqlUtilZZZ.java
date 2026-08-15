package use.database.sql.console;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractList.HashMapZZZ;
import basic.zBasic.util.console.multithread.AbstractConsoleUserZZZ;
import basic.zBasic.util.console.multithread.IConsoleZZZ;
import basic.zBasic.util.crypt.code.CryptAlgorithmFactoryZZZ;
import basic.zBasic.util.crypt.code.CryptAlgorithmMaintypeZZZ;
import basic.zBasic.util.crypt.code.ICharacterPoolEnabledZZZ;
import basic.zBasic.util.crypt.code.ICryptZZZ;
import basic.zBasic.util.crypt.code.IVigenereNnZZZ;
import basic.zBasic.util.crypt.thread.KeyPressThreadEncryptZZZ;
import basic.zBasic.util.datatype.character.CharacterExtendedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.IFlagZEnabledZZZ;

public class ConsoleUserSqlUtilZZZ extends AbstractConsoleUserSqlUtilZZZ {
	private static final long serialVersionUID = 1L;

	public ConsoleUserSqlUtilZZZ() throws ExceptionZZZ {
		super();
	}
	
	public ConsoleUserSqlUtilZZZ(IConsoleZZZ objConsole) throws ExceptionZZZ {
		super(objConsole);
	}
	public ConsoleUserSqlUtilZZZ(IConsoleZZZ objConsole, String sFlag) throws ExceptionZZZ {
		super(objConsole, sFlag);
	}
	public ConsoleUserSqlUtilZZZ(IConsoleZZZ objConsole, String[] saFlag) throws ExceptionZZZ {
		super(objConsole, saFlag);
	}
	
	private int iCounter = 0;
		
	public int getcounter() {
		return this.iCounter;
	}
	
	@Override
	public boolean start() throws ExceptionZZZ {
		boolean bReturn = false;
		try {
		main:{
			this.getConsole().isConsoleUserThreadRunning(true);
			//Merke: Diesen Teil nicht als Schleife ausführen... viel zu kompliziert... es gibt schon genug andere Threads
			//while(!this.isStopped()) {
			if(this.isStopped()) break main;
			if(this.isOutputAllFinished()) break main; //wenn Z.B. schon ein Menuepunkt ausgefuehrt worden ist. Z.B. eine einfache ASCII-Tabelle ausgegeben wurde.
			if(!this.isInputAllFinished()) break main; 
			String sInput = null;
			
			//Merke: Man kann keine zweite Scanner Klasse auf den sys.in Stream ansetzen.
			//       Darum muss man alles in dem KeyPressThread erledigen
			//Warten auf die fertige Eingabe.			
			//if(!this.getConsole().isKeyPressThreadFinished()) break main;
			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### CryptThread START: WARTE AUF FERTIGE KONSOLENEINGABE ######");				
			do {
				 try {				 
					 Thread.sleep(200);
					 //System.out.println("CryptThread wartet auf fertige Konsoleneingabe");
				} catch (InterruptedException e) {
					System.out.println("KeyPressThread: Wait Error");
					e.printStackTrace();
				}				 
			}while(!this.getConsole().isInputAllFinished());
			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### CryptThread ENDE: WARTE AUF FERTIGE KONSOLENEINGABE ######");
			
			
			//this.isOutputAllFinished(false);
			
			
			this.iCounter++;
			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("Zähler crypt: " + iCounter);

			HashMapZZZ<String,Object>hmVariable=this.getConsole().getVariableHashMap();
			if(hmVariable!=null) {
				String sDebug = hmVariable.computeDebugString("<BR>","|");
				System.out.println(sDebug);
			}
			
			if(hmVariable!=null) {
				//Ausgabewerte zurücksetzen
				hmVariable.remove(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_ENCRYPTED);
				hmVariable.remove(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_UNCRYPTED);
				hmVariable.remove(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_DECRYPTED);
			}
					
			//Die eingegebenen Variablen über eine HashMap aus der Console für die Steuereung der Verschlüsselung nutzen. 			
			//String sCipher = (String) hmVariable.get(CryptCipherAlgorithmMappedValueZZZ.CryptCipherTypeZZZ.ROT13.getAbbreviation());
			String sCipher = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_CIPHER);
			if(!StringZZZ.isEmpty(sCipher)) {
				ICryptZZZ objCrypt = CryptAlgorithmFactoryZZZ.getInstance().createAlgorithmType(sCipher);
				boolean bSuccess = this.preProcessing(objCrypt, hmVariable);
				if(!bSuccess) {					
					System.out.println("PreProcessing nicht erfolgreich, Abbruch");
					bReturn=false;
					break main;
				}
								
				//+++++++++++++++++++++++++++++++++++++++++++++++++
								
				sInput = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_TEXT_UNCRYPTED);				
				try {
					String sOutput = objCrypt.encrypt(sInput);
					hmVariable.put(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_ENCRYPTED, sOutput);
					
					System.out.println("Verschluesselter Wert:\n"+sOutput);
					String sOutput2 = objCrypt.decrypt(sOutput);
					hmVariable.put(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_DECRYPTED, sOutput2);
					System.out.println("Wieder entschluesselter Wert:\n"+sOutput2);
					
					bReturn = true;
				}catch( IllegalArgumentException e) {
					String sError=e.getMessage();
					System.out.println("Fehler bei der Eingabe.\nText enthaelt fuer die Argumentkombination ungueltige Werte.\nFehler: "+sError +"\nbei Eingabe: "+sInput);
					bReturn=false;
				}
				
			}else {
				System.out.println("noch kein Schluesselalgorithmus festgelegt.");
				bReturn = false;
			}
			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### CryptThread START: DUMMYWARTEN ALS TEST ######");
			 try {				 
				 Thread.sleep(4500);
			} catch (InterruptedException e) {
				System.out.println("KeyPressThread: Wait Error");
				e.printStackTrace();
			}
			 if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### CryptThread ENDE: DUMMYWARTEN ALS TEST ######");			 
			 this.isOutputAllFinished(true);			
			//}//end while !isStopped
		}//end main:
		}catch(ExceptionZZZ ez) {
			ez.printStackTrace();
		}
		this.getConsole().isConsoleUserThreadFinished(true);
		return bReturn;
	}
}
