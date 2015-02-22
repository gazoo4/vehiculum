package sk.berops.android.fueller.gui.report;

import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.FragmentEntryEditDelete;
import sk.berops.android.fueller.gui.fuelling.ActivityFuellingEdit;
import sk.berops.android.fueller.gui.garage.ActivityCarEdit;
import sk.berops.android.fueller.io.xml.XMLHandler;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						"Click ListItem Number " + position, Toast.LENGTH_LONG)
						.show();
				setSelectedEntryPosition(position);
				DialogFragment dialog = new FragmentEntryEditDelete();
				dialog.show(getFragmentManager(), "FragmentEntryEditDelete");
				return true;
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
