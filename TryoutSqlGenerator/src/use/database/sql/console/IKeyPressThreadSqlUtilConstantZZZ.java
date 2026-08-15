package use.database.sql.console;

import basic.zBasic.util.console.multithread.IKeyPressThreadConstantZZZ;
import basic.zBasic.util.crypt.code.ICharacterPoolEnabledZZZ;
import basic.zBasic.util.crypt.code.IROTUserZZZ;

/**Konstanten, mit denen Werte in einer HashMap gespeichert werden.
 * Die HashMap wird verwendet um die Daten zwischen Threads auszutauschen.
 * @author Fritz Lindhauer, 22.01.2023, 09:21:59
 * 
 */
public interface IKeyPressThreadSqlUtilConstantZZZ extends IKeyPressThreadConstantZZZ{
	public static String sINPUT_CIPHER = "INPUT_CIPHER";            //Der Verschluesselungsalgorithmus wird mit diesem Schluessel in der HashMap gespeichert
	public static String sINPUT_KEY_NUMERIC = "INPUT_KEY_NUMERIC";  //Schluesselwert, z.B. fuer ROT Verschluesselung
	public static String sINPUT_KEY_STRING = "INPUT_KEY_STRING";    //Schluesselwort, z.B. fuer Vigenere Verschlüsselung
	public static String sINPUT_TEXT_UNCRYPTED = "INPUT_TEXT_UNCRYPTED";
	public static String sINPUT_TEXT_ENCRYPTED = "INPUT_TEXT_ENCRYPTED";
	public static String sINPUT_CHARACTERPOOL = "INPUT_CHARACTERPOOL";
	public static String sINPUT_CHARACTERPOOL_ADDITIONAL = "INPUT_CHARACTERPOOL_ADDITIONAL";
	public static String sINPUT_BOOLEAN_SKIP_ARGUMENTS = "INPUT_BOOLEAN_SKIP_ARGUMENTS";
	
	//Merke: Mit "INPUT_FLAG" werden Eingabewerte gekennzeichnet, die später dann als Flag den Objekten übergeben werden können.	
	public static String sINPUT_FLAG_USE_STRATEGY_CASECHANGE = IKeyPressThreadConstantZZZ.sINPUT_FLAG + IROTUserZZZ.FLAGZ.USESTRATEGY_CASECHANGE.name();
	public static String sINPUT_FLAG_USE_STRATEGY_CHARACTERPOOL = IKeyPressThreadConstantZZZ.sINPUT_FLAG + ICharacterPoolEnabledZZZ.FLAGZ.USESTRATEGY_CHARACTERPOOL.name();
	
	public static String sINPUT_FLAG_CHARACTER_BLANK = IKeyPressThreadConstantZZZ.sINPUT_FLAG + IROTUserZZZ.FLAGZ.USEBLANK.name();
	public static String sINPUT_FLAG_CHARACTER_UPPERCASE = IKeyPressThreadConstantZZZ.sINPUT_FLAG + ICharacterPoolEnabledZZZ.FLAGZ.USEUPPERCASE.name();
	public static String sINPUT_FLAG_CHARACTER_LOWERCASE = IKeyPressThreadConstantZZZ.sINPUT_FLAG + ICharacterPoolEnabledZZZ.FLAGZ.USELOWERCASE.name();
	public static String sINPUT_FLAG_CHARACTER_NUMERIC = IKeyPressThreadConstantZZZ.sINPUT_FLAG + ICharacterPoolEnabledZZZ.FLAGZ.USENUMERIC.name();		
	public static String sINPUT_FLAG_CHARACTER_ADDITIONAL = IKeyPressThreadConstantZZZ.sINPUT_FLAG + ICharacterPoolEnabledZZZ.FLAGZ.USEADDITIONALCHARACTER.name();
	
	public static String sOUTPUT_TEXT_ENCRYPTED = "OUTPUT_TEXT_ENCRYPTED";   
	public static String sOUTPUT_TEXT_UNCRYPTED = "OUTPUT_TEXT_UNCRYPTED";
	public static String sOUTPUT_TEXT_DECRYPTED = "OUTPUT_TEXT_DECRYPTED";
	
    
}
