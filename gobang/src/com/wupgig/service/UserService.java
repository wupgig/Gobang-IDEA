package com.wupgig.service;

import com.wupgig.pojo.User;

public interface UserService {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 如果返回null,说明没有这个用户。反之亦然
     */
    public User queryUserByAccount(String account);
    
    
    /**
     * 根据 用户名和密码查询用户信息
     * @param username
     * @param password
     * @return 如果返回null,说明用户名或密码错误,反之亦然
     */
    public User queryUserByAccountAndPassword(String account, String password);
    
    /**
     * 保存用户信息
     * @param user
     * @return 返回-1表示操作失败，其他是sql语句影响的行数
     */
    public int saveUser(User user);
    
    /**
     * 更新赢棋用户的信息
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:45:48
    * @param account
    * @return
     */
    public int saveWinChessByAccount(String account);
    
    
    /**
     * 更新败棋用户的信息
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:48:48
    * @param account
    * @return
     */
    public int saveLostChessByAccount(String account);
    
    
    /**
     * 更新和棋用户的信息
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:57:34
    * @param account
    * @return
     */
    public int saveDrawChessByAccount(String account);
    
    /**
     * 查询指定用户的积分
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:53:15
    * @param account
    * @return
     */
    public int queryScoreByAccount(String account);
    
    /**
     * 查询指定用户的总局数
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:54:12
    * @param account
    * @return
     */
    public int queryTotalNumsByAccount(String account);
    
    /**
     * 查询指定用户的胜局数
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:54:58
    * @param account
    * @return
     */
    public int queryWinNumsByAccount(String account);
    
    /**
     * 查询指定用户的败局数
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:55:20
    * @param account
    * @return
     */
    public int queryLostNumsByAccount(String account);
    
    
    
    /**
     * 查询指定用户的和局数
    * @Description 
    * @author wupgig
    * @version
    * @date 2021年3月25日下午3:55:56
    * @param account
    * @return
     */
    public int queryDrawNumsByAccount(String account);
}
