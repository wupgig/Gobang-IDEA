package com.wupgig.service.impl;

import com.wupgig.dao.AddressDAO;
import com.wupgig.dao.impl.AddressDAOImpl;
import com.wupgig.pojo.Address;
import com.wupgig.service.AddressService;

public class AddressServiceImpl implements AddressService{
	private AddressDAO addressDAO = new AddressDAOImpl();
	@Override
	public Address queryAccountByIP(String address) {
		// TODO Auto-generated method stub
		return addressDAO.queryAccountByIP(address);
	}

	@Override
	public int saveAddress(Address address) {
		// TODO Auto-generated method stub
		return addressDAO.saveAddress(address);
	}

	@Override
	public int updateRemember(int remember, String account) {
		// TODO Auto-generated method stub
		return addressDAO.updateRemember(remember, account);
	}

	@Override
	public int updateAccount(String ip, String account) {
		// TODO Auto-generated method stub
		return addressDAO.updateAccount(ip, account);
	}

}
