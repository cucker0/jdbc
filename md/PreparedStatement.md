PreparedStatement是如何防止SQL注入的
==

## 原理
```text
对传入参数的特殊字符进行转换

主要目的：
确保 String型参数 的最外层有单引号包裹，把特殊字符进行转义，如：'xxxxxxx'，
其它类型的参数 则用String.valueOf(Object o) 转换成字符串

参数特殊字符转换规则：
===================

特殊字符    替换后的字符串    说明
----------+----------------+----
'\u0000'  "\\0"      
'\n'      "\\n"
'\r'      "\\r"
'\u001a'  "\\Z"
'"'       "\\""
'\''      "\\'"
'\\'      "\\\\"
'         "''"            一个单引号替换为2个单引号
'¥'       "\\¥"           这里会根据你使用的charsetEncoder.encode来决定是否转换，如果所使用的字符集把'¥'转为ByteBuffer后是以'\'开头来表示的(ASCII码对应值为92)，则需要转换，否则不用
'₩'       "\\₩"           同用法'¥'
```


## 接口、类之间的关系
1. PreparedStatement接口
```java
public interface PreparedStatement extends Statement {
    void setString(int parameterIndex, String x) throws SQLException;
}
```

2. PreparedStatementWrapper类
```java
public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {
    public void setString(int parameterIndex, String x) throws SQLException {
        try {
            if (this.wrappedStmt == null) {
                throw SQLError.createSQLException(Messages.getString("Statement.AlreadyClosed"), "S1000", this.exceptionInterceptor);
            }

            ((PreparedStatement)this.wrappedStmt).setString(parameterIndex, x);
        } catch (SQLException var4) {
            this.checkAndFireConnectionError(var4);
        }

    }
}
```

3. ClientPreparedStatement类
```java
public class ClientPreparedStatement extends StatementImpl implements JdbcPreparedStatement {
    // 方法
    public void setString(int parameterIndex, String x) throws SQLException {
        try {
            synchronized(this.checkClosed().getConnectionMutex()) {
                ((PreparedQuery)this.query).getQueryBindings().setString(this.getCoreParameterIndex(parameterIndex), x);
            }
        } catch (CJException var7) {
            throw SQLExceptionsMapping.translateException(var7, this.getExceptionInterceptor());
        }
    }
    
    protected final int getCoreParameterIndex(int paramIndex) throws SQLException {
        int parameterIndexOffset = this.getParameterIndexOffset(); // 0
        this.checkBounds(paramIndex, parameterIndexOffset);
        return paramIndex - 1 + parameterIndexOffset;
    }
    
    protected int getParameterIndexOffset() {
        return 0;
    }
}


/*
((PreparedQuery)this.query).getQueryBindings()  // T Query 对象

this.getCoreParameterIndex(parameterIndex) // 返回值为 parameterIndex - 1


*/
```

4. StatementImpl实现类
```java
public class StatementImpl implements JdbcStatement {
    protected JdbcConnection checkClosed() {
        JdbcConnection c = this.connection;
        if (c == null) {
            throw (StatementIsClosedException)ExceptionFactory.createException(StatementIsClosedException.class, Messages.getString("Statement.AlreadyClosed"), this.getExceptionInterceptor());
        } else {
            return c;
        }
    }
}
```

5. MysqlConnection
```java
public interface MysqlConnection {
    Object getConnectionMutex();
}
```

6. ConnectionImpl
```java
public class ConnectionImpl implements JdbcConnection, SessionEventListener, Serializable {
    private InvocationHandler realProxy = null;
    private JdbcConnection topProxy = null;
    private String database = null;
    private DatabaseMetaData dbmd = null;
    private HostInfo origHostInfo;
    private String user = null;
    private String password = null;
    private NativeSession session = null;
    protected Properties props = null;
    private boolean readOnly = false;
    protected JdbcPropertySet propertySet;
    private RuntimeProperty<Boolean> autoReconnect;
    private int autoIncrementIncrement = 0;
    
    // 构造器
    protected ConnectionImpl() {
    }
    
    // 方法
    public Object getConnectionMutex() {
        return this.realProxy != null ? this.realProxy : this.getProxy();
    }
    
    private JdbcConnection getProxy() {
        return (JdbcConnection)(this.topProxy != null ? this.topProxy : this);
    }
    
    public void setProxy(JdbcConnection proxy) {
        if (this.parentProxy == null) {
            this.parentProxy = proxy;
        }

        this.topProxy = proxy;
        this.realProxy = this.topProxy instanceof MultiHostMySQLConnection ? ((MultiHostMySQLConnection)proxy).getThisAsProxy() : null;
    }
}
```

7. ConnectionWrapper
```java
public class ConnectionWrapper extends WrapperBase implements JdbcConnection {
    protected JdbcConnection mc = null;
    
    // 构造器
    public ConnectionWrapper(MysqlPooledConnection mysqlPooledConnection, JdbcConnection mysqlConnection, boolean forXa) throws SQLException {
        super(mysqlPooledConnection);
        this.mc = mysqlConnection;
        this.closed = false;
        this.isForXa = forXa;
        if (this.isForXa) {
            this.setInGlobalTx(false);
        }
    }
    // 方法
    public Object getConnectionMutex() {
        return this.mc.getConnectionMutex();
    }
}
```

8. MultiHostMySQLConnection
```java
public class MultiHostMySQLConnection implements JdbcConnection {
    protected MultiHostConnectionProxy thisAsProxy;
  
    // 构造器
    public MultiHostMySQLConnection(MultiHostConnectionProxy proxy) {
            this.thisAsProxy = proxy;
    }
    // 方法
    public Object getConnectionMutex() {
        return this.getActiveMySQLConnection().getConnectionMutex();
    }
    
    public JdbcConnection getActiveMySQLConnection() {
        synchronized(this.thisAsProxy) {
            return this.thisAsProxy.currentConnection;
        }
    }
}
```

9. 
```java
public class StatementImpl implements JdbcStatement {
    protected Query query;
    
    // 构造器
    public StatementImpl(JdbcConnection c, String db) throws SQLException {
        ...
        if (c != null && !c.isClosed()) {
            this.connection = c;
            this.session = (NativeSession)c.getSession();
            this.exceptionInterceptor = c.getExceptionInterceptor();
        
            try {
                this.initQuery();
            } catch (CJException var6) {
                throw SQLExceptionsMapping.translateException(var6, this.getExceptionInterceptor());
            }
        }
        ...
    }
    protected void initQuery() {
        this.query = new SimpleQuery(this.session);
    }
}
```

10. 
```java
public interface PreparedQuery<T extends QueryBindings<?>> extends Query {
    T getQueryBindings();
}
```

11. 
```java
public abstract class AbstractPreparedQuery<T extends QueryBindings<?>> extends AbstractQuery implements PreparedQuery<T> {
    protected T queryBindings = null;
    
    // 方法
    public T getQueryBindings() {
        return this.queryBindings;
    }
    
    public void setQueryBindings(T queryBindings) {
        this.queryBindings = queryBindings;
    }
    
    /**
    * 把?点位符sql替换成参数值
    * 
    * @param quoteStreamsAndUnknowns
    * @return 
    */
    public String asSql(boolean quoteStreamsAndUnknowns) {
        StringBuilder buf = new StringBuilder();
        Object batchArg = null;
        if (this.batchCommandIndex != -1) {
            batchArg = this.batchedArgs.get(this.batchCommandIndex);
        }
        
        // 以?分隔出来的二维字节数组
        /*
        * 可以这么理解：String sql = "SELECT * FROM employees WHERE `name` = ? AND passwd = ?;"
        * 以?分隔为 String[] sArr = ["SELECT * FROM employees WHERE `name` = ", " AND passwd = ", ";"]
        * 然后把字符串转为字节数组
        * byte[][] staticSqlStrings = ["SELECT * FROM employees WHERE `name` = ".getBytes(), " AND passwd = ".getBytes(), ";".getBytes()]
        * */
        byte[][] staticSqlStrings = this.parseInfo.getStaticSql();

        for(int i = 0; i < this.parameterCount; ++i) {
            buf.append(this.charEncoding != null ? StringUtils.toString(staticSqlStrings[i], this.charEncoding) : StringUtils.toString(staticSqlStrings[i]));
            byte[] val = null;
            if (batchArg != null && batchArg instanceof String) {
                buf.append((String)batchArg);
            } else {
                byte[] val = this.batchCommandIndex == -1 ? (this.queryBindings == null ? null : this.queryBindings.getBindValues()[i].getByteValue()) : ((QueryBindings)batchArg).getBindValues()[i].getByteValue();
                boolean isStreamParam = this.batchCommandIndex == -1 ? (this.queryBindings == null ? false : this.queryBindings.getBindValues()[i].isStream()) : ((QueryBindings)batchArg).getBindValues()[i].isStream();
                if (val == null && !isStreamParam) {
                    buf.append(quoteStreamsAndUnknowns ? "'** NOT SPECIFIED **'" : "** NOT SPECIFIED **");
                } else if (isStreamParam) {
                    buf.append(quoteStreamsAndUnknowns ? "'** STREAM DATA **'" : "** STREAM DATA **");
                } else {
                    buf.append(StringUtils.toString(val, this.charEncoding));
                }
            }
        }

        buf.append(this.charEncoding != null ? StringUtils.toString(staticSqlStrings[this.parameterCount], this.charEncoding) : StringUtils.toAsciiString(staticSqlStrings[this.parameterCount]));
        return buf.toString();
    }
}
```

12. 
```java
public interface QueryBindings<T extends BindValue> {
    void setString(int var1, String var2);
}
```

13.
```java
public class ClientPreparedQueryBindings extends AbstractQueryBindings<ClientPreparedQueryBindValue> {
    public void setString(int parameterIndex, String x) {
        if (x == null) {
            this.setNull(parameterIndex);
        } else {
            int stringLength = x.length();
            byte[] parameterAsBytes;
            if (this.session.getServerSession().isNoBackslashEscapesSet()) { // 无\转义模式
                boolean needsHexEscape = this.isEscapeNeededForString(x, stringLength); // 判断字符中是否有转义符\\
                if (!needsHexEscape) {
                    StringBuilder quotedString = new StringBuilder(x.length() + 2);
                    quotedString.append('\'');
                    quotedString.append(x);
                    quotedString.append('\'');
                    parameterAsBytes = this.isLoadDataQuery ? StringUtils.getBytes(quotedString.toString()) : StringUtils.getBytes(quotedString.toString(), this.charEncoding);
                    this.setValue(parameterIndex, parameterAsBytes, MysqlType.VARCHAR);
                } else {
                    byte[] parameterAsBytes = this.isLoadDataQuery ? StringUtils.getBytes(x) : StringUtils.getBytes(x, this.charEncoding);
                    this.setBytes(parameterIndex, parameterAsBytes);
                }

                return;
            }

            String parameterAsString = x;
            boolean needsQuoted = true;
            // isLoadDataQuery 或 x字符串中包含\转义符
            if (this.isLoadDataQuery || this.isEscapeNeededForString(x, stringLength)) {
                needsQuoted = false;
                StringBuilder buf = new StringBuilder((int)((double)x.length() * 1.1D)); // 每10个字符，增加一个长度
                buf.append('\'');

                for(int i = 0; i < stringLength; ++i) {
                    char c = x.charAt(i);
                    switch(c) {
                    case '\u0000':
                        buf.append('\\');
                        buf.append('0');
                        break;
                    case '\n':
                        buf.append('\\');
                        buf.append('n');
                        break;
                    case '\r':
                        buf.append('\\');
                        buf.append('r');
                        break;
                    case '\u001a':
                        buf.append('\\');
                        buf.append('Z');
                        break;
                    case '"': // " 替换为 \\"
                        if (this.session.getServerSession().useAnsiQuotedIdentifiers()) {
                            buf.append('\\');
                        }
                        buf.append('"');
                        break;
                    case '\'': // \' 替换为 ''
                        buf.append('\'');
                        buf.append('\'');
                        break;
                    case '\\':
                        buf.append('\\');
                        buf.append('\\');
                        break;
                    case '¥':
                    case '₩':
                        if (this.charsetEncoder != null) {
                            CharBuffer cbuf = CharBuffer.allocate(1);
                            ByteBuffer bbuf = ByteBuffer.allocate(1);
                            cbuf.put(c);
                            cbuf.position(0);
                            this.charsetEncoder.encode(cbuf, bbuf, true);
                            if (bbuf.get(0) == 92) {
                                buf.append('\\');
                            }
                        }

                        buf.append(c);
                        break;
                    default:
                        buf.append(c);
                    }
                }

                buf.append('\'');
                parameterAsString = buf.toString();
            }

            parameterAsBytes = this.isLoadDataQuery ? StringUtils.getBytes(parameterAsString) : (needsQuoted ? StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charEncoding) : StringUtils.getBytes(parameterAsString, this.charEncoding));
            this.setValue(parameterIndex, parameterAsBytes, MysqlType.VARCHAR);
        }

    }
    
    /**
    * 判断字符串是否包含\\
    * 
    * @param x 字符串
    * @param stringLength
    * @return 
    */
    private boolean isEscapeNeededForString(String x, int stringLength) {
        boolean needsHexEscape = false;

        for(int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);
            switch(c) {
            case '\u0000':
            case '\n':
            case '\r':
            case '\u001a':
            case '"':
            case '\'':
            case '\\':
                needsHexEscape = true;
            }

            if (needsHexEscape) {
                break;
            }
        }

        return needsHexEscape;
    }
}
```
```text
this.session.getServerSession().isNoBackslashEscapesSet()

public class NativeServerSession implements ServerSession {
    public boolean isNoBackslashEscapesSet() {
        String sqlModeAsString = (String)this.serverVariables.get("sql_mode");
        return sqlModeAsString != null && sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1;
    }
}



```


14. 
```java
public class ServerPreparedQueryBindings extends AbstractQueryBindings<ServerPreparedQueryBindValue> {
    public void setString(int parameterIndex, String x) {
        if (x == null) {
            this.setNull(parameterIndex);
        } else {
            ServerPreparedQueryBindValue binding = this.getBinding(parameterIndex, false);
            this.sendTypesToServer.compareAndSet(false, binding.resetToType(253, (long)this.numberOfExecutions));
            binding.value = x;
            binding.charEncoding = this.charEncoding;
            binding.parameterType = MysqlType.VARCHAR;
        }

    }
}
```

15. 
```java
public abstract class AbstractQueryBindings<T extends BindValue> implements QueryBindings<T> {
    protected T[] bindValues;
    
    public final synchronized void setValue(int paramIndex, byte[] val, MysqlType type) {
        this.bindValues[paramIndex].setByteValue(val);
        this.bindValues[paramIndex].setMysqlType(type);
    }
    public void setObject(int parameterIndex, Object parameterObj) {
        if (parameterObj == null) {
            this.setNull(parameterIndex);
        } else if (parameterObj instanceof Byte) {
            this.setInt(parameterIndex, ((Byte)parameterObj).intValue());
        } else if (parameterObj instanceof String) {
            this.setString(parameterIndex, (String)parameterObj);
        } else if (parameterObj instanceof BigDecimal) {
            this.setBigDecimal(parameterIndex, (BigDecimal)parameterObj);
        } else if (parameterObj instanceof Short) {
            this.setShort(parameterIndex, (Short)parameterObj);
        } else if (parameterObj instanceof Integer) {
            this.setInt(parameterIndex, (Integer)parameterObj);
        } else if (parameterObj instanceof Long) {
            this.setLong(parameterIndex, (Long)parameterObj);
        } else if (parameterObj instanceof Float) {
            this.setFloat(parameterIndex, (Float)parameterObj);
        } else if (parameterObj instanceof Double) {
            this.setDouble(parameterIndex, (Double)parameterObj);
        } else if (parameterObj instanceof byte[]) {
            this.setBytes(parameterIndex, (byte[])((byte[])parameterObj));
        } else if (parameterObj instanceof Date) {
            this.setDate(parameterIndex, (Date)parameterObj);
        } else if (parameterObj instanceof Time) {
            this.setTime(parameterIndex, (Time)parameterObj);
        } else if (parameterObj instanceof Timestamp) {
            this.setTimestamp(parameterIndex, (Timestamp)parameterObj);
        } else if (parameterObj instanceof Boolean) {
            this.setBoolean(parameterIndex, (Boolean)parameterObj);
        } else if (parameterObj instanceof InputStream) {
            this.setBinaryStream(parameterIndex, (InputStream)parameterObj, -1);
        } else if (parameterObj instanceof Blob) {
            this.setBlob(parameterIndex, (Blob)parameterObj);
        } else if (parameterObj instanceof Clob) {
            this.setClob(parameterIndex, (Clob)parameterObj);
        } else if ((Boolean)this.treatUtilDateAsTimestamp.getValue() && parameterObj instanceof java.util.Date) {
            this.setTimestamp(parameterIndex, new Timestamp(((java.util.Date)parameterObj).getTime()));
        } else if (parameterObj instanceof BigInteger) {
            this.setString(parameterIndex, parameterObj.toString());
        } else if (parameterObj instanceof LocalDate) {
            this.setDate(parameterIndex, Date.valueOf((LocalDate)parameterObj));
        } else if (parameterObj instanceof LocalDateTime) {
            this.setTimestamp(parameterIndex, Timestamp.valueOf((LocalDateTime)parameterObj));
        } else if (parameterObj instanceof LocalTime) {
            this.setTime(parameterIndex, Time.valueOf((LocalTime)parameterObj));
        } else {
            this.setSerializableObject(parameterIndex, parameterObj);
        }

    }
}
```

16.
```java
public class ClientPreparedQueryBindValue implements BindValue {
    protected boolean isNull;
    protected boolean isStream = false;
    protected MysqlType parameterType;
    public Object value;
    public Object origValue;
    protected long streamLength;
    protected boolean isSet;
        
    public void setByteValue(byte[] parameterValue) {
        this.isNull = false;
        this.isStream = false;
        this.value = parameterValue;
        this.streamLength = 0L;
        this.isSet = true;
    }
    
    public void setMysqlType(MysqlType type) {
        this.parameterType = type;
    }
    
    public synchronized void setNull(int parameterIndex) {
        this.setValue(parameterIndex, "null", MysqlType.NULL);
        ((ClientPreparedQueryBindValue[])this.bindValues)[parameterIndex].setNull(true);
    }
}
```
