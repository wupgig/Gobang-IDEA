package com.wupgig.pojo;

import java.io.Serializable;

/**
 * 在线玩家实体类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月29日下午7:57:31
 *
 */
public class Sinfo implements Serializable{


	private int id;
	private String account;
	private String address;
	private int status;
	
	
	@Override
	public String toString() {
		return "Sinfo [id=" + id + ", account=" + account + ", address=" + address + ", status=" + status + "]";
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
