package use.database.sql.generate.console;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.console.thread.ConsoleControllerZZZ;
import basic.zBasic.util.console.thread.IConsoleServiceZZZ;
import basic.zBasic.util.console.thread.IConsoleControllerUserZZZ;
import basic.zBasic.util.console.thread.IConsoleControllerZZZ;
import basic.zBasic.util.console.thread.IKeyPressThreadMenueableZZZ;
import basic.zBasic.util.crypt.thread.ConsoleServiceEncryptZZZ;
import basic.zBasic.util.crypt.thread.KeyPressThreadEncryptZZZ;
import use.database.sql.console.ConsoleServiceSqlGeneratorZZZ;
import use.database.sql.console.KeyPressThreadSqlGeneratorZZZ;

public class ConsoleSqlGeneratorMainZZZ {

	public static void main(String[] args) {
		try {
			//TODOGOON20230203;//Per args uebergebenen Parameter auslesen
			//Parameter aus args auslesen
			//String[]saFlag = {"useExpression","useFormula","useEncryption"};
			//ConfigCryptZZZ   machen wie:    ConfigOVPN objConfig = new ConfigOVPN(saArg, saFlag);
			//aber in dieser Applikation OHNE Kernel, also OHNE ini-Konfiguration auskommen!!!    this.objKernel = new KernelZZZ(objConfig, (String) null); //Damit kann man ueber die Startparameter ein anders konfiguriertes Kernel-Objekt erhalten.
			//TODOGOON20230203: Daher das objConfig in .getInstance(objConfig) übergeben!!!

			IConsoleControllerZZZ objConsoleController = ConsoleControllerZZZ.getInstance();
			
			//Merke: Ziel ist es, das was in DebugRot13ZZZ (oder ähnlichen) gemacht wird in einer Endlosschleife durchzuführen.
			//Der ConsoleUser und die Eingabe so eng miteinander verknüpft, dass man hier den KeyPressCryptThreadZZZ
			//übergeben wird, der das Verhalten des ...UserCrypt... steuert.
			//KeyPressThreadEncryptZZZ objKeyPressThread = new KeyPressThreadEncryptZZZ(objConsole);
			IKeyPressThreadMenueableZZZ objKeyPressThread = new KeyPressThreadSqlGeneratorZZZ(objConsoleController);
			objConsoleController.setKeyPressThread(objKeyPressThread);
						
			//TODOGOON20230203; Übergib die Argumente aus objConsole and objConsoleUser, analog zu KernelKernelZZZ und dort an FileIniZZZ
			//siehe.....
			//read the ini-content: 
			//Merke: Falls die Datei nicht existiert, wird ein Fehler geworfen								
//			HashMap<String, Boolean> hmFlag = new HashMap<String, Boolean>();					
//			FileIniZZZ exDummy = new FileIniZZZ();
//			//String[] saFlagRelevantFileIniZZZ = exDummy.getFlagZ();//Aber darin sind ja auch DEBUG, INIT, etc.
//			String[] saFlagRelevantFileIniZZZ = {"USEEXPRESSION", "USEFORMULA", "USEFORMULA_MATH", "USEJSON", "USEJSON_ARRAY", "USEJSON_MAP", "USEENCRYPTION"};
//			String[] saFlagZpassedFalse = this.getFlagZ_passable(false, true, exDummy);	
//			
//			String[] saFlagFileIniZZZ = StringArrayZZZ.remove(saFlagRelevantFileIniZZZ, saFlagZpassedFalse, true);				
//			FileIniZZZ objFileIniZZZ = new FileIniZZZ(this, this.getFileConfigKernelDirectory(), this.getFileConfigKernelName(), saFlagFileIniZZZ);
//			this.setFileConfigKernelIni(objFileIniZZZ);
			//..........................................
			
			
			 
			
			//Merke: Ziel ist, dass der ConsoleUser-Thread und der KeyPressThread "Daten" miteinander austauschen können. 
			//IConsoleUserZZZ objConsoleUser = new ConsoleUserCryptZZZ(objConsole,"DEBUG");
			IConsoleServiceZZZ objConsoleService = new ConsoleServiceSqlGeneratorZZZ(objConsoleController);
			objConsoleController.setConsoleServiceObject(objConsoleService);
			objConsoleController.start();
			
			//TODO 20230127: Weitere-Threads anbinden können, d.h. objConsole.setConsoleUserObejects(Array von IConsoleUser) 
			//               Aber: Zu bedenken ist, das wohl immer nur 1 Thread per Scanner - Klasse auf die Eingaben hören kann. 
		} catch (ExceptionZZZ e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
