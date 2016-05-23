package com.lzg.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;

import com.lzg.goods.user.domain.User;


/**
 * 用户模块持久层
 * @author 刚刚
 */
public class UserDao {
	private QueryRunner queryRunner = new TxQueryRunner();
	
	/**
	 * 更新密码
	 * @param uid
	 * @param password
	 * @throws SQLException
	 */
	public void updatePassword(String uid , String password) throws SQLException{
	    String 	sql = "update t_user set loginpass = ? where uid = ?";
	    queryRunner.update(sql, password, uid);
	}
	/**
	 * 按照uid和loginpass查询
	 * @param uid
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public boolean findByUidAndPassword(String uid, String password) throws SQLException{
		String sql = "select count(*) from t_user where uid = ? and loginpass = ?";
		Number number = (Number) queryRunner.query(sql, new ScalarHandler(), uid , password);
		return number.intValue() > 0;
	}
	/**
	 * 按用户名和密码查询
	 * @param loginname
	 * @param loginpass
	 * @return
	 * @throws SQLException 
	 */
	public User findByLoginnameAndLoginpass(String loginname, String loginpass) throws SQLException{
		String sql = "select * from t_user where loginname = ? and loginpass = ?";
		return queryRunner.query(sql, new BeanHandler<User>(User.class), loginname, loginpass);
	}
	
	/**
	 * 校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "select count(*) from t_user where loginname=?";
	    Number number= (Number) queryRunner.query(sql, new ScalarHandler(), loginname);
		return number.intValue() == 0;
	}

	/**
	 * Email是否注册
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "select count(1) from t_user where email=?";
		Number number = (Number) queryRunner.query(sql, new ScalarHandler(), email);
		return number.intValue() == 0;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @throws SQLException 
	 */
	public void add(User user) throws SQLException{
		String sql = "insert into t_user values(?,?,?,?,?,?)";
		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),
				user.getEmail(),user.isStatus(),user.getActivationCode()};
		queryRunner.update(sql, params);
	}
	
	/**
	 * 通过激活码查询用户
	 * @param activationCode
	 * @throws SQLException 
	 */
	public User findByActivationCode(String activationCode) throws SQLException{
		String sql = "select * from t_user where activationCode = ?";
		return queryRunner.query(sql, new BeanHandler<User>(User.class), activationCode);
	}
	
	/**
	 * 修改用户激活状态
	 * @param status
	 * @param activationCode
	 * @throws SQLException 
	 */
	public void updateStatus(boolean status, String activationCode) throws SQLException{
		String sql = "update t_user set status = ? where activationCode = ?";
		queryRunner.update(sql, status, activationCode);
	}
	
	
}
