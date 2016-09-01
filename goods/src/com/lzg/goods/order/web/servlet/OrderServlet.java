package com.lzg.goods.order.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

import com.lzg.goods.order.domain.Order;
import com.lzg.goods.order.service.OrderService;
import com.lzg.goods.paper.PageBean;
import com.lzg.goods.user.domain.User;

/**
 * <p>Title:OrderServlet</p>
 * <p>Description:订单servlet</p>
 * @author 刘增刚
 * @date 2016年8月31日下午6:52:09
 */
public class OrderServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderService orderService = new OrderService();

	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch (RuntimeException e) {
			}
		}
		return pc;
	}

	/**
	 * 获取url 同BookServlet
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * 获取我的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到pc:如果页面传递，使用页面的，否则，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获得查询条件，本方法就是用户id
		 */
		User user = (User) req.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		/*
		 * 4.使用pc和uid调用service#myOrders得到PageBean
		 */
		System.out.println(pc+"======"+uid);
		
		PageBean<Order> pb = orderService.myOrders(uid, pc);
		
		System.out.println(pb);
		
		/*
		 * 5.给PageBean设置url，保存PageBean，转发到/jsps/order/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/order/list.jsp";

	}

}
