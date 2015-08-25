package sk.berops.android.fueller.gui.maintenance;

import java.util.LinkedList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.common.TyreDrawer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityTyreChangeScheme extends Activity implements TouchCallbackInterface {
	
	private Car car;
	private TyreChangeEntry tyreChangeEntry;
	private LinkedList<Tyre> tyreList;
	
	private RelativeLayout tyreSchemeLayout;
	private ListView listView;
	private ViewTyreChangeGraphics graphics;
	
	private TyreDrawer td;
	private TyreSchemeHelper helper;
	private TyreConfigurationScheme tyreScheme;
	TyrePoolAdapter adapter;
	ViewGroup viewGroup;
	
	TextView textViewBrandModelHint;
	TextView textViewDimensionsHint;
	TextView textViewDotYearWearHint;
	TextView textViewPatternHint;
	TextView textViewMileageHint;
	
	TextView textViewBrandModelValue;
	TextView textViewDimensionsValue;
	TextView textViewDotYearWearValue;
	TextView textViewPatternValue;
	TextView textViewMileageValue; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tyre_change_scheme);
		viewGroup = (ViewGroup) findViewById(R.id.activity_tyre_change_scheme_top_layout);
		car = MainActivity.garage.getActiveCar();
		tyreChangeEntry = ActivityTyreChange.tyreChangeEntryStatic;
		super.onCreate(savedInstanceState);
		
		helper = TyreSchemeHelper.getInstance();
		
		attachGuiObjects();
		buildDynamicLayout();
	}
	
	private void attachGuiObjects() {
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
	}
	
	private void buildDynamicLayout() {
		TyreConfigurationScheme tyreScheme = car.getCurrentTyreScheme().clone();
		graphics = new ViewTyreChangeGraphics(this, car, tyreScheme);
		graphics.setOnTouchListener(new TyreTouchListener(this)); 
		graphics.setPadding(3, 3, 3, 3);
		
		tyreSchemeLayout = (RelativeLayout) findViewById(R.id.activity_tyre_change_scheme_graphical_layout);
		tyreSchemeLayout.removeAllViews();
		tyreSchemeLayout.addView(graphics);
		
		listView = (ListView) findViewById(R.id.activity_tyre_change_scheme_tyre_pool_list_view);
		// TODO: this is here just for the testing purposes
		if (MainActivity.garage.getAvailableTyres().size() < 10) {
			for (int i = 0; i < 10; i++) {
				Tyre tyre = new Tyre();
				tyre.setThreadLevel(9 * Math.random());
				tyre.setSeason(Tyre.Season.getSeason((int) (4 * Math.random())));
				MainActivity.garage.addNewTyre(tyre);
			}
		}
		tyreList = MainActivity.garage.getAvailableTyres();
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
	}
	
	private void tyreClicked(Tyre tyre) {
		if (helper.getSelectedTyre() == tyre) {
			deselectTyre();
		} else {
			selectTyre(tyre);
		}
		reloadTyreStats(helper.getSelectedTyre());
	}
	
	private void deselectTyre() {
		helper.setSelectedTyre(null);
		helper.setFlashingMode(false);
		helper.setFlashingPhase(-1.0);
		listView.clearChoices();
		listView.setSelector(new ColorDrawable(0x0));
		adapter.notifyDataSetChanged();
	}
	
	private void selectTyre(Tyre tyre) {
		helper.setSelectedTyre(tyre);
		helper.setFlashingMode(true);
		if (tyreList.contains(tyre)) {
			listView.setSelector(new ColorDrawable(0x80ffffff));
		}
	}
	
	
	private void reloadTyreStats(Tyre tyre) {
		String text;
		try {
			text = "";
			if (tyre.getProducer() != null) {
				text = tyre.getProducer() +" ";
			}
			if (tyre.getModel() != null) {
				text += tyre.getModel();
			}
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewBrandModelHint, textViewBrandModelValue);
		try {
			text="";
			if (tyre.getWidth() == 0 && tyre.getHeight() == 0 && tyre.getDiameter() == 0.0) throw new NullPointerException();
			text = ""+ tyre.getWidth() +"/"+ tyre.getHeight() +"/R"+ tyre.getDiameter();
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewDimensionsHint, textViewDimensionsValue);
		try {
			text="";
			if (tyre.getDot() == null) throw new NullPointerException();
			text = tyre.getDot() +" "+ tyre.getYear() +" ("+ (100 - tyre.getWearLevel()) +"% tread left)";
		} catch (NullPointerException e) {
			text = null;
		}
		reloadSingleTyreStat(text, textViewDotYearWearHint, textViewDotYearWearValue);
		try {
			text = "";
			if (tyre.getMileageDriveAxle() != 0.0) {
				text += tyre.getMileageDriveAxle() +" (engine driven axle) ";
			}
			if (tyre.getMileageNonDriveAxle() != 0.0) {
				text += tyre.getMileageDriveAxle() +" (engine non-driven axle) ";
			}
			if (tyre.getMileageDriveAxle() != 0.0 && tyre.getMileageNonDriveAxle() != 0.0) {
				text += (tyre.getMileageDriveAxle() + tyre.getMileageNonDriveAxle()) +" (total)";
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
	
	private void reloadSingleTyreStat(String text, TextView hint, TextView value) {
		if (text == null || text == "") {
			hint.setVisibility(View.GONE);
			value.setVisibility(View.GONE);
		} else {
			value.setText(" "+ text);
			hint.setVisibility(View.VISIBLE);
			value.setVisibility(View.VISIBLE);
		}
		viewGroup.invalidate();
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
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.activity_tyre_change_scheme_button_tyre_add:
			startActivity(new Intent(this, ActivityTyreAdd.class));
			break;
		case R.id.activity_tyre_change_scheme_button_tyre_delete:
			break;
		case R.id.activity_tyre_change_scheme_button_tyre_move:
			break;
		}
	}
}