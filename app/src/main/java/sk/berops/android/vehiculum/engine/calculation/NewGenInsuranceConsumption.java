package sk.berops.android.vehiculum.engine.calculation;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenInsuranceConsumption extends NewGenConsumption {
	private HashMap<InsuranceEntry.Type, Cost> totalITypeCost = new HashMap<>();

	public HashMap<InsuranceEntry.Type, Cost> getTotalITypeCost() {
		return totalITypeCost;
	}

	public void setTotalITypeCost(HashMap<InsuranceEntry.Type, Cost> totalITypeCost) {
		this.totalITypeCost = totalITypeCost;
	}
}
