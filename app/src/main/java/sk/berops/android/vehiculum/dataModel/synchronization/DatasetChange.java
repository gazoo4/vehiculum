package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an information update done on the garage dataset. This information is usually used
 * for updating other parties with the changes done on the data in order to have it synchronized.
 *
 * @author Bernard Halas
 * @date 5/22/17
 */

public abstract class DatasetChange {
	protected ChangeType changeType;

	public enum ChangeType {
		CREATE(0, "create"),
		UPDATE(1, "update"),
		DELETE(2, "delete");

		private static Map<Integer, ChangeType> idToChangeTypeMapping;
		private int id;
		private String changeType;

		ChangeType(int id, String changeType) {
			this.setId(id);
			this.setChangeType(changeType);
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
	}
	
	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}
}
