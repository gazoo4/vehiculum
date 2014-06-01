package sk.berops.android.fueller.dataModel.expense;

import java.util.LinkedList;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Part;


public class MaintenanceEntry extends Entry {
	@Element(name="regularMaintenance")
	private boolean regularMaintenance;//or crash maintenance?
	@Element(name="laborCost", required=false)
	private double laborCost;
	@Element(name="parts", required=false)
	private LinkedList<Part> replacedParts;
	public MaintenanceEntry() {
		super();
		this.setExpenseType(Entry.ExpenseType.MAINTENANCE);
	}
	public int compareTo(MaintenanceEntry e) {
		return super.compareTo(e);
	}
	public boolean isRegularMaintenance() {
		return regularMaintenance;
	}
	public void setRegularMaintenance(boolean regularMaintenance) {
		this.regularMaintenance = regularMaintenance;
	}
	public double getLaborCost() {
		return laborCost;
	}
	public void setLaborCost(double laborCost) {
		this.laborCost = laborCost;
	}
	public LinkedList<Part> getReplacedParts() {
		return replacedParts;
	}
	public void setReplacedParts(LinkedList<Part> replacedParts) {
		this.replacedParts = replacedParts;
	}
}
