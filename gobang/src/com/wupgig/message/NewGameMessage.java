package com.wupgig.message;

/**
 * 新局消息类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月23日下午2:30:57
 *
 */
public class NewGameMessage extends Message{
	private String account;

	private int state;
    // 请求
    public static final int REQUEST = 0;
    // 同意新局
    public static final int OK = 1;
    // 拒绝新局
    public static final int NO = 2;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
    public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
    
}
