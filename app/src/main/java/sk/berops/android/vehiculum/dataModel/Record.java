package sk.berops.android.vehiculum.dataModel;

import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import sk.berops.android.vehiculum.engine.Searchable;
import sk.berops.android.vehiculum.engine.synchronization.controllers.RecordController;

public class Record implements Serializable, Identifiable, Searchable {
	// largest signed integer (31bit) prime
	protected static int EMPTY_RECORD_HASH_CODE = 0x7FFFFFFF;

	/**
	 * Define an empty Record container, so that in case an axle doesn't have wheels installed,
	 * we don't use "null" references as these are not synchronized during the updates
	 */
	public static final Record NULL;
	static {
		NULL = new Record();
		NULL.setEmpty(true);
	}

	@Element(name="empty", required=false)
	private boolean empty;
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
		setUuid(getUuid());
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

	public RecordController getController() {
		return new RecordController(this);
	}

	/**
	 * Override method hashCode
	 * @return hashcode of this object
	 */
	@Override
	public int hashCode() {
		if (isEmpty()) {
			// all the empty Records should return this value as we don't distinguish between empty
			// car or empty garage; this depends on the context
			return EMPTY_RECORD_HASH_CODE;
		}
		return uuid.hashCode();
	}

	/**
	 * Overriden method equals for comparing 2 Records
	 * @param obj
	 * @return true is objects are equal. Otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		// Basic checks
		if (obj == null) {
			return false;
		}
		if (!Record.class.isAssignableFrom(obj.getClass())) {
			return false;
		}


		final Record other = (Record) obj;
		if (this.isEmpty() && other.isEmpty()) {
			return true;
		}
		if ((this.uuid == null) ? (other.uuid != null) : (!this.uuid.equals(other.uuid))) {
			return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
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

	private void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		if (uuid == null) {
			generateNewUuid();
		}
		return uuid;
	}

	public void generateNewUuid() {
		uuid = UUID.randomUUID();
	}

	/****************************** Searchable interface methods follow ***************************/

	/**
	 * Method used to search for an object by its UUID within the Object tree of this Object.
	 * @param uuid of the searched object
	 * @return Record that matches the searched UUID
	 */
	public Record getRecordByUUID(UUID uuid) {
		if (this.getUuid().equals(uuid)) {
			return this;
		} else {
			return null;
		}
	}
}