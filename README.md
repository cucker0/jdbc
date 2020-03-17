JDBC
==

# Table Of Contents
<details>
<summary>JDBC概述</summary>

* [JDBC概述](md/JDBC.md#JDBC概述)
    * [java中的数据存储技术](md/JDBC.md#java中的数据存储技术)
    * [JDBC概念](md/JDBC.md#JDBC概念)
    * [JDBC体系结构](md/JDBC.md#JDBC体系结构)
    * [JDBC驱动程序四种类型](md/JDBC.md#JDBC驱动程序四种类型)
    * [JDBC编程步骤](md/JDBC.md#JDBC编程步骤)
</details>

<details>
<summary>获取数据库连接</summary>

* [获取数据库连接](md/JDBC.md#获取数据库连接)
    * [JDBC API](md/JDBC.md#JDBC-API)
    * [要素1：Driver接口实现类](md/JDBC.md#要素1Driver接口实现类)
        * [IntelliJ IDEA扩展jdbc Driver以及多种数据库的jdbc Driver下载地址](md/JDBC.md#IntelliJ-IDEA扩展jdbc-Driver以及多种数据库的jdbc-Driver下载地址)
    * [要素2：URL](md/JDBC.md#要素2URL)
    * [要素3：用户名和密码](md/JDBC.md#要素3用户名和密码)
    * [JDBC连接数据库示例](md/JDBC.md#JDBC连接数据库示例)
</details>

<details>
<summary>使用PreparedStatement实现CRUD操作</summary>

* [使用PreparedStatement实现CRUD操作](md/JDBC.md#使用PreparedStatement实现CRUD操作)
    * [访问数据库并操作](md/JDBC.md#访问数据库并操作)
    * [使用Statement操作数据表及其弊端](md/JDBC.md#使用Statement操作数据表及其弊端)
    * [PreparedStatement的使用](md/JDBC.md#PreparedStatement的使用)
        * [PreparedStatement介绍，如何处理sql语句中的特殊字符。?占位参数原理](md/JDBC.md#PreparedStatement介绍)
        * [获取?占位符SQL语句替换参数后的SQL语句](md/JDBC.md#asSql)
        * [PreparedStatement是如何防止SQL注入的](md/PreparedStatement.md)
        * [PreparedStatement vs Statement](md/JDBC.md#PreparedStatement-vs-Statement)
        * [java与数据库之间的数据类型转换表](md/JDBC.md#java与数据库之间的数据类型转换表)
    * [使用PreparedStatement实现增、删、改操作](md/JDBC.md#使用PreparedStatement实现增删改操作)
    * [使用PreparedStatement实现查询操作](md/JDBC.md#使用PreparedStatement实现查询操作)
    * [ResultSet与ResultSetMetaData](md/JDBC.md#ResultSet与ResultSetMetaData)
        * [ResultSet](md/JDBC.md#ResultSet)
        * [ResultSetMetaData](md/JDBC.md#ResultSetMetaData)
    * [DatabaseMetaData](md/JDBC.md#DatabaseMetaData)
    * [获取插入数据时自动生成的主键值](md/JDBC.md#获取插入数据时自动生成的主键值)
    * [资源的释放](md/JDBC.md#资源的释放)
    * [JDBC API小结](md/JDBC.md#JDBC-API小结)
</details>

[章节练习](day02/src/com/java/exercise/README.md)

<details>
<summary>操作BLOB类型字段</summary>

* [操作BLOB类型字段](md/JDBC.md#操作BLOB类型字段)
    * [MySQL BLOB类型](md/JDBC.md#MySQL-BLOB类型)
    * [向数据表中插入BLOB数据类型](md/JDBC.md#向数据表中插入BLOB数据类型)
    * [更新数据表中的BLOB类型字段](md/JDBC.md#更新数据表中的BLOB类型字段)
    * [读取BLOB类型数据](md/JDBC.md#读取BLOB类型数据)
</details>



* [JDBC中处理事务](md/JDBC.md#JDBC中处理事务)
    * [JDBC事务隔离级别](md/JDBC.md#JDBC事务隔离级别)
* [批量插入](md/JDBC.md#批量插入)
    * [批量执行SQL语句](md/JDBC.md#批量执行SQL语句)
* [DAO](md/JDBC.md#DAO)
    * [DAO项目示例：BookMall](md/JDBC.md#DAO项目示例BookMall)
* [BeanUtils](md/JDBC.md#BeanUtils)

<details>
<summary>DbUtils实现CRUD操作</summary>

* [DbUtils实现CRUD操作](md/JDBC.md#DbUtils实现CRUD操作)
    * [DbUtils简介](md/JDBC.md#DbUtils简介)
    * [DbUtils API主要方法](md/JDBC.md#DbUtils-API主要方法)
    * [QueryRunner类](md/JDBC.md#QueryRunner类)
    * [ResultSetHandler接口及实现类](md/JDBC.md#ResultSetHandler接口及实现类)
    * [QueryLoader](md/JDBC.md#QueryLoader)
</details>    

<details>
<summary>数据库连接池</summary>

* [数据库连接池](md/JDBC.md#数据库连接池)
    * [JDBC数据库连接池的必要性](md/JDBC.md#JDBC数据库连接池的必要性)
    * [数据库连接池技术](md/JDBC.md#数据库连接池技术)
    * [多种开源的数据库连接池](md/JDBC.md#多种开源的数据库连接池)
    * [DBCP数据库连接池](md/JDBC.md#DBCP数据库连接池)
        * [DBCP示例](md/JDBC.md#DBCP示例)
    * [c3p0数据库连接池](md/JDBC.md#c3p0数据库连接池)
        * [c3p0 properties风格配置](md/JDBC.md#c3p0-properties风格配置)
        * [c3p0 xml风格配置](md/JDBC.md#c3p0-xml风格配置)
        * [c3p0示例](md/JDBC.md#c3p0示例)
    * [Druid数据库连接池](md/JDBC.md#Druid数据库连接池)
        * [Druid示例](md/JDBC.md#Druid示例)
</details>

* [使用JDBC调用数据库中的存储过程、函数](md/JDBC.md#使用JDBC调用数据库中的存储过程函数)
    * [JDBC调用存储过程](md/JDBC.md#JDBC调用存储过程)
    * [JDBC调用函数](md/JDBC.md#JDBC调用函数)