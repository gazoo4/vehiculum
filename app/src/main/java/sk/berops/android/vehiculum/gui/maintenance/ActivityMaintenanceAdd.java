package sk.berops.android.vehiculum.gui.maintenance;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.LinkedList;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.FieldEmptyException;
import sk.berops.android.vehiculum.dataModel.expense.MaintenanceEntry;
import sk.berops.android.vehiculum.dataModel.expense.MaintenanceEntry.Type;
import sk.berops.android.vehiculum.dataModel.maintenance.ReplacementPart;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.ActivityEntryGenericAdd;
import sk.berops.android.vehiculum.gui.common.FragmentEntryEditDelete;
import sk.berops.android.vehiculum.gui.common.GuiUtils;
import sk.berops.android.vehiculum.gui.common.TextFormatter;

public class ActivityMaintenanceAdd extends ActivityEntryGenericAdd implements
		FragmentEntryEditDelete.EntryEditDeleteDialogListener {

	protected static final int ADD_PART = 1;
	protected static final int EDIT_PART = 2;

	protected int selectedPartPosition;
	protected LinkedList<ReplacementPart> parts;
	PartsAdapter adapter;

	protected ListView listView;
	protected EditText editTextLaborCost;
	protected TextView textViewPartsCost;
	protected TextView textViewTotalCost;
	protected RadioGroup radioGroupType;
	protected Spinner spinnerLaborCostCurrency;

	protected MaintenanceEntry maintenanceEntry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (maintenanceEntry == null) {
			maintenanceEntry = new MaintenanceEntry();
		}
		parts = maintenanceEntry.getParts();

		entry = maintenanceEntry;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_maintenance);
	}

	@Override
	protected void attachGuiObjects() {
		super.attachGuiObjects();
		textViewDistanceUnit = (TextView) findViewById(R.id.activity_maintenance_distance_unit);
		textViewPartsCost = (TextView) findViewById(R.id.activity_maintenance_parts_cost);
		textViewTotalCost = (TextView) findViewById(R.id.activity_maintenance_total_cost);

		editTextMileage = (EditText) findViewById(R.id.activity_maintenance_mileage);
		editTextLaborCost = (EditText) findViewById(R.id.activity_maintenance_labor_cost);
		editTextCost = (EditText) findViewById(R.id.activity_maintenance_cost);
		editTextComment = (EditText) findViewById(R.id.activity_maintenance_comment);

		spinnerCurrency = (Spinner) findViewById(R.id.activity_maintenance_currency);
		spinnerLaborCostCurrency = (Spinner) findViewById(R.id.activity_maintenance_labor_cost_currency);

		radioGroupType = (RadioGroup) findViewById(R.id.activity_maintenance_type);

		listView = (ListView) findViewById(R.id.activity_maintenance_parts_listview);
		adapter = new PartsAdapter(this, parts);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "Click ListItem Number " + position,
						Toast.LENGTH_LONG).show();
				setSelectedPartPosition(position);
				DialogFragment dialog = new FragmentEntryEditDelete();
				dialog.show(getFragmentManager(), "FragmentEntryEditDelete");
				return true;
			}
		});

		listEditTexts.add(editTextMileage);
		listEditTexts.add(editTextLaborCost);
		listEditTexts.add(editTextCost);
		listEditTexts.add(editTextComment);

		mapSpinners.put(R.array.activity_expense_add_currency, spinnerCurrency);
		mapSpinners.put(R.array.activity_expense_add_currency, spinnerLaborCostCurrency);

		listRadioGroups.add(radioGroupType);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		spinnerCurrency.setSelection(0);
	}

	@Override
	public void afterTextChanged(Editable s) {
		super.afterTextChanged(s);
		if (s == editTextLaborCost.getText()) {
			reloadCost();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		super.onItemSelected(parent, view, position, id);
		if (parent == spinnerLaborCostCurrency) {
			reloadCost();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ADD_PART:
			if (resultCode == RESULT_OK) {
				ReplacementPart result = (ReplacementPart) data.getExtras().getSerializable("part");
				maintenanceEntry.getParts().add(result);
				reloadCost();
				setUpdateOngoing(true);
			} else if (resultCode == RESULT_CANCELED) {
				// If no result, no issue
			}
			break;
		case EDIT_PART:
			if (resultCode == RESULT_OK) {
				ReplacementPart result = (ReplacementPart) data.getExtras().getSerializable("part");
				maintenanceEntry.getParts().remove(selectedPartPosition);
				maintenanceEntry.getParts().add(selectedPartPosition, result);
				reloadCost();
				setUpdateOngoing(true);
			} else if (resultCode == RESULT_CANCELED) {
				// If no result, no issue
			}
			break;
		}
	}

	protected void reloadCost() {
		double partsCostSI = maintenanceEntry.getPartsCostSI();
		double totalCostSI;

		Currency.Unit currency = Currency.Unit.getUnit(spinnerLaborCostCurrency.getSelectedItemPosition());

		try {
			double laborCost = GuiUtils.extractDouble(editTextLaborCost);
			maintenanceEntry.setLaborCost(new Cost(laborCost, currency));
		} catch (NumberFormatException ex) {
			// if editTextLaborCost is empty, it should not be an issue
		} finally {
			totalCostSI = partsCostSI + maintenanceEntry.getLaborCostSI();
		}

		double totalCostNative = Currency.convert(totalCostSI, Currency.Unit.getUnit(0), currency,
				entry.getEventDate());
		textViewTotalCost.setText(TextFormatter.format(totalCostNative, "#######.##") + " "
				+ currency.getUnitIsoCode());
		editTextCost.setText(Double.toString(totalCostNative));
		
		double partsCostNative = Currency.convert(partsCostSI, Currency.Unit.getUnit(0), currency, entry.getEventDate());
		textViewPartsCost.setText(TextFormatter.format(partsCostNative, "#######.##") + " "
				+ currency.getUnitIsoCode());
		
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void updateFields() throws FieldEmptyException {
		super.updateFields();
		updateType();
	}

	private void updateType() throws FieldEmptyException {
		MaintenanceEntry.Type type;
		switch (radioGroupType.getCheckedRadioButtonId()) {
		case R.id.activity_maintenance_type_planned:
			type = Type.PLANNED;
			break;
		case R.id.activity_maintenance_type_unplanned:
			type = Type.UNPLANNED;
			break;
		case R.id.activity_maintenance_type_accident:
			type = Type.ACCIDENT_REPAIR;
			break;
		default:
			throwAlertFieldsEmpty(R.string.activity_maintenance_type_hint);
			return;
		}
		maintenanceEntry.setType(type);
	}

	@Override
	public boolean onClick(View view) {
		if (super.onClick(view)) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		}

		switch (view.getId()) {
			case R.id.activity_maintenance_button_add:
				startActivityForResult(new Intent(this, ActivityPartAdd.class), ADD_PART);
				return true;
			case R.id.activity_maintenance_button_delete:
				return true;
		}

		return false;
	}

	@Override
	public void onDialogEditClick(DialogFragment dialog) {
		int position = parts.size() - 1 - getSelectedPartPosition();
		System.out.println("Editing entry in position " + getSelectedPartPosition());
		Intent newIntent = new Intent(this, ActivityPartEdit.class);
		newIntent.putExtra("ReplacementPart", maintenanceEntry.getParts().get(position));
		startActivityForResult(newIntent, EDIT_PART);
	}

	@Override
	public void onDialogDeleteClick(DialogFragment dialog) {
		ReplacementPart part = parts.get(parts.size() - 1 - getSelectedPartPosition());
		System.out.println("Removing part with ID " + (parts.size() - 1 - getSelectedPartPosition()));  
		parts.remove(part);
		try {
			MainActivity.dataHandler.saveGarage(MainActivity.garage);
		} catch (IOException ex) {
			Log.d("ERROR", "Problem when saving changes: "+ ex.getMessage());
		}
		adapter.notifyDataSetChanged();
		reloadCost();
	}

	public int getSelectedPartPosition() {
		return selectedPartPosition;
	}

	public void setSelectedPartPosition(int selectedPartPosition) {
		this.selectedPartPosition = selectedPartPosition;
	}
}