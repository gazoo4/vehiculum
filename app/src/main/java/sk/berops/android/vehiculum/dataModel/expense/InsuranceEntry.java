package sk.berops.android.vehiculum.dataModel.expense;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.vehiculum.dataModel.calculation.InsuranceConsumption;
import sk.berops.android.vehiculum.engine.synchronization.controllers.InsuranceEntryController;

public class InsuranceEntry extends Entry {
	@Element(name = "type", required = false)
	private Type type;

	public InsuranceEntry() {
		super();
		this.setExpenseType(ExpenseType.INSURANCE);
		setType(Type.BASIC);
	}
	
	public enum Type {
		BASIC(0, "basic", 0xFF2C712C),
		EXTENDED(2, "extended", 0xFF4DA1AE),
		FULL(3, "full", 0xFFDF6D2B),
		OTHER(4, "other", 0xFFB74BCF);
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

	public InsuranceConsumption generateConsumption() {
		return new InsuranceConsumption();
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public InsuranceEntryController getController() {
		return new InsuranceEntryController(this);
	}
}
