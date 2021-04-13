package com.wupgig.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 用户信息的实体类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月24日下午9:09:49
 *
 */
public class User implements Serializable{
	// 用户id
	private int id;
	// 用户账号
	private String account;
	// 用户密码
	private String password;
	// 注册时间
	private Timestamp regTime;
//	private String regTime;
	// 积分
	private int score;
	// 总对局数
	private int totalNums;
	// 赢棋局数
	private int winNums;
	// 输棋局数
	private int lostNums;
	// 平局局数
	private int drawNums;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getRegTime() {
		return regTime;
	}
	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTotalNums() {
		return totalNums;
	}
	public void setTotalNums(int totalNums) {
		this.totalNums = totalNums;
	}
	public int getWinNums() {
		return winNums;
	}
	public void setWinNums(int winNums) {
		this.winNums = winNums;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", password=" + password + ", regTime=" + regTime
				+ ", score=" + score + ", totalNums=" + totalNums + ", winNums=" + winNums + ", lostNums=" + lostNums
				+ ", drawNums=" + drawNums + "]";
	}
	public int getLostNums() {
		return lostNums;
	}
	public void setLostNums(int lostNums) {
		this.lostNums = lostNums;
	}
	public int getDrawNums() {
		return drawNums;
	}
	public void setDrawNums(int drawNums) {
		this.drawNums = drawNums;
	}

	
}
