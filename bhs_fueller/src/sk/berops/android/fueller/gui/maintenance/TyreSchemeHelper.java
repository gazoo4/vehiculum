package sk.berops.android.fueller.gui.maintenance;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.common.TyreDrawer;

public class TyreSchemeHelper {
	
	/**
	 * Defines the granularity of the flashing animation progress steps
	 */
	private static final int FLASHING_CYCLE_DURATION = 10;
	
	/**
	 * Defining whether the empty tyre containers should be flashing in the GUI
	 * or not
	 */
	private boolean flashingMode;

	/**
	 * Variable controls the progress of the flashing animation Holds values
	 * from interval <-1, 1>
	 */
	private double flashingPhase = -1;
	
	/**
	 * Variable that holds the link to the selected tyre which we want display
	 * in a different color
	 */
	private Tyre selectedTyre;
	
	private static TyreSchemeHelper instance = null;
	
	public static TyreSchemeHelper getInstance() {
		
		// singleton builder
		if (instance == null) { 
			instance = new TyreSchemeHelper();
		}
		return instance;
	}
	
	private TyreSchemeHelper() {
		selectedTyre = null;
	}
	
	/**
	 * This method is used to control the progress of the flashing animation and
	 * it controls the flashingPhase values between -1 and 1
	 */
	public void progressFlashingPhase() {
		double phase = getFlashingPhase();
		phase += 1.0 / FLASHING_CYCLE_DURATION;
		if (phase >= 1) {
			phase = -1;
		}
		setFlashingPhase(phase);
	}
	
	public double getFlashingPhase() {
		return flashingPhase;
	}

	public void setFlashingPhase(double flashingPhase) {
		this.flashingPhase = flashingPhase;
	}
	
	public boolean isFlashingMode() {
		return flashingMode;
	}

	public void setFlashingMode(boolean flashingMode) {
		this.flashingMode = flashingMode;
	}
	
	public Tyre getSelectedTyre() {
		return selectedTyre;
	}

	public void setSelectedTyre(Tyre selectedTyre) {
		this.selectedTyre = selectedTyre;
	}
}
