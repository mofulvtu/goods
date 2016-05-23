package com.lzg.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

import com.lzg.goods.user.dao.UserDao;
import com.lzg.goods.user.domain.User;
import com.lzg.goods.user.service.exception.UserException;

/**
 * 业务层
 * @author 刚刚
 *
 */
public class UserService {
	
	private UserDao userDao = new UserDao();
	/**
	 * 
	 * @param uid
	 * @param oldPass
	 * @param newPass
	 * @throws UserException
	 */
	public void updatePassword(String uid, String oldPass, String newPass)throws UserException{
		try {
			/*
			 * 1.校验老密码
			 */
			boolean bool = userDao.findByUidAndPassword(uid, oldPass);
			if(!bool){
				throw new UserException("原密码错误！");
			}
			/*
			 * 2.修改密码
			 */
			userDao.updatePassword(uid, newPass);
		} catch (SQLException e) {
            throw new RuntimeException(e);
		}
	}	
	/**
	 * 登录功能
	 * @param user
	 * @return
	 */
	public User login(User user){
		try {
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 用户名校验
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Email校验
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 添加用户
	 * @param user
	 */
	public void regist(User user){
		/*
		 * 1.数据补齐
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		/*
		 * 2.向数据库插入
		 */
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		/*
		 * 3.发邮件
		 */
		/*
		 * 把配置文件加载到properties
		 */
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException();
		}
		/*
		 * 登录邮件服务器，得到session
		 */
		String host = properties.getProperty("host");//服务器主机名
		String name = properties.getProperty("username");//登录名
		String pass = properties.getProperty("password");//登录密码
		Session session = MailUtils.createSession(host, name, pass);
		/*
		 * 创建Mail对象
		 */
		String from = properties.getProperty("from");
		String to = user.getEmail();
		String subject = properties.getProperty("subject");
		/*
		 * MessageFormat,format方法会把第一个参数中的{0}，使用第二个参数来替换
		 * 例如：MessageFormat,format("你好{0}，你{1}！"，"张三"，"去死吧")；返回"你好张三，你去死吧！"
		 */
		String content = MessageFormat.format(properties.getProperty("content"),user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		/*
		 * 发送邮件
		 */
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 用户激活
	 * @param activationCode
	 * @return
	 * @throws UserException 
	 * @throws Exception 
	 */
	public void activation(String activationCode) throws UserException{
		/*
		 * 1.通过激活码查询用户
		 * 2.如果User为null,说明是无效激活码，抛出异常，给出异常信息(无效激活码)
		 * 3.查看用户状态是否为true,如果为true，抛出异常，给出异常信息(请不要二次激活)
		 * 4.修改用户状态为true
		 */
		try {
			User user = userDao.findByActivationCode(activationCode);
			if(user == null) throw new UserException("无效的激活码！");
			if(user.isStatus())throw new UserException("您已经激活过了，不要二次激活！");
			userDao.updateStatus(true, activationCode);//修改状态
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
