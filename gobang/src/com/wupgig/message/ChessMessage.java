package com.wupgig.message;


/**
 * 包含棋子在棋盘上的 x y 和棋子颜色坐标
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月22日下午9:39:53
 *
 */
public class ChessMessage extends Message{
	private int x;
	private int y;
	private int blackOrWhite;
	
	public ChessMessage(int x, int y, int blackOrWhite) {
		this.x = x;
		this.y = y;
		this.blackOrWhite = blackOrWhite;
	}

	public int getBlackOrWhite() {
		return blackOrWhite;
	}

	public void setBlackOrWhite(int blackOrWhite) {
		this.blackOrWhite = blackOrWhite;
	}

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


}
