package com.wupgig.dao;

import java.util.List;

import com.wupgig.pojo.Address;
import com.wupgig.pojo.Sinfo;

public interface SinfoDAO {
	
	/**
	 * 保存已登录玩家用户名和ip
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午8:00:02
	* @param sinfo
	* @return
	 */
	public int saveSinfo(Sinfo sinfo);
	
	/**
	 * 通过账号查询玩家ip和登录状态
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午8:01:22
	* @param account
	* @return
	 */
	public Sinfo queryIPByAccount(String account);
	
	/**
	 * 根据账号删除在线玩家
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午8:18:02
	* @param account
	* @return
	 */
	public int deleteSinfoByAccount(String account);
	
	/**
	 * 查找在线玩家表中的所有账号
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月29日下午9:32:10
	* @return
	 */
	public List<Sinfo> queryAllAccount();
	
	/**
	 * 根据账号修改玩家状态
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月30日上午8:43:39
	* @param account
	* @return
	 */
	public int updateStatusByAccount(String account, int status);
	
	/**
	 * 根据账号更改玩家的ip地址
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日上午8:32:08
	* @param account
	* @param ip
	* @return
	 */
	public int updateIPByAccount(String account, String ip);
	
	
	/**
	 * 分页查询在线玩家的信息
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午3:23:46
	* @param index
	* @param size
	* @return
	 */
	public List<Sinfo> queryAllByLimit(int index, int size);
	
	/**
	 * 查询在线玩家总个数
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午4:09:13
	* @return
	 */
	public Long queryAllCount();
}
