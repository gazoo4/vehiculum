package sk.berops.android.fueller.dataModel;

import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.Date;

public abstract class Record implements Serializable, Identifiable {
	@Element(name="comment", required=false)
	private String comment;
	@Element(name="creationDate")
	private Date creationDate; 
	@Element(name="modifiedDate", required=false)
	private Date modifiedDate;
	@Element(name="id", required=false)
	private long id;

	private long generatedId() {
		return (new java.util.Random()).nextLong();
	}

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
	public long getId() {
		if (id == 0) {
			id = generatedId();
		}
		return id;
	}
}