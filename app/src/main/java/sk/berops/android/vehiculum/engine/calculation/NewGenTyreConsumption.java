package sk.berops.android.vehiculum.engine.calculation;

import java.util.HashMap;

import sk.berops.android.vehiculum.dataModel.charting.PieCharter;
import sk.berops.android.vehiculum.dataModel.charting.TyreCharter;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenTyreConsumption extends NewGenConsumption {
	private HashMap<Tyre.Season, Cost> tyreCost = new HashMap<>();
	private Cost materialCost;
	private Cost laborCost;

	public PieCharter generateCharter() {
		return new TyreCharter(this);
	}

	public Cost getTotalTyreCost() {
		Cost cost = new Cost();
		for (Tyre.Season s: tyreCost.keySet()) {
			cost = Cost.add(cost, tyreCost.get(s));
		}
		return cost;
	}

	public HashMap<Tyre.Season, Cost> getTyreCost() {
		return tyreCost;
	}

	public void setTyreCost(HashMap<Tyre.Season, Cost> tyreCost) {
		this.tyreCost = tyreCost;
	}

	public Cost getMaterialCost() {
		return materialCost;
	}

	public void setMaterialCost(Cost materialCost) {
		this.materialCost = materialCost;
	}

	public Cost getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(Cost laborCost) {
		this.laborCost = laborCost;
	}
}
