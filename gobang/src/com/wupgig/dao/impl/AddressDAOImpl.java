package com.wupgig.dao.impl;

import com.wupgig.dao.AddressDAO;
import com.wupgig.pojo.Address;

/**
 * 
 * @author wupgig
 * @email 1725059354@qq.com
 * @version
 * @date 2021年3月29日上午8:54:59
 *
 */
public class AddressDAOImpl extends BaseDAO implements AddressDAO{

	@Override
	public Address queryAccountByIP(String address) {
		// TODO Auto-generated method stub
		String sql = "select id,account,address,remember from address_user where address = ?";
		return select(Address.class, sql, address);
	}

	@Override
	public int saveAddress(Address address) {
		// TODO Auto-generated method stub
		// 默认不记住密码
		String sql = "insert into address_user values(null,?,?,?)";
		return update(sql, address.getAccount(), address.getAddress(),0);
	}

	@Override
	public int updateRemember(int remember, String account) {
		// TODO Auto-generated method stub
		String sql = "update address_user set remember = ? where account = ?";
		return update(sql, remember, account);
	}

	@Override
	public int updateAccount(String ip, String account) {
		// TODO Auto-generated method stub
		String sql = "update address_user set account = ? where address = ?";
		return update(sql, account, ip);
	}

}
