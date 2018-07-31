	
JavaEE三层架构回顾

	视图层 - web层	   - 使用struts2框架
	业务层 - service层 - 使用spring框架
	持久层 - dao层	   - 使用hibernate框架

	MVC思想，模型视图控制器，不是Java独有的分层思想

==============================================================================================================================================

Hibrenate概述

	框架：框架能够帮我们实现一部分功能，或可以使我们少写一部分代码就可以实现一些功能

	1.开源轻量级框架，应用在dao层，版本：hibernate5.x

	2.使用orm思想（object relational mapping 对象关系映射）对数据库进行crud操作
		
			1.使用配置文件让实体类与与数据库表对应，实体类属性和表字段对应//javabean更正确的叫法为实体类
			2.不需要直接操作数据库表，而是操作表对应的实体类对象

	3.底层代码就是jdbc，并对其进行了封装，不再需要写jdbc代码和sql语句
	
============================================================================================================================================

搭建hibernate环境

	1.导包：lib\required 的jar包、lib\jpa 的jar包、日志jar包

	2.创建实体类，hibernate要求实体类中有一个属性唯一的值，对应表中的主键，hibernate可以创建表，但是数据库需要手动创建

	3.使用配置文件实现实体类与数据库表的映射关系

		1.创建映射配置文件：文件名称和位置没有固定要求，但一般建议与实体类放在一起，命名为 实体类名.hbm.xml

		2.引入dtd约束并配置映射关系
															
			public class User {						
				private int uid;					
				private String username;				
				private String password;				
				private String address;				
				set/get······
			}		
					
			<?xml version="1.0" encoding="UTF-8"?>
			<!DOCTYPE hibernate-mapping PUBLIC
			"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
			"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
			<hibernate-mapping>
				<class name="cn.itcast.domin.user.User" table="t_user">		//类路径与表名
					<id name="uid" column="uid">							//属性名和主键字段名称，如果省略column，与name值相同
						<generator class="native"></generator>				//主键增长策略
					</id>
					<property name="username" column="username"></property>	//属性和字段对应，type属性指定字段类型，省略默认对应属性类型
					<property name="password" column="password"></property>
					<property name="address" column="address"></property>		
				</class>														
			</hibernate-mapping>


	4.创建hibernate核心配置文件，也是使用dtd约束，如果单独使用hibernate框架，那么该文件必须在src目录下，名称必须为hibernate.cfg.xml

		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE hibernate-configuration PUBLIC
			"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
			"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
		<hibernate-configuration>
			<session-factory>
				//第一部分：配置数据库的信息
				<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
				<property name="hibernate.connection.url">jdbc:mysql:///mydb</property>						//localhost:3306可以省略
				<property name="hibernate.connection.username">root</property>
				<property name="hibernate.connection.password">123</property>
				//第二部分：配置hibernate信息，这部分是可选的
				<property name="hibernate.show_sql">true</property>											//控制台是否输出SQL语句		
				<property name="hibernate.format_sql">true</property>										//是否格式化SQL语句
				<property name="hibernate.hbm2ddl.auto">update</property>									//有表更新，无表自动创建
				<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>			//让hibernate识别数据库方言
				//第三部分：引入映射配置文件，hibernate操作过程中，只会加载核心配置文件，所以需要引入其他配置文件
				<mapping resource="cn/itcast/domin/user/User.hbm.xml"/>						
			</session-factory>
		</hibernate-configuration>			

============================================================================================================================================

Hibernate核心API

	1.Configuration

		其实单独使用hibernate也可以指定核心配置文件的位置的，这时就需要手动去加载映射文件，并且可以在代码中引入映射配置文件到核心配置文件中
		
			Configuration config = new Configuration().configure("/config/hibernate.cfg.xml");
			configuration.addResource("cn/itcast/domain/Customer.hbm.xml");

	2.SessionFactory

		创建该对象的过程会创建表，很耗资源，所以建议一个项目只有一个该对象，通过工具类的静态代码块在类加载时创建，并且只创建一次

	3.Session

		这个单线程对象(只能自己使用)的作用类似于jdbc中的Connection，调用其方法可以实现crud操作，底层就是ThreadLocal

			1.添加：save(对象)，与id无关，id由表自动生成，如果该对象是查询出来的对象，那么调用该方法执行的是修改操作
								该方法返回一个Serializable值，是生成的id值，该值为null说明添加失败										****								

			2.修改：update(对象)，根据对象的id值查询到对象后进行修改，会将对象的所有非id的属性值在对应的表字段中进行修改

			3.删除：delete(对象)，根据对象的id值查询到对象后删除对应的表记录，删除后主键增长依然保持
							      注意：如果是查询出来的对象，就算修改了id值，删除的也会是原id对应的表记录

			4.查询：get(实体类Class,id值)，返回对象

			5.saveOrUpdate()：既能实现添加（无id），也能实现修改（有id）

		为了保证Session是单线程对象，可以将Session与本地线程绑定来实现
		
			1.在核心配置文件中添加配置 <property name="hibernate.current_session_context_class">thread</property>
		
			2.通过SessionFactory的getCurrentSession()方法获取

			3.这时就不需要手动关闭这个与本地线程绑定的Session了

	4.Transaction

		事物对象，有beginTransaction、commit和rollback方法，事务有四个特性，原子性、一致性、隔离线、持久性
		在hibernate核心配置文件中也是可以配置事务的隔离级别的

============================================================================================================================================

工具类

	public class HibernateUtil {

		private static SessionFactory sessionFactory;
		
		static{
			try {
				Configuration configuration=new Configuration();
				configuration.configure();
				sessionFactory=configuration.buildSessionFactory();
			} catch (ExceptionInInitializerError e) {
				throw new ExceptionInInitializerError("配置文件出错");
			}
		}
		
		public static Session openSession(){
			return sessionFactory.openSession();
		}
		
		public static SessionFactory getSessionFactory(){
			return sessionFactory;
		}
		
		public static Session getCurrentSession(){
			return sessionFactory.getCurrentSession();
		}
	}

============================================================================================================================================

基础代码								

		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction transaction = null;
		try{
			Configuration configuration=new Configuration();
			configuration.configure();								// 1.加载hibernate核心配置文件到Configuration对象中
			sessionFactory = configuration.buildSessionFactory();	// 2.读取文件，创建SessionFactory对象，这个过程可以创建表
			session = sessionFactory.openSession();					// 3.创建Session对象
			transaction = session.beginTransaction();				// 4.开启事务，hibernate要求手动开启事务
			······													// 5.crud操作
			transaction.commit();									// 6.提交事务
		}catch(Exception e){
			transaction.rollback();
			e.printStackTrace();									// 7.回滚事务
		}finally{
			if(session != null)session.close();
			if(sessionFactory != null)sessionFactory.close();		// 8.关闭资源，在Web项目中，SessionFactory是不需要关闭的
		}

============================================================================================================================================

实体类的编写规则

	1.属性私有并提供get/set方法

	2.有属性作为唯一值，对应数据库表的主键，一般为id

	3.属性建议使用包装类，因为多一个null

============================================================================================================================================

实体类对象的状态

	1.瞬时态：对象没有id值，也与Session对象没有关联

	2.托管态：对象有id值，但与Session对象没有关联

	3.持久态：对象有id值，也去Session对象有关联

============================================================================================================================================

主键增长策略

	1.increment：每次增加1，适用于long、int、short类型

	2.identity：条件是数据库支持自增长的数据类型

	3.sequence：条件是数据库支持序列

	4.native：根据本地数据库自动选择主键增长策略

	5.uuid：自动生成uuid字符串值

============================================================================================================================================

表间关系

	1.一对多映射配置
		
		1.创建两个实体类，这里以客户和联系人为例，客户为一方，多方关联一方

		2.让实体类之间互相关联：在客户中给出Set<LinkMan> setLinkMan成员属性并赋值和提供get/set方法，注意要使用Set集合
								在联系人中给出Customer customer成员属性和get/set方法

		3.创建两个映射配置文件并配置一对多关系

			Customer.hbm.xml：  <set name="setLinkMan">
									<key column="外健名称"></key>
									<one-to-many class="多方的类路径"/>
								</set>
																		
			LinkMan.hbm.xml：	<many-to-one name="customer" class="一方的类路径" column="与上面对应"></many-to-one>
		

		4.在核心配置文件中引入这两个映射配置文件
		
	2.一对多级联操作

		1.级联保存：添加一个客户时还为其添加多个联系人

			创建客户和联系人，在客户中添加联系人，最后都需要保存，但在客户的Set标签中添加属性 cascade="save-update"，就只需保存客户了

		2.级联删除：在客户的Set标签中的cascade属性添加delete值，cascade="save-update,delete"，删除查询出来客户，他的联系人也会被删除
					过程：查询客户 -> 根据客户id与联系人外键查询联系人 -> 把查询出的联系人的外键设置为NULL -> 删除联系人 -> 删除客户
					如果不加 cascade="delete" 那么就只是删除客户，其相关联系人外键变为NULL而已
					如果不是通过查询得到的客户，就算有他的id有对应的联系人匹配，删除该客户，这个联系人也不会被删除，只是外键变为NULL

		3.修改操作：根据id查询出客户或联系人，一方设置成员即可。

	3.其实配置实体类之间的关系是不需要两端都进行配置的，而是根据需求，例如我只需要通过多方查询一方，那么一方就不需要配置映射关系了

================================================================================

	3.多对多映射配置

		1.创建实体类，这里以用户和角色为例

		2.让实体类之间互相关联：在用户和角色中都给出Set成员属性并赋值和提供get/set方法

		3.创建两个映射文件并配置多对多关系
													  
			User.hbm.xml：  <set name="setRole" table="第三张表的名称">
								<key column="在第三张表中的外键名称"></key>
								<many-to-many class="···Role" column="roleid"></many-to-many>
							</set>				 //角色类的全路径  角色在第三张表中的外键
																	
			Role.hbm.xml：	<set name="setUser" table="与上对应">
								<key column="与上对应"></key>
								<many-to-many class="···User" column="userid"></many-to-many>
							</set>		

		4.在核心配置文件中引入两个映射配置文件

	4.多对多级联操作

		1.级联保存：在用户的Set标签中添加属性 cascade="save-update"，在代码中只需设置一方的成员并且只需保存即可

		2.级联删除：在用户的Set标签中的cascade属性添加delete值，在代码中只需删除用户对象，关联的角色对象也会被删除，
				    但是注意！这里就算一个角色可能还关联其他用户，也会被删除！
					
	5.维护第三张表

		1.用户和角色间的关系是通过第三张表来维护的

			1.给用户(任意方)添加角色:session.get(User.class,n).getSetRole().add(session.get(Role.class,n));再保存用户

			2.给用户(任意方)移除角色：session.get(User.class,n).getSetRole().remove(session.get(Role.class,n));再保存用户

============================================================================================================================================

查询对象

		1.Query：		Query query=session.createQuery("hql语句");
						List<User> list=query.list();								

		2.Criteria：	Criteria criteria=session.createCriteria(实体类Class);		//不需要写语句，直接调用方法
						List<User> list=criteria.list();

		3.SQLQuery：	SQLQuery sqlQuery=session.createSQLQuery("普通sql语句");
						List<Object[]> list=sqlQuery.list();						//返回的List集合每部分都是数组

						sqlQuery.addEntity(实体类Class);							//想要返回对象，需要手动添加实体类
						List<User> list=sqlQuery.list();

============================================================================================================================================

Hibernate的查询方式

	1.对象导航查询：适用于查询某个客户中的所有联系人，通过得到查询用户的Set集合来实现

	2.OID查询：就是get方法

	3.hql查询
			
		1.使用Query和hibernate提供的hql查询语言(hibernate query language)，其与普通sql语句相似，但操作的是实体类和类的属性

		2.常用的hql语句

			1.查询所有：Query query=session.createQuery("from "); -> List<User> list=query.list();

			2.条件查询：from 实体类 where 属性 = ? and 属性 like ?	->	query.setParameter(下标,值);

			3.排序查询：order by 属性 asc/desc，asc为默认可不写
										
			4.分页查询：query.setFirstResult(开始位置);	query.setMaxResults(记录数);	//下标从0开始

			5.投影查询：就是查询指定列：select 属性 from 实体类，单个返回Object，多个查询返回Object数组，select后面不支持写"*"

			6.聚集函数：count(Long)、sum(Long)、avg(Double)、max(Integer)、min，写在select后 -> Object object = query.uniqueResult();

	4.QBC查询

		1.使用Criteria，不需要写查询语句，直接通过方法实现查询，也是操作实体类和类的属性

		3.qbc查询

			1.查询所有：Criteria criteria=session.createCriteria(Class); -> List<User> list=criteria.list();

			2.条件查询：在Criteria对象的add方法来设置条件的值，参数中传入Restrictions类的匹配方法
						
				Restrictions.eq("属性名称",值);	 =，	allEq(Map<String,?>)	匹配多个属性值				
							 gt	>， ge >=， lt <， le<=， between， like， in，······
 
			3.排序查询：在Criteria对象的addOrder方法中使用Order类的两个方法：asc和desc，这两个方法中传入属性名称

			4.分页查询：setFirstResult(开始位置); setMaxResults(记录数);

			5.统计查询：在Criteria对象的setProjection方法中传入Projections类的方法，使用Criteria对象的uniqueResult方法得到Object对象

			6.离线查询：不在dao层也能拼接出查询条件																						****

				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Class);		//不需要使用session
				Criteria criteria = detachedCriteria.getExecutableCriteria(Session对象);	//执行才需要session

	5.本地sql查询：使用SQLQuery对象，使用普通sql语句查询

============================================================================================================================================

HQL多表查询
	
	1.内连接	Query query = session.createQuery("from Customer c inner join c.setLinkMan");
				List list = query.list();    //list里面每部分都是Object数组形式

	2.左外连接：left outer join，数组形式

	3.右外连接

	4.迫切内连接：与内连接底层实现一样，在join后加上fetch，对象形式
		
	5.迫切左外连接：对象形式，没有迫切右外连接

============================================================================================================================================

Hibernate缓存

	缓存：数据库本身是文件系统，通过流来操作文件效率低，将数据存在内存中可以直接读取数据，效率提高了

	hibernate缓存就是该框架提供优化方式的一种

		hibernate一级缓存：1.默认打开

						   2.使用范围是从创建Session对象到关闭对象资源

						   3.存储的数据必须是持久态数据，存储的是值，不是对象

						   4.执行过程：Session对象被创建后，会有一个一级缓存，查询数据时会先到一级缓存中查询，如果没有，再到数据库查询，
									   查询到的数据会被放到一级缓存中，如果在一级缓存中查询到，会把这些值封装成一个新的对象返回
									   两次查询同一个数据，只会在第一次查询时输出查询语句，因为查询的是数据库

						   5.持久态自动更新数据库，修改持久态对象的属性值后不用执行修改操作，在提交事务时会自动执行
		
								原理：持久态对象数据缓存后，会在快照区(副本)也存一份，修改数据后，缓存中的数据会修改，但快照区中的数据不会
									  在最后提交事务时，会对两块内容进行比较，只要不相同会自动执行更新操作


		hibernate二级缓存：1.默认关闭，需要配置开启
						   2.使用范围是从创建SessionFactory对象到关闭对象资源
						   3.目前已经被redis技术替代

============================================================================================================================================

Hibernate检索策略

	1.立即查询：get方法一被调用就发送查询语句

	2.延迟查询：load方法不会立即发送查询语句，返回的对象里面只有id值，只有当用到对象的其他属性时，才会发送查询语句查询

		1.类级别延迟：根据id查询返回实体类对象，调用load方法不会马上发送语句

		2.关联级别延迟：查询某个客户的所有联系人的过程是否需要延迟，这个过程就称为关联级别延迟

			默认在得到客户的成员集合时，不发送语句，需要集合的内容时才发送语句
			可以在客户的Set标签配置fetch和lazy属性来改变，这两个属性默认值是select和true，主要通过修改lazy属性来改变延迟

				lazy属性：1.false：不延迟，一次性直接发送完语句
						  2.true： 延迟，默认
						  3.extra：及其延迟，与设值为true时类似，但是发送的查询语句是针对性查询，需要什么查什么

============================================================================================================================================

批量抓取

	查询所有客户和所有联系人需要使用两个循环，发送很多语句，效率低，
	在客户的Set标签中配置batch-size属性，内容值为整数就行，值越大发送的语句越少，效率越高