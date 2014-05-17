package sk.bhs.android.fueller.gui.garage;

import java.util.LinkedList;

import sk.bhs.android.fueller.R;
import sk.bhs.android.fueller.dataModel.Car;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GarageCarsAdapter extends ArrayAdapter<Car> {
	
	private LinkedList<Car> cars;
	private final Context context;
	
	public GarageCarsAdapter(Context context, LinkedList<Car> cars) {
		super(context, R.layout.list_garage_cars, cars);
		this.context = context;
		this.cars = cars;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Car car = cars.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_garage_cars, parent, false);
		
		TextView brand = (TextView) rowView.findViewById(R.id.list_garage_cars_brand);
		TextView model = (TextView) rowView.findViewById(R.id.list_garage_cars_model);
		TextView licensePlate = (TextView) rowView.findViewById(R.id.list_garage_cars_license_plate);
		
		brand.setText(car.getBrand());
		model.setText(car.getModel());
		licensePlate.setText(car.getLicensePlate());
		
		return rowView;
	}

}
