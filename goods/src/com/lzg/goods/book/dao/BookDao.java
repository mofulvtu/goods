package com.lzg.goods.book.dao;

import org.apache.commons.dbutils.QueryRunner;

import cn.itcast.jdbc.TxQueryRunner;

/**
 * <p>Title:BookDao</p>
 * <p>Description:dao层</p>
 * @author 刘增刚
 * @date 2016年7月29日下午2:23:58
 */
public class BookDao {
	
	QueryRunner queryRunner = new TxQueryRunner();

}
