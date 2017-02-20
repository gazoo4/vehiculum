package sk.berops.android.vehiculum.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.LinkedList;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.gui.common.UtilsActivity;

/**
 * Created by bernard.halas on 06/08/2016.
 *
 * Activity holding the collections of buttons, textViews, editTexts, icons,... ensuring a single
 * place for modifying the visual style of the application
 */
public abstract class DefaultActivity extends Activity  implements TextWatcher, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    protected LinkedList<Button> listButtons;
    protected LinkedList<EditText> listEditTexts;
    protected LinkedList<ImageView> listIcons;
	protected LinkedList<RadioGroup> listRadioGroups;
    protected HashMap<Integer, Spinner> mapSpinners;

	/**
	 * Variable indicating if any values in the activity have been changed yet since the activity
	 * has been started.
	 */
	private boolean updateOngoing;

	/**
	 * As the spinners' onItemSelected listeners fire up before user starts interacting with the device
	 * added this variable to differentiate the real spinner selection from the initial automatic
	 * spinner selection
	 */
	private boolean userInteracted;

    /**
     * Constructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listButtons = new LinkedList<>();
        listEditTexts = new LinkedList<>();
        listIcons = new LinkedList<>();
	    listRadioGroups = new LinkedList<>();
        mapSpinners = new HashMap<>();

        loadLayout();
        attachGuiObjects();
        styleGuiObjects();
        initializeGuiObjects();
    }

    /**
     * Abstract method to ensure the layout is loaded via setContentView(View) in the right sequence
     */
    protected abstract void loadLayout();

    /**
     * This method is intended to create assignments between GUI elements like buttons into their respective variables in the code.
     * Also it's used to populate collections like listEditTexts, listIcons and mapSpinners for easy GUI customizations via method StyleGuiObjects.
     */
    protected abstract void attachGuiObjects();

    /**
     * Method to customize the visuals of the GUI objects.
     */
    protected void styleGuiObjects() {
        for (Button b: listButtons) {
            System.out.print("Flashing button "+ b.getText());
            UtilsActivity.styleButton(b);
            System.out.println(" flashed!");
        }

        for (EditText e: listEditTexts) {
            UtilsActivity.styleEditText(e);
        }

        Spinner s;
        for (Integer id: mapSpinners.keySet()) {
            s = mapSpinners.get(id);
            UtilsActivity.styleSpinner(s, this, id);
        }

        for (ImageView i: listIcons) {
            UtilsActivity.tintIcon(i);
        }
    }

    /**
     * Method to link the java objects to the elements in the layout xml file
     */
    protected void initializeGuiObjects() {
	    for (EditText e: listEditTexts) {
		    e.addTextChangedListener(this);
	    }
	    for (Spinner s: mapSpinners.values()) {
		    s.setOnItemSelectedListener(this);
	    }
	    for (RadioGroup rg: listRadioGroups) {
		    rg.setOnCheckedChangeListener(this);
	    }
    }

	/**
	 * Interrupting the back button press action. In case user already updated some fields, we need to warn him
	 * that the changes will be discarded if he leaves the activity.
	 */
	@Override
	public void onBackPressed() {
		if (isUpdateOngoing()) {
			AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle(R.string.activity_generic_warning_hint)
					.setMessage(R.string.activity_generic_discard_changes_warning)
					.setCancelable(true)
					.setPositiveButton(R.string.activity_generic_discard_hint, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							DefaultActivity.super.onBackPressed();
						}
					})
					.setNegativeButton(R.string.activity_generic_cancel_hint, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					})
					.create();
			dialog.show();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		setUserInteracted(true);
	}

	protected void setUpdateOngoing(boolean updateOngoing) {
		this.updateOngoing = updateOngoing;
	}

	protected boolean isUpdateOngoing() {
		return updateOngoing;
	}

	protected void setUserInteracted(boolean userInteracted) {
		this.userInteracted = userInteracted;
	}

	protected boolean isUserInteracted() {
		return userInteracted;
	}

	@Override
	public void afterTextChanged(Editable s) {
		setUpdateOngoing(true);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
	                              int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
	                          int count) {
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (isUserInteracted()) {
			setUpdateOngoing(true);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (isUserInteracted()) {
			setUpdateOngoing(true);
		}
	}
}