package com.lzg.goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;

import com.lzg.goods.cart.dao.CartItemDao;
import com.lzg.goods.cart.domain.CartItem;

/**
 * <p>Title:CartItemService</p>
 * <p>Description:购物车service层</p>
 * @author 刘增刚
 * @date 2016年8月29日上午10:14:48
 */
public class CartItemService {
	private CartItemDao cartItemDao = new CartItemDao();

	/**
	 * 添加条目
	 * @param cartItem
	 */
	public void add(CartItem cartItem) {
		/*
		 * 1.使用uid和bid去数据库中查询这个条目是否存在
		 */
		try {
			CartItem _cartItem = cartItemDao.findByUidAndBid(cartItem.getUser()
					.getUid(), cartItem.getBook().getBid());
			if(_cartItem == null){//如果原来没有这个条目，那么添加条目
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}else{//如果原来有这个条目，修改数量
				//使用原有条目和新条目之和，作为新的数量
				int quantity = _cartItem.getQuantity() + cartItem.getQuantity();
				//修改数据库数量
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		 
	}

	/**
	 * 通过用户获取购物车信息
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid) {
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
