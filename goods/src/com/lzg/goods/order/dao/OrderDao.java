package com.lzg.goods.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

import com.lzg.goods.book.domain.Book;
import com.lzg.goods.order.domain.Order;
import com.lzg.goods.order.domain.OrderItem;
import com.lzg.goods.paper.Expression;
import com.lzg.goods.paper.PageBean;
import com.lzg.goods.paper.PageConstants;

/**
 * <p>Title:OrderDao</p>
 * <p>Description:订单dao</p>
 * @author 刘增刚
 * @date 2016年8月31日下午6:50:34
 */
public class OrderDao {
	private QueryRunner queryRunner = new TxQueryRunner();

	/**
	 * 通过订单id加载订单信息
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public Order load(String oid) throws SQLException {
		String sql = "select * from t_order where oid = ?";
		Order order = queryRunner.query(sql,
				new BeanHandler<Order>(Order.class), oid);
		loadOrderItem(order);//加载订单条目
		return order;
	}

	/**
	 * 生成订单
	 * @param order
	 * @throws SQLException 
	 */
	public void add(Order order) throws SQLException {
		/*
		 * 1.插入订单
		 */
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = { order.getOid(), order.getOrdertime(),
				order.getTotal(), order.getStatus(), order.getAddress(),
				order.getOwner().getUid() };
		queryRunner.update(sql, params);
		/*
		 * 2.循环遍历订单的所有条目，让每个条目生成一个Object[] 多个条目就对应Object[][] 执行批处理
		 */
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();
		Object[][] objs = new Object[len][];
		for (int i = 0; i < len; i++) {
			OrderItem item = order.getOrderItemList().get(i);
			System.out.println(item.getBook().getAuthor());
			objs[i] = new Object[] { item.getOrderItemId(), item.getQuantity(),
					item.getSubtotal(), item.getBook().getBid(),
					item.getBook().getBname(), item.getBook().getCurrPrice(),
					item.getBook().getImage_b(), item.getOrder().getOid() };
		}
		queryRunner.batch(sql, objs);
	}

	/**
	 * 按用户查询订单
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid", "=", uid));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 通用的查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Order> findByCriteria(List<Expression> exprList, int pc)
			throws SQLException {
		/*
		 * 1.得到ps 2.得到tr 3.得到beanList 4.创建PageBean，返回
		 */
		/*
		 * 1.得到ps
		 */
		int ps = PageConstants.ORDER_PAGE_SIZE;// 每页记录数
		/*
		 * 2.通过exprList来生成where子句
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();// SQL中有问号，对应问号值
		for (Expression expr : exprList) {
			/*
			 * 添加一个条件上 1)以and开头 2)条件的名称 3)条件的运算符，可以是=、!=、>、< ...is null, is
			 * null没有值 4)如果条件不是is null，再追加问号，然后再向params中添加与问号对应的值
			 */
			whereSql.append(" and ").append(expr.getName()).append(" ")
					.append(expr.getOperator()).append(" ");
			// where 1=1 and bid = ?
			if (!expr.getOperator().equals("is null")) {
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		/*
		 * 3.得到tr 总记录数 select count(*) from t_book where
		 */
		String sql = "select count(*) from t_order " + whereSql;
		Number number = (Number) queryRunner.query(sql, new ScalarHandler(),
				params.toArray());
		int tr = number.intValue();// 得到总记录数

		/*
		 * 4.得到beanList，即当前页记录
		 */
		sql = "select * from t_order" + whereSql
				+ " order by ordertime desc limit ? ,?";
		params.add((pc - 1) * ps);// 第一个问号 计算当前页首行记录的下标
		params.add(ps);// 一共查询几行，就是每页记录数

		List<Order> beanList = queryRunner.query(sql, new BeanListHandler<>(
				Order.class), params.toArray());
		// 虽然已经获取所有订单，但每个订单中并没有订单条目
		// 遍历每个订单，为其加载它的所有订单条目
		for (Order order : beanList) {
			loadOrderItem(order);
		}

		/*
		 * 5.创建PageBean，设置参数
		 */
		PageBean<Order> pb = new PageBean<>();
		/*
		 * url由servlet完成
		 */
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setBeanList(beanList);
		pb.setTr(tr);

		System.out.println(sql);
		return pb;
	}

	/*
	 * 为指定的order加载它的所有OrderItem
	 */
	private void loadOrderItem(Order order) throws SQLException {
		/*
		 * 1.sql语句 2.执行得到List<OrderItem> 3.设置给Order对象
		 */
		String sql = "select * from t_orderItem where oid = ?";
		List<Map<String, Object>> mapList = queryRunner.query(sql,
				new MapListHandler(), order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}

	/**
	 * 把多个Map转换成多个OrderItem
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (Map<String, Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);

		}
		return orderItemList;
	}

	/**
	 * 把一个Map转换成一个OrderItem
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

}
