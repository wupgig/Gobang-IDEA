package com.wupgig.dao.impl;

import java.util.List;

import com.wupgig.dao.SinfoDAO;
import com.wupgig.pojo.Sinfo;

public class SinfoDAOImpl extends BaseDAO implements SinfoDAO{

	@Override
	public int saveSinfo(Sinfo sinfo) {
		// TODO Auto-generated method stub
		String sql = "insert into sinfo values(null, ?, ?, ?)";
		return update(sql, sinfo.getAccount(), sinfo.getAddress(), 0);
	}

	@Override
	public Sinfo queryIPByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "select address,status from sinfo where account = ?";
		return select(Sinfo.class, sql, account);
	}

	@Override
	public int deleteSinfoByAccount(String account) {
		// TODO Auto-generated method stub
		String sql = "delete from sinfo where account = ?";
		return update(sql, account);
	}

	@Override
	public List<Sinfo> queryAllAccount() {
		// TODO Auto-generated method stub
		String sql = "select account, status from sinfo where status != 0";
		return getForList(Sinfo.class, sql);
		
	}

	@Override
	public int updateStatusByAccount(String account, int status) {
		// TODO Auto-generated method stub
		String sql = "update sinfo set status = ? where account = ?";
		return update(sql, status, account);
	}

	@Override
	public int updateIPByAccount(String account, String ip) {
		// TODO Auto-generated method stub
		String sql = "update sinfo set address = ? where account = ?";
		return update(sql, ip, account);
	}

	@Override
	public List<Sinfo> queryAllByLimit(int index, int size) {
		// TODO Auto-generated method stub
		String sql = "select account, status from sinfo where status != 0 limit ?, ?";
		return getForList(Sinfo.class, sql, index, size);
	}

	@Override
	public Long queryAllCount() {
		// TODO Auto-generated method stub
		String sql = "select count(*) from sinfo where status != 0";
		return getValue(sql);
	}

}
