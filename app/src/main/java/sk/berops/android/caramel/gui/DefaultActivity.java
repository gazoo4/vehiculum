package sk.berops.android.caramel.gui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.LinkedList;

import sk.berops.android.caramel.gui.common.UtilsActivity;

/**
 * Created by bernard.halas on 06/08/2016.
 *
 * Activity holding the collections of buttons, textViews, editTexts, icons,... ensuring a single
 * place for modifying the visual style of the application
 */
public abstract class DefaultActivity extends Activity {

    protected LinkedList<Button> listButtons;
    protected LinkedList<EditText> listEditTexts;
    protected LinkedList<ImageView> listIcons;
    protected HashMap<Integer, Spinner> mapSpinners;

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
        /*
        empty method, sometimes overriden by the children (if they need to instantiate some
        visual elements like spinners or textviews)
         */
    }
}