package sk.berops.android.fueller.gui.common;

import java.io.IOException;
import java.io.InputStream;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
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
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;

public class TyreDrawer {
	private final static String tyreSummerBitmapFilename = "axles/tyre_summer.png";
	private final static String tyreAllSeasonsBitmapFilename = "axles/tyre_allseasons.png";
	private final static String tyreWinterBitmapFilename = "axles/tyre_winter.png";
	private final static String tyreSpikesBitmapFilename = "axles/tyre_spikes.png";
	private final static String tyreEmptyBitmapFilename = "axles/tyre_empty.png";
	private Bitmap tyreSummerBitmap;
	private Bitmap tyreAllSeasonsBitmap;
	private Bitmap tyreWinterBitmap;
	private Bitmap tyreSpikesBitmap;
	private Bitmap tyreEmptyBitmap;
	private AssetManager manager;
	private Paint tyrePaint;
	
	private static TyreDrawer instance = null;
	private static Context context = null;
	
	public static TyreDrawer getInstance(Context newContext) {
		if (context != newContext || instance == null) { // the code never reaches the second part of the formula, which is expected. extra verbosity present to make clear this is singleton
			context = newContext;
			instance = new TyreDrawer(context);
		} 
		return instance;
	}
	
	private TyreDrawer(Context context) {
		this.manager = context.getAssets();
		init();
	}
	
	private void init() {
		tyrePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		tyrePaint.setStyle(Paint.Style.FILL);
	}
	
	public Bitmap getTyreBitmap(Tyre tyre) {
		if (tyre == null) return getTyreEmptyBitmap();
		if (tyre.getSeason() == null) return getTyreSummerBitmap();
		
		switch (tyre.getSeason()) {
		case ALL_SEASON:
			return getTyreAllSeasonsBitmap();
		case SPIKES:
			return getTyreSpikesBitmap();
		case SUMMER:
			return getTyreSummerBitmap();
		case WINTER:
			return getTyreWinterBitmap();
		default:
			Log.d("ERROR", "Unknown tyre type encountered");
			return null;
		}
	}
	
	public Bitmap getTyreSummerBitmap() {
		if (tyreSummerBitmap == null) {
			tyreSummerBitmap = loadBitmap(tyreSummerBitmapFilename);
		}
		
		return tyreSummerBitmap;
	}
	
	public Bitmap getTyreAllSeasonsBitmap() {
		if (tyreAllSeasonsBitmap == null) {
			tyreAllSeasonsBitmap = loadBitmap(tyreAllSeasonsBitmapFilename);
		}
		
		return tyreAllSeasonsBitmap;
	}
	
	public Bitmap getTyreWinterBitmap() {
		if (tyreWinterBitmap == null) {
			tyreWinterBitmap = loadBitmap(tyreWinterBitmapFilename);
		}
		
		return tyreWinterBitmap;
	}
	
	public Bitmap getTyreSpikesBitmap() {
		if (tyreSpikesBitmap == null) {
			tyreSpikesBitmap = loadBitmap(tyreSpikesBitmapFilename);
		}
		
		return tyreSpikesBitmap;
	}
	
	public Bitmap getTyreEmptyBitmap() {
		if (tyreEmptyBitmap == null) {
			tyreEmptyBitmap = loadBitmap(tyreEmptyBitmapFilename);
		}
		
		return tyreEmptyBitmap;
	}
	
	private Bitmap loadBitmap(String filename) {
		Bitmap bitmap = null;
		
		try {
			InputStream istr = manager.open(filename);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			Log.d("ERROR", "init: problem reading asset "+ filename);
		} catch (Exception e) {
			Log.d("ERROR", "init: exception "+ e.getStackTrace());
		}
		
		return bitmap;
	}
	
	public BitmapDrawable getTyreBitmapDrawable(Resources resources, Tyre tyre) {
		return new BitmapDrawable(resources, getTyreBitmap(tyre));
	}
	
	public int getTyreColor(Tyre tyre) {
		if (tyre == null) {
			return 0xFF000000;
		} else {
			return GuiUtils.getShade(Color.GREEN, 0xFFFFFF00, Color.RED, tyre.getWearLevel()/100.0); //orange in the middle
		}
	}
	
	public void drawTyre(Canvas canvas, Tyre tyre, int x, int y, int width, int height) {
		Rect dst = new Rect(x, y, x + width, y + height);
		RectF dstF = new RectF(dst);
		
		if (tyre == null) {
			canvas.drawBitmap(getTyreEmptyBitmap(), null, dst, tyrePaint); //draw dummy tyre structure
		} else {
			tyrePaint.setColor(getTyreColor(tyre));
			canvas.drawRoundRect(dstF, 10, 10, tyrePaint); //draw colored rectangle
			canvas.drawBitmap(getTyreBitmap(tyre), null, dst, tyrePaint); //draw tyre structure
		}
	}
	
	public LayerDrawable getTyreDrawable(Resources resources, Tyre tyre) {
		BitmapDrawable structureDrawable;
		ShapeDrawable shapeDrawable;
		LayerDrawable layers;
		Bitmap tyreBitmap;
		RoundRectShape rect;
		float[] radius = {10,10,10,10,10,10,10,10};
		
		layers= new LayerDrawable(null);
		
		tyreBitmap = getTyreBitmap(tyre);
		structureDrawable = new BitmapDrawable(resources, tyreBitmap);
		layers.setDrawableByLayerId(0, structureDrawable);
		
		rect = new RoundRectShape(radius, null, null);
		shapeDrawable = new ShapeDrawable(rect);
		shapeDrawable.setAlpha(100);
		layers.setDrawableByLayerId(1, shapeDrawable);
		return layers;
	}
}
