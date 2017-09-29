package sk.berops.android.vehiculum.engine.calculation;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.charting.MaintenanceCharter;
import sk.berops.android.vehiculum.dataModel.charting.PieCharter;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.MaintenanceEntry;

/**
 * @author Bernard Halas
 * @date 8/15/17
 */

public class NewGenMaintenanceConsumption extends NewGenConsumption {
	/**
	 * Total cost per maintenance type
	 */
	private HashMap<MaintenanceEntry.Type, Cost> totalMTypeCost;
	/**
	 * Total labor cost
	 */
	private Cost totalLaborCost;
	/**
	 * Total parts cost
	 */
	private Cost totalPartsCost;

	public PieCharter generateCharter() {
		return new MaintenanceCharter(this);
	}

	public HashMap<MaintenanceEntry.Type, Cost> getTotalMTypeCost() {
		return totalMTypeCost;
	}

	public void setTotalMTypeCost(HashMap<MaintenanceEntry.Type, Cost> totalMTypeCost) {
		this.totalMTypeCost = totalMTypeCost;
	}

	public Cost getTotalLaborCost() {
		return totalLaborCost;
	}

	public void setTotalLaborCost(Cost totalLaborCost) {
		this.totalLaborCost = totalLaborCost;
	}

	public Cost getTotalPartsCost() {
		return totalPartsCost;
	}

	public void setTotalPartsCost(Cost totalPartsCost) {
		this.totalPartsCost = totalPartsCost;
	}
}
