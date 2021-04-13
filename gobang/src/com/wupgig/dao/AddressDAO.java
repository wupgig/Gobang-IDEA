package com.wupgig.dao;

import com.wupgig.pojo.Address;

public interface AddressDAO {
	
	/**
	 * 根据ip地址查询用户的账号
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日上午8:49:41
	* @param address
	* @return
	 */
	public Address queryAccountByIP(String address);
	
	
	/**
	 * 保存用户账号和ip地址
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日上午8:52:22
	* @param address
	* @return
	 */
	public int saveAddress(Address address);
	
	/**
	 * 根据账号更新记住密码
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日上午10:56:08
	* @param remember
	* @return
	 */
	public int updateRemember(int remember, String account);
	
	/**
	 * 根据ip地址更新账号名字
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午10:39:34
	* @param account
	* @return
	 */
	public int updateAccount(String ip, String account);
}
