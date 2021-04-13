package com.wupgig.common;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtils {
	 private static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
	 
	/**
	 * 获取连接
	 * @return 如果返回null,说明获取连接失败<br/>有值就是获取连接成功
	 */
	public static Connection getConnection(){
	    Connection conn = conns.get();
	    if (conn == null) {
	        try {
	    		// 1.读取配置文件中的4个基本信息
	    		InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");
	
	    		Properties pros = new Properties();
	    		pros.load(is);
	
	    		String user = pros.getProperty("username");
	    		String password = pros.getProperty("password");
	    		String url = pros.getProperty("url");
	    		String driverClass = pros.getProperty("driver");
	
	    		// 2.加载驱动
	    		Class.forName(driverClass);
	
	    		// 3.获取连接
	    		conn = DriverManager.getConnection(url, user, password);
	    		// 保存到ThreadLocal对象中，供后面的jdbc操作使用
	            conns.set(conn); 
	            // 设置为手动管理事务
//	            conn.setAutoCommit(false);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return conn;
	}
    /**
     * 提交事务
     */
    public static void commit(Connection conn) {
        if (conn != null) { // 如果不等于null，说明 之前使用过连接，操作过数据库
            try {
            	conn.commit(); // 提交 事务
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 一定要执行remove操作，否则就会出错。（因为Tomcat服务器底层使用了线程池技术）
//        conns.remove();
    }
    
    /**
     * 回滚事务
     */
    public static void rollback(Connection conn)  {
        if (conn != null) { // 如果不等于null，说明 之前使用过连接，操作过数据库
            try {
            	conn.rollback();//回滚事务
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }
        // 一定要执行remove操作，否则就会出错。（因为Tomcat服务器底层使用了线程池技术）
//        conns.remove();
    }
    /**
     * 设置事务的手动提交
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月27日下午5:22:51
    * @param conn
     */
    public static void disableAutocommit(Connection conn) {
    	if (conn != null) {
    		try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * 关闭数据库连接
     * @param conn
     * @throws SQLException 
*/
    public static void close(Connection conn){
        try {
        	if (conn != null && !conn.isClosed()) {
	            conn.close();
	            conns.remove();
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭资源
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月24日下午10:24:51
    * @param conn
    * @param ps
     */
	public static void closeResource(Connection conn,Statement ps){
		try {
			if(ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null) {
				conn.close();
				conns.remove();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 关闭资源
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月24日下午10:24:25
    * @param conn
    * @param ps
    * @param rs
     */
	public static void closeResource(Connection conn,Statement ps,ResultSet rs){
		try {
			if(ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null) {
				conn.close();
				conns.remove();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
