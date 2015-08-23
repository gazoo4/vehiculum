package sk.berops.android.fueller.gui.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.maintenance.TyreGUIContainer;
import sk.berops.android.fueller.gui.maintenance.TyreSchemeHelper;
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
