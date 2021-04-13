package com.wupgig.service.impl;

import com.wupgig.dao.RecordDAO;
import com.wupgig.dao.impl.RecordDAOImpl;
import com.wupgig.pojo.Record;
import com.wupgig.service.RecordService;

public class RecordServiceImpl implements RecordService{
	private RecordDAO recordDAO = new RecordDAOImpl();

	@Override
	public int saveRecord(Record record) {
		// TODO Auto-generated method stub
		return recordDAO.saveRecord(record);
	}

}
