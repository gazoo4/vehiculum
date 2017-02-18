package sk.berops.android.vehiculum.dataModel.maintenance;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.vehiculum.dataModel.expense.Expense;

public abstract class GenericPart extends Expense {
	@Element(name="condition")
	private Condition condition;
	@Element(name="brand", required=false)
	private String brand;
	@Element(name="producerPartID", required=false)
	private String producerPartID;
	@Element(name="carmakerPartID", required=false)
	private String carmakerPartID;

	public enum Condition {
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

	/**
	 * Constructor
	 */
	public GenericPart() {
		super();
	}

	/**
	 * Copy constructor
	 * @param genericPart
	 */
	public GenericPart(GenericPart genericPart) {
		super(genericPart);
		this.condition = genericPart.condition;
		this.brand = genericPart.brand;
		this.producerPartID = genericPart.producerPartID;
		this.carmakerPartID = genericPart.carmakerPartID;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getProducerPartID() {
		return producerPartID;
	}
	public void setProducerPartID(String producerPartID) {
		this.producerPartID = producerPartID;
	}
	public String getCarmakerPartID() {
		return carmakerPartID;
	}
	public void setCarmakerPartID(String carmakerPartID) {
		this.carmakerPartID = carmakerPartID;
	}
}
