package com.bookmall.dao;

import com.bookmall.beans.Book;
import com.bookmall.beans.Page;

import java.sql.Connection;
import java.util.List;

public interface BookDao {
    /**
     * 查询所有的book
     *
     * @param conn
     * @return
     */
    List<Book> getBooks(Connection conn);

    /**
     * 把指定的Book对象保存到数据库表中
     *
     * @param conn: Connection数据库连接对象
     * @param book: Book对象
     */
    int saveBook(Connection conn, Book book);

    /**
     * 删除自定ID的book
     *
     *
     * @param conn
     * @param bookId
     */
    void deleteBookById(Connection conn, String bookId);

    /**
     * 获取指定ID的book
     *
     * @param conn
     * @param bookId
     * @return
     */
    Book getBookById(Connection conn, String bookId);

    /**
     * 更新Book对象的id, 更新数据库中中对应id的记录
     *
     * @param conn
     * @param book
     */
    void updateBook(Connection conn, Book book);

    /**
     * 获取带分页的图书信息
     *
     * @param conn
     * @param page
     * @return
     */
    Page<Book> getPageBooks(Connection conn, Page<Book> page);

    /**
     * 获取带分页和价格范围的图书信息
     *
     * @param conn
     * @param page
     * @param minPrice
     * @param maxPrice
     * @return
     */
    Page<Book> getPageBooksByPrice(Connection conn, Page<Book> page, double minPrice, double maxPrice );
}
