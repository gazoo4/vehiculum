package sk.berops.android.vehiculum.gui.common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import sk.berops.android.vehiculum.gui.tyres.TyreGUIContainer;
import sk.berops.android.vehiculum.gui.tyres.TyreSchemeHelper;

public class TyreDrawer {
	private Paint tyrePaint;

	private static TyreDrawer instance = null;

	/**
	 * Padding around the picture
	 */
	private static final int PADDING = 3;

	public static TyreDrawer getInstance() {
		
		// singleton builder
		if (instance == null) { 
			instance = new TyreDrawer();
		}
		return instance;
	}

	private TyreDrawer() {
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

		Rect dst = new Rect(x + PADDING, y + PADDING, x + width - PADDING, y + height - PADDING);
		RectF dstF = new RectF(dst);
		
		int alpha = 0;
		TyreSchemeHelper helper = TyreSchemeHelper.getInstance();
		if (helper.isFlashingMode()) {
			// flashingPhase progressing in the interval <-1..0..1>
			// convert to alpha <0..255..0>
			alpha = (int) ((1 - Math.abs(helper.getFlashingPhase())) * 255);
		}

		if (tc.getTyre() == null) {
			// draw dummy tyre structure
			tyrePaint.setColor(Color.BLACK);
			tyrePaint.setAlpha(255);
			canvas.drawBitmap(tc.getTyreEmptyBitmap(), null, dst, tyrePaint);
			if (helper.isFlashingMode()) {
				tyrePaint.setAlpha(alpha);
				canvas.drawBitmap(tc.getTyreFlashingBitmap(), null, dst, tyrePaint);
			}
		} else {
			tyrePaint.setColor(tc.getTyreColor());
			// selected tyre should be flashing, so apply alpha on top of the color
			if (helper.isFlashingMode() && tc.getTyre() == helper.getSelectedTyre()) {
				alpha = 255 - alpha;
				tyrePaint.setAlpha(alpha);
			}
			// draw colored rectangle
			canvas.drawRoundRect(dstF, 10, 10, tyrePaint);
			// draw tyre structure
			tyrePaint.setAlpha(255);
			canvas.drawBitmap(tc.getTyreBitmap(), null, dst, tyrePaint);
		}
	}
}
