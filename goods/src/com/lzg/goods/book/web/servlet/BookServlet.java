package com.lzg.goods.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

import com.lzg.goods.book.domain.Book;
import com.lzg.goods.book.service.BookService;
import com.lzg.goods.paper.PageBean;

public class BookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService = new BookService();

	/**
	 * 获取当前页页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return pc;
	}

	/**
	 * 截取url,页面中的分页导航中需要使用它作为超链接的目标
	 * @param req
	 * @return
	 */
	/*
	 * http://localhost:8080/goods/BookServlet?method=findByCategory&cid=xxx
	 * goods/BookServlet + method=findByCategory&cid=xxx&pc=3
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();// getQueryString()获取的是？后的字符串
		/*
		 * 如果url中存在pc参数，截取掉
		 */
		int index = url.lastIndexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	public String findByCategory(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc:如果页面传递，使用页面的，否则，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获得查询条件，本方法就是cid,即分类的id
		 */
		String cid = req.getParameter("cid");
		/*
		 * 4.使用pc和cid调用service#findByCategory得到PageBean
		 */
		System.out.println(pc+"======"+cid);
		
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		
		System.out.println(pb);
		
		/*
		 * 5.给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}

}
