package com.lzg.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lzg.goods.user.domain.User;
import com.lzg.goods.user.service.UserService;
import com.lzg.goods.user.service.exception.UserException;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

/**
 * 用户模块WEB层
 * @author 刚刚
 *
 */
public class UserServlet extends BaseServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
	
	/**
	 * ajax 用户名是否注册
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("123");
		/*
		 * 1.获取用户名
		 */
		String loginname = req.getParameter("loginname");
		System.out.println(loginname);
		/*
		 * 2.通过service得到校验结果
		 */
		boolean b =userService.ajaxValidateLoginname(loginname);
		System.out.println(b);
		/*
		 * 3.发给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}
	/**
	 * ajax Eamil是否注册
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取Email
		 */
		String email = req.getParameter("email");
		/*
		 * 2.通过service得到校验结果
		 */
		boolean b =userService.ajaxValidateEmail(email);
		/*
		 * 3.发给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}
	/**
	 * ajax 验证码校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取输入框中的验证码
		 */
		String verifyCode = req.getParameter("verifyCode");
		/*
		 * 2.获取图片上的真实的校验码
		 */
		String vcode = (String) req.getSession().getAttribute("vCode");
		/*
		 * 3.进行忽略大小写比较，得到结果
		 */
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		/*
		 * 4.发给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}
	
	/**
	 * 注册功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        /*
         * 1.封装表单数据到User对象
         */
        User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
        /*
         * 2.校验
         */
        Map<String, String> errors = validateRegist(formUser, req.getSession());
        if(errors.size() > 0){
        	req.setAttribute("form", formUser);
        	req.setAttribute("errors", errors);
        	return "f:/jsps/user/regist.jsp";
        }
        /*
         * 3.使用service完成业务
         */
        userService.regist(formUser);
        /*
         * 4.保存成功信息，转发到msg.jsp显示！
         */
        req.setAttribute("code", "success");
        req.setAttribute("msg", "注册成功，请马上到邮箱激活！");
		return "f:/jsps/msg.jsp";
	}
	/*
	 * 注册校验(后台校验)
	 * 对表单的字段进行逐个校验，如果有错误，使用当前字段名称为key,错误信息为value,保存到map中
	 */
	private Map<String, String> validateRegist(User formUser,HttpSession session){
		Map<String, String> errors = new HashMap<String,String>();
		/*
		 * 1.校验登录名
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()){
			errors.put("loginname", "用户名不能为空！");
		}else if(loginname.length() < 3 || loginname.length() > 20){
			errors.put("loginname", "用户名长度必须在3-20之间！");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "用户名已被注册！");
		}
		
		/*
		 * 2.校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空！");
		}else if(loginpass.length() < 3 || loginpass.length() > 20){
			errors.put("loginpass", "密码长度必须在3-20之间！");
		}
		/*
		 * 3.确认密码校验
		 */
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()){
			errors.put("reloginpass", "确认密码不能为空！");
		}else if(!reloginpass.equals(loginpass)){
			errors.put("reloginpass", "两次输入不一致！");
		}
		/*
		 * 4.Email校验
		 */
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()){
			errors.put("email", "Email不能为空！");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			errors.put("email", "Email格式错误！");
		}else if(!userService.ajaxValidateEmail(email)){
			errors.put("loginname", "Email已被注册！");
		}
		/*
		 * 5.验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	
	/**
	 * 激活功能
	 * @return
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("activation()...");
		/*
		 * 1.获取参数激活码
		 * 2.用激活码调用service方法完成激活
		 *   >service方法有可能抛出异常，获取异常信息，保存到request中，转发到msg.jsp显示
		 * 3.保存成功信息到request，转发到msg.jsp显示
		 */
		String activationCode = req.getParameter("activationCode");
		System.out.println(activationCode);
		
		try {
			userService.activation(activationCode);
			req.setAttribute("code", "success");//通知msg.jsp显示对号
			req.setAttribute("msg", "恭喜，激活成功，请马上登录！");
		} catch (UserException e) {
			//说明service抛出异常
			req.setAttribute("code", "error");//通知msg.jsp显示x
		}
		
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 登录功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        /*
         * 1.封装表单数据到User对象
         */
        User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
        /*
         * 2.校验表单数据
         */
        Map<String, String> errors = validateLogin(formUser, req.getSession());
        if(errors.size() > 0){
        	req.setAttribute("form", formUser);
        	req.setAttribute("errors", errors);
        	return "f:/jsps/user/login.jsp";
        }
        /*
         * 3.调用service完成查询
         */
        User user = userService.login(formUser);
        /*
         * 4.判断
         */
        if(user == null){
        	req.setAttribute("msg", "用户名或密码错误！");
        	req.setAttribute("user", formUser);
        	return "f:/jsps/user/login.jsp";
        }else{
        	if(!user.isStatus()){
        		req.setAttribute("msg", "您还没有激活！");
        		req.setAttribute("user", formUser);
        		return "f:/jsps/user/login.jsp";
        	}else{
        		//保存用户到session中
        		req.getSession().setAttribute("sessionUser", user);
        		//获取用户名保存到cookie中
        		String loginname = user.getLoginname();
        		loginname = URLEncoder.encode(loginname, "utf-8");
        		Cookie cookie = new Cookie("loginname", loginname);
        		cookie.setMaxAge(60 * 60 * 24 * 10);//保存十天
        		resp.addCookie(cookie);
        		return "r:/index.jsp";//重定向到页面
        	}
        }
	}
	/**
	 * 退出功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
	/**
	 * 注册校验(后台校验)
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String, String> validateLogin(User formUser,HttpSession session){
		Map<String, String> errors = new HashMap<String,String>();
		/*
		 * 1.校验登录名
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()){
			errors.put("loginname", "用户名不能为空！");
		}else if(loginname.length() < 3 || loginname.length() > 20){
			errors.put("loginname", "用户名长度必须在3-20之间！");
		}else if(userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "用户名不存在！");
		}
		
		/*
		 * 2.校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空！");
		}else if(loginpass.length() < 3 || loginpass.length() > 20){
			errors.put("loginpass", "密码长度必须在3-20之间！");
		}
		/*
		 * 3.验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	/**
	 * 修改密码
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updatePassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据到user中
		 * 2.从session中获取uid
		 * 3.使用uid和表单中的oldPass和newPass来调用service方法
		 *   >如果出现异常，保存异常信息到request中，转发到pwd.jsp
		 * 4.保存成功信息到request中
		 * 5.转发到msg.jsp
		 */
		User userForm  = CommonUtils.toBean(req.getParameterMap(), User.class);
		User user = (User) req.getSession().getAttribute("sessionUser");
		if(user == null){
			req.setAttribute("msg", "您还没登录！");
			return "f:/jsps/user/login.jsp";
		}
		
		try {
			userService.updatePassword(user.getUid(), userForm.getLoginpass(), userForm.getNewpass());
			req.setAttribute("msg", "修改密码成功!");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());//保存异常信息到request中
			req.setAttribute("user", userForm);//为了回显
			return "f:/jsps/user/pwd.jsp";
		}
		
	}

}
