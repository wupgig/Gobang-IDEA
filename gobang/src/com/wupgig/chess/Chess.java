package com.wupgig.chess;

import javafx.scene.paint.Color;
/**
 * 棋子类，里面包含棋子的颜色，和在棋盘上的x y坐标
 * 和该棋子在小棋仙帮忙落子时评估的分数
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月19日上午11:29:50
 *
 */
public class Chess {
	// 棋子在棋盘上的x轴坐标
	private int x;
	// 棋子在棋盘上的y轴坐标
	private int y;
	// 棋子颜色
	private Color color;
	// 小棋仙落子时的分数
	private int score;
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
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

	public Chess(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public Chess(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
}
