package sk.berops.android.vehiculum.gui.garage;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.LinkedList;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.gui.DefaultActivity;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.FragmentEntryEditDeleteActivate;

public class ActivityGarageManagement extends DefaultActivity implements FragmentEntryEditDeleteActivate.EntryEditDeleteActivateDialogListener {

	private LinkedList<Car> cars;
	ListView carView;
	private Button buttonCarAdd;
	GarageCarsAdapter adapter;
	private int selectedCarPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		cars = MainActivity.garage.getCars();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_garage_management);
	}

	@Override
	protected void attachGuiObjects() {
		buttonCarAdd = (Button) findViewById(R.id.activity_garage_management_button_car_add);
		carView = (ListView) findViewById(R.id.activity_garage_management_cars_listview);

		listButtons.add(buttonCarAdd);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		adapter = new GarageCarsAdapter(this, cars);
		carView.setAdapter(adapter);

		carView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				setSelectedCarPosition(position);
				Toast.makeText(getApplicationContext(), "Clicked car "+ position, Toast.LENGTH_LONG).show();
				DialogFragment dialog = new FragmentEntryEditDeleteActivate();
				dialog.show(getFragmentManager(), "FragmentEntryEditDeleteActivate");
			}
		});
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.activity_garage_management_button_car_add:
			startActivity(new Intent(this, ActivityCarAdd.class));
			break;
		}
	}

	@Override
	public void OnDialogEditClick(DialogFragment dialog) {
		Car car = cars.get(getSelectedCarPosition());
		System.out.println("Editing car ID " + getSelectedCarPosition());
		Intent newIntent = new Intent(this, ActivityCarEdit.class);
		newIntent.putExtra("position", getSelectedCarPosition());
		startActivity(newIntent);
	}

	@Override
	public void OnDialogDeleteClick(DialogFragment dialog) {
		Car car = cars.get(getSelectedCarPosition());
		System.out.println("Removing car ID " + getSelectedCarPosition());
		cars.remove(car);
		try {
			MainActivity.dataHandler.saveGarage(MainActivity.garage);
		} catch (IOException ex) {
			Log.d("ERROR", "Problem when saving changes: "+ ex.getMessage());
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void OnDialogActivateClick(DialogFragment dialog) {
		MainActivity.garage.setActiveCarId(getSelectedCarPosition());
		try {
			MainActivity.dataHandler.saveGarage(MainActivity.garage);
		} catch (IOException ex) {
			Log.d("ERROR", "Problem when saving changes: "+ ex.getMessage());
		}
		startActivity(new Intent(this, MainActivity.class));
	}
	
	public int getSelectedCarPosition() {
		return selectedCarPosition;
	}

	public void setSelectedCarPosition(int selectedCarPosition) {
		this.selectedCarPosition = selectedCarPosition;
	}
}
