package com.wupgig.common;

import java.util.List;

import com.wupgig.chess.Chess;

import javafx.scene.paint.Color;

/**
 * 通用代码
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年4月12日下午8:47:39
 *
 */
public class Logic {
	
	/**
	 * 判断是否五连
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月12日下午8:43:25
	* @param chessList 棋盘上所有棋子的集合
	* @param chess 当前落子
	* @param arr 用于判断棋盘上是否有棋子
	* @param colors 所有棋子的颜色
	* @return
	 */
	public static boolean isWin(List<Chess> chessList,Chess chess, boolean[][] arr, Color[][] colors) {
//		 棋子少于9个，不可能出现五子
		if (chessList.size() < 9) {
			return false;
		}
		// 当前连子个数
		int count = 1;
		// 横左有几个连子
		for (int i = chess.getX() - 1; i >= 0 && i >= chess.getX() - 4; i--) {
			if (arr[i][chess.getY()] && chess.getColor().equals(colors[i][chess.getY()])) {
				count++;
			} else {
				break;
			}
		}
		// 横右有几个连子
		for (int i = chess.getX() + 1; i <= 14  && i <= chess.getX() + 4; i++) {
			if (arr[i][chess.getY()] && chess.getColor().equals(colors[i][chess.getY()])) {
				count++;
			} else {
				break;
			}
		}
		// 横线是否五连
		if (count >= 5) {
			return true;
		}
		count = 1;
		
		// 竖上有几个连子
		for (int i = chess.getY() - 1; i >= 0 && i >= chess.getY() - 4; i--) {
			if (arr[chess.getX()][i] && chess.getColor().equals(colors[chess.getX()][i])) {
				count++;
			} else {
				break;
			}
		}
		// 竖下有几个连子
		for (int i = chess.getY() + 1; i <= 14 && i <= chess.getY() + 4; i++) {
			if (arr[chess.getX()][i] && chess.getColor().equals(colors[chess.getX()][i])) {
				count++;
			} else {
				break;
			}
		}
		// 竖线是否五连
		if (count >= 5) {
			return true;
		}
		count = 1;
		
		// 右斜上有几个连子
		for (int i = chess.getX() - 1,  j = chess.getY() - 1; 
				i >= 0 &&  i >= chess.getX() - 4 && j >= 0 && 
						j >= chess.getY() - 4; i--, j--) {
			if (arr[i][j] && chess.getColor().equals(colors[i][j])) {
				count++;
			} else {
				break;
			}
			
		}
		
		// 右斜下有几个连子
		for (int i = chess.getX() + 1,  j = chess.getY() + 1; 
				i <= 14 &&  i <= chess.getX() + 4 && j <= 14 && 
						j <= chess.getY() + 4; i++, j++) {
			if (arr[i][j] && chess.getColor().equals(colors[i][j])) {
				count++;
			} else {
				break;
			}
			
		}
		// 右斜线是否五连
		if (count >= 5) {
			return true;
		}
		count = 1;
		// 左斜上有几个连子
		for (int i = chess.getX() + 1,  j = chess.getY() - 1; 
				i <= 14 &&  i <= chess.getX() + 4 && j >= 0 && 
						j >= chess.getY() - 4; i++, j--) {
			if (arr[i][j] && chess.getColor().equals(colors[i][j])) {
				count++;
			} else {
				break;
			}
			
		}
		
		// 左斜下有几个连子
		for (int i = chess.getX() - 1,  j = chess.getY() + 1; 
				i >= 0 &&  i >= chess.getX() - 4 && j <= 14 && 
						j <= chess.getY() + 4; i--, j++) {
			if (arr[i][j] && chess.getColor().equals(colors[i][j])) {
				count++;
			} else {
				break;
			}
			
		}
		// 左斜线是否五连
		if (count >= 5) {
			return true;
		}
		return false;
	}

}
