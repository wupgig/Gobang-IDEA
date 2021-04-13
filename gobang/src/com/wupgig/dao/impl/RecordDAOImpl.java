package com.wupgig.dao.impl;

import com.wupgig.dao.RecordDAO;
import com.wupgig.pojo.Record;

public class RecordDAOImpl extends BaseDAO implements RecordDAO{

	/**
	 * 保存对战信息到数据库
	 */
	@Override
	public int saveRecord(Record record) {
		String sql = "insert into chess_record values(?,?,?,?,?)";
		return update(sql, null, record.getBlack(), 
				record.getWhite(), record.getChessTime(), record.getResult());
	}
}
