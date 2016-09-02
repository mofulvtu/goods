package com.lzg.goods.order.service;

import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;

import com.lzg.goods.order.dao.OrderDao;
import com.lzg.goods.order.domain.Order;
import com.lzg.goods.paper.PageBean;

/**
 * <p>Title:OrderService</p>
 * <p>Description:订单service</p>
 * @author 刘增刚
 * @date 2016年8月31日下午6:51:24
 */
public class OrderService {

	private OrderDao orderDao = new OrderDao();

	/**
	 * 通过订单id加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid){
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 生成订单
	 * @param order
	 */
	public void createOrder(Order order){
		try {
			JdbcUtils.beginTransaction();
			orderDao.add(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过用户查询订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}

}
