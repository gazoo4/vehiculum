package sk.berops.android.fueller.gui.maintenance;

import java.util.ArrayList;
import java.util.LinkedList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.ActivityEntryAdd;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.GuiUtils;
import sk.berops.android.fueller.gui.garage.ActivityGarageManagement;
import sk.berops.android.fueller.gui.report.ActivityStatsShow;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityTyreChangeScheme extends Activity {
	
	private Car car;
	private TyreChangeEntry tyreChangeEntry;;
	private LinkedList<Tyre> tyreList;
	
	private LinearLayout tyreChangeScheme;
	private ListView listView;
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
		View graphics = new ViewTyreChangeGraphics(this, car);
		
		tyreChangeScheme = (LinearLayout) findViewById(R.id.activity_tyre_change_scheme_graphical_layout);
		tyreChangeScheme.removeAllViews();
		tyreChangeScheme.addView(graphics);
		
		listView = (ListView) findViewById(R.id.activity_tyre_change_scheme_tyre_pool_list_view);
		for (int i = 0; i < 10; i++) {
			Tyre tyre = new Tyre();
			tyre.setThreadLevel((int) (9 * Math.random()));
			tyre.setSeason(Tyre.Season.getSeason((int) (4 * Math.random())));
			MainActivity.garage.addTyre(tyre);
		}
		tyreList = MainActivity.garage.getTyres();
		adapter = new TyrePoolAdapter(this, tyreList);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				reloadTyreStats(tyreList.get(position));
			}
		});
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