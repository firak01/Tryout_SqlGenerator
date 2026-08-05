package use.database.sql.generate.courseOfStudy;

public class AcademicDegreeTitle {
	private String sStg = null;
	private String sAbschl = null;
	private String sVert = null;
	
	
	private String sTextDefault=null;
	private String sTextDefault_female=null;
	
	private String sTextLong=null;
	private String sTextLong_female=null;
	
	//statische Werte
	private String sUniquename;
	private int iK_language_id;	
	private String sObj_guid;
	
	//statische Werte wg. Constrainsts
	private int iPosition_of_title;
	

	//### GETTER / SETTER
	public String getStudiengang() {
		return sStg;
	}
	public void setStudiengang(String sStg) {
		this.sStg = sStg;
	}
	
	public String getAbschluss() {
		return sAbschl;
	}
	public void setAbschluss(String sAbschl) {
		this.sAbschl = sAbschl;
	}
	
	public String getVertiefung() {
		return sVert;
	}
	public void setVertiefung(String sVert) {
		this.sVert = sVert;
	}
	
	public String getDefaulttext() {
		return sTextDefault;
	}
	public void setDefaulttext(String sTextDefault) {
		this.sTextDefault = sTextDefault;
	}
	
	public String getDefaulttext_female() {
		return sTextDefault_female;
	}
	public void setDefaulttext_female(String sTextDefault_female) {
		this.sTextDefault_female = sTextDefault_female;
	}
	
	public String getLongtext() {
		return sTextLong;
	}
	public void setLongtext(String sTextLong) {
		this.sTextLong = sTextLong;
	}
	
	public String getLongtext_female() {
		return sTextLong_female;
	}
	public void setLongtext_female(String sTextLong_female) {
		this.sTextLong_female = sTextLong_female;
	}
	
	//Statische Werte
	public int getK_language_id() {
		return iK_language_id;
	}
	public void setK_language_id(int sK_language_id) {
		this.iK_language_id = sK_language_id;
	}
	
	public String getUniquename() {
		return sUniquename;
	}
	public void setUniquename(String sUniquename) {
		this.sUniquename = sUniquename;
	}
	
	public String getObj_guid() {
		return sObj_guid;
	}
	public void setObj_guid(String sObj_guid) {
		this.sObj_guid = sObj_guid;
	}
	
	//Statische Werte, wg. Constraints
	public int getPosition_of_title() {
		return iPosition_of_title;
	}
	public void setPosition_of_title(int iPosition_of_title) {
		this.iPosition_of_title = iPosition_of_title;
	}

}
