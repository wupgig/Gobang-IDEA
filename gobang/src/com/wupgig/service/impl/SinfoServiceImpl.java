package com.wupgig.service.impl;

import java.util.List;

import com.wupgig.dao.SinfoDAO;
import com.wupgig.dao.impl.SinfoDAOImpl;
import com.wupgig.pojo.Sinfo;
import com.wupgig.service.SinfoService;

public class SinfoServiceImpl implements SinfoService{
	private SinfoDAO sinfoDAO = new SinfoDAOImpl();

	@Override
	public int saveSinfo(Sinfo sinfo) {
		// TODO Auto-generated method stub
		return sinfoDAO.saveSinfo(sinfo);
	}

	@Override
	public Sinfo queryIPByAccount(String account) {
		// TODO Auto-generated method stub
		return sinfoDAO.queryIPByAccount(account);
	}

	@Override
	public int deleteSinfoByAccount(String account) {
		// TODO Auto-generated method stub
		return sinfoDAO.deleteSinfoByAccount(account);
	}

	@Override
	public List<Sinfo> queryAllAccount() {
		// TODO Auto-generated method stub
		return sinfoDAO.queryAllAccount();
	}

	@Override
	public int updateStatusByAccount(String account, int status) {
		// TODO Auto-generated method stub
		return sinfoDAO.updateStatusByAccount(account, status);
	}

	@Override
	public int updateIPByAccount(String account, String ip) {
		// TODO Auto-generated method stub
		return sinfoDAO.updateIPByAccount(account, ip);
	}

	@Override
	public List<Sinfo> queryAllByLimit(int index, int size) {
		// TODO Auto-generated method stub
		return sinfoDAO.queryAllByLimit(index, size);
	}

	@Override
	public Long queryAllCount() {
		// TODO Auto-generated method stub
		return sinfoDAO.queryAllCount();
	}

}
