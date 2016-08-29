package com.lzg.goods.cart.domain;

import java.math.BigDecimal;

import com.lzg.goods.book.domain.Book;
import com.lzg.goods.user.domain.User;

/**
 * <p>Title:CartItem</p>
 * <p>Description:购物车条目类</p>
 * @author 刘增刚
 * @date 2016年8月29日上午10:09:46
 */
public class CartItem {
	private String cartItemId;// 主键
	private int quantity;// 数量
	private Book book;// 条目对应的图书
	private User user;// 所属用户

	// 添加小计方法
	public double getSubtotal() {
		/*
		 * 使用BigDecimal不会有误差. 要求必须使用String类型构造器
		 */
		BigDecimal b1 = new BigDecimal(book.getCurrPrice() + "");
		BigDecimal b2 = new BigDecimal(quantity + "");
		BigDecimal b3 = b1.multiply(b2);
		return b3.doubleValue();
	}

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static void main(String[] args) {
		// double类型不精确，所以会出现这种情况
		System.out.println(2.0 - 1.1);// 0.8999999999999999
		
		// Java为我们提供了一个叫BigDecimal的类。
		BigDecimal bd1 = new BigDecimal(2.0 + "");
		BigDecimal bd2 = new BigDecimal(1.1 + "");
		BigDecimal bd3 = bd1.subtract(bd2);
		System.out.println(bd3.doubleValue());
	}

}
