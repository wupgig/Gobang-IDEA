package com.wupgig.service.impl;

import com.wupgig.dao.UserDAO;
import com.wupgig.dao.impl.UserDAOImpl;
import com.wupgig.pojo.User;
import com.wupgig.service.UserService;

public class UserServiceImpl implements UserService{
	private UserDAO userDAO = new UserDAOImpl();

	@Override
	public User queryUserByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.queryUserByAccount(account);
	}

	@Override
	public User queryUserByAccountAndPassword(String account, String password) {
		// TODO Auto-generated method stub
		return userDAO.queryUserByAccountAndPassword(account, password);
	}

	@Override
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		return userDAO.saveUser(user);
	}

	@Override
	public int saveWinChessByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.saveWinChessByAccount(account);
	}

	@Override
	public int saveLostChessByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.saveLostChessByAccount(account);
	}

	@Override
	public int saveDrawChessByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.saveDrawChessByAccount(account);
	}

	@Override
	public int queryScoreByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.queryScoreByAccount(account);
	}

	@Override
	public int queryTotalNumsByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.queryTotalNumsByAccount(account);
	}

	@Override
	public int queryWinNumsByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.queryWinNumsByAccount(account);
	}

	@Override
	public int queryLostNumsByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.queryLostNumsByAccount(account);
	}

	@Override
	public int queryDrawNumsByAccount(String account) {
		// TODO Auto-generated method stub
		return userDAO.queryDrawNumsByAccount(account);
	}
	
	

}
