package com.wupgig.message;

public class ReplayMessage extends Message{
	// 表示哪种悔棋消息
	private int flag;
	// 悔棋请求
	public static final int REPLAY_REQUEST = 1;
	// 同意悔棋
	public static final int REPLAY_AGRRE = 2;
	// 拒绝悔棋
	public static final int REPLAY_REFUSE= 3;
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
		
	

}
