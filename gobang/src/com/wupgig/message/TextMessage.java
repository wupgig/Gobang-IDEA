package com.wupgig.message;

/**
 * 给对手发送的聊天文本消息
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月30日下午3:55:49
 *
 */
public class TextMessage extends Message{
	

	private String account;
	private String textMessage;

	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	
	
}
