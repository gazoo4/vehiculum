package sk.berops.android.fueller.gui.maintenance;

import java.util.ArrayList;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Axle.AxleType;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;
import sk.berops.android.fueller.gui.common.TyreDrawer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class ViewTyreChangeGraphics extends View implements Runnable {

	private static final double tyreWidth = 0.2; //percentages of the canvas block
	private static final double tyreHeight = 0.5; //percentages of the canvas block
	private static final int tyrePadding = 0; //pixels padding between tandem tires
	
	private Car car;
	private Paint backgroundPaint;
	private Paint chasisPaint;
	private TyreDrawer tyreDrawer;
	private Context context;
	private TyreConfigurationScheme tyreScheme;
	
	public ViewTyreChangeGraphics(Context context, Car car, TyreConfigurationScheme tyreScheme) {
		super(context);
		this.car = car;
		this.context = context;
		this.tyreScheme = tyreScheme;
		init();
	}
	
	private void init() {
		backgroundPaint = new Paint();
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setColor(Color.TRANSPARENT);
		
		chasisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		chasisPaint.setStyle(Paint.Style.FILL);
		
		tyreDrawer = TyreDrawer.getInstance();	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBackground(canvas);
		drawChasis(canvas);
		//TODO drawEngine(canvas)
		postDelayed(this, 10);
	}
	
	@Override
	public void run() {
		if (tyreDrawer.isFlashingMode()) {
			tyreDrawer.progressFlashingPhase();
		}
		//updateState();
		invalidate();
	}
	
	private void drawBackground(Canvas canvas) {
		canvas.drawPaint(backgroundPaint);
	}
	
	private void drawChasis(Canvas canvas) {
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
			drawAxle(canvas, axle, x, yOffset, yRelative, i);
		}
	}
	
	private void drawAxle(Canvas canvas, Axle axle, int width, int yOffset, int height, int axlePosition) {
		LinkedList<Tyre> tyres = axle.getTyres();
		AxleType type = axle.getAxleType();
		
		int x = 0;
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
		
		if (type == AxleType.SINGLE) {
			x = (int) (1.0 * width/2 - 1.0 * width/2 * tyreWidth);
			TyreGUIContainer tc = new TyreGUIContainer(context, tyres.get(0), x, y + yOffset, (int) (width * tyreWidth), (int) (height * tyreHeight));
			tyreDrawer.drawTyre(canvas, tc);
		} else {
			for (int i = -tyres.size()/2; i < tyres.size()/2; i++) {
				x = (int) (1.0 * i * width * tyreWidth);
				if (i == -2) {
					x -= tyrePadding;
				} else if (i == 1) {
					x += tyrePadding;
				}
				if (i < 0) {
					x += width;
				}
				Tyre tyre = tyres.get((i + tyres.size()) % tyres.size());
				TyreGUIContainer tc = new TyreGUIContainer(context, tyre, x, y + yOffset, (int) (width * tyreWidth), (int) (height * tyreHeight));
				tyreDrawer.drawTyre(canvas, tc);
			}
		}
	}
	
	public TyreConfigurationScheme getTyreScheme() {
		return tyreScheme;
	}
	
	public void setTyreScheme(TyreConfigurationScheme tyreScheme) {
		this.tyreScheme = tyreScheme;
	}
}