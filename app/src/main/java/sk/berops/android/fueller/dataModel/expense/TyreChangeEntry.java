package sk.berops.android.fueller.dataModel.expense;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;
import sk.berops.android.fueller.gui.MainActivity;

public class TyreChangeEntry extends Entry {
	
	private Car car;
	
	@Element(name="laborCost", required=false)
	private double laborCost;

	@Element(name="extraMaterialCost", required=false)
	private double extraMaterialCost;

	@Element(name="tyresCost", required=false)
	private double tyresCost;

	/**
	 * Car's initial tyreScheme. Not used yet. So far we assume that the initiall tyreScheme is always null (meaning no tyres installed). 
	 */
	@Element(name="tyreScheme", required=false)
	private TyreConfigurationScheme tyreScheme;

	/**
	 * List of tyres bought 
	 */
	@ElementList(inline=true, required=false)
	private ArrayList<Tyre> boughtTyres;
	
	/**
	 * List of tyres thrown away
	 */
	@ElementList(inline=true, required=false)
	private ArrayList<UUID> deletedTyreIDs;

	/**
	 * Map of tyre IDs with new thread levels of the respective tyres
	 */
	@ElementMap(inline = false, required = false)
	private HashMap<UUID, Double> threadLevelUpdate;

	public TyreChangeEntry() {
		super();
		boughtTyres = new ArrayList<>();
		deletedTyreIDs = new ArrayList<>();
		threadLevelUpdate = new HashMap<>();
	}
	
	@Override
	public void initAfterLoad(Car car) {
		super.initAfterLoad(car);
		this.car = car;
		tyreScheme.initAfterLoad(car);
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public double getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(double laborCost) {
		this.laborCost = laborCost;
	}
	
	public void getLaborCostSI() {
		Currency.convertToSI(getLaborCost(), getCurrency(), getEventDate());
	}

	public double getExtraMaterialCost() {
		return extraMaterialCost;
	}

	public void setExtraMaterialCost(double extraMaterialCost) {
		this.extraMaterialCost = extraMaterialCost;
	}
	
	public void getExtraMaterialCostSI() {
		Currency.convertToSI(getExtraMaterialCost(), getCurrency(), getEventDate());
	}

	public double getTyresCost() {
		return tyresCost;
	}

	public void setTyresCost(double tyresCost) {
		this.tyresCost = tyresCost;
	}
	
	public void getTyresCostSI() {
		Currency.convertToSI(getTyresCost(), getCurrency(), getEventDate());
	}
	
	public TyreConfigurationScheme getTyreScheme() {
		return tyreScheme;
	}
	
	public void setTyreScheme(TyreConfigurationScheme tyreScheme) {
		this.tyreScheme = tyreScheme;
	}

	public ArrayList<Tyre> getBoughtTyres() {
		return boughtTyres;
	}

	public void setBoughtTyres(ArrayList<Tyre> boughtTyres) {
		this.boughtTyres = boughtTyres;
	}

	public ArrayList<UUID> getDeletedTyreIDs() {
		return deletedTyreIDs;
	}

	public void setDeletedTyreIDs(ArrayList<UUID> deletedTyreIDs) {
		this.deletedTyreIDs = deletedTyreIDs;
	}

	public ArrayList<Tyre> getDeletedTyres() {
		return MainActivity.garage.getTyresByIDs(getDeletedTyreIDs());
	}

	/**
	 * Map of tyre IDs with new thread levels of the respective tyres
	 */
	public HashMap<UUID, Double> getThreadLevelUpdate() {
		return threadLevelUpdate;
	}

	public void setThreadLevelUpdate(HashMap<UUID, Double> threadLevelUpdate) {
		this.threadLevelUpdate = threadLevelUpdate;
	}
}