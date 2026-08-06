package use.database.sql.generate;
import java.util.Map;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;

/**Merke:
 * Aus Komfortgründen gibt es für jede Befehlszeile die ...Stmt Variante.
 * Dies ist eine Befehlszeile mit dem "STATEMENT SEPARATOR".
 * 
 * @author Fritz Lindhauer
 *
 */
public class SqlUtilZZZ implements IConstantZZZ {	
	public static final String sSTATEMENT_SEPARATOR=";";
	
	public enum SEARCH_TYPEZZZ{
		IN,
		LIKE
	}
	
	 // Erzeugt den Spaltenstring, ignoriere die ggfs. vorhandene ID Spalte
    public static String erzeugeColumnsString(Map<String, String> aliasMap) {
        StringBuilder sb = new StringBuilder();
        for (String column : aliasMap.keySet()) {
        	if(!column.equalsIgnoreCase("\"id\"")) {
        		if (sb.length() > 0) sb.append(", ");
        		sb.append(column);
        	}
        }
        return sb.toString();
    }

    //Erzeugt den Wertstring. Gehe dabei von SQL-konformen Hochkommata für Strings aus.
    //Damit können auch Integer Werte verarbeitet werden.
    //Ignoriere die ggfs. vorhandene ID Spalte    
    public static String erzeugeValues(Map<String, String> aliasMap) {
        StringBuilder sb = new StringBuilder();
        for (String column : aliasMap.keySet()) {
        	if(!column.equalsIgnoreCase("\"id\"")) {
	            String value = aliasMap.get(column);
	            if (sb.length() > 0) sb.append(", ");
	
	            // Wenn Wert schon in einfache Hochkommata eingeschlossen ist, nicht doppelt verpacken
	            if(value!=null) {
		            if(!value.equalsIgnoreCase("NULL")) {
		            	sb.append(value);	            	          
		            }
	            }
        	}
        }
        return sb.toString();
    }
    
    //Erzeugt den Spaltenstring, setzte Hochkomma um die Werte.
    //Ignoriere die ggfs. vorhandene ID Spalte    
    public static String erzeugeValuesString(Map<String, String> aliasMap) {
        StringBuilder sb = new StringBuilder();
        for (String column : aliasMap.keySet()) {
        	if(!column.equalsIgnoreCase("\"id\"")) {
	            String value = aliasMap.get(column);
	            if (sb.length() > 0) sb.append(", ");
	
	            // Wenn Wert schon in einfache Hochkommata eingeschlossen ist, nicht doppelt verpacken
	            if (value.startsWith("'") && value.endsWith("'")) {
	                sb.append(value);
	            } else {
	            	if(value.equalsIgnoreCase("NULL")) {
	            		sb.append(value);
	            	}else {
	            		sb.append("'").append(value).append("'");
	            	}
	            }
        	}
        }
        return sb.toString();
    }
	
	 public static String toSqlValue( String sValue) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		
	    		StringBuilder sb = new StringBuilder();
	    		
	    		 // Wenn Wert schon in einfache Hochkommata eingeschlossen ist, nicht doppelt verpacken
	            if (sValue.startsWith("'") && sValue.endsWith("'")) {
	                sb.append(sValue);
	            } else {
	            	if(sValue.equalsIgnoreCase("NULL")) {
	            		sb.append(sValue);
	            	}else {
	            		sb.append("'").append(sValue).append("'");
	            	}
	            }
	            
	            sReturn = sb.toString();
	    	}
	    	return sReturn;
	    }
	    
	    public static String toSqlValue( int iValue) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		sReturn = Integer.toString(iValue); //setze also keine Hochkommatas drum
	    	}
	    	return sReturn;
	    }
	    
	    public static String toStatement(String sCommandLine) throws ExceptionZZZ {
	    	String sReturn = ";";
	    	main:{
	    		if(sCommandLine==null) break main;
	    		
	    		sReturn = sCommandLine + sSTATEMENT_SEPARATOR; //setze also keine Hochkommatas drum
	    	}
	    	return sReturn;
	    }
	    
	    public static String createSearchPath(String sSchema) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		sReturn = "SET search_path='" + sSchema + "'";
	    	}
	    	return sReturn;
	    }
	    
	    public static String createSearchPathStmt(String sSchema) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		sReturn = SqlUtilZZZ.toStatement(SqlUtilZZZ.createSearchPath(sSchema));
	    	}
	    	return sReturn;
	    }

	    
	    public static String createInsert(String sTable, String sColumns, String sValues) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		sReturn = "INSERT INTO " + sTable + " (" + sColumns + ") VALUES (" + sValues + ") ON CONFLICT DO NOTHING";
	    	}
	    	return sReturn;
	    }
	    
	    public static String createInsertStmt(String sTable, String sColumns, String sValues) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		sReturn = SqlUtilZZZ.toStatement(SqlUtilZZZ.createInsert(sTable, sColumns, sValues));
	    	}
	    	return sReturn;
	    }
	    
	    /**
	     * 
	     * 
	     * Merke:
	     Was macht SELECT 1?

Das 1 ist eigentlich beliebig. Man könnte genauso schreiben:

SELECT *

oder

SELECT uniquename

Der Inhalt der SELECT-Liste wird von EXISTS gar nicht ausgewertet. PostgreSQL interessiert sich ausschließlich dafür, ob mindestens eine Zeile zurückkommt.

Darum hat sich SELECT 1 als Konvention etabliert. Es signalisiert dem Leser:

"Ich möchte keine Daten lesen, sondern nur wissen, ob überhaupt etwas existiert."

Warum kein COUNT(*)?

Man könnte auch schreiben:

WHERE (
    SELECT COUNT(*)
    FROM academicdegree
    WHERE uniquename = 'dipxing'
) = 0;

Das liefert dasselbe Ergebnis.

Der Unterschied liegt in der Arbeitsweise:

EXISTS kann abbrechen, sobald der erste passende Datensatz gefunden wurde.
COUNT(*) muss alle passenden Datensätze zählen, bevor das Ergebnis feststeht.

Angenommen, versehentlich existieren bereits 500 Datensätze mit uniquename = 'dipxing':

EXISTS findet den ersten und beendet die Suche sofort.
COUNT(*) zählt alle 500.

Deshalb ist EXISTS in der Regel effizienter.

Warum NOT EXISTS?

Die Logik lautet:

Existiert bereits ein Datensatz mit uniquename = 'dipxing'?
    Ja  -> NOT EXISTS = FALSE -> INSERT wird nicht ausgeführt.
    Nein -> NOT EXISTS = TRUE -> INSERT wird ausgeführt.

Das WHERE entscheidet also, ob die SELECT überhaupt eine Zeile liefert. Nur wenn sie eine Zeile liefert, führt INSERT ... SELECT einen Insert aus.

Fazit

Für reine Existenzprüfungen gilt in SQL als Best Practice:

✔ EXISTS / NOT EXISTS → wenn nur interessiert, ob etwas vorhanden ist.
✔ COUNT(*) → wenn die Anzahl der Datensätze tatsächlich benötigt wird.

Da du nur wissen möchtest, ob ein uniquename bereits existiert, ist NOT EXISTS die idiomatische und meist auch performantere Lösung. 
	     
	     * 
	     * @param sTable
	     * @param sColumns
	     * @param sValues
	     * @param sColumnUnique
	     * @param sValueUnique
	     * @return
	     * @throws ExceptionZZZ
	     */
	    public static String createInsertUnique(String sTable, String sColumns, String sValues, String sColumnUnique, String sValueUnique) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		sReturn = "INSERT INTO " + sTable + " (" + sColumns + ") SELECT " + sValues
	    				+ " WHERE NOT EXISTS (SELECT 1 FROM " + sTable
	    				+ " WHERE " + sColumnUnique + " = " + SqlUtilZZZ.toSqlValue(sValueUnique) + " )"
	    				+ " ON CONFLICT DO NOTHING";
	    	}
	    	return sReturn;
	    }
	    
	    public static String createInsertUniqueStmt(String sTable, String sColumns, String sValues, String sColumnUnique, String sValueUnique) throws ExceptionZZZ {
	    	String sReturn = null;
	    	main:{
	    		sReturn = SqlUtilZZZ.toStatement(SqlUtilZZZ.createInsertUnique(sTable, sColumns, sValues, sColumnUnique, sValueUnique));
	    	}
	    	return sReturn;
	    }

		/** z.B. Ergebnis
		 *  select id from academicdegree where uniquename in ('diplxing')
		 *  
		 *  
		 * @param sSelectColumn
		 * @param sWhereTable
		 * @param sWhereColumn
		 * @param sWhereValue
		 * @return
		 * @throws ExceptionZZZ
		 */
		public static String createSelectConditioned(String sSelectColumn, String sSelectTable, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
	    		sReturn = "SELECT " + sSelectColumn + " FROM " + sSelectTable + " WHERE " + sWhereColumn + " IN ( " + SqlUtilZZZ.toSqlValue(sWhereSingleValue) + " )";
	    	}
	    	return sReturn;
		}

		public static String createSelectConditionedStmt(String sSelectColumn, String sSelectTable, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
	    		sReturn = SqlUtilZZZ.toStatement(SqlUtilZZZ.createSelectConditioned(sSelectColumn, sSelectTable, sWhereColumn, sWhereSingleValue));
	    	}
	    	return sReturn;
		}
		
		
		public static String createUpdateConditioned(SEARCH_TYPEZZZ enumSearchType, String sTable, String sColumn, String sSingleValue, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
				if(enumSearchType == SEARCH_TYPEZZZ.IN) {
					sReturn = createUpdateConditioned_IN(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue);
				}else if(enumSearchType == SEARCH_TYPEZZZ.LIKE) {
					sReturn = createUpdateConditioned_LIKE(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue);
				}else {
					ExceptionZZZ ez = new ExceptionZZZ("Nicht behandelter SEARCH_TYPEZZZ" + enumSearchType.name(), iERROR_PARAMETER_VALUE, SqlUtilZZZ.class, ReflectCodeZZZ.getPositionCurrent());
					throw ez;
				}	    		
	    	}
	    	return sReturn;
		}
		
		public static String createUpdateConditionedStmt(SEARCH_TYPEZZZ enumSearchType, String sTable, String sColumn, String sSingleValue, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
				if(enumSearchType == SEARCH_TYPEZZZ.IN) {
					sReturn = createUpdateConditionedStmt_IN(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue);
				}else if(enumSearchType == SEARCH_TYPEZZZ.LIKE) {
					sReturn = createUpdateConditionedStmt_LIKE(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue);
				}else {
					ExceptionZZZ ez = new ExceptionZZZ("Nicht behandelter SEARCH_TYPEZZZ" + enumSearchType.name(), iERROR_PARAMETER_VALUE, SqlUtilZZZ.class, ReflectCodeZZZ.getPositionCurrent());
					throw ez;
				}	    		
	    	}
	    	return sReturn;
		}
		
		/**
		 *  z.B. Ergebnis
		 * 	update course_of_study set academicdegree_id = (select id from academicdegree where uniquename in ('diplxing'))
			where uniquename LIKE '11|032|-|-|H|%|0390|P|V|%|'

		 * @param sTable
		 * @param sColumns
		 * @param sValues
		 * @param sCondition
		 * @return
		 * @throws ExceptionZZZ
		 */
		public static String createUpdateConditioned_LIKE(String sTable, String sColumn, String sSingleValue, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
	    		sReturn = "UPDATE " + sTable + " SET " + sColumn + " = " + sSingleValue + " WHERE " + sWhereColumn + " LIKE " + SqlUtilZZZ.toSqlValue(sWhereSingleValue) + "";
	    	}
	    	return sReturn;
		}
		
		public static String createUpdateConditionedStmt_LIKE(String sTable, String sColumn, String sSingleValue, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
	    		sReturn = SqlUtilZZZ.toStatement(SqlUtilZZZ.createUpdateConditioned_LIKE(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue));
	    		}
	    	return sReturn;
		}
		
		public static String createUpdateConditioned_IN(String sTable, String sColumn, String sSingleValue, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
	    		sReturn = "UPDATE " + sTable + " SET " + sColumn + " = " + sSingleValue + " WHERE " + sWhereColumn + " IN ( " + SqlUtilZZZ.toSqlValue(sWhereSingleValue) + " )";
	    	}
	    	return sReturn;
		}
		
		public static String createUpdateConditionedStmt_IN(String sTable, String sColumn, String sSingleValue, String sWhereColumn, String sWhereSingleValue) throws ExceptionZZZ {
			String sReturn = null;
	    	main:{
	    		sReturn = SqlUtilZZZ.toStatement(SqlUtilZZZ.createUpdateConditioned_IN(sTable, sColumn, sSingleValue, sWhereColumn, sWhereSingleValue));
	    		}
	    	return sReturn;
		}

}
