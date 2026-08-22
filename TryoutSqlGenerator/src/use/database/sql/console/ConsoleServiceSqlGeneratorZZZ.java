package use.database.sql.console;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractList.HashMapZZZ;
import basic.zBasic.util.console.thread.AbstractConsoleServiceZZZ;
import basic.zBasic.util.console.thread.IConsoleControllerZZZ;
import basic.zBasic.util.console.thread.IKeyPressThreadConstantZZZ;
import basic.zBasic.util.console.thread.KeyPressThreadUtilZZZ;
import basic.zBasic.util.crypt.code.CryptAlgorithmFactoryZZZ;
import basic.zBasic.util.crypt.code.CryptAlgorithmMaintypeZZZ;
import basic.zBasic.util.crypt.code.ICharacterPoolEnabledZZZ;
import basic.zBasic.util.crypt.code.ICryptZZZ;
import basic.zBasic.util.crypt.code.IVigenereNnZZZ;
import basic.zBasic.util.crypt.thread.ConsoleServiceDecryptZZZ;
import basic.zBasic.util.crypt.thread.ConsoleServiceEncryptZZZ;
import basic.zBasic.util.crypt.thread.KeyPressThreadEncryptZZZ;
import basic.zBasic.util.datatype.character.CharacterExtendedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.IFlagZEnabledZZZ;
import use.database.sql.generate.SqlUtilZZZ;

public class ConsoleServiceSqlGeneratorZZZ extends AbstractConsoleServiceSqlGeneratorZZZ {
	private static final long serialVersionUID = 1L;

	public ConsoleServiceSqlGeneratorZZZ() throws ExceptionZZZ {
		super();
	}
	
	public ConsoleServiceSqlGeneratorZZZ(IConsoleControllerZZZ objConsole) throws ExceptionZZZ {
		super(objConsole);
	}
	public ConsoleServiceSqlGeneratorZZZ(IConsoleControllerZZZ objConsole, String sFlag) throws ExceptionZZZ {
		super(objConsole, sFlag);
	}
	public ConsoleServiceSqlGeneratorZZZ(IConsoleControllerZZZ objConsole, String[] saFlag) throws ExceptionZZZ {
		super(objConsole, saFlag);
	}
	
	private int iCounter = 0;
		
	public int getcounter() {
		return this.iCounter;
	}
	
//	@Override
//	public boolean start() throws ExceptionZZZ {
//		boolean bReturn = false;
//		try {
//		main:{
//			this.getConsole().isConsoleUserThreadRunning(true);
//			//Merke: Diesen Teil nicht als Schleife ausführen... viel zu kompliziert... es gibt schon genug andere Threads
//			//while(!this.isStopped()) {
//			if(this.isStopped()) break main;
//			if(this.isOutputAllFinished()) break main; //wenn Z.B. schon ein Menuepunkt ausgefuehrt worden ist. Z.B. eine einfache ASCII-Tabelle ausgegeben wurde.
//			if(!this.isInputAllFinished()) break main; 
//			String sInput = null;
//			
//			//Merke: Man kann keine zweite Scanner Klasse auf den sys.in Stream ansetzen.
//			//       Darum muss man alles in dem KeyPressThread erledigen
//			//Warten auf die fertige Eingabe.			
//			//if(!this.getConsole().isKeyPressThreadFinished()) break main;
//			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### ConsoleUserSqlGenerator START: WARTE AUF FERTIGE KONSOLENEINGABE ######");				
//			do {
//				 try {				 
//					 Thread.sleep(200);
//					 //System.out.println("CryptThread wartet auf fertige Konsoleneingabe");
//				} catch (InterruptedException e) {
//					System.out.println("KeyPressThread: Wait Error");
//					e.printStackTrace();
//				}				 
//			}while(!this.getConsole().isInputAllFinished());
//			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### ConsoleUserSqlGenerator ENDE: WARTE AUF FERTIGE KONSOLENEINGABE ######");
//			
//			
//			//this.isOutputAllFinished(false);
//			
//			
//			this.iCounter++;
//			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("Zähler ConsoleUserSqlGenerator: " + iCounter);
//
//			
//			//TODOGOON20260816: //In AbstractConsoleUser die Methode start() machen, die den obigen Code ausführt.
//			//TODOGOON20260816; //Darin dann am Schluss die abstrakte Methode .startIt() aufrufen.... Die dann vom Konkreten verwendeten Thread genutzt wird.
//			//this.startit();  //Im ConsoleUserSqlGenerator müsste dann vom jeweiligen Thread-Objekt .startit() aufgerufen werden....
//			
//			//TODOGOON20260816; //Nachfolgenden Code dann auch in die startit() Methode verlagern...
//			//TODOGOON20260816; //Hier eine allgemeine Methode .reset() aufrufen.... Die dann vom Konkreten verwendeten Thread genutzt wird.
//			HashMapZZZ<String,Object>hmVariable=this.getConsole().getVariableHashMap();
//			this.startit(hmVariable);
//					
//			
//			if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### SqlGeneratorThread START: DUMMYWARTEN ALS TEST ######");
//			 try {				 
//				 Thread.sleep(4500);
//			} catch (InterruptedException e) {
//				System.out.println("KeyPressThread: Wait Error");
//				e.printStackTrace();
//			}
//			 if(this.getFlag(IFlagZEnabledZZZ.FLAGZ.DEBUG)) System.out.println("####### SqlGeneratorThread ENDE: DUMMYWARTEN ALS TEST ######");			 
//			 this.isOutputAllFinished(true);			
//			//}//end while !isStopped
//		}//end main:
//		}catch(ExceptionZZZ ez) {
//			ez.printStackTrace();
//		}
//		this.getConsole().isConsoleUserThreadFinished(true);
//		return bReturn;
//	}
	
	public boolean startit(HashMapZZZ hmVariable) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{				
			//Jetzt können Variablen aus dem KeyPressThread entgegengenommen werden.
			String sCallingMethod= (String) hmVariable.get(IKeyPressThreadConstantZZZ.sINPUT_STRING_METHOD_USED);
			
			//Nutze auch die nicht startit fähigen Methoden
			if(!StringZZZ.isEmptyNull(sCallingMethod)) {
				switch(sCallingMethod){	
					case "ascii":
						ascii_(hmVariable);
						break;
					case "processSqlObjGuid":
						processSqlObjGuid_(hmVariable);
						break;
					case "processEncryptROT13":
						processEncryptROT13_(hmVariable);						
						break;
					case "processDecryptROT13":
						processDecryptROT13_(hmVariable);						
						break;
					default:
						ExceptionZZZ ez = new ExceptionZZZ("Nicht behandelte Methode: '" + sCallingMethod + "'", iERROR_PROPERTY_VALUE, this.getClass(), ReflectCodeZZZ.getPositionCurrent());
						throw ez;
				}
			}else {
				//############## ALTE VERSION, NOCH NICHT ENTFERNT STARTBAR
				if(hmVariable!=null) {
					//Ausgabewerte zurücksetzen
					hmVariable.remove(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_ENCRYPTED);
					hmVariable.remove(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_UNCRYPTED);
					hmVariable.remove(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_DECRYPTED);
				}
				
				//Debugausgabe, ob auch alles leer ist
				if(hmVariable!=null) {
					String sDebug = hmVariable.computeDebugString("<BR>","|");
					System.out.println(sDebug);
				}
				
				//Hier ein Beispiel für Encryption
//				if(hmVariable!=null) {
//					String sCipher = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_CIPHER);
//					if(!StringZZZ.isEmpty(sCipher)) {
//						ICryptZZZ objCrypt = CryptAlgorithmFactoryZZZ.getInstance().createAlgorithmType(sCipher);
//						boolean bSuccess = this.preProcessing(objCrypt, hmVariable);
//						if(!bSuccess) {					
//							System.out.println("PreProcessing nicht erfolgreich, Abbruch");
//							bReturn=false;
//							break main;
//						}
//										
//						//+++++++++++++++++++++++++++++++++++++++++++++++++
//										
//						sInput = (String) hmVariable.get(KeyPressThreadEncryptZZZ.sINPUT_TEXT_UNCRYPTED);				
//						try {
//							String sOutput = objCrypt.encrypt(sInput);
//							hmVariable.put(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_ENCRYPTED, sOutput);
//							
//							System.out.println("Verschluesselter Wert:\n"+sOutput);
//							String sOutput2 = objCrypt.decrypt(sOutput);
//							hmVariable.put(KeyPressThreadEncryptZZZ.sOUTPUT_TEXT_DECRYPTED, sOutput2);
//							System.out.println("Wieder entschluesselter Wert:\n"+sOutput2);
//							
//							bReturn = true;
//						}catch( IllegalArgumentException e) {
//							String sError=e.getMessage();
//							System.out.println("Fehler bei der Eingabe.\nText enthaelt fuer die Argumentkombination ungueltige Werte.\nFehler: "+sError +"\nbei Eingabe: "+sInput);
//							bReturn=false;
//						}
//						
//					}else {
//						System.out.println("noch kein Schluesselalgorithmus festgelegt.");
//						bReturn = false;
//					}
					
				}	//end if sCallingMethod				
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	//########################################
		private boolean ascii_(HashMapZZZ hmVariable) throws ExceptionZZZ {
			KeyPressThreadUtilZZZ.printTableAscii();		
			return true;
		}
		
	//########################################
	
	
	
	//#################################
	public boolean processEncryptROT13_(HashMapZZZ hmVariable) throws ExceptionZZZ {
		//Ausgabe einer errechneten ObjGuid
				boolean bReturn = false;
				main:{
					ConsoleServiceEncryptZZZ objEncrypter = new ConsoleServiceEncryptZZZ();
					bReturn = objEncrypter.startit(hmVariable);
					
				}//end main:
				return bReturn;	
	}
	
	//#################################
	public boolean processDecryptROT13_(HashMapZZZ hmVariable) throws ExceptionZZZ {
		//Ausgabe einer errechneten ObjGuid
				boolean bReturn = false;
				main:{
					ConsoleServiceDecryptZZZ objDecrypter = new ConsoleServiceDecryptZZZ();
					bReturn = objDecrypter.startit(hmVariable);
					
				}//end main:
				return bReturn;	
	}
	
	
	//#################################
	/**Dadurch soll diese Methode aus anderen Threads nutzbar sein
	 * @param hmVariable
	 * @return
	 * @throws ExceptionZZZ
	 */
	public boolean processSqlObjGuid(HashMapZZZ hmVariable) throws ExceptionZZZ{
		return processSqlObjGuid_(hmVariable);
	}
		
	private boolean processSqlObjGuid_(HashMapZZZ hmVariable) throws ExceptionZZZ{		
		//Ausgabe einer errechneten ObjGuid
		boolean bReturn = true;
		main:{

			//In dieser einfachen Methode gibt es keine weiteren Parameter entgegenzunehmen....
			//eigentlich müsste diese Methode umbenannt werden in irgenwas mit Input...ParameterCustom...
			
			String sObjGuid = SqlUtilZZZ.createObj_guid();
			System.out.println(sObjGuid);
		}//end main:
		return bReturn;	
	}
	
}
