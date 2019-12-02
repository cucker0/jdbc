JDBC
==


## JDBC概述
### java中的数据存储技术
* 连接数据库技术分类
    * JDBC直接访问数据库
    * JDO技术
    * 第三方O/R工具，如Hibernate、ibatis等
**JDBC是java访问数据库的基石，JDO, Hibernate等只是更好的封装了JDBC**


### JDBC概念
```text
* JDBC(Java Database Connectivity)是一个独立于特定数据库管理系统、通用的SQL数据库存取和操作的公共接口（一组API），
定义了用来访问数据库的标准Java类库，使用这个类库可以以一种标准的方法、方便地访问数据库资源

另外：ODBC（Open DataBase Connectivity）

* JDBC为访问不同的数据库提供了一种统一的途径，为开发者屏蔽了一些细节问题

* JDBC的目标是使Java程序员使用JDBC可以连接任何提供了JDBC驱动程序的数据库系统。
简化和加快了开发过程
```
![](images/jdbc_架构示意图.png)  

### JDBC体系结构
JDBC接口(API)包括两个层次
![](images/jdbc_结构体系.png)  

* 面向应用的API
    ```text
    Java API，抽象接口，供应用程序开发人员使用（连接数据库，执行SQL语句，获得结果）
    ```
* 面向数据库的API
    ```text
    Java Driver API，供开发商开发数据库驱动程序用
    ```

```text
JDBC驱动程序：
JDBC是sun公司提供一套用于数据库操作的接口，
java程序员只需要面向这套接口编程即可。
各个数据库厂商根据JDBC的规范制作的 JDBC 实现类的类库
```

### JDBC驱动程序四种类型
1. JDBC-ODBC桥
2. 部分本地API部分Java的驱动程序
3. JDBC网络纯Java驱动程序
4. 本地协议的纯 Java 驱动程序

第3、4种性能、可移植性、功能等方面都有优势
* JDBC-ODBC桥
    ```text
    java应用程序 -->JDBC API  -->JDBC-ODBC桥 -->ODBC API -->ODBC层 -->数据库
    
    把标准的 JDBC 调用转换成相应的 ODBC 调用，并通过 ODBC 访问数据库
    要通过多层调用，所以利用JDBC-ODBC 桥访问数据库的效率较低
    JDK中提供相应的驱动：sun.jdbc.odbc.JdbcOdbcDriver
    ```
**ODBC**  
```text
ODBC(Open Database Connectivity),放式数据库连接。微软推出的Windows平台提供统一访问数据库的方式。
所有数据库都可用ODBC API进行访问，ODBC的最大优点是能以统一的方式处理所有的数据库
```

* 部分本地API部分Java的驱动程序
    ```text
    java应用程序 -->JDBC API  -->JDBC驱动程序 -->厂商提供的本地API -->数据库
    ```

* JDBC网络纯Java驱动程序
    ```text
    java应用程序 -->JDBC API  -->JDBC驱动程序 -->应用服务器 -->厂商提供的本地API -->数据库
    ```
* 本地协议的纯 Java 驱动程序
    ```text
    java应用程序 -->JDBC API  -->JDBC驱动程序 -->数据库
    ```

### JDBC编程步骤
![](./images/JDBC编程步骤.png)  


## 获取数据库连接
### JDBC API
```text
是一系列的接口，它使得应用程序能够进行数据库联接，执行SQL语句，并且得到返回结果
```


### 要素1：Driver接口实现类
* Driver接口
```text
* Java.sql.Driver 接口是所有 JDBC 驱动程序需要实现的接口

* 在程序中不需要直接去访问实现了 Driver 接口的类，而是由驱动程序管理器类(java.sql.DriverManager)去调用这些Driver实现
这种方式更灵活
```

* 加载与注册 JDBC 驱动
```text
* 加载 JDBC 驱动需调用 Class 类的静态方法 forName()，向其传递要加载的 JDBC 驱动的类名
* DriverManager 类是驱动程序管理器类，负责管理驱动程序
* 通常不用显式调用 DriverManager 类的 registerDriver() 方法来注册驱动程序类的实例，
    因为 Driver 接口的驱动程序类都包含了静态代码块，
    在这个静态代码块中，会调用 DriverManager.registerDriver() 方法来注册自身的一个实例
```

#### IntelliJ IDEA扩展数据库连接驱动
```text
这里以扩展全局lib为例(mysql-connector-java-8.0.18)，与扩展其他jar包方法一样。
mysql-connector-java jar包需要事先下载好，可到https://maven.aliyun.com/mvn/view，然后到
/mysql/mysql-connector-java/8.0.18/mysql-connector-java-8.0.18.jar 路径下下载该jar包。

## jdbc mysql
https://dev.mysql.com/downloads/connector/j/
如下载适用于redhat的mysql-connector-java-8.0.18-1.el8.noarch.rpm，rpm需要解压后才能得到jar包，
rpm2cpio mysql-connector-java-8.0.18-1.el8.noarch.rpm |cpio -div
jar包即在解压后的./usr/share/java/mysql-connector-java.jar

## jdbc postgresql
https://jdbc.postgresql.org

## jdbc oracle
https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html

## jdbc sqlserver
https://docs.microsoft.com/zh-cn/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server?view=sql-server-ver15
```
**具体操作**  
<details>
<summary>具体操作</summary>


与添加一般的jar lib库一样
1. 点击 Project Structure  
    ![](images/mysql-connector-java_0.png)  

2. 点击Global Libraries，点击 + 按钮，选择Java  
    ![](images/mysql-connector-java_1.png)  

3. 路径选择框中，选择mysql-connector jar包所在的路径，再点击OK  
    ![](images/mysql-connector-java_2.png)  

4. 在Global Libraries中把，右击mysql-connector-java-8.0.18，点击Add to Modules，添加到当前模块中  
    ![](images/mysql-connector-java_3.png)  
</details>


### 要素2：URL
* JDBC URL 用于标识一个被注册的驱动程序，驱动程序管理器通过这个 URL 选择正确的驱动程序，从而建立到数据库的连接。

* JDBC URL的标准由三部分组成，各部分间用冒号分隔。 
    ```text
    jdbc:子协议:子名称
    
    示例：jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false
    ```
    * 协议：JDBC URL中的协议总是jdbc 
    * 子协议
        ```text
        子协议用于标识一个数据库驱动程序
        ```
    * 子名称
        ```text
        一种标识数据库的方法。子名称可以依不同的子协议而变化，
        用子名称的目的是为了 定位数据库 提供足够的信息。
        包含 主机名(对应服务端的ip地址)，端口号，数据库名
        ```
    
### 要素3：用户名和密码
* user,password可以用“属性名=属性值”方式告诉数据库
* 可以调用 DriverManager 类的 getConnection() 方法建立到数据库的连接

### JDBC连接数据库示例
* [JDBC连接数据库 testDriver方法](./day01/src/com/java/www/JdbcTest.java)
    ```java
        @Test
        public void testDriver() throws SQLException {
            // 1. 创建一个Driver实现类的对象
            Driver driver = new com.mysql.jdbc.Driver();
            /*
            * mysql-connector-java-8 建议使用 com.mysql.cj.jdbc.Driver 驱动类
            * */
    
            // 2. 提供连接数据库的的基本信息：url, user, password,
            String url = "jdbc:mysql://127.0.0.1:3306/testdb?serverTimezone=Asia/Shanghai";
            Properties info = new Properties();
            info.put("user", "root");
            info.put("password", "py123456");
    
            // 3. 调用 Driver 接口的 connect(url, info) 方法
            Connection connection = driver.connect(url, info);
            System.out.println(connection);
            /*
             * Loading class `com.mysql.jdbc.Driver'.This is deprecated.
             * The new driver class is `com.mysql.cj.jdbc.Driver'.
             * The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
             * */
        }
    ```
* [JDBC连接数据库 testDriverManager](./day01/src/com/java/www/DriverManagerTest.java)
    ```java
        @Test
        public void testDriverManager() throws Exception {
            // 1. 提供连接数据库的基本信息
            String driverClass = "com.mysql.cj.jdbc.Driver";
            String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
            String user = "root";
            String password = "py123456";
    
            // 2. 加载数据库驱动(并自动注册驱动，com.mysql.cj.jdbc.Driver类有静态代码块 DriverManager.registerDriver(new Driver());)
            Class.forName(driverClass);
    
            // 3. 通过DriverManager 的 getConnection 方法获取数据库连接对象
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("DriverManager");
            System.out.println(connection);
        }
    ```

* [JDBC连接数据库 getConnection, 最终版](./day01/src/com/java/www/DriverManagerTest.java)
    ```java
        /**
         * 通过 DriverManager 实现类获取数据库连接对象
         *
         * @return 数据库连接对象
         * @throws Exception
         */
        public Connection getConnection() throws Exception {
            // 读取jdbc.properties文件配置
            InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(in);
    
            String driverClass = properties.getProperty("driver");
            String jdbcUrl = properties.getProperty("jdbcUrl");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
    
            // 加载数据库驱动并注册
            Class.forName(driverClass);
    
            // 通过 DriverManager类的getConnection 获取数据库连接对象，并返回
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            return connection;
        }
    ```
    * 工程的src目录下创建 jdbc.properties 文件，内容如下
    ```text
    # mysql connector 8
    driverClass = com.mysql.cj.jdbc.Driver
    jdbcUrl = jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false
    #characterEncoding：字符集
    #serverTimezone: mysql服务器端的时区，UTC为0时区，东8区：Asia/Shanghai 或GMT%2B8
    user = root
    password = py123456
    ```
    
* **[jdbc.properties 与mysql、postgresql、oracle、sqlserver的配置示例](./day01/src/jdbc.properties)**


## 使用PreparedStatement实现CRUD操作
### 访问数据库并操作
* 数据库连接被用于向数据库服务器发送命令和 SQL 语句，并接受数据库服务器返回的结果。其实一个数据库连接就是一个Socket连接。

* 在 java.sql 包中有 3 个接口分别定义了对数据库的调用的不同方式
    * Statement：用于执行静态 SQL 语句并返回它所生成结果的对象。 
    * PrepatedStatement：SQL 语句被预编译并存储在此对象中，可以使用此对象多次高效地执行该语句。
    * CallableStatement：用于执行 SQL 存储过程  
    ![](images/jdbc_api.png)  

### 使用Statement操作数据表及其弊端
* 通过调用 Connection 对象的 createStatement() 方法创建该对象。该对象用于执行静态的 SQL 语句，并且返回执行结果。

* Statement 接口中定义了下列方法用于执行 SQL 语句：

  ```java
  int excuteUpdate(String sql)  // 执行更新操作INSERT、UPDATE、DELETE
  ResultSet executeQuery(String sql)  // 执行查询操作SELECT
  ```

* 但是使用Statement操作数据表存在弊端：
    * **问题一：存在拼串操作，繁琐**
    * **问题二：存在SQL注入问题**

* SQL 注入是利用某些系统没有对用户输入的数据进行充分的检查，而在用户输入数据中注入非法的 SQL 语句段或命令
    ```text
    如：
    SELECT user, password FROM user_table WHERE user='a' OR 1 = ' AND password = ' OR '1' = '1') ，
    从而利用系统的 SQL 引擎完成恶意行为的做法。
    ```
* 对于 Java 而言，要防范 SQL 注入，只要用 PreparedStatement(从Statement扩展而来) 取代 Statement 就可以了。
[SQL注入测试 testSQLInjection](./day02/src/com/java/exe/JdbcTest.java)


### PreparedStatement的使用

####  PreparedStatement介绍
* 可以通过调用 Connection 对象的 **preparedStatement(String sql)** 方法获取 PreparedStatement 对象

* **PreparedStatement 接口是 Statement 的子接口，它表示一条预编译过的 SQL 语句，并对特殊字符进行转义**

    原字符 | 转换后字符串
    :--- |:---  
    `\u0000` |`\\0`
    `\n` |`\\n`
    `\r` |`\\r`
    `\u001a` |`\\Z`
    `"` |`\\"`
    `\'` |`\\'`
    `\\` |`\\\\`
    `'` |`''`

* PreparedStatement 对象所代表的 SQL 语句中的参数用问号(?)来表示，
    ```text
    调用 PreparedStatement 对象的 setXxx() 方法来设置这些参数. setXxx() 方法有两个参数，
    第一个参数是要设置的 SQL 语句中的参数的索引(从 1 开始)，第二个是设置的 SQL 语句中的参数的值
    ```
* **((ClientPreparedStatement) preparedStatement).asSql()  
    可查看PreparedStatement设置参数、特殊字符等处理后的sql语句**


#### PreparedStatement vs Statement
* 代码的可读性和可维护性。

* **PreparedStatement 能最大可能提高性能**
    ```text
    * DBServer会对 预编译语句提供性能优化。
          因为预编译语句有可能被重复调用，
          所以语句在被DBServer的编译器编译后的执行代码被缓存下来，
          那么下次调用时只要是相同的预编译语句就不需要编译，
          只要将参数直接传入编译过的语句执行代码中就会得到执行
    * 在statement语句中,即使是相同操作但因为数据内容不一样,所以整个语句本身不能匹配,
          没有缓存语句的意义.事实是没有数据库会对普通语句编译后的执行代码缓存。
          这样每执行一次都要对传入的语句编译一次。
    * 语法检查，语义检查，翻译成二进制命令，缓存
    ```
* PreparedStatement 可以防止 SQL 注入 

#### java与数据库之间的数据类型转换表
java类型 |SQL类型
:--- |:---
boolean |bit 
byte |tinyint 
short |smallint 
int |int 
long |bigint 
float |float(M,D)
double |double(M,D)
String |char, varchar, longvarchar 
byte array |binary, var binary 
java.sql.Date |date 
java.sql.Time |time 
java.sql.Datetime |datetime 
java.sql.Timestamp |timestamp 

### 使用PreparedStatement实现增、删、改操作
[PreparedStatementTest](./day02/src/com/java/exe/PreparedStatementTest.java)  

### 使用PreparedStatement实现查询操作
[JdbcUtils get](./day02/src/com/java/exe/JdbcUtils.java)

## DAO
```text
Data Access Object 数据访问对象.

## 定义
访问数据信息的类，包含了对数据的CRUD(create, read, update, delete)操作，
不包含任何业务相关的信息

## 优点
实现功能的模块化，更有利于代码的维护和升级


```

## BeanUtils
```text
https://commons.apache.org/proper/commons-beanutils/
http://commons.apache.org/proper/commons-logging/
http://commons.apache.org/collections/

* compile编译依赖的jar包
commons-collections-3.2.2.jar
commons-logging-1.2.jar
* 测试依赖的jar包
commons-collections-testframework-3.2.1.jar
junit-4.12.jar
hamcrest-core-1.3.jar
```


## 数据库连接池
### dbcp
```text

DBCP
https://commons.apache.org/proper/commons-dbcp/index.html
Apache Commons DBCP 2.7.0 for JDBC 4.2 on Java 8
commons-dbcp.jar、commons-pool.jar、commons-logging.jar

```

* dbcp.properties配置文件
```text
##DBCP连接池
#文件放置于src根目录下
#必填写参数
driverClassName = com.mysql.cj.jdbc.Driver
url= jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false
username = root
password = py123456

#可选参数
initialSize = 10
maxTotal = 50
maxIdle = 10
minIdle = 2
maxWaitMillis = 5000
```

### c3p0
```text
官网
https://www.mchange.com/projects/c3p0/
jar包下载
https://sourceforge.net/projects/c3p0/

依赖jar包：c3p0.jar、commons-logging.jar

配置文件示例：
https://www.mchange.com/projects/c3p0/#configuration_files

当c3p0.properties、c3p0-config.xml配置文件同时存在时，优先使用c3p0-config.xml配置
```

### DbUtils
```
https://commons.apache.org/proper/commons-dbutils/
依赖jar包：commons-dbutils.jar
```


