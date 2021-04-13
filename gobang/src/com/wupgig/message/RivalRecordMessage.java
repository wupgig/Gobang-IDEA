package com.wupgig.message;

import com.wupgig.pojo.User;

/**
 * 用于标识请求获取对手账号的消息类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月25日上午11:27:40
 *
 */
public class RivalRecordMessage extends Message {
	// 从两个静态常量中取值
	private int rivalRecord;

	// 用来保存对手信息
	private  User user;

	// 请求消息
	public static final int REQUEST = 1;
	// 响应消息
	public static final int RESPONSE = 2;
	
	public int getRivalRecord() {
		return rivalRecord;
	}
	public void setRivalRecord(int rivalRecord) {
		this.rivalRecord = rivalRecord;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
