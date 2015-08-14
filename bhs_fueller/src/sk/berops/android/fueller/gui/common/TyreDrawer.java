package sk.berops.android.fueller.gui.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.maintenance.TyreGUIContainer;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;

public class TyreDrawer {
	private Paint tyrePaint;

	private static TyreDrawer instance = null;

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

	/**
	 * Padding around the picture
	 */
	private static final int PADDING = 3;

	/**
	 * Defines the granularity of the flashing animation progress steps
	 */
	private static final int FLASHING_CYCLE_DURATION = 10;

	public static TyreDrawer getInstance() {
		
		// singleton builder
		if (instance == null) { 
			instance = new TyreDrawer();
		}
		return instance;
	}

	private TyreDrawer() {
		selectedTyre = null;
		tyrePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		tyrePaint.setStyle(Paint.Style.FILL);
	}

	public void drawTyre(Canvas canvas, TyreGUIContainer tc) {
		int x = tc.getX();
		int y = tc.getY();
		int width = tc.getWidth();
		int height = tc.getHeight();
		
		if ((width < PADDING + 2) || (height < PADDING + 2)) {
			Log.d("ERROR", "View size too small to draw things...");
			return;
		}
		// TODO: do some fancy light effects if the tyre is selected

		Rect dst = new Rect(x + PADDING, y + PADDING, x + width - PADDING, y + height - PADDING);
		RectF dstF = new RectF(dst);
		
		int alpha = 0;
		if (isFlashingMode()) {
			// flashingPhase progressing in the interval <-1..0..1>
			// convert to alpha <0..255..0>
			alpha = (int) ((1 - Math.abs(getFlashingPhase())) * 255);
		}

		if (tc.getTyre() == null) {
			// draw dummy tyre structure
			tyrePaint.setColor(Color.BLACK);
			tyrePaint.setAlpha(255);
			canvas.drawBitmap(tc.getTyreEmptyBitmap(), null, dst, tyrePaint);
			if (this.isFlashingMode()) {
				tyrePaint.setAlpha(alpha);
				canvas.drawBitmap(tc.getTyreFlashingBitmap(), null, dst, tyrePaint);
			}
		} else {
			// draw colored rectangle
			tyrePaint.setColor(tc.getTyreColor());
			tyrePaint.setAlpha(255);
			canvas.drawRoundRect(dstF, 10, 10, tyrePaint);
			// draw tyre structure
			canvas.drawBitmap(tc.getTyreBitmap(), null, dst, tyrePaint);
			if (this.isFlashingMode() && tc.getTyre() == getSelectedTyre()) {
				tyrePaint.setAlpha(alpha);
				//
			}
		}
	}

	// TODO: not used yet
	public LayerDrawable getTyreDrawable(Resources resources, TyreGUIContainer tc) {
		return getTyreDrawable(resources, tc, 50);
	}

	// TODO: not used yet
	public LayerDrawable getTyreDrawable(Resources resources, TyreGUIContainer tc, int alpha) {
		BitmapDrawable structureDrawable;
		ShapeDrawable shapeDrawable;
		LayerDrawable layerDrawable;
		Drawable[] layers;
		Bitmap tyreBitmap;
		RoundRectShape rect;
		float[] radius = { 10, 10, 10, 10, 10, 10, 10, 10 };

		layers = new Drawable[2];

		layerDrawable = new LayerDrawable(layers);

		tyreBitmap = tc.getTyreBitmap();
		structureDrawable = new BitmapDrawable(resources, tyreBitmap);
		layerDrawable.setDrawableByLayerId(0, structureDrawable);

		rect = new RoundRectShape(radius, null, null);
		shapeDrawable = new ShapeDrawable(rect);
		shapeDrawable.setAlpha(alpha);
		layerDrawable.setDrawableByLayerId(1, shapeDrawable);
		return layerDrawable;
	}

	public Tyre getSelectedTyre() {
		return selectedTyre;
	}

	public void setSelectedTyre(Tyre selectedTyre) {
		this.selectedTyre = selectedTyre;
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
}
