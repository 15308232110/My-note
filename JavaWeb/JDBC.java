JDBC
	
	JDBC（Java DataBase Connectivity）可以用Java语言来操作数据库，Mysql数据库需要jar包：mysql-connector-java
    JDBC是接口，而JDBC驱动(访问数据库的API)是接口的实现，没有驱动就无法连接数据库！每个数据库厂商都有自己的驱动连接自己公司的数据库

    1.得到Connection

		1. 准备四大参数(不同的数据库的四大参数都是不同的)

			String driverClassName = "com.mysql.jdbc.Driver";	
			String url = "jdbc:mysql://localhost:3306/mydb3";		//jdbc协议的格式：jdbc:厂商的名称:子协议(由厂商自己来规定)
			String username = "root";
			String password = "123";
						 
		2. 加载驱动类(注册驱动)

			Class.forName(driverClassName);		/*所有的java.sql.Driver实现类，都提供了static块，来把自己注册到DriverManager中
												  jdbc4.0之后在每个驱动jar包中的META-INF/services目录下都提供了一个名为java.sql.Driver的文件
												  其内容就是该接口的实现类名称。所以就不用再做这一步操作了，但是为了兼容性还是建议写*/

		3. 得到连接对象

			Connection con=DriverManager.getConnection(url,username,password);

    2.操作数据库

		1. 通过连接对象创建语句发送器，它可以发送DQL、DML和DDL语句，并且发送的sql语句最后会自动添加";"号

			Statement sta = c.createStatement();

		2. Statement类的方法：
			
				int executeUpdate(String sql);				//返回的值是影响记录数，针对DML和DDL语句
				ResultSet executeQuery(String sql);			//返回值是一个结果集，针对的是DQL语句，想要看到查询结果，还需要解析结果集
				boolean execute(String sql);				//返回值表示执行结果是否返回结果集，针对DDL、DML和DQL
				int getUpdateCount();						//得到影响记录数
				ResultSet getResultSet();					//得到结果集

    3.关闭资源：倒关，ResultSet、Statement和Connection对象都需要关闭，其中对于JavaWeb项目，连接对象必须关闭

============================================================================================================================================
    
ResultSet

	它是一个二维的表格！ResultSet内部有一个行光标（游标），ResultSet提供了一系列的方法来移动游标。

	方法：void beforeFirst/afterLast()：							//把光标移到第一行的前面/最后一行的后面，这也是光标默认的位置
		  boolean first/last()：									//把光标移到第一行/最后一行，返回值表示是否成功

		  boolean isBeforeFirst/isAfterLast/isFirst/isLast()：		//当前光标位置是否在第一行前面//最后一行后面/第一行/最后一行；
		
		  boolean next/previous()：									//把光标向上/下挪一行；

		  boolean relative(int row)：								//相对位移，当row为正数时，表示向下移动row行
		  boolean absolute(int row)：								//绝对位移，把光标移动到指定的行上；
		  int getRow()：											//返回当前光标所在行。

		  XXX getXXX();												//可以得到不同的类型，"()"中可以填列序号也可以填列名称

		  ResultSetMetaData getMetaData():							//得到元数据
		
			  int getColumnCount()								 	//获取结果集列数
			  String getColumnName(int colIndex)					//获取指定列的列名

	特性：是否可滚动		不可滚动的结果集只能使用next()方法来移动游标
		  是否敏感
		  是否可更新

		在通过连接对象得到Statement对象时就已经确定了生成结果集的特性

			1.Statement createStatement()：		//不滚动、不敏感、不可更新！但注意！在MySQL中，生成的结果集依然是可滚动的！

			2.Statement createStatement(int resultSetType, int resultSetConcurrency)：

				ResultSet.TYPE_FORWARD_ONLY：			//不滚动结果集；
				ResultSet.TYPE_SCROLL_INSENSITIVE：		//滚动结果集，但结果集数据不会再跟随数据库而变化；
				ResultSet.TYPE_SCROLL_SENSITIVE：		//滚动结果集，但结果集数据不会再跟随数据库而变化；太耗费资源，没有数据库驱动支持它！

				CONCUR_READ_ONLY：						//结果集是只读的，不能通过修改结果集而反向影响数据库；
				CONCUR_UPDATABLE：						//结果集是可更新的，对结果集的更新可以反向影响数据库。	

============================================================================================================================================

PreparedStatement

	1.SQL攻击：用户在输入项中输入SQL语句与DAO层中已有SQL语句合成一个完整的SQL语句，就是SQL攻击！所以需要校验输入的数据中是否包含非法字符

	2.PreparedStatement叫预编译声明！是Statement的子接口，作用：防止SQL攻击，提高代码的可读性和可维护性，模板可以重复使用以提高效率
	
	3.用法

		1.使用连接对象调用PreparedStatement prepareStatement(String sql模板)方法可得到PreparedStatement对象；sql模板中以"?"代替用户输入内容
		
		2.使用PreparedStatement对象调用setXXX(int parameterIndex,XXX xxx)系列方法给指定"?"赋值得到完整sql语句，这一步就是防止SQL攻击的一步
		
		3.使用PreparedStatement对象调用executeQuery()方法获取ResultSet对象或executeUpdate()方法执行语句，返回影响记录数

	4.预处理的原理

		第一次:服务器会先校验sql模板的语法，然后将sql模板编译成类似有参函数的东西，执行时只是带着参数去调用这个函数
		若二次:直接执行！不用再次校验语法及编译sql模板！(前提：连接的数据库必须支持预处理！几乎没有不支持的！但MySQL的预处理默认关闭！)
	
	5.PreparedStatement开启预处理：在url参数后追加参数"?useServerPrepStmts=true&cachePrepStmts=true"，表示开启预编译和开启对编译内容的缓存
			
============================================================================================================================================

JdbcUtils类V1：得到连接对象的步骤重复，这个类就专门用来获取连接对象，并且可以根据配置文件来修改连接到不同的数据库

	public class JdbcUtils {

		private static Properties pro = new Properties();
	
		static {// static块只在JdbcUtils类被加载时执行一次！
			try {
				pro.load(JdbcUtils.class.getResourceAsStream("/dbconfig.properties"));
				Class.forName(pro.getProperty("driverClassName"));
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		public static Connection getConnection() {
			try {
				return DriverManager.getConnection(pro.getProperty("url"),pro.getProperty("username"),pro.getProperty("password"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

dbconfig.properties配置文件，放在src目录下

	driverClassName=com.mysql.jdbc.Driver
	url=jdbc:mysql:///exam?useServerPrepStmts=true&cachePrepStmts=true			//localhost:3306可省略
	username=root
	password=123

============================================================================================================================================

DAO模式：service层的dao成员是一个接口，值为它的一个实现类对象。该对象通过工厂类的静态方法创建，方法中使用反射和配置文件，所以实现类可切换
	
============================================================================================================================================

时间类型

	1.时间相关类在数据库与java中的类型对应关系：

		DATE 			java.sql.Date		//日期，只有年月日，转换时会丢失时间；
		TIME 			java.sql.Time		//时间，只有时分秒，转换时会丢失日期；
		TIMESTAMP 	java.sql.Timestamp	//时间戳，有年月日时分秒，以及毫秒。

	2.领域对象（domain）中的所有属性不能出现java.sql包下的东西！

	3.时间类型的转换：
		
		ResultSet#getDate()方法返回值和PreparedStatement#setDate(int, Date)方法传入的都是java.sql.Date()类型，所以需要类型转换

		1.java.util.Date -> java.sql.Date、Time、Timestamp		//使用毫秒值转换

		2.java.sql.Date、Time、Timestamp -> java.util.Date		//使用多态转换

============================================================================================================================================

大数据

	所谓大数据，就是大的字节数据或字符数据。

	1.操作之前需要在my.ini文件中配置二进制数据大小上限：max_allowed_packet=xxx；重启服务器后生效
	
	2.还需要创建一张有blob类型的表
	
	3.p.setBlob(2,new SerialBlob(IOUtils.toByteArray(new FileInputStream("e:/Jony J - My City 南京.mp3"))));	//插入数据
	
	4.IOUtils.copy(r.getBlob(2).getBinaryStream(),new FileOutputStream("f:/a.mp3"));							//读取数据
			
============================================================================================================================================

批处理：针对的是更新语句，MySQL的批处理默认是关闭批处理的，开启需要手动在url参数后追加参数"?rewriteBatchedStatements=true"
														 如果不追加这个参数，也就是没有开启批处理，那么速度极慢
		
	添加批：通过PreparedStatement对象调用void executeBatch()方法可以将一条语句添加到"批"中
	执行批：通过Statement对象调用int[] executeBatch()方法可以将"批"中语句全部执行，返回值表示每条语句所影响的行数据
			当该方法执行之后，"批中语句就会被清空"，就是说连续调用两次该方法，第二次是没有SQL语句的空执行
	清空批：通过Statement对象调用void clearBatch()方法可以清空"批"中的所有语句

============================================================================================================================================

事务的四大特性：ACID

	原子性（Atomicity）：	不可再分割。事务中所有操作的执行要么全部成功，要么全部失败。
	一致性（Consistency）：	事务执行前后，业务数据保持一致。
	隔离性（Isolation）：	多个事务并发操作时可以隔离开来。
	持久性（Durability）：	数据到达数据库之后不能再出问题，就算数据库崩溃，也能恢复数据。


MySQL事务：默认情况下，每一条SQL语句都是一个单独的事务。但如果使用事务，多条SQL语句就是一次性操作。通过在操作数据库前后使用sql语句来完成

 	开启事务：start transaction;			结束事务：commit;或rollback;


JDBC事务：通过Connection完成，同一事务中所有的操作，必须同一个连接对象！在try块中开始事务和执行事务，在catch块中回滚事务。

	1. 开始事务：void setAutoCommit(boolean);				设为false表示开启事务，true为默认值
	2. 结束事务；void commit();或void rollback();
	
============================================================================================================================================

保存点

	保存点的是可以回滚到事务中的某个位置，而不是回滚整个事务！回滚到保存点不会结束事务！
	
	都是Connection对象的方法

		设置保存点：		Savepoint setSavepoint();
		回滚到保存点：		void rollback(Savepoint savepoint);

============================================================================================================================================

事务隔离级别：	脏读：			读到另一个事务的未提交的数据
				不可重复读：	读取到了另一事务的更新
				幻读：			读取到了另一事务的插入


四大隔离级别：	SERIALIZABLE（串行化）：		不会出现任何并发问题，因为它对同一数据的访问是串行的(非并发)，性能最差；
				REPEATABLE READ（可重复读）：	防止了脏读和不可重复读，性能比SERIALIZABLE好，MySQL默认
				READ COMMITTED（读已提交）：	只防止了脏读，性能比REPEATABLE READ好，Oracle默认
				READ UNCOMMITTED（读未提交）：	可能出现任何事务并发问题，性能最好

	在相同数据环境下，执行相同的工作，根据不同的隔离级别可以导致不同的结果。不同的事务隔离级别能解决的数据并发问题的能力是不同的。

	在MySQL数据库中：设置隔离级别：SET TRANSACTION ISOLATION LEVEL 隔离界别		查询隔离级别：select @@tx_isolation

	Mysql在JDBC中通过Connection对象调用setTransactionIsolation(int level)方法设置隔离界别

============================================================================================================================================

数据库连接池：连接池创建连接对象也是通过四大参数完成的！并且只增强了连接对象的close()方法！该方法不再是销毁连接，而是把当前连接归还给池！
			  使用池来管理连接的生命周期，可以节省资源，提高性能。

	接口：Java提供的连接池接口是javax.sql.DataSource，任何厂商的连接池都需要实现这一接口。


DBCP连接池：由Apache提供的一款开源免费的数据库连接池！Hibernate3.0之后不再对DBCP提供支持！连接池实现类是BasicDataSource

		所需jar包：commons-dbcp、commons-pool、数据库驱动包
		
		BasicDataSource dataSource = new BasicDataSource();			//创建DBCP连接池对象
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");		//配置四大参数
		dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
		dataSource.setUsername("root");
		dataSource.setPassword("123");
		
		//配置池参数（所有池参数都有默认值）
		dataSource.setMaxActive(int);								//最大连接数，默认值为8，如果设为非正数，表示无限制！
		dataSource.setMaxIdle(int);									//最大空闲连接数，默认值为8，如果设为负数，表示无限制！
		dataSource.setInitialSize(int);								//初始化池大小，默认值为0
		dataSource.setMinIdle(int);									//最小空闲连接，默认值为0
		dataSource.setMaxWait(long) ;								//最大等待时间，等待时间一到就抛异常，默认为-1，表示无期限等待
		
		Connection con = dataSource.getConnection();				//得到连接，使用的是装饰者模式
		con.close();												//归还连接

============================================================================================================================================

C3P0连接池：也是开源免费的连接池！连接池实现类是ComboPooledDataSource

	所需jar包：c3p0-pre、mchange-commons、数据库驱动包

	ComboPooledDataSource ds = new ComboPooledDataSource();		//创建C3P0连接池对象
	ds.setJdbcUrl("jdbc:mysql://localhost:3306/mydb1");			//配置四大参数
	ds.setUser("root");
	ds.setPassword("123");
	ds.setDriverClass("com.mysql.jdbc.Driver");
			
	//配置池参数（所有池参数都有默认值）
	ds.setAcquireIncrement(5) ;									
	ds.setInitialPoolSize(20) ; 
	ds.setMinPoolSize(2) ;
	ds.setMaxPoolSize(50) ;
			
	Connection con = ds.getConnection();						//得到连接，使用的是动态代理
	con.close();												//归还连接


三种得到连接池的方法：1.手动配置，如上代码就是手动配置
					  2.默认配置，在src目录下添加c3p0配置文件，在创建连接池时不传参
					  3.指定配置，在c3p0配置文件中自定义配置信息，然后在创建连接池时指定自定义配置信息元素的名称


	C3P0配置文件：	名称必须叫c3p0-config.xml，位置必须在src目录下
					<?xml version="1.0" encoding="UTF-8"?>
					<c3p0-config>
						<!--默认使用的配置信息-->
						<default-config> 
							<!-- 连接四大参数配置 -->
							<property name="jdbcUrl">jdbc:mysql:///mydb</property>
							<property name="driverClass">com.mysql.jdbc.Driver</property>
							<property name="user">root</property>
							<property name="password">123</property>
							<!-- 池参数配置 -->
							<property name="acquireIncrement">3</property>
							······
						</default-config>

						<!-- 专门针对提供的配置信息 -->
						<named-config name="xxxx">
						</named-config>
					</c3p0-config>


JdbcUtils类V2：添加了线程池，需要配置文件，使用的是配置文件的默认配置

	public class JdbcUtils {

		static private ComboPooledDataSource dataSource=new ComboPooledDataSource();//需要配置文件，使用的是默认配置
		
		public static Connection getConnection() throws SQLException{
			return dataSource.getConnection();
		}
		
		public static DataSource getDataSource(){
			return dataSource;
		}
	}

============================================================================================================================================

Tomcat配置连接池

	1.Tomcat配置JNDI资源

		JNDI（Java Naming and Directory Interface）Java命名和目录接口，作用：在服务器上配置资源，然后通过统一的方式获取这些配置资源。

		server.xml中的<Host>元素中添加，或在conf/catalina/localhost/下创建项目名.xml文件，文件中中添加如下内容：
		//注意！如果配置之后在Tomcat下找不到对应的项目时，会抛出异常

			<Context>  
				<Resource name="mydbcp"									//资源名称，在获取资源时需要使用它
					type="org.apache.tomcat.dbcp.dbcp.BasicDataSource"	//资源类型
					factory="org.apache.naming.factory.BeanFactory"		//创建资源的工厂，这个值基本上是固定的
					username="root"										//其他的东西都是资源的参数
					password="123"							     
					driverClassName="com.mysql.jdbc.Driver"    
					url="jdbc:mysql://127.0.0.1/mydb"
					maxIdle="3"
					maxWait="5000"
					···
					/>
			</Context>

			<Context>  
				<Resource name="myc3p0"									//c3p0资源
					type="com.mchange.v2.c3p0.ComboPooledDataSource"
					factory="org.apache.naming.factory.BeanFactory"
					user="root" 
					password="123" 
					classDriver="com.mysql.jdbc.Driver"    
					jdbcUrl="jdbc:mysql://127.0.0.1/mydb"
					maxPoolSize="20"
					minPoolSize ="5"
					···
					/>
			</Context>  

	2.获取Tomcat资源

		Context cxt = new InitialContext();										//创建JNDI的上下文对象，这里需要的类都在javax.naming包下
		Context envContext = (Context)cxt.lookup("java:comp/env");				//查询入口
		DataSource dataSource = (DataSource)envContext.lookup("myc3p0");		//二次查询，找到资源，这里就需要资源名称
		Connection con = dataSourc.getConnection();								//得到连接

		DataSource dataSourc = (DataSource)cxt.lookup("java:/comp/env/myc3p0");	//其实两次查询可以一步完成

============================================================================================================================================

ThreadLocal<T>：内部是Map，它的键就是当前线程，值为泛型类。每个线程都有且只有自己的一个值，重复设置则覆盖。只有三个方法：
	
	void set(Object value)：保存值		T get()：获取值		  void remove()：移除值

============================================================================================================================================

dbutils源码剖析：这里要求JavaBean与数据库表对象关系映射

		class QR<T> {

			private DataSource dataSource;
			
			public QR(DataSource dataSource) {														// 创建该对象需要提供连接池
				this.dataSource = dataSource;
			}

			public QR() {
				super();
			}

			public int update(String sql, Object... params) {
				Connection con = null;
				PreparedStatement pstmt = null;
				try {
					con = dataSource.getConnection();
					pstmt = con.prepareStatement(sql);
					initParams(pstmt, params);														// 调用方法设置参数
					return pstmt.executeUpdate();
				} catch(Exception e) {
					throw new RuntimeException(e);
				} finally {
					try {
						if(pstmt != null) pstmt.close();
						if(con != null) con.close();
					} catch(SQLException e1) {}
				}
			}
			
			private void initParams(PreparedStatement pstmt, Object... params)throws SQLException {	// 循环遍历设置参数的方法
				for(int i = 0; i < params.length; i++) {
					pstmt.setObject(i+1, params[i]);
				}
			}
			
			public T query(String sql, RsHandler<T> rh, Object... params) {	
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					con = dataSource.getConnection();	
					pstmt = con.prepareStatement(sql);				
					initParams(pstmt, params);														// 调用方法设置参数
					rs = pstmt.executeQuery();
					return rh.handle(rs);															// 返回指定类型的对象
				} catch(Exception e) {
					throw new RuntimeException(e);
				} finally {
					try {
						if(pstmt != null) pstmt.close();
						if(con != null) con.close();
					} catch(SQLException e1) {}
				}
			}
		}


		interface RsHandler<T> {																	// 该接口让实现类自己指定类型

			public T handle(ResultSet rs) throws SQLException;										// 给实现类提供的封装数据到对象的方法
		}


		class Demo2 {

			public void addStu(Stu stu) {
				QR qr = new QR(JdbcUtils.getDataSource());											// 通过工具类方法得到连接池创建QR对象
				String sql = "insert into t_stu values(?,?,?,?)";									// 可给出增、删、改三种类型的sql模板
				
				Object[] params = {stu.getSid(), stu.getSname(), stu.getAge(), stu.getGender()};	// 并提供参数
				
				qr.update(sql, params);																// 调用操作数据库的方法
			}
			
			public Stu load(int sid) {
				QR qr = new QR(JdbcUtils.getDataSource());			
				String sql = "select * from t_stu where sid=?";										// 给出查询的sql模板	
				Object[] params = {sid};															// 并提供参数
				RsHandler<Stu> rh = new RsHandler<Stu>() {											// 创建接口的实现类对象，这里指定类型
					public Stu handle(ResultSet rs) throws SQLException {							// 实现方法，封装数据
						if(!rs.next()) return null;				
						Stu stu = new Stu();
						stu.setSid(rs.getInt("sid"));
						stu.setSname(rs.getString("sname"));
						stu.setAge(rs.getInt("age"));
						stu.setGender(rs.getString("gender"));
						return stu;
					}
				};
				return (Stu) qr.query(sql, rh, params);												//返回对象
			}
		}

============================================================================================================================================

commons-dbutils jar包中的QueryRunner类和ResultSetHandler接口及其实现类的方法：

	1.QueryRunner

	    update方法：int update(String sql, Object... params)						// 可执行增、删、改语句
					int update(Connection con, String sql, Object... parmas)		// 需要提供Connection，不再管理Connection了，支持事务！
									       
	    query方法:T query(String sql, ResultSetHandler rsh, Object... params)					// 可执行查询语句
				  T query(Connection con, String sql, ResultSetHadler rsh, Object... params)	// 同样支持事务

	2.ResultSetHandler接口：	

		1.BeanHandler(单行)				// 其构造器需要一个Class类型的参数，用来把一行结果集转换成指定类型的JavaBean对象

		2.BeanListHandler(多行)			// 其构造器也是需要一个Class类型的参数，可以把多行结果集转换为指定类型的一个JavaBean对象集合

		3.MapHandler(单行)				// 把一行结果集转换成一个Map对象，适用于一行记录对应多个JavaBean类型的情况

		4.MapListHandler(多行)			// 把多行结果集转换成一个Map对象集合，适用于一行记录对应多个JavaBean类型的情况

		5.ScalarHandler(单行单列)		// 适用于查询聚合函数的情况，不同数据库的返回类型不同，可以先转为它们的共同父类Number再转成指定类型

============================================================================================================================================

BaseServlet源码剖析：public abstract class BaseServlet extends HttpServlet {
	
						public void service(HttpServletRequest req, HttpServletResponse resp)
								throws ServletException, IOException {
							
							// 得到method参数值，并要求不能为空，它对应一个方法名称
							String methodName = req.getParameter("method");		
							if(methodName == null || methodName.trim().isEmpty()) {
								throw new RuntimeException("您没有传递method参数！无法确定您想要调用的方法！");
							}
							
							// 查找对应的方法，要求方法必须存在
							Method method = null;
							try {
								method = getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
							} catch (Exception e) {
								throw new RuntimeException("您要调用的方法不存在！");
							}
							
							// 通过反射执行该方法
							try {
								// 获取方法的字符串返回值，以r开头为重定向，以f开头为转发，默认为转发，如果没有返回值，那么不进行任何操作
								// 要求字符串格式为："x:/资源路径"
								String result = (String)method.invoke(this, req, resp);
								if(result == null || result.trim().isEmpty()) {
									return;
								}
								
								if(result.contains(":")) {
									int index = result.indexOf(":");
									String s = result.substring(0, index);
									String path = result.substring(index+1);
									if(s.equalsIgnoreCase("r")) {
										resp.sendRedirect(req.getContextPath() + path);
									} else if(s.equalsIgnoreCase("f")) {
										req.getRequestDispatcher(path).forward(req, resp);
									} else {
										throw new RuntimeException("当前版本不支持你指定的操作！");
									}
								} else {
									req.getRequestDispatcher(result).forward(req, resp);
								}
							} catch (Exception e) {
								System.out.println("您调用的方法内部抛出了异常！");
								throw new RuntimeException(e);
							}
						}
					}

这个类继承了HttpServlet，但它并不是一个Servlet，只是一个普通类，继承这个类的Servlet可以处理多种请求！但需要给出method参数来对应一个请求方法。

=============================================================================================================================================

Service层处理事务

	问题：1.在Dao中不应该存在业务，应该只能对数据库进行零散的基本操作

		  2.但在Service中又不应该出现Connection的事务操作
		  
	      3.所以需要在Service层处理事务时将所有的操作隐藏起来，例如通过工具类来封装事务操作


	JdbcUtils类V3：添加了对事务处理的功能，需要保证开启事务、提交、回滚以及Service层中的操作，使用的都是同一个连接对象 
		
		public class JdbcUtils {
			
			// 将连接对象定义为成员，保证了本类的开启事务、提交、回滚三个方法使用的都是同一个连接对象
			private static Connection connection = null;
			
			static private ComboPooledDataSource dataSource = new ComboPooledDataSource();
			
			// 通过判断返回成员对象，保证与Service层共用同一个连接对象
			public static Connection getConnection() throws SQLException {
				if(connection != null)return connection;
				return dataSource.getConnection();
			}

			public static DataSource getDataSource() {
				return dataSource;
			}
			
			// 开启事务方法：开启事务时得到连接对象，并将其赋值给成员，还有提交事务操作
			public static void beginTransaction() throws SQLException {
				connection = getConnection();
				connection.setAutoCommit(false);
			}
			
			// 提交方法：处理了没有开启事务就提交的情况发生，并且关闭了连接资源，将连接对象赋值为null可以将这次事务下一次事务的关系切断
			public static void commitTransaction() throws SQLException {
				if(connection == null)throw new SQLException("未开启事务！");
				connection.commit();
				connection.close();
				connection = null;
			}
			
			// 回滚方法：同上
			public static void rollbackTransaction() throws SQLException {
				if(connection == null)throw new SQLException("未开启事务！");
				connection.rollback();
				connection.close();
				connection = null;
			}
			
			// 释放资源方法：通过判断保证未开启事务时，和事务非本次事务时，连接能够及时关闭
			public static void releaseConnection(Connection con) throws SQLException{
				if(con == null)con.close();
				if(connection != con)con.close();
			}
		}


	JdbcUtils类V4：通过ThreadLocal处理了并发问题
	
		public class JdbcUtils {
			
			// 添加ThreadLocal保证每个线程使用自己线程中的连接对象
			private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
			
			static private ComboPooledDataSource dataSource = new ComboPooledDataSource();
			
			// 使用ThreadLocal更简单的保证了连接对象一致
			public static Connection getConnection() throws SQLException {
				if(threadLocal.get() != null)return threadLocal.get();
				return dataSource.getConnection();
			}

			public static DataSource getDataSource() {
				return dataSource;
			}
			
			// 得到的连接对象不再是赋值给成员，而是添加到ThreadLocal中
			public static void beginTransaction() throws SQLException {
				Connection connection = threadLocal.get();
				connection.setAutoCommit(false);
				threadLocal.set(connection);
			}
			
			// 事务结束时需要移除连接对象，与下一次事务切断关系
			public static void commitTransaction() throws SQLException {
				Connection connection = threadLocal.get();
				if(connection == null)throw new SQLException("未开启事务！");
				connection.commit();
				connection.close();
				threadLocal.remove();
			}
			
			// 同上
			public static void rollbackTransaction() throws SQLException {
				Connection connection = threadLocal.get();
				if(connection == null)throw new SQLException("未开启事务！");
				connection.rollback();
				connection.close();
				threadLocal.remove();
			}
			
			public static void releaseConnection(Connection con) throws SQLException{
				if(threadLocal.get() == null)con.close();
				if(threadLocal.get() != con)con.close();
			}
		}


	问题：这时发现在Dao层创建QueryRunner时不再提供连接对象，因为那样创建的连接对象不能够支持事务，所以需要频繁手动地去创建连接和释放连接
		  设想写：一个类重写QyeryRunner中的所有不带连接参数的方法，在方法中提供连接再调用有连接参数的重载方法，最后释放连接，例如：
			
		
		public class TxQueryRunner extends QueryRunner {
			
			@Override
			public int[] batch(String sql, Object[][] params) throws SQLException {
				
				Connection con = JdbcUtils.getConnection();
				int[] result = super.batch(con, sql, params);
				JdbcUtils.releaseConnection(con);
				return result;
			}

=============================================================================================================================================