package sk.berops.android.fueller.dataModel.expense;

import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;

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
	
	public TyreChangeEntry() {
		super();
	}
	
	@Override
	public void initAfterLoad(Car car) {
		super.initAfterLoad(car);
		this.car = car;
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

	public double getExtraMaterialCost() {
		return extraMaterialCost;
	}

	public void setExtraMaterialCost(double extraMaterialCost) {
		this.extraMaterialCost = extraMaterialCost;
	}

	public double getTyresCost() {
		return tyresCost;
	}

	public void setTyresCost(double tyresCost) {
		this.tyresCost = tyresCost;
	}
	
	public TyreConfigurationScheme getTyreScheme() {
		return tyreScheme;
	}
	
	public void setTyreScheme(TyreConfigurationScheme tyreScheme) {
		this.tyreScheme = tyreScheme;
	}
}