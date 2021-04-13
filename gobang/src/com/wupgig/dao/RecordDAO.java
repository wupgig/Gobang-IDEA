package com.wupgig.dao;

import com.wupgig.pojo.Record;

public interface RecordDAO {
	/**
	 * 插入对战记录
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日下午10:31:34
	* @param record
	* @return
	 */
	public int saveRecord(Record record);
}
