package com.lzg.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

import com.lzg.goods.book.domain.Book;
import com.lzg.goods.category.domain.Category;
import com.lzg.goods.paper.Expression;
import com.lzg.goods.paper.PageBean;
import com.lzg.goods.paper.PageConstants;

/**
 * <p>Title:BookDao</p>
 * <p>Description:dao层</p>
 * @author 刘增刚
 * @date 2016年7月29日下午2:23:58
 */
public class BookDao {

	QueryRunner queryRunner = new TxQueryRunner();

	/**
	 * 按id查询
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public Book findByBid(String bid) throws SQLException{
		String sql = "select * from t_book where bid = ?";
		//一行记录中包含了很多的book属性，还有一个cid属性
		Map<String, Object> map = queryRunner.query(sql, new MapHandler(), bid);
		//把map中除了cid以外的属性映射到Book对象中
		Book book = CommonUtils.toBean(map, Book.class);
		//把map中的cid属性映射到Category中，即这个Category只有cid
		Category category = CommonUtils.toBean(map, Category.class);
		//两者建立关系
		book.setCategory(category);
		return book;
	}

	/**
	 * 按分类查询
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCategory(String cid, int pc)
			throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 按书名模糊查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%" + bname + "%"));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 按作者查询
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author, int pc)
			throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author", "like", "%" + author + "%"));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 按出版社查询
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press", "like", "%" + press + "%"));
		return findByCriteria(exprList, pc);
	}

	public PageBean<Book> findByCombinastion(Book criteria, int pc)
			throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%" + criteria.getBname()
				+ "%"));
		exprList.add(new Expression("author", "like", "%"
				+ criteria.getAuthor() + "%"));
		exprList.add(new Expression("press", "like", "%" + criteria.getPress()
				+ "%"));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 通用的查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc)
			throws SQLException {
		/*
		 * 1.得到ps 2.得到tr 3.得到beanList 4.创建PageBean，返回
		 */
		/*
		 * 1.得到ps
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;// 每页记录数
		/*
		 * 2.通过exprList来生成where子句
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();// SQL中有问号，对应问号值
		for (Expression expr : exprList) {
			/*
			 * 添加一个条件上 1)以and开头 2)条件的名称 3)条件的运算符，可以是=、!=、>、< ...is null, is null
			 * 没有值 4)如果条件不是is null，再追加问号，然后再向params中添加与问号对应的值
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
		String sql = "select count(*) from t_book " + whereSql;
		Number number = (Number) queryRunner.query(sql, new ScalarHandler(),
				params.toArray());
		int tr = number.intValue();// 得到总记录数

		/*
		 * 4.得到beanList，即当前页记录
		 */
		sql = "select * from t_book" + whereSql
				+ " order by orderBy limit ? ,?";
		params.add((pc - 1) * ps);// 第一个问号 计算当前页首航记录的下标
		params.add(ps);// 一共查询几行，就是每页记录数

		List<Book> beanList = queryRunner.query(sql, new BeanListHandler<>(
				Book.class), params.toArray());

		/*
		 * 5.创建PageBean，设置参数
		 */
		PageBean<Book> pb = new PageBean<>();
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

	public static void main(String[] args) throws SQLException {
		BookDao bookDao = new BookDao();
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bid", "=", "1"));
		exprList.add(new Expression("bname", "like", "%java%"));
		exprList.add(new Expression("edition", "is null", null));

		bookDao.findByCriteria(exprList, 10);
	}
}
