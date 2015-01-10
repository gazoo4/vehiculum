package sk.berops.android.fueller.gui.maintenance;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.common.TyreDrawer;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class ViewTyreGraphics extends View {
	private Tyre tyre;
	private TyreDrawer tyreDrawer;
	
	public ViewTyreGraphics(Context context, Tyre tyre) {
		super(context);
		init(context);
		this.tyre = tyre;
	}

	private void init(Context context) {
		tyreDrawer = TyreDrawer.getInstance(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTyre(canvas);
	}
	
	private void drawTyre(Canvas canvas) {
		int x = getWidth();
		int y = getHeight();
		tyreDrawer.drawTyre(canvas, tyre, 0, 0, (int) (y * 0.5), y);
	}
}
