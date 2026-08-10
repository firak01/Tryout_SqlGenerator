package use.database.sql.generate.courseOfStudy;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import use.database.sql.generate.common.AcademicDegreeTitle;

public class CourseOfStudy_with_AcademicDegree implements IConstantZZZ {
	//Merke aus HISinOne Datei zur Migration gx-hisinone.xml stammt das Template
	//<param name="signatureTemplate" value="[abschl]|[stg]|[vert]|[schwp]|[kzfa]|[pversion]" />
	
	//Aber Ziel ist folgendes SQL, man sieht, das geht über die MigrationsTemplate Signatur hinaus:
	//update course_of_study set academicdegree_id = (select id from academicdegree where uniquename in ('diplxing'))
	//		where uniquename LIKE '11|032|-|-|H|%|0390|P|V|%|'
	
	public static final String sSIGNATURE_TEMPLATE_FOR_SEARCH = "[abschl]|[stg]|[vert]|-|H|%|0390|P|V|%|";
	//Merke: Schwerpunkte gibt es nicht, daher '-' an der Stelle, H = Hauptstudium,  
	//       % an der Stelle der Prüfungsordnungsversion, da die Zuordnung des AcademicDegree für alle Prüfungsordnungsversionen gelten soll
	//       '0390' ist die Prüfungsamtsnummer 
	//       P,V ???
	//       % an der Stelle des Startsemesters, da die Zuordnung des AcademicDegree für alle Startsemester gelten soll
	
	private AcademicDegreeTitle objAcademicDegree = null;
	private String sSignatureTemplateForSearch = null;
	private String sAbschl = null;
	private String sStg = null;
	private String sVert = null;
	
	private String sAcademicDegreeUniquename = null;
	

	//### GETTER / SETTER
	public AcademicDegreeTitle getAcademicDegreeObject() {
		return objAcademicDegree;
	}
	public void setAcademicDegreeObject(AcademicDegreeTitle objAcademicDegree) {
		this.objAcademicDegree = objAcademicDegree;
	}
	
	public String getStudiengang() throws ExceptionZZZ {
		if(StringZZZ.isEmptyNull(this.sStg)) {
			String sStg = this.readStudiengang();
			sStg=sStg.trim();
			if(StringZZZ.isEmpty(sStg)) {
				sStg = "%";
			}
			return sStg;
		}else {
			return this.sStg;
		}					
	}
	public void setStudiengang(String sStg) {
		this.sStg = sStg;
	}
	
	public String getAbschluss() throws ExceptionZZZ {
		if(StringZZZ.isEmptyNull(this.sAbschl)) {
			String sAbschl = this.readAbschluss();
			sAbschl=sAbschl.trim();
			if(StringZZZ.isEmptyTrimmed(sAbschl)) {
				sAbschl = "%";
			}
			return sAbschl;
		}else {
			return this.sAbschl;
		}	
	}
	public void setAbschluss(String sAbschl) {
		this.sAbschl = sAbschl;
	}
	
	public String getVertiefung() throws ExceptionZZZ {
		if(StringZZZ.isEmptyNull(this.sVert)) {
			String sVert = this.readVertiefung();
			sVert = sVert.trim();
			if(StringZZZ.isEmpty(sVert)) {
				sVert = "%";
			}
			return sVert;
		}else {
			return this.sVert;
		}	
	}
	public void setVertiefung(String sVert) {
		this.sVert = sVert;
	}
	
	public String getAcademicDegreeUniquename() throws ExceptionZZZ {
		if(StringZZZ.isEmpty(this.sAcademicDegreeUniquename)) {
			return this.readAcademicdegreeUniquename();
		}else {
			return this.sAcademicDegreeUniquename;
		}	
	}
	public void setAcademicDegreeUniquename(String sAcademicDegreeUniquename) {
		this.sAcademicDegreeUniquename = sAcademicDegreeUniquename;
	}
	
	public static String getSignatureTemplateForSearchDefault() {
		return sSIGNATURE_TEMPLATE_FOR_SEARCH;
	}
	

	//Nur so als Idee, falls eine Suchsignatur mal veränderbar sein sollte. SETTER fehlt noch.
	public String getSignatureTemplateForSearch() throws ExceptionZZZ {
		if(StringZZZ.isEmpty(this.sSignatureTemplateForSearch)) {
			return getSignatureTemplateForSearchDefault();
		}else {
			return this.sSignatureTemplateForSearch;
		}
	}
	

	//### METHODEN ###############
	public String readStudiengang() throws ExceptionZZZ {
		AcademicDegreeTitle objAcademicDegree = this.getAcademicDegreeObject();
		return objAcademicDegree.getStudiengang();
	}
	
	public String readAbschluss() throws ExceptionZZZ {
		AcademicDegreeTitle objAcademicDegree = this.getAcademicDegreeObject();
		return objAcademicDegree.getAbschluss();
	}
	
	public String readVertiefung() throws ExceptionZZZ {
		AcademicDegreeTitle objAcademicDegree = this.getAcademicDegreeObject();
		return objAcademicDegree.getVertiefung();
	}
	
	public String readAcademicdegreeUniquename() throws ExceptionZZZ {
		AcademicDegreeTitle objAcademicDegree = this.getAcademicDegreeObject();
		return objAcademicDegree.getUniquename();
	}
	
	public String createUniquenameForSearch() throws ExceptionZZZ {
		String sReturn = null;
		main:{
			String sAbschl=this.getAbschluss();
			String sStg=this.getStudiengang();
			String sVert=this.getVertiefung();
			String sSignatureTemplateForSearch=this.getSignatureTemplateForSearch();
			sReturn = CourseOfStudy_with_AcademicDegree.createUniquenameForSearch(sAbschl,sStg,sVert, sSignatureTemplateForSearch);
		}//end main:
		return sReturn;
	}



	//#### STATIC METHODEN ###############
	public static String createUniquenameForSearch(String sAbschl, String sStg, String sVertIn, String sSignatureTemplateForSearchIn) throws ExceptionZZZ{
		String sReturn = null;
		main:{
			if(StringZZZ.isEmpty(sAbschl)) {
				ExceptionZZZ ez = new ExceptionZZZ("Abschluss", iERROR_PARAMETER_MISSING, CourseOfStudy_with_AcademicDegree.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
			if(StringZZZ.isEmpty(sStg)) {
				ExceptionZZZ ez = new ExceptionZZZ("Studiengang", iERROR_PARAMETER_MISSING, CourseOfStudy_with_AcademicDegree.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
			String sVert;
			if(StringZZZ.isEmpty(sVertIn)) {
				sVert = "-";
			}else {
				sVert = sVertIn;								
			}
			
			String sSignatureTemplateForSearch;
			if(StringZZZ.isEmpty(sSignatureTemplateForSearchIn)) {
				sSignatureTemplateForSearch = sSIGNATURE_TEMPLATE_FOR_SEARCH;
			}else {
				sSignatureTemplateForSearch = sSignatureTemplateForSearchIn;								
			}
	
			sSignatureTemplateForSearch = StringZZZ.replace(sSignatureTemplateForSearch, "[abschl]", sAbschl);		
			sSignatureTemplateForSearch = StringZZZ.replace(sSignatureTemplateForSearch, "[stg]", sStg);			
			sSignatureTemplateForSearch = StringZZZ.replace(sSignatureTemplateForSearch, "[vert]", sVert);
			
			sReturn = sSignatureTemplateForSearch;
		}//end main;
		return sReturn;
	}
}
