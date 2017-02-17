package sk.berops.android.caramel.gui.common;

import android.graphics.Canvas;

public abstract class GUIObjectContainer {
	
	/**
	 * X-axis coordinate of the graphics
	 */
	private int x;
	
	/**
	 * Y-axis coordinate of the graphics
	 */
	private int y;
	
	/**
	 * Width of the graphics
	 */
	private int width;
	
	/**
	 * Height of the graphics
	 */
	private int height;
	
	public GUIObjectContainer(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract void draw(Canvas canvas);
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
