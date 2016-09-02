package com.lzg.goods.order.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.lzg.goods.cart.domain.CartItem;
import com.lzg.goods.cart.service.CartItemService;
import com.lzg.goods.order.domain.Order;
import com.lzg.goods.order.domain.OrderItem;
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
	private CartItemService cartItemService = new CartItemService();

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
	
	/**
	 * 生成订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String createOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取所有购物车条目的id，查询
		 */
		String cartItemIds  = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		/*
		 * 2.创建Order
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(String.format("%tF %<tT", new Date()));//下单时间
		order.setStatus(1);//设置状态，1表示未付款
		order.setAddress(req.getParameter("address"));
		User owner = (User) req.getSession().getAttribute("sessionUser");
		order.setOwner(owner);//设置订单所有者
		/*
		 * 
		 */
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList){
			total = total.add(new BigDecimal(cartItem.getSubtotal() + ""));
		}
		order.setTotal(total.doubleValue());//设置总计
		
		/*
		 * 3.创建List<OrderItem>
		 * 一个CartItem对应一个OrderItem
		 */
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(CartItem cartItem : cartItemList){
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());//设置主键
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		System.out.println(order.getOrderItemList().get(0));
		
		/*
		 * 4.调用service完成添加
		 */
		orderService.createOrder(order);
		System.out.println("生成订单。。。。");
		
		//订单生成后需要删除购物车条目
		cartItemService.batchDelete(cartItemIds);
		
		/*
		 * 5.保存订单，转发到ordersucc.jsp
		 */
		req.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";
	}
	
	/**
	 * 加载订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");
		req.setAttribute("btn", btn);
		return "f:/jsps/order/desc.jsp";
	}

}
