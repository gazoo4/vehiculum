package sk.berops.android.vehiculum.dataModel.expense;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.charting.MaintenanceCharter;
import sk.berops.android.vehiculum.dataModel.charting.PieCharter;
import sk.berops.android.vehiculum.dataModel.maintenance.ReplacementPart;
import sk.berops.android.vehiculum.engine.calculation.NewGenMaintenanceConsumption;
import sk.berops.android.vehiculum.engine.synchronization.controllers.MaintenanceEntryController;

public class MaintenanceEntry extends Entry {
	@Element(name="type")
	private Type type;
	@Element(name="laborCost", required=false)
	private Cost laborCost;
	@ElementList(inline=true, required=false)
	private LinkedList<ReplacementPart> parts;
	
	public MaintenanceEntry() {
		super();
		this.setExpenseType(Entry.ExpenseType.MAINTENANCE);
		this.setParts(new LinkedList<ReplacementPart>());
	}
	
	public enum Type{
		PLANNED(0, "planned", 0xFFABD14C),
		UNPLANNED(1, "unplanned", 0xFF4CB0D1),
		ACCIDENT_REPAIR(2, "accident repair", 0xFFD14CB2);
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
			idToTypeMapping = new HashMap<Integer, MaintenanceEntry.Type>();
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
	
	public Cost getPartsCost() {
		if ((getParts() == null)
			|| (getParts().size() == 0)) {
			return new Cost();
		}

		HashSet<Cost> costs = new HashSet<>();
		getParts().stream()
				.forEach(p -> costs.add(Cost.multiply(p.getCost(), p.getQuantity())));

		return Cost.sum(costs);
	}
	
	public int compareTo(MaintenanceEntry e) {
		return super.compareTo(e);
	}
	
	public Cost getLaborCost() {
		return laborCost;
	}
	public void setLaborCost(Cost laborCost) {
		this.laborCost = laborCost;
	}

	public LinkedList<ReplacementPart> getParts() {
		return parts;
	}
	public void setParts(LinkedList<ReplacementPart> parts) {
		this.parts = parts;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public NewGenMaintenanceConsumption getMaintenanceConsumption() {
		return (NewGenMaintenanceConsumption) this.getConsumption();
	}

	public NewGenMaintenanceConsumption generateConsumption() {
		return new NewGenMaintenanceConsumption();
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public MaintenanceEntryController getController() {
		return new MaintenanceEntryController(this);
	}

	/****************************** Searchable interface methods follow ***************************/

	/**
	 * Method used to search for an object by its UUID within the Object tree of this Object.
	 * @param uuid of the searched object
	 * @return Record that matches the searched UUID
	 */
	public Record getRecordByUUID(UUID uuid) {
		// Are they looking for me? Delegate task to Record.getRecordByUUID to find out.
		Record result = super.getRecordByUUID(uuid);

		if (result == null) {
			result = laborCost.getRecordByUUID(uuid);
		}

		Iterator<ReplacementPart> p = parts.iterator();

		// Search deeper to find the right object
		while (result == null && p.hasNext()) {
			result = p.next().getRecordByUUID(uuid);
		}

		return result;
	}

	/****************************** PieChartable interface methods follow *************************/

	public PieCharter getPieCharter() {
		return (charter == null) ? generatePieCharter() : charter;
	}

	public MaintenanceCharter generatePieCharter() {
		return new MaintenanceCharter(getMaintenanceConsumption());
	}
}
