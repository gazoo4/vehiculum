package sk.berops.android.vehiculum.engine.calculation;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.ServiceEntry;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenServiceConsumption extends NewGenConsumption {
	private HashMap<ServiceEntry.Type, Cost> totalSTypeCost = new HashMap<>();

	public HashMap<ServiceEntry.Type, Cost> getTotalSTypeCost() {
		return totalSTypeCost;
	}

	public void setTotalSTypeCost(HashMap<ServiceEntry.Type, Cost> totalSTypeCost) {
		this.totalSTypeCost = totalSTypeCost;
	}
}
