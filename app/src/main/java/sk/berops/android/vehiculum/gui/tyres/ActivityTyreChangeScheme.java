package sk.berops.android.vehiculum.gui.tyres;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.UUID;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.Axle;
import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.expense.History;
import sk.berops.android.vehiculum.dataModel.expense.TyreChangeEntry;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;
import sk.berops.android.vehiculum.dataModel.maintenance.TyreConfigurationScheme;
import sk.berops.android.vehiculum.gui.DefaultActivity;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.FragmentEntryEditDelete;
import sk.berops.android.vehiculum.gui.common.GuiUtils;

public class ActivityTyreChangeScheme extends DefaultActivity implements TouchCallbackInterface, FragmentEntryEditDelete.EntryEditDeleteDialogListener {

	private Car car;
	private LinkedList<Tyre> tyreList;

	private RelativeLayout tyreSchemeLayout;
	private ListView listView;
	private int selectedTyrePosition;
	private ViewTyreChangeGraphics graphics;

	private TyreSchemeHelper helper;
	private TyreChangeEntry tyreEntry;
	private TyreConfigurationScheme tyreScheme;
	private TyrePoolAdapter adapter;

	private TextView textViewBrandModelHint;
	private TextView textViewDimensionsHint;
	private TextView textViewDotYearWearHint;
	private TextView textViewPatternHint;
	private TextView textViewMileageHint;

	private TextView textViewBrandModelValue;
	private TextView textViewDimensionsValue;
	private TextView textViewDotYearWearValue;
	private TextView textViewPatternValue;
	private TextView textViewMileageValue;

	private Button buttonUninstall;
	private Button buttonDelete;

	public static final int REQUEST_CODE_ADD_TYRE = 1;
	public static final int REQUEST_CODE_EDIT_TYRE = 2;
	public static final String INTENT_TYRE_ENTRY = "tyre entry";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		car = MainActivity.garage.getActiveCar();
		tyreEntry = (TyreChangeEntry) getIntent().getSerializableExtra(INTENT_TYRE_ENTRY);
		if (tyreEntry == null) {
			tyreEntry = new TyreChangeEntry();
		}

		tyreScheme = tyreEntry.getTyreScheme();

		helper = TyreSchemeHelper.getInstance();

		super.onCreate(savedInstanceState);

		buildDynamicLayout();
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_tyre_change_scheme);
	}

	@Override
	protected void attachGuiObjects() {
		textViewBrandModelHint = (TextView) findViewById(R.id.activity_tyre_change_scheme_brand_model);
		textViewDimensionsHint = (TextView) findViewById(R.id.activity_tyre_change_scheme_dimensions);
		textViewDotYearWearHint = (TextView) findViewById(R.id.activity_tyre_change_scheme_dot_year_wear);
		textViewPatternHint = (TextView) findViewById(R.id.activity_tyre_change_scheme_pattern);
		textViewMileageHint = (TextView) findViewById(R.id.activity_tyre_change_scheme_mileage);

		textViewBrandModelValue = (TextView) findViewById(R.id.activity_tyre_change_scheme_brand_model_value);
		textViewDimensionsValue = (TextView) findViewById(R.id.activity_tyre_change_scheme_dimensions_value);
		textViewDotYearWearValue = (TextView) findViewById(R.id.activity_tyre_change_scheme_dot_year_wear_value);
		textViewPatternValue = (TextView) findViewById(R.id.activity_tyre_change_scheme_pattern_value);
		textViewMileageValue = (TextView) findViewById(R.id.activity_tyre_change_scheme_mileage_value);

		buttonDelete = (Button) findViewById(R.id.activity_tyre_change_scheme_button_tyre_delete);
		buttonUninstall = (Button) findViewById(R.id.activity_tyre_change_scheme_button_tyre_uninstall);

		listButtons.add(buttonDelete);
		listButtons.add(buttonUninstall);

		tyreSchemeLayout = (RelativeLayout) findViewById(R.id.activity_tyre_change_scheme_graphical_layout);
		listView = (ListView) findViewById(R.id.activity_tyre_change_scheme_tyre_pool_list_view);
	}

	@Override
	protected void initializeGuiObjects() {
		super.initializeGuiObjects();
		tyreList = tyreEntry.getAvailableTyres();
		adapter = new TyrePoolAdapter(this, tyreList);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {
				Tyre tyre = tyreList.get(position);
				tyreClicked(tyre);
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				setSelectedTyrePosition(position);
				DialogFragment dialog = new FragmentEntryEditDelete();
				dialog.show(getFragmentManager(), "FragmentEntryEditDelete");
				return true;
			}
		});

		buttonDelete.setVisibility(View.INVISIBLE);
		buttonUninstall.setVisibility(View.INVISIBLE);
	}

	/**
	 * Metod to create the graphical objects on this activity
	 */
	private void buildDynamicLayout() {
		graphics = new ViewTyreChangeGraphics(this, car, tyreEntry);
		graphics.setOnTouchListener(new TyreTouchListener(this));
		graphics.setPadding(3, 3, 3, 3);

		tyreSchemeLayout.removeAllViews();
		tyreSchemeLayout.addView(graphics);
	}

	/**
	 * If a tyre is clicked, this method is responsible to handle tyre selection
	 * @param tyre
	 */
	private void tyreClicked(Tyre tyre) {
		if (helper.getSelectedTyre() == tyre) {
			deselectTyre();
		} else {
			selectTyre(tyre);
		}
	}

	/**
	 * Method responsible for all the steps that are necessary when a tyre is deselected.
	 */
	private void deselectTyre() {
		helper.setSelectedTyre(null);
		helper.setFlashingMode(false);
		helper.setFlashingPhase(-1.0);
		buttonDelete.setVisibility(View.INVISIBLE);
		buttonUninstall.setVisibility(View.INVISIBLE);
		listView.clearChoices();
		listView.setSelector(new ColorDrawable(0x0));
		adapter.notifyDataSetChanged();
		reloadTyreStats(null);
	}

	/**
	 * Method responsible for all the steps that are necessary when a tyre is selected.
	 */
	private void selectTyre(Tyre tyre) {
		helper.setSelectedTyre(tyre);
		helper.setFlashingMode(true);
		buttonDelete.setVisibility(View.VISIBLE);
		if (tyreList.contains(tyre)) {
			listView.setSelector(new ColorDrawable(0x80ffffff));
			buttonUninstall.setVisibility(View.INVISIBLE);
		} else {
			buttonUninstall.setVisibility(View.VISIBLE);
		}
		reloadTyreStats(tyre);
	}

	/**
	 * Method responsible to update the textviews in the activity which show the details of the
	 * selected tyre
	 * @param tyre which has been selected
	 */
	private void reloadTyreStats(Tyre tyre) {
		String text;
		try {
			text = "";
			if (tyre.getBrand() != null) {
				text = tyre.getBrand() + " ";
			}
			if (tyre.getModel() != null) {
				text += tyre.getModel();
			}
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewBrandModelHint, textViewBrandModelValue);
		try {
			text = "";
			if (tyre.getWidth() == 0 && tyre.getHeight() == 0 && tyre.getDiameter() == 0.0)
				throw new NullPointerException();
			text = "" + tyre.getWidth() + "/" + tyre.getHeight() + "/R" + tyre.getDiameter();
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewDimensionsHint, textViewDimensionsValue);
		try {
			text = "";
			if (tyre.getDot() == null) throw new NullPointerException();
			text = tyre.getDot() + " " + tyre.getYear() + " (" + (100 - tyre.getWearLevel()) + "% tread left)";
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewDotYearWearHint, textViewDotYearWearValue);
		try {
			text = "";
			if (tyre.getMileageDriveAxle() != 0.0) {
				text += tyre.getMileageDriveAxle() + " (engine driven axle) ";
			}
			if (tyre.getMileageNonDriveAxle() != 0.0) {
				text += tyre.getMileageDriveAxle() + " (non-driven axle) ";
			}
			if (tyre.getMileageDriveAxle() != 0.0 && tyre.getMileageNonDriveAxle() != 0.0) {
				text += (tyre.getMileageDriveAxle() + tyre.getMileageNonDriveAxle()) + " (total)";
			}
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewMileageHint, textViewMileageValue);
		try {
			text = tyre.getSeason().getSeason();
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewPatternHint, textViewPatternValue);
	}

	/**
	 * Method to add a single line to the stats of the selected tyre
	 * @param text text to enter
	 * @param hintView view carrying the text hint (e.g. brand, model)
	 * @param valueView view carrying the values (e.g. UnitedTyres, WinterWind T100)
	 */
	private void reloadSingleTyreStat(String text, TextView hintView, TextView valueView) {
		if (text == null || text == "") {
			hintView.setVisibility(View.GONE);
			valueView.setVisibility(View.GONE);
		} else {
			valueView.setText(" " + text);
			hintView.setVisibility(View.VISIBLE);
			valueView.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void touchCallback(float x, float y) {
		TyreGUIContainer tContainer;
		tContainer = (TyreGUIContainer) GuiUtils.determineObjectClicked(graphics.getTyreGUIObjects(), x, y);
		// check if we've clicked on a tyre container
		if (tContainer != null) {
			// check if this spot is free for installing tyre
			if (tContainer.getTyre() == null) {
				Tyre selectedTyre = helper.getSelectedTyre();
				// check if there's a tyre selected from the tyre list
				if (selectedTyre != null) {
					// we're moving the tyre, so clear any possible old locations
					// in list of tyres (if tyre is there)
					tyreList.remove(selectedTyre);
					// in graphics (if tyre is there)
					GuiUtils.removeTyreFromContainer(selectedTyre, graphics.getTyreGUIObjects());
					// populate structures at new location
					tContainer.setTyre(selectedTyre);
					tContainer.installTyre();
					// cleanup
					deselectTyre();
				}
			} else {
				if (tContainer.getTyre() == helper.getSelectedTyre()) {
					deselectTyre();
				} else {
					selectTyre(tContainer.getTyre());
				}
			}
		}
	}

	/**
	 * Method responsible for checking if it's safe to delete a selected tyre. It checks if there's
	 * any newer entry which works with the tyre we want to delete.
	 * @param tyre to be checked for
	 * @return true if it's safe to delete the tyre; false otherwise
	 */
	private boolean isSafeToDelete(Tyre tyre) {
		for (Car c: MainActivity.garage.getCars()) {
			// Check all the cars
			LinkedList<TyreChangeEntry> entries = c.getHistory().getTyreChangeEntries();
			Date filteringDate = new Date();
			// we need to shift the time by 1 ms for filtering so that it doesn't scream if on our own entry
			filteringDate.setTime(tyreEntry.getEventDate().getTime() + 1);
			History.filterEntriesByDate(entries, filteringDate, null);
			for (TyreChangeEntry e: entries) {
				if (e.getTyreScheme() == null) {
					continue;
				}
				// All the TyreChangeEntries after current date
				for (Axle a: e.getTyreScheme().getAxles()) {
					// All the axles
					for (UUID uuid: a.getTyreIDs().values()) {
						// And all the tyres
						if (uuid.equals(tyre.getUuid())) {
							String message = getResources().getString(R.string.activity_tyre_change_scheme_tyre_used_alert_1);
							message += e.getDynamicId() + " ";
							message += getResources().getString(R.string.activity_tyre_change_scheme_tyre_used_alert_2);
							SimpleDateFormat format = new SimpleDateFormat();
							format.setTimeZone(TimeZone.getDefault());
							message += " " + DateFormat.getDateInstance().format(e.getEventDate()) + " "; // Format in device's locale
							message += getResources().getString(R.string.activity_tyre_change_scheme_tyre_used_alert_3);
							message += " " + c.getNickname();
							// And verify that the tyre the user wants to delete is not installed
							AlertDialog alertDialog = new AlertDialog.Builder(this)
								.setTitle(R.string.activity_generic_failure_hint)
								.setMessage(message)
								.setNeutralButton(R.string.activity_generic_ok_hint,
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									}
								)
								.create();
							alertDialog.show();
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Method to delete the tyre from tyreList list and from the graphical tyreContainer.
	 * @param tyre Tyre to be deleted.
	 * @return True if tyre has been successfully deleted. False otherwise.
	 */
	private boolean deleteTyre(Tyre tyre) {
		if (!isSafeToDelete(tyre)) return false;

		if (tyreList.contains(tyre)) {
			// Tyre is stored in the pool of available tyres
			tyreList.remove(tyre);
		} else {
			// Tyre is installed on the vehicle
			GuiUtils.removeTyreFromContainer(tyre, graphics.getTyreGUIObjects());
		}
		tyreEntry.getDeletedTyreIDs().add(tyre.getUuid());
		deselectTyre();
		return true;
	}

	/**
	 * Removes the tyre from the graphical TyreContainer and puts it back to the pool.
	 * @param tyre
	 */
	private void uninstallTyre(Tyre tyre) {
		GuiUtils.removeTyreFromContainer(tyre, graphics.getTyreGUIObjects());
		tyreList.add(tyre);
		deselectTyre();
	}

	/**
	 * Once the new tyres details are entered, we need to make the entry aware of them.
	 * If this concerns an update of an existing tyre, replace the old tyre by the updated tyre.
	 * @param tyre Tyre which was bought
	 * @param count of tyres added
	 */
	private void registerNewTyres(Tyre tyre, int count) {
		Tyre newTyre;
		int index = tyreList.size();
		if (tyreList.contains(tyre)) {
			// If we're updating an existing tyre, remember where it's located as into the same place
			// we'll be adding new tyre.
			index = tyreList.indexOf(tyre);
			tyreList.remove(tyre);
		}

		for (int i = 0; i < count; i++) {
			newTyre = new Tyre(tyre);
			tyreEntry.getBoughtTyres().add(newTyre);
			tyreList.add(index, newTyre);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDialogEditClick(DialogFragment dialog) {
		Intent newIntent = new Intent(this, ActivityTyreEdit.class);
		newIntent.putExtra(ActivityTyreEdit.INTENT_TYRE, tyreList.get(getSelectedTyrePosition()));
		startActivityForResult(newIntent, REQUEST_CODE_EDIT_TYRE);
	}

	@Override
	public void onDialogDeleteClick(DialogFragment dialog) {
		// this calls deleteTyre(Tyre tyre)
		// and removes the tyre from the boughtTyres as well
		Tyre tyre = tyreList.get(getSelectedTyrePosition());
		if (deleteTyre(tyre)) {
			// Try to delete the tyre from tyreList. If successfully deleted from tyreList,
			// delete it from the boughtTyres and deletedTyres as well as we're completely clearing
			// the history now
			for (Car c : MainActivity.garage.getCars()) {
				for (TyreChangeEntry e : c.getHistory().getTyreChangeEntries()) {
					if (e.getBoughtTyres().contains(tyre)) {
						e.getBoughtTyres().remove(tyre);
						break;
					}
				}
			}
			tyreEntry.getDeletedTyreIDs().remove(tyre.getUuid());
		}
	}

	/**
	 * Setter
	 * @param selectedTyrePosition
	 */
	private void setSelectedTyrePosition(int selectedTyrePosition) {
		this.selectedTyrePosition = selectedTyrePosition;
	}

	/**
	 * Getter
	 * @return value
	 */
	protected int getSelectedTyrePosition() {
		return selectedTyrePosition;
	}

	/**
	 * Callback method from follow-up activities
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CODE_ADD_TYRE:
				// this is intended as for both REQUEST_CODE_ADD_TYRE and REQUEST_CODE_EDIT_TYRE we have the same handling
				// the differences are handled inside registerNewTyres() method
			case REQUEST_CODE_EDIT_TYRE:
				if (resultCode == RESULT_OK) {
					Tyre tyre = (Tyre) data.getExtras().getSerializable(ActivityTyreAdd.INTENT_TYRE);
					int count = (Integer) data.getExtras().getSerializable(ActivityTyreAdd.INTENT_COUNT);
					registerNewTyres(tyre, count);
					reloadTyreStats(helper.getSelectedTyre());
					adapter.notifyDataSetChanged();
					setUpdateOngoing(true);
				} else if (resultCode == RESULT_CANCELED) {
					// If no result, no issue
				}
				break;
		}
	}

	/**
	 * Handler for all buttons
	 * @param view which has been clicked
	 */
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.activity_tyre_change_scheme_button_tyre_add:
				startActivityForResult(new Intent(this, ActivityTyreAdd.class), REQUEST_CODE_ADD_TYRE);
				break;
			case R.id.activity_tyre_change_scheme_button_tyre_delete:
				deleteTyre(helper.getSelectedTyre());
				break;
			case R.id.activity_tyre_change_scheme_button_tyre_uninstall:
				uninstallTyre(helper.getSelectedTyre());
				break;
			case R.id.activity_common_new_record_button_save:
				Intent returnIntent = new Intent();
				returnIntent.putExtra(INTENT_TYRE_ENTRY, tyreEntry);
				setResult(RESULT_OK, returnIntent);
				finish();
				break;
		}
	}
}