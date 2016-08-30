package com.lzg.goods.order.domain;

import com.lzg.goods.book.domain.Book;

/**
 * <p>Title:OrderItem</p>
 * <p>Description:订单条目</p>
 * @author 刘增刚
 * @date 2016年8月30日下午6:18:04
 */
public class OrderItem {
	private String orderItemId;// 主键
	private int quantity;// 数量
	private double subtotal;// 小计
	private Book book;// 关联的book
	private Order order;// 所属订单

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
