package sk.berops.android.fueller.gui.report;

import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.FragmentEntryEditDelete;
import sk.berops.android.fueller.gui.fuelling.ActivityFuellingEdit;
import sk.berops.android.fueller.gui.garage.ActivityCarEdit;
import sk.berops.android.fueller.gui.maintenance.ActivityMaintenanceEdit;
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

public class ActivityStatsEntries extends Activity implements FragmentEntryEditDelete.EntryEditDeleteDialogListener {
	private int selectedEntryPosition;
	private LinkedList<Entry> entries;
	EntriesReportAdapter adapter;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats_fuelling);
		entries = MainActivity.garage.getActiveCar().getHistory().getEntries();
		
		listView = (ListView) findViewById(R.id.activity_stats_fuelling_listview);
		adapter = new EntriesReportAdapter(this, entries);
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
		
		Intent newIntent = null;
		switch (entries.get(position).getExpenseType()) {
		case TOLL:
			//newIntent = new Intent(this, ActivityTollEdit.class);
			break;
		case FUEL:
			newIntent = new Intent(this, ActivityFuellingEdit.class);
			break;
		case MAINTENANCE:
			newIntent = new Intent(this, ActivityMaintenanceEdit.class);
			break;
		case SERVICE:
			//newIntent = new Intent(this, ActivityServiceEdit.class);
			break;
		case TYRES:
			break;
		case BURREAUCRATIC:
			//newIntent = new Intent(this, ActivityBurreaucraticEdit.class);
			break;
		case INSURANCE:
			//newIntent = new Intent(this, ActivityInsuranceEdit.class);
			break;
		default:
			break;
		}
		newIntent.putExtra("dynamicID", entries.get(position).getDynamicId());
		startActivity(newIntent);
	}

	@Override
	public void OnDialogDeleteClick(DialogFragment dialog) {
		Entry entry;
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
