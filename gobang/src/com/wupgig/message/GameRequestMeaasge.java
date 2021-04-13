package com.wupgig.message;

/**
 * 请求对战消息类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月29日下午8:44:30
 *
 */
public class GameRequestMeaasge extends Message{
	private String Account;
	
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	// 对战请求消息类型，1， 2，3
	private int requestType;
	
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	// 对战请求
	public final static int GAME_REQUEST = 1;
	// 同意对战
	public final static int GAME_AGRRE = 2;
	// 拒绝对战
	public final static int GAME_REFUSE= 3;

}
