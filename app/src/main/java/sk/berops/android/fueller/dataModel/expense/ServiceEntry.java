package sk.berops.android.fueller.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

public class ServiceEntry extends Entry {
	@Element(name="type")
	private Type type;
	
	public ServiceEntry() {
		super();
		this.setExpenseType(Entry.ExpenseType.SERVICE);
	}
	
	public enum Type {
		TOWING(0, "towing service", 0xFFDAA92D),
		REPLACEMENT_VEHICLE(1, "replacement vehicle", 0xFF862DDA),
		PARKING_(2, "parking", 0xFF2D78DA),
		OTHER(3, "other service", 0xFF767676);
		private int id;
		private String type;
		private int color;

		Type(int id, String type) {
			this(id, type, (int) (Math.random() * Integer.MAX_VALUE) | 0xFF000000);
		}

		Type(int id, String type, int color) {
			this.setId(id);
			this.setType(type);
			this.setColor(color);
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

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
