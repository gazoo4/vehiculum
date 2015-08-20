package sk.berops.android.fueller.gui.report;

import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.burreaucratic.ActivityBurreaucraticEdit;
import sk.berops.android.fueller.gui.common.FragmentEntryEditDelete;
import sk.berops.android.fueller.gui.fuelling.ActivityFuellingEdit;
import sk.berops.android.fueller.gui.garage.ActivityCarEdit;
import sk.berops.android.fueller.gui.insurance.ActivityInsuranceEdit;
import sk.berops.android.fueller.gui.maintenance.ActivityMaintenanceEdit;
import sk.berops.android.fueller.gui.service.ActivityServiceEdit;
import sk.berops.android.fueller.gui.toll.ActivityTollEdit;
import sk.berops.android.fueller.io.xml.XMLHandler;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityEntriesShow extends Activity implements FragmentEntryEditDelete.EntryEditDeleteDialogListener {
	private int selectedEntryPosition;
	private LinkedList<Entry> entries;
	EntriesReportAdapter adapter;
	ListView listView;
	Spinner spinnerEntriesType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entries_show);
		// need to create a copy of the entries list on which we will do the filtering of the viewing items
		// we also do the intial population of this list
		entries = new LinkedList<Entry>(MainActivity.garage.getActiveCar().getHistory().getEntries());
		attachGuiObjects();
		styleGuiObjects();
		initializeGuiObjects();
	}
	
	protected void attachGuiObjects() {
		listView = (ListView) findViewById(R.id.activity_entries_list_listview);
		spinnerEntriesType = (Spinner) findViewById(R.id.activity_entries_list_type);
	}
	
	protected void styleGuiObjects() {
		ArrayAdapter<CharSequence> adapterEntriesType = ArrayAdapter
				.createFromResource(this, R.array.activity_entries_list_types,
						R.layout.spinner_white);
		adapterEntriesType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEntriesType.setAdapter(adapterEntriesType);
	}
	
	protected void initializeGuiObjects() {
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
		
		spinnerEntriesType.setSelection(0);
		spinnerEntriesType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				LinkedList<Entry> newEntries;
				if (position == 0) {
					newEntries = MainActivity.garage.getActiveCar().getHistory().getEntries(); 
				} else {
					position--;
					Entry.ExpenseType type = Entry.ExpenseType.getExpenseType(position);
					newEntries = MainActivity.garage.getActiveCar().getHistory().getEntriesFiltered(type);
				}
				feedNewEntries(entries, newEntries);
				adapter.notifyDataSetChanged();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
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
			newIntent = new Intent(this, ActivityTollEdit.class);
			break;
		case FUEL:
			newIntent = new Intent(this, ActivityFuellingEdit.class);
			break;
		case MAINTENANCE:
			newIntent = new Intent(this, ActivityMaintenanceEdit.class);
			break;
		case SERVICE:
			newIntent = new Intent(this, ActivityServiceEdit.class);
			break;
		case TYRES:
			break;
		case BURREAUCRATIC:
			newIntent = new Intent(this, ActivityBurreaucraticEdit.class);
			break;
		case INSURANCE:
			newIntent = new Intent(this, ActivityInsuranceEdit.class);
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
	
	private void feedNewEntries(LinkedList<Entry> oldEntries, LinkedList<Entry> newEntries) {
		oldEntries.clear();
		for (Entry e : newEntries) {
			oldEntries.add(e);
		}
	}
}
