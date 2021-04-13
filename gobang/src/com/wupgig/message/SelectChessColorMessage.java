package com.wupgig.message;

public class SelectChessColorMessage extends Message{
	private int color;
	// 黑白颜色
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	
}
