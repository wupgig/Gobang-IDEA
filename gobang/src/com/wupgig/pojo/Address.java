package com.wupgig.pojo;

import java.io.Serializable;

/**
 * 用户ip地址表的实体类
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月29日上午8:46:35
 *
 */
public class Address implements Serializable{
	@Override
	public String toString() {
		return "Address [id=" + id + ", account=" + account + ", address=" + address + ", remember=" + remember + "]";
	}
	private int id;
	private String account;
	private String address;
	private int remember;
	public int getRemember() {
		return remember;
	}
	public void setRemember(int remember) {
		this.remember = remember;
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
