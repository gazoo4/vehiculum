package sk.berops.android.fueller.gui.tyres;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Axle.Type;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;

public class ViewTyreChangeGraphics extends View implements Runnable {

	private static final double tyreWidth = 0.2; //percentages of the canvas block
	private static final double tyreHeight = 0.5; //percentages of the canvas block
	private static final int tyrePadding = 2; //pixels padding between tandem tires
	
	private Car car;
	private Paint backgroundPaint;
	private Paint chasisPaint;
	private TyreSchemeHelper helper;
	private Context context;
	private TyreChangeEntry entry;
	private TyreConfigurationScheme tyreScheme;
	private LinkedList<TyreGUIContainer> tyreObjects;
	private boolean chasisUpdated = true;
	
	public ViewTyreChangeGraphics(Context context, Car car, TyreChangeEntry entry) {
		super(context);
		this.car = car;
		this.context = context;
		this.entry = entry;
		this.tyreScheme = entry.getTyreScheme();
		init();
	}
	
	private void init() {
		backgroundPaint = new Paint();
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setColor(Color.TRANSPARENT);
		
		chasisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		chasisPaint.setStyle(Paint.Style.FILL);

		helper = TyreSchemeHelper.getInstance();
		tyreObjects = new LinkedList<>();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBackground(canvas);
		drawChasis(canvas);
		//TODO drawEngine(canvas)
		postDelayed(this, 40);
	}
	
	@Override
	public void run() {
		if (helper.isFlashingMode()) {
			helper.progressFlashingPhase();
		}
		//updateState();
		invalidate();
	}
	
	private void drawBackground(Canvas canvas) {
		canvas.drawPaint(backgroundPaint);
	}
	
	private void drawChasis(Canvas canvas) {
		if (chasisUpdated) {
			int x = getWidth();
			int y = getHeight();
			
			LinkedList<Axle> axles = getTyreScheme().getAxles();
			int count = axles.size();
			int i = 0; //as axlePosition 0: last, 1: first, 2+: middle
			int yOffset, yRelative;
			
			yRelative = Math.round(y/count);
			for(Axle axle : axles) {
				yOffset = Math.round(y * i++/count);
				if (i == count) i = 0;
				parseAxle(axle, x, yOffset, yRelative, i);
			}
		}
		drawObjects(canvas);
		chasisUpdated = false;
	}
	
	private void parseAxle(Axle axle, int width, int yOffset, int height, int axlePosition) {
		LinkedList<Tyre> tyres = entry.getTyresByIDs(axle.getTyreIDs().values());
		Type type = axle.getType();
		
		int x;
		int y = 0;
		
		switch (axlePosition) {
			case 0: //last; we want to position the tyre at the end of the canvas block
				y = (int) (height * (1 - tyreHeight));
				break;
			case 1: //first; nothing to do here, we want to position the tyre at the beginning of the canvas block
				break;
			default: //middle; here we want to position the tyre into the middle of the canvas block
				y = (int) (height * (0.5 - tyreHeight/2));
				break;
		}
		
		if (type == Axle.Type.SINGLE) {
			x = (int) (1.0 * width/2 - 1.0 * width/2 * tyreWidth);
			TyreGUIContainer tc = new TyreGUIContainer(context, tyres.get(0), x, y + yOffset, (int) (width * tyreWidth), (int) (height * tyreHeight), axle, 0);
			tyreObjects.add(tc);
		} else {
			for (int i = 0; i < tyres.size(); i++) {
				int size = tyres.size();

				int sign;
				if (i < (size/2.0)) {
					// For wheels on the left
					sign = 1;
				} else {
					// For wheels on the right
					sign = -1;
				}
				// Width Multiplier
				int wMult = (int) Math.floor((i * 2.0 / size));
				// Padding Multiplier
				// pMult = (int) -abs(i - size/2 - 0.5) + size/2 - 1
				int pMult = (int) (((-1) * Math.floor(Math.abs(i - size/2.0 + 0.5))) + (size/2.0 - 1));
				// Tyre Multiplier
				// tMult = (int) -abs(i - size/2) + size/2
				int tMult =  (int) (((-1) * Math.abs(i - (size/2.0))) + (size/2.0));
				x = wMult * width + sign * (pMult * tyrePadding + (int) (tMult * tyreWidth * width));
				System.out.println("TYRE");
				System.out.println("wMult: "+ wMult +" pMult: "+ pMult +" tMult: "+ tMult + " sign: "+ sign);
				System.out.println("Width: "+ width +" X: "+ x);

				Tyre tyre = tyres.get(i);
				TyreGUIContainer tc = new TyreGUIContainer(context, tyre, x, y + yOffset, (int) (width * tyreWidth), (int) (height * tyreHeight), axle, i);
				tyreObjects.add(tc);
			}
		}
	}
	
	private void drawObjects(Canvas canvas) {
		for (TyreGUIContainer t : tyreObjects) {
			t.draw(canvas);
		}
	}
	
	public TyreConfigurationScheme getTyreScheme() {
		return tyreScheme;
	}
	
	public void setTyreScheme(TyreConfigurationScheme tyreScheme) {
		this.tyreScheme = tyreScheme;
	}
	
	public LinkedList<TyreGUIContainer> getTyreGUIObjects() {
		return tyreObjects;
	}
}