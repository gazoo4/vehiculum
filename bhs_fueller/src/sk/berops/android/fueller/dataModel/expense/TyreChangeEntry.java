package sk.berops.android.fueller.dataModel.expense;

import java.util.LinkedList;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;

public class TyreChangeEntry extends Entry {
	
	private Car car;
	@Element(name="axles")
	private LinkedList<Axle> axles;
	@Element(name="laborCost", required=false)
	private double laborCost;
	@Element(name="extraMaterialCost", required=false)
	private double extraMaterialCost;
	
	@SuppressWarnings("unchecked")
	public TyreChangeEntry(Car car) {
		super();
		this.car = car;
		this.axles = (LinkedList<Axle>) car.getAxles().clone();
	}
	
	@Override
	public void initAfterLoad(Car car) {
		super.initAfterLoad(car);
		this.car = car;
		this.axles = car.getAxles();
	}

	public LinkedList<Axle> getAxles() {
		return axles;
	}

	public void setAxles(LinkedList<Axle> axles) {
		this.axles = axles;
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
}