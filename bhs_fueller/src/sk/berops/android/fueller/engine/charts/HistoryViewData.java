package sk.berops.android.fueller.engine.charts;

import java.util.Date;

import com.jjoe64.graphview.GraphViewDataInterface;

public class HistoryViewData implements GraphViewDataInterface {
	
	private boolean timebased;
	private Date date;
	private double mileage;
	private double value;
		
	public HistoryViewData(double mileage, double value) {
		super();
		this.mileage = mileage;
		this.value = value;
		this.timebased = false;
	}
	
	public HistoryViewData(Date date, double value) {
		super();
		this.date = date;
		this.value = value;
		this.timebased = true;
	}

	@Override
	public double getX() {
		if (timebased) {
			return this.date.getTime();
		} else {
			return this.mileage;
		}
	}

	@Override
	public double getY() {
		return this.value;
	}

}
