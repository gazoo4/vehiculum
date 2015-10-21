package sk.berops.android.fueller.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

public class TollEntry extends Entry {
	@Element(name="type")
	private Type type;
	
	public TollEntry() {
		super();
		this.setExpenseType(Entry.ExpenseType.TOLL);
	}
	
	public enum Type {
		FERRY(0, "ferry"),
		TOLL_ROAD(1, "toll road"),
		OTHER(2, "other fee");
		private int id;
		private String type;	
		Type(int id, String type) {
			this.setId(id);
			this.setType(type);
		}
		
		private static Map<Integer, Type> idToTypeMapping;

		public static Type getType(int id) {
			if (idToTypeMapping == null) {
				initMapping();
			}
			
			Type result = null;
			result = idToTypeMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToTypeMapping = new HashMap<Integer, Type>();
			for (Type type : values()) {
				idToTypeMapping.put(type.id, type);
			}
		}
	
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
