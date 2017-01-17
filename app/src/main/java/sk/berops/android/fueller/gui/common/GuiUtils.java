package sk.berops.android.fueller.gui.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.tyres.TyreGUIContainer;

public class GuiUtils {
	
	public static BitmapDrawable LoadDrawable(Context context, String filename) {
		AssetManager assets = context.getResources().getAssets();
	    InputStream buffer;
	    BitmapDrawable drawable;
		try {
			buffer = new BufferedInputStream((assets.open(filename)));
			Bitmap bitmap = BitmapFactory.decodeStream(buffer);
			drawable = new BitmapDrawable(context.getResources(), bitmap);
			return drawable;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isEmptyEditText(EditText e) {
		try {
			if (e.getText().toString().equals("")) {
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException npe) {
			// if EditText is null, we consider the string being empty
			return true;
		}
	}
	
	public static double extractDouble(EditText editText) {
		return Double.parseDouble(editText.getText().toString());
	}
	
	public static int extractInteger(EditText editText) {
		return Integer.parseInt(editText.getText().toString());
	}
	
	public static BitmapDrawable flip(Context context, BitmapDrawable d) {
		Matrix m = new Matrix();
		m.preScale(1, -1);
		Bitmap src = d.getBitmap();
		Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
		dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		return new BitmapDrawable(context.getResources(), dst);
	}
	
	public static int getShade(int start, int middle, int end, double distance) {
		if (distance < 0.5) 
			return getShade(start, middle, distance*2);
		else 
			return getShade(middle, end, (distance - 0.5)*2);
	}
	
	public static int getShade(int start, int end, double distance) {
		if (distance < 0) distance = 0;
		if (distance > 1) distance = 1;
		int alpha = (int) (Color.alpha(start) + Math.round((Color.alpha(end) - Color.alpha(start)) * distance));
		int red = (int) (Color.red(start) + Math.round((Color.red(end) - Color.red(start)) * distance));
		int green = (int) (Color.green(start) + Math.round((Color.green(end) - Color.green(start)) * distance));
		int blue = (int) (Color.blue(start) + Math.round((Color.blue(end) - Color.blue(start)) * distance));
		return 0x00000000 | alpha<<24 | red<<16 | green<<8 | blue;
	}
	
	/**
	 * 
	 * @param objects List of objects from which to look for the one which has been clicked on
	 * @param x X-coordinate of the click
	 * @param y Y-coordinate of the click
	 * @return The object which has been clicked on
	 */
	public static GUIObjectContainer determineObjectClicked(Collection<? extends GUIObjectContainer> objects, float x, float y) {
		for (GUIObjectContainer o : objects) {
			if (x >= o.getX() 
					&& x <= (o.getX() + o.getWidth())
					&& y >= o.getY() 
					&& y <= (o.getY() + o.getHeight())) {
				return o;
			}
		}
		return null;
	}
	
	public static void removeTyreFromContainer(Tyre tyre, Collection<TyreGUIContainer> objects) {
		for (TyreGUIContainer o : objects) {
			if (o.getTyre() == tyre) {
				// We've found the container with the correct tyre

				// UUIDs of the tyres mounted on the parent axle of the tyre container in focus
				ArrayList<UUID> uuids = o.getParentAxle().getTyreIDs();
				for (int i = 0; i < uuids.size(); i++) {
					// Check the parent axle from the tyre that's being removed
					if (uuids.get(i).equals(tyre.getUuid())) {
						// Clear the UUID from the parent axle as well
						uuids.set(i, null);
						break;
					}
				}
				o.setTyre(null);
				return;
			}
		}
	}
}
