package com.wupgig.dao.impl;

import com.wupgig.dao.UserDAO;
import com.wupgig.pojo.User;

public class UserDAOImpl extends BaseDAO implements UserDAO {

	/**
	 * 判断是否有该账号
	 */
	@Override
	public User queryUserByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "SELECT id,account,`password`,regtime regTime,score,"
				+ "totalnums totalNums,winnums winNums,lostnums lostNums,drawnums drawNums" + 
				" FROM chess_user WHERE account = ?";
		return select(User.class, sql, account);
	}

	/**
	 * 用户登录
	 */
	@Override
	public User queryUserByAccountAndPassword(String account, String password) {
		// TODO Auto-generated method stub
		String sql = "SELECT id,account,`password`,regtime regTime"
				+ ",score,totalnums totalNums,winnums winNums,lostnums lostNums,drawnums drawNums" + 
				" FROM chess_user WHERE account = ? and password = ?";
		return select(User.class, sql, account, password);
	}
	/**
	 * 注册账号
	 */
	@Override
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO chess_user VALUES(?,?,?,?,?,?,?,?,?)";
		return update(sql, null,user.getAccount(), user.getPassword(),user.getRegTime(),
				 100, 0, 0, 0, 0);
	}
	/**
	 * 
	 */
	@Override
	public int saveWinChessByAccount(String account) {
		// TODO Auto-generated method stub
		int newScore = this.queryScoreByAccount(account) + 1;
		int newTotalNums = this.queryTotalNumsByAccount(account) + 1;
		int newWinNums = this.queryWinNumsByAccount(account) + 1;
		String sql = "update chess_user set score = ?, totalnums = ?, winnums = ? where account = ?";
		return update(sql, newScore, newTotalNums, newWinNums, account);
	}

	@Override
	public int saveLostChessByAccount(String account) {
		// TODO Auto-generated method stub
		int newScore = this.queryScoreByAccount(account) - 1;
		int newTotalNums = this.queryTotalNumsByAccount(account) + 1;
		int newLostNums = this.queryLostNumsByAccount(account) + 1;
		String sql = "update chess_user set score = ?, totalnums = ?, lostnums = ? where account = ?";
		return update(sql, newScore, newTotalNums, newLostNums, account);
	}

	@Override
	public int saveDrawChessByAccount(String account) {
		// TODO Auto-generated method stub
		int newTotalNums = this.queryTotalNumsByAccount(account) + 1;
		int newDrawNums = this.queryDrawNumsByAccount(account) + 1;
		String sql = "update chess_user set totalnums = ?, drawnums = ? where account = ?";
		return update(sql, newTotalNums, newDrawNums, account);
	}

	@Override
	public int queryScoreByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "select score from chess_user where account = ?";
		return select(User.class, sql, account).getScore();
	}

	@Override
	public int queryTotalNumsByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "select totalnums totalNums from chess_user where account = ?";
		return select(User.class, sql, account).getTotalNums();
	}

	@Override
	public int queryWinNumsByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "select winnums winNums from chess_user where account = ?";
		return select(User.class, sql, account).getWinNums();
	}

	@Override
	public int queryLostNumsByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "select lostnums lostNums from chess_user where account = ?";
		return select(User.class, sql, account).getLostNums();
	}

	@Override
	public int queryDrawNumsByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "select drawnums drawNums from chess_user where account = ?";
		return select(User.class, sql, account).getDrawNums();
	}



}
