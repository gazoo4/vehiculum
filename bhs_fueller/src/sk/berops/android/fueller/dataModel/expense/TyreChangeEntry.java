package sk.berops.android.fueller.dataModel.expense;

import java.util.LinkedList;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;

public class TyreChangeEntry extends Entry {
	
	@Element(name="newTyres")
	private Tyre[] newTyres;
	private Car car;
	private LinkedList<Axle> axles;
	
	public TyreChangeEntry(Car car) {
		super();
		this.car = car;
		this.axles = car.getAxles();
	}
	
	@Override
	public void initAfterLoad(Car car) {
		super.initAfterLoad(car);
		this.car = car;
		this.axles = car.getAxles();
	}

	public Tyre[] getNewTyres() {
		return newTyres;
	}

	public void setNewTyres(Tyre[] newTyres) {
		this.newTyres = newTyres;
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
}