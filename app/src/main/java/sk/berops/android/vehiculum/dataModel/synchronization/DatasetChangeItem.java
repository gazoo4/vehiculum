package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;

/**
 * This class represents an information update done on the garage dataset. This information is usually used
 * for updating other parties with the changes done on the data in order to have it synchronized.
 *
 * @author Bernard Halas
 * @date 5/22/17
 */

public abstract class DatasetChangeItem {
	protected ChangeType changeType;

	public enum ChangeType {
		CREATE(0, "create", DataCreate.class),
		UPDATE(1, "update", DataUpdate.class),
		DELETE(2, "delete", DataDelete.class);

		private static Map<Integer, ChangeType> idToChangeTypeMapping;
		private int id;
		private String changeType;
		private Class aClass;

		ChangeType(int id, String changeType, Class aClass) {
			this.setId(id);
			this.setChangeType(changeType);
			this.setAClass(aClass);
		}

		public static ChangeType getChangeType(int id) {
			if (idToChangeTypeMapping == null) {
				initMapping();
			}

			ChangeType result = null;
			result = idToChangeTypeMapping.get(id);
			return result;
		}

		private static void initMapping() {
			idToChangeTypeMapping = new HashMap<Integer, ChangeType>();
			for (ChangeType ChangeType : values()) {
				idToChangeTypeMapping.put(ChangeType.id, ChangeType);
			}
		}

		public String getChangeType() {
			return changeType;
		}

		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Class<DatasetChangeItem> getAClass() {
			return aClass;
		}

		public void setAClass(Class<DatasetChangeItem> aClass) {
			this.aClass = aClass;
		}
	}

	public abstract UUID getBaseRecordUUID();
	
	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public String toString() {
		String result = "Parent: " + getBaseRecordUUID().toString() + " & Action: " + changeType.toString();
		return result;
	}
}
