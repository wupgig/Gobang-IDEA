package com.wupgig.message;

/**
 * 对战结果消息类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月25日下午2:11:58
 *
 */
public class ResultMessage extends Message{
	// 表示是否为认输，默认是
	private boolean lose = true;
	// 自己的账号
	private String account;
	// 对局结果, 1, 2, 3
	private int result;
	// 黑胜
	public static final int BLACK_WIN = 1;
	// 白胜
	public static final int WHITE_WIN = 2;
	// 黑方发送平局
	public static final int BLACK_DRAW = 3;
	// 白方发送平局
	public static final int WHITE_DRAW = 4;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public boolean isLose() {
		return lose;
	}
	public void setLose(boolean lose) {
		this.lose = lose;
	}
	
	
}
