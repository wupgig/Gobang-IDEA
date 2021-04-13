package com.wupgig.dao.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wupgig.common.JdbcUtils;

public abstract class BaseDAO {
	//通用的增删改操作
	public int update(String sql,Object ...args){//sql中占位符的个数与可变形参的长度相同！
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//1.获取数据库的连接
			conn = JdbcUtils.getConnection();
			//2.预编译sql语句，返回PreparedStatement的实例
			ps = conn.prepareStatement(sql);
			//3.填充占位符
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);//小心参数声明错误！！
			}
			//4.执行
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//5.资源的关闭
			JdbcUtils.closeResource(null, ps);
			
		}
		return 0;
	}
	
	/**
	 * 
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月24日下午10:19:44
	* @param <T>通用的查询操作
	* @param clazz
	* @param sql
	* @param args
	* @return
	 */
	public <T> T select(Class<T> clazz,String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();

			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			// 获取结果集的元数据 :ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// 通过ResultSetMetaData获取结果集中的列数
			int columnCount = rsmd.getColumnCount();

			if (rs.next()) {
				T t = clazz.newInstance();
				// 处理结果集一行数据中的每一个列
				for (int i = 0; i < columnCount; i++) {
					// 获取列值
					Object columValue = rs.getObject(i + 1);

					// 获取每个列的列名
					// String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 给t对象指定的columnName属性，赋值为columValue：通过反射
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeResource(null, ps, rs);

		}

		return null;
	}
	
	
	// 通用的查询操作，用于返回数据表中的多条记录构成的集合（version 2.0：考虑上事务）
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			// 获取结果集的元数据 :ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// 通过ResultSetMetaData获取结果集中的列数
			int columnCount = rsmd.getColumnCount();
			// 创建集合对象
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				T t = clazz.newInstance();
				// 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
				for (int i = 0; i < columnCount; i++) {
					// 获取列值
					Object columValue = rs.getObject(i + 1);

					// 获取每个列的列名
					// String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 给t对象指定的columnName属性，赋值为columValue：通过反射
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columValue);
				}
				list.add(t);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeResource(null, ps, rs);

		}

		return null;
	}
	
	
	
	/**
	 * 用于查询特殊值的通用的方法  返回值为Long类型
	* @Description 
	* @author wupgig
	* @version
	* @date 2021年3月31日下午4:12:25
	* @param <E>
	* @param conn
	* @param sql
	* @param args
	* @return
	 */
	public <E> E getValue(String sql,Object...args){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);
				
			}
			
			rs = ps.executeQuery();
			if(rs.next()){
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JdbcUtils.closeResource(null, ps, rs);
			
		}
		return null;
		
	}	
}
