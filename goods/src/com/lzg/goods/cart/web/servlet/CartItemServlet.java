package com.lzg.goods.cart.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.lzg.goods.book.domain.Book;
import com.lzg.goods.cart.domain.CartItem;
import com.lzg.goods.cart.service.CartItemService;
import com.lzg.goods.user.domain.User;

/**
 * <p>Title:CartItemServlet</p>
 * <p>Description:购物车Servlet</p>
 * @author 刘增刚
 * @date 2016年8月29日上午10:15:52
 */
public class CartItemServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CartItemService cartItemService = new CartItemService();

	/**
	 * 查看购物车
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.得到uid
		 */
		User user = (User) req.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		/*
		 * 2.通过service得到当前用户的购物车条目
		 */
		List<CartItem> cartItemList = cartItemService.myCart(uid);
		/*
		 * 3.保存并转发到/cart/list.jsp
		 */
		req.setAttribute("cartItemList", cartItemList);

		return "f:/jsps/cart/list.jsp";
	}

	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据到CartItem(bid,quantity)
		 */
		Map map = req.getParameterMap();
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = (User) req.getSession().getAttribute("sessionUser");
		cartItem.setBook(book);
		cartItem.setUser(user);
		
		/*
		 * 2.调用service完成添加
		 */
		cartItemService.add(cartItem);
		
        /*
         * 3.查询所有条目，转发到list.jsp显示
         */
		return myCart(req, resp);
	}

}
