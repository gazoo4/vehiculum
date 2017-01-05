package sk.berops.android.fueller.dataModel;

import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Record implements Serializable, Identifiable {
	@Element(name="comment", required=false)
	private String comment;
	@Element(name="creationDate")
	private Date creationDate; 
	@Element(name="modifiedDate", required=false)
	private Date modifiedDate;
	@Element(name="uuid", required=false)
	private UUID uuid;

	/**
	 * Constructor
	 */
	public Record() {
		setCreationDate(new Date());
	}

	/**
	 * Copy constructor
	 * @param record
	 */
	public Record(Record record) {
		this.comment = record.comment;
		this.creationDate = record.creationDate;
		this.modifiedDate = record.modifiedDate;
		// When using copy constructor, we want to use new UUID for the newly created object.
		this.uuid = getUuid();
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

	public UUID getUuid() {
		if (uuid == null) {
			uuid = UUID.randomUUID();
		}
		return uuid;
	}
}