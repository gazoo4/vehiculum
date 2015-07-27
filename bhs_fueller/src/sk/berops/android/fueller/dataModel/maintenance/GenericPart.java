package sk.berops.android.fueller.dataModel.maintenance;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart.Originality;

public abstract class GenericPart extends GenericItem {
	@Element(name="condition")
	private Condition condition;
	@Element(name="quantity", required=false)
	protected int quantity;
	
	public enum Condition{
		NEW(0, "new"),
		USED(1, "used"),
		REFUBRISHED(2, "refubrished");
		private int id;
		private String condition;	
		Condition(int id, String condition) {
			this.setId(id);
			this.setCondition(condition);
		}
		
		private static Map<Integer, Condition> idToConditionMapping;

		public static Condition getCondition(int id) {
			if (idToConditionMapping == null) {
				initMapping();
			}
			
			Condition result = null;
			result = idToConditionMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToConditionMapping = new HashMap<Integer, Condition>();
			for (Condition condition : values()) {
				idToConditionMapping.put(condition.id, condition);
			}
		}
	
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
