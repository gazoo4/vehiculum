package sk.bhs.android.fueller.gui.report;

import java.util.LinkedList;

import sk.bhs.android.fueller.R;
import sk.bhs.android.fueller.dataModel.expense.FuellingEntry;
import sk.bhs.android.fueller.gui.MainActivity;
import sk.bhs.android.fueller.gui.common.FragmentEntryEditDelete;
import sk.bhs.android.fueller.gui.fuelling.ActivityFuellingEdit;
import sk.bhs.android.fueller.gui.garage.ActivityCarEdit;
import sk.bhs.android.fueller.io.xml.XMLHandler;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityStatsFuelling extends Activity implements FragmentEntryEditDelete.EntryEditDeleteDialogListener {
	private int selectedEntryPosition;
	private LinkedList<FuellingEntry> entries;
	FuellingStatsAdapter adapter;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats_fuelling);
		entries = MainActivity.garage.getActiveCar().getHistory().getFuellingEntries();
		
		listView = (ListView) findViewById(R.id.activity_stats_fuelling_listview);
		adapter = new FuellingStatsAdapter(this, entries);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						"Click ListItem Number " + position, Toast.LENGTH_LONG)
						.show();
				setSelectedEntryPosition(position);
				DialogFragment dialog = new FragmentEntryEditDelete();
				dialog.show(getFragmentManager(), "FragmentEntryEditDelete");
			}
		});
	}
	
	@Override
	public void OnDialogEditClick(DialogFragment dialog) {
		int position = entries.size() - 1 - getSelectedEntryPosition();
		System.out.println("Editing entry in position " + getSelectedEntryPosition());
		Intent newIntent = new Intent(this, ActivityFuellingEdit.class);
		newIntent.putExtra("position", position);
		startActivity(newIntent);
	}

	@Override
	public void OnDialogDeleteClick(DialogFragment dialog) {
		FuellingEntry entry;
		entry = entries.get(entries.size() - 1 - getSelectedEntryPosition());
		System.out.println("Removing entry with ID " + entry.getDynamicId());  
		entries.remove(entry);
		MainActivity.garage.getActiveCar().getHistory().removeEntry(entry);
		MainActivity.dataHandler.persistGarage(MainActivity.garage);
		adapter.notifyDataSetChanged();
	}
	
	public int getSelectedEntryPosition() {
		return selectedEntryPosition;
	}

	public void setSelectedEntryPosition(int selectedEntryPosition) {
		this.selectedEntryPosition = selectedEntryPosition;
	}
}
