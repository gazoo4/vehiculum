package sk.berops.android.fueller.dataModel;

import java.io.Serializable;
import java.util.Date;

import org.simpleframework.xml.Element;

public abstract class Record implements Serializable {
	@Element(name="comment", required=false)
	private String comment;
	@Element(name="creationDate")
	private Date creationDate;
	@Element(name="modifiedDate", required=false)
	private Date modifiedDate;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
