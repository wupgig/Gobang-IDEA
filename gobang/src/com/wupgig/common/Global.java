package com.wupgig.common;

import com.wupgig.chess.ChessBoard;

/**
 * 用来保存用户 和 对局相关的信息
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月20日下午10:19:41
 *
 */
public class Global {
	// 自己的ip
	public static String myIP;
	// 自己的端口号
	public static int myPort = 8089;
	// 对手的ip
	public static String oppoIP;
	// 临时对手ip，用于新局
	public static String temporaryOppoIP;
	
	// 对手的端口号
	public static int oppoPort = 8088;
	// 用户账号
	public static String account;
	// 棋盘信息
	public static ChessBoard chessBoard;

}
