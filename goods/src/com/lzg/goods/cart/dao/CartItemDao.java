package com.lzg.goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

import com.lzg.goods.book.domain.Book;
import com.lzg.goods.cart.domain.CartItem;
import com.lzg.goods.user.domain.User;

/**
 * <p>Title:CartItemDao</p>
 * <p>Description:购物车dao层</p>
 * @author 刘增刚
 * @date 2016年8月29日上午10:10:40
 */
public class CartItemDao {
	private QueryRunner queryRunner = new TxQueryRunner();

	/**
	 * 查询某本图书的购物车条目是否存在
	 * @param uid
	 * @param bid
	 * @return
	 * @throws SQLException 
	 */
	public CartItem findByUidAndBid(String uid, String bid) throws SQLException {
		String sql = "select * from t_cartItem where uid =? and bid = ?";
		Map<String, Object> map = queryRunner.query(sql, new MapHandler(), uid,
				bid);
		CartItem cartItem = toCartItem(map);
		return cartItem;
	}

	/**
	 * 修改指定条目的数量
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException 
	 */
	public void updateQuantity(String cartItemId, int quantity)
			throws SQLException {
		String sql = "update t_cartItem set quantity = ? where cartItemId = ?";
		queryRunner.update(sql, quantity, cartItemId);
	}

	/**
	 * 添加购物条目到数据库
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem) throws SQLException {
		String sql = "insert into t_cartItem(cartItemId,quantity,bid,uid)"
				+ " values(?,?,?,?)";
		Object[] params = { cartItem.getCartItemId(), cartItem.getQuantity(),
				cartItem.getBook().getBid(), cartItem.getUser().getUid() };
		queryRunner.update(sql, params);
	}

	/*
	 * 把一个map映射成一个CartItem
	 */
	private CartItem toCartItem(Map<String, Object> map) {
		if (map == null || map.size() == 0) {
			return null;
		}
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}

	/*
	 * 把多个Map(List<Map>)映射成多个CartItem(List<CartItem>)
	 */
	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList) {
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for (Map<String, Object> map : mapList) {
			cartItemList.add(toCartItem(map));
		}
		return cartItemList;
	}

	/**
	 * 通过用户查询购物车条目
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException {
		String sql = "select * from t_cartItem c,t_book b where c.bid = b.bid and uid = ? order by c.orderBy";
		List<Map<String, Object>> mapList = queryRunner.query(sql,
				new MapListHandler(), uid);
		return toCartItemList(mapList);
	}

}
