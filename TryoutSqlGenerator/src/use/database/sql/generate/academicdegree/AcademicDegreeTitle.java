package use.database.sql.generate.academicdegree;

public class AcademicDegreeTitle {
	private String sTextDefault=null;
	private String sTextDefault_female=null;
	
	private String sTextLong=null;
	private String sTextLong_female=null;
	
	//### GETTER / SETTER
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

}
