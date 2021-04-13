package com.wupgig.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 对战记录实体类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月24日下午9:19:42
 *
 */
public class Record implements Serializable{
	private int id;
	private String black;
	private String white;
	private Timestamp chessTime;
	private int result;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBlack() {
		return black;
	}
	public void setBlack(String black) {
		this.black = black;
	}
	public String getWhite() {
		return white;
	}
	public void setWhite(String white) {
		this.white = white;
	}

	public Timestamp getChessTime() {
		return chessTime;
	}
	public void setChessTime(Timestamp chessTime) {
		this.chessTime = chessTime;
	}

	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Record [id=" + id + ", black=" + black + ", white=" + white + ", chessTime=" + chessTime + ", result="
				+ result + "]";
	}
}
