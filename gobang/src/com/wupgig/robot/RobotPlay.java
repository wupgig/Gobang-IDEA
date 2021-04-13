package com.wupgig.robot;

import java.util.ArrayList;
import java.util.List;

import com.wupgig.chess.Chess;

import javafx.scene.paint.Color;

/**
 * 用于获取机器人判断出的最佳落子坐标
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年4月12日上午11:22:22
 *
 */
public class RobotPlay{
	// 获取机器人的最佳落子坐标
	/**
	 * 
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年4月12日上午11:29:53
	* @param arr 用于判断棋盘上指定坐标是否有棋子
	* @param color 机器人落子的颜色
	* @param colors 所有棋子的颜色
	* @param size 棋盘的横竖线的条数
	* @return
	 */
	public static Chess getChess(Color[][] colors, boolean[][] arr, Color color, int size) {
		return explore(colors, arr, color, size);
	}

	/**	
	 * 获取需要打分的空位的集合
	 * 对每个非空位置，将其米字形周围的空位添加到集合中
	 * 注意去掉重复的位置
	 * 
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:13:32
	* @param arr 用于判断棋盘上指定坐标是否有棋子
	* @param size 棋盘的横竖线的条数
	* @return
	 */
	private static List<Chess> getallMayRobotChess(boolean[][] arr, int size) {
		List<Chess> allMayRobotChess = new ArrayList<>();
		
		// 搜索棋盘获取可行棋的点,存在重复，
		// 利用addToList(List<RobotChess> allMayRobotChess, int x, int y)去重
		// 原理为，遍历棋盘上所有棋子，其周围米字形(九宫格除了中间的剩下八个)内的空位即为可行棋的点
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				if (arr[i][j]) {
					if (j != 0 && !arr[i][j - 1])
						addToList(allMayRobotChess, i, j - 1);
					if (j != (size - 1) && !arr[i][j + 1])
						addToList(allMayRobotChess, i, j + 1);
					if (i != 0 && j != 0 && !arr[i - 1][j - 1])
						addToList(allMayRobotChess, i - 1, j - 1);
					if (i != 0 && !arr[i - 1][j])
						addToList(allMayRobotChess, i - 1, j);
					if (i != 0 && j != (size - 1) && !arr[i - 1][j + 1])
						addToList(allMayRobotChess, i - 1, j + 1);
					if (i != (size - 1) && j != 0 && !arr[i + 1][j - 1])
						addToList(allMayRobotChess, i + 1, j - 1);
					if (i != (size - 1) && !arr[i + 1][j])
						addToList(allMayRobotChess, i + 1, j);
					if (i != (size - 1) && j != (size - 1) && !arr[i + 1][j + 1])
						addToList(allMayRobotChess, i + 1, j + 1);
				}
			}
		return allMayRobotChess;
	}
	
	
	/**
	 * 对每个非空位置，找到它们四周的位置点(九宫格)
	 * 该方法用来过滤掉其中重复的点，并将不重复的点加入集合中
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午10:49:25
	* @param allMayRobotChess
	* @param x
	* @param y
	 */
	private static void addToList(List<Chess> allMayRobotChess, int x, int y) {
		boolean flag = true;
		for (Chess chess : allMayRobotChess)
			if (chess.getX() == x && chess.getY() == y) {
				flag = false;
				break;
			}
		if (flag) allMayRobotChess.add(new Chess(x, y));
	}
	
	
	/**
	 * 根据棋型计算位置得分
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:23:56
	* @param count 连子个数
	* @param leftStatus 左侧封堵情况 1:空位，2：对方或墙
	* @param rightStatus 右侧封堵情况 1:空位，2：对方或墙
	* @return 分数
	 */
	private static int getScoreBySituation(int count, int leftStatus, int rightStatus) {
		int score = 0;
		
		// 五子情况
		if (count >= 5)
			score += 200000;// 赢了

		// 四子情况
		else if (count == 4) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 50000;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 3000;
			if (leftStatus == 2 && rightStatus == 2)
				score += 1000;
		}

		//三子情况
		else if (count == 3) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 3000;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 1000;
			if (leftStatus == 2 && rightStatus == 2)
				score += 500;
		}
		
		//二子情况
		else if (count == 2) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 500;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 200;
			if (leftStatus == 2 && rightStatus == 2)
				score += 100;
		}
		
		//一子情况
		else if (count == 1) {
			if (leftStatus == 1 && rightStatus == 1)
				score += 100;
			if ((leftStatus == 2 && rightStatus == 1) || (leftStatus == 1 && rightStatus == 2))
				score += 50;
			if (leftStatus == 2 && rightStatus == 2)
				score += 30;
		}
		
		return score;
	}
	
	
	
	/**
	 * 获取该空位在纵向上的得分
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:26:39
	* @param x 位置横坐标
	* @param y 位置纵坐标
	* @param color 机器人落子的颜色
	* @param colors 所有棋子的颜色
	* @param size 棋盘的横竖线的条数
	* @return 评分
	 */
	private static int getVerticalScore(int x, int y, Color color, Color[][] colors, int size) {

		// 自己棋子的颜色
		Color myself  = color;
		
		// 对方棋子的颜色
		Color other = myself.equals(Color.BLACK) ? Color.ALICEBLUE : Color.BLACK;
		// 模拟落子
		colors[x][y] = myself;
		
		//上侧、下侧的状态，用来记录棋型
		int topStatus = 0;
		int bottomStatus = 0;
		//相连棋子个数
		int count = 0;
		
		//扫描记录棋型
		for (int j = y; j < size; j++) {
			if (myself.equals(colors[x][j]))   
				count++;
			else {
				if (colors[x][j] == null)
					bottomStatus = 1;//下侧为空
				else if (other.equals(colors[x][j]))
					bottomStatus = 2;// 下侧被对方堵住
				break;
			}
		}
		for (int j = y - 1; j >= 0; j--) {
			if (myself.equals(colors[x][j]))
				count++;
			else {
				if (colors[x][j] == null)
					topStatus = 1;// 上侧为空
				else if (other.equals(colors[x][j]))
					topStatus = 2;// 上侧被对方堵住
				break;
			}
		}
		
		// 恢复
		colors[x][y] = null;
		
		//根据棋型计算空位分数
		return getScoreBySituation(count, topStatus, bottomStatus);
	}

	
	/**
	 * 获取该点在横向上的得分
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:29:33
	* @param x 位置横坐标
	* @param y 位置纵坐标
	* @param color 机器人落子的颜色
	* @param colors 所有棋子的颜色
	* @param size 棋盘的横竖线的条数
	* @return 评分
	 */
	private static int getLevelScore(int x, int y, Color color, Color[][] colors, int size) {
		// 自己棋子的颜色
		Color myself  = color;
		
		// 对方棋子的颜色
		Color other = myself.equals(Color.BLACK) ? Color.ALICEBLUE : Color.BLACK;
		// 模拟落子
		colors[x][y] = myself;

		//左侧、右侧的状态，用来记录棋型
		int leftStatus = 0;
		int rightStatus = 0;
		// 相连棋子个数
		int count = 0;
		
		//扫描记录棋型
		for (int i = x; i < size; i++) {
			if (myself.equals(colors[i][y]))
				count++;
			else {
				if (colors[i][y] == null)
					rightStatus = 1;// 右侧为空
				else if (other.equals(colors[i][y]))
					rightStatus = 2;// 右侧被对方堵住
				break;
			}
		}
		for (int i = x - 1; i >= 0; i--) {
			if (myself.equals(colors[i][y]))
				count++;
			else {
				if (colors[i][y] == null)
					leftStatus = 1;// 左侧为空
				else if (other.equals(colors[i][y]))
					leftStatus = 2;// 左侧被对方堵住
				break;
			}
		}
		// 恢复
		colors[x][y] = null;
		
		return getScoreBySituation(count, leftStatus, rightStatus);
	}


	/**
	 * 正斜向扫描计算得分,左上到右下
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:30:32
	* @param x 位置横坐标
	* @param y 位置纵坐标
	* @param color 机器人落子的颜色
	* @param colors 所有棋子的颜色
	* @param size 棋盘的横竖线的条数
	* @return 评分
	 */
	private static int getSkewScore1(int x, int y, Color color, Color[][] colors, int size) {
		// 自己棋子的颜色
		Color myself  = color;
		
		// 对方棋子的颜色
		Color other = myself.equals(Color.BLACK) ? Color.ALICEBLUE : Color.BLACK;
		// 模拟落子
		colors[x][y] = myself;

		int topStatus = 0;
		int bottomStatus = 0;
		int count = 0;
		
		for (int i = x, j = y; i < size && j < size; i++, j++) {
			if (myself.equals(colors[i][j]))
				count++;
			else {
				if (colors[i][j] == null)
					bottomStatus = 1;// 下侧为空
				else if (other.equals(colors[i][j]))
					bottomStatus = 2;// 下侧被对方堵住
				break;
			}
		}

		for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
			if (myself.equals(colors[i][j]))
				count++;
			else {
				if (colors[i][j] == null)
					topStatus = 1;// 上侧为空
				else if (other.equals(colors[i][j]))
					topStatus = 2;// 上侧被对方堵住
				break;
			}
		}
		// 恢复
		colors[x][y] = null;
		
		return getScoreBySituation(count, topStatus, bottomStatus);
	}

	
	
	/**
	 * 反斜向扫描计算得分,从右上到左下
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:35:32
	* @param x 位置横坐标
	* @param y 位置纵坐标
	* @param color 机器人落子的颜色
	* @param colors 所有棋子的颜色
	* @param size 棋盘的横竖线的条数
	* @return 评分
	 */
	private static int getSkewScore2(int x, int y, Color color, Color[][] colors, int size) {
		// 自己棋子的颜色
		Color myself  = color;
		
		// 对方棋子的颜色
		Color other = myself.equals(Color.BLACK) ? Color.ALICEBLUE : Color.BLACK;
		// 模拟落子
		colors[x][y] = myself;

		int topStatus = 0;
		int bottomStatus = 0;
		// 从右上到左下
		int count = 0;
		for (int i = x, j = y; i < size && j >= 0; i++, j--) {
			if (myself.equals(colors[i][j]))
				count++;
			else {
				if (colors[i][j] == null)
					bottomStatus = 1;// 下侧为空
				else if (other.equals(colors[i][j]))
					bottomStatus = 2;// 下侧被对方堵住
				break;
			}
		}

		for (int i = x - 1, j = y + 1; i >= 0 && j < size; i--, j++) {
			if (myself.equals(colors[i][j]))
				count++;
			else {
				if (colors[i][j] == null)
					topStatus = 1;// 上侧为空
				else if (other.equals(colors[i][j]))
					topStatus = 2;// 上侧被对方堵住
				break;
			}
		}

		// 恢复
		colors[x][y] = null;
		return getScoreBySituation(count, topStatus, bottomStatus);
	}
	
	
	/**
	 * 为坐标为(x,y)的空位评分
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:35:27
	* @param x
	* @param y
	* @param color 机器人落子的颜色
	* @param colors 所有棋子的颜色
	* @param size 棋盘的横竖线的条数
	* @return 分数
	 */
	private static int getScore(int x, int y, Color color, Color[][] colors, int size) {
		// 对方棋子颜色
		Color otherColor = color.equals(Color.BLACK) ? Color.ALICEBLUE : Color.BLACK;
		//己方棋子和对方棋子模拟落子计算分数和，以达到攻守皆备
		// 纵向得分
		int verticalScore = getVerticalScore(x, y, color, colors, size) + getVerticalScore(x, y, otherColor, colors, size);
		// 横向得分
		int levelScore = getLevelScore(x, y, color, colors, size) + getLevelScore(x, y, otherColor, colors, size);
		// 正斜得分
		int skewScore1 = getSkewScore1(x, y, color, colors, size) + getSkewScore1(x, y, otherColor, colors, size);
		// 反斜得分
		int skewScore2 = getSkewScore2(x, y, color, colors, size) + getSkewScore2(x, y, otherColor, colors, size);
		return verticalScore + levelScore + skewScore1 + skewScore2;
	}
	

	/**
	 *  AI对棋盘分析，控制器的核心方法
	 * 本算法计算出的位置对双方都是有益的
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月27日上午11:40:33
	* @param color 机器人落子的颜色
	* @param colors 所有棋子的颜色
	* @param size 棋盘的横竖线的条数
	* @param arr 用于判断棋盘上是否有棋子
	* @return 机器人最佳下棋坐标
	 */
	private static Chess explore(Color[][] colors, boolean[][] arr, Color color, int size) {
		// 得到可行位置的集合
		List<Chess> allMayRobotChess = getallMayRobotChess(arr, size);
		
		// 所有得分最大且相同的位置
		//打分时可能存在分数相同的位置，将这个位置保存起来随机落子
		List<Chess> allMaxRobotChess = new ArrayList<>();
		
		// 对每个可落子的空位计算分数
		int max = 0;//最大分数
		
		//遍历可行位置集合
		for (Chess chess : allMayRobotChess) {
			
			//计算位置得分并设置位置分数
			int score = getScore(chess.getX(), chess.getY(), color, colors, size);
			chess.setScore(score);
			
			//判断
			if (score > max) max = score;
			
			//如果socre是当前最大值且不是0
			//如果allMaxLocation集合中第一个元素值小于max
			//先清空，然后再添加该位置
			//否则，直接添加该位置
			if (max != 0 && score == max) {
				if (allMaxRobotChess.size() > 0)
					if (allMaxRobotChess.get(0).getScore() < max)
						allMaxRobotChess.clear();
				allMaxRobotChess.add(chess);
			}
		}
		
		//从最高分集合中随机抽取一个位置
		Chess pos = allMaxRobotChess.get((int) (Math.random() * allMaxRobotChess.size()));

		//返回分析的位置
		return new Chess(pos.getX(), pos.getY());
	}
}


