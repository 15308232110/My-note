Spring概念

	1.Spring是一个开源的轻量级框架(不需要依赖于很多其他的东西)，替代了重量级框架EJB，版本：Spring4.x，有两个核心部分：aop和ioc
	
	2.Spring是一站式框架，在JAVAEE三层结构中，都提供相关的技术:web层：SpringMVC、service层：Spring的ioc、dao层：Spring的jdbcTemplate

=============================================================================================================================================

Spring中的ioc

	1.ioc控制反转将创建对象的过程交给Spring通过配置进行管理，ioc主要的两部分操作：配置文件和注解

	2.ioc底层原理：使用工厂设计模式先定义工厂类，在其方法中通过反射由dom4j解析xml配置文件得到的类路径来创建对象

=============================================================================================================================================

Spring基础环境搭建

	1.导包，Spring四个核心jar包：beans、context、core、expression，日志jar包：logging、log4j

	2.创建Spring核心配置文件，名称和位置不固定，使用的是schame约束，建议放在src下，取名为applicationContext.xml

=============================================================================================================================================

Spring的bean管理（配置文件）

	1.bean实例化的三种方式

		1.无参构造：<bean id = "user" class = "cn.itcast.ioc.User"></bean>//允许：接口 = 实现类、多态

		2.静态工厂：<bean id="user" class = "cn.itcast.ioc.UserFactory" factory-method = "getUser"></bean>//静态方法中返回一个new出来的对象
					
		3.实例工厂：<bean id = "UserFactory" class = "cn.itcast.ioc.UserFactory"></bean>//成员方法中返回一个new出来的对象
					<bean id = "user" factory-bean = "UserFactory" factory-method = "getUser"></bean>

		ApplicationContext context = new ClassPathXmlApplicationContext("Spring核心配置文件路径");//这个加载核心配置文件的过程会创建所有对象
		User user = (User)context.getBean("user");

=============================================================================================================================================

bean标签的属性：id：就是一个名称，不能包含特殊符号
				class：对象的类路径
				name属性：功能和id属性一样，但可以包含特殊符号
				scope属性值：singleton：		单例，默认
							 prototype：		多例
							 request：			创建对象放在request域中				//为什么加上这个不能创建对象，怎么证明对象在域中    ****
							 session			创建对象放在session域中										
							 golbalSession：	创建对象放在golbalSession中，例如登陆了百度文库，就不需要再登录百度贴吧

=============================================================================================================================================

属性注入的三种方式

	DI: 依赖注入：在创建对象时，向对象所在类里面的属性赋值，不能单独存在，需要在IOC基础之上完成操作

	1.set方法：通过set方法对成员赋值，Spring支持，添加bean的子标签：<property name="属性名称" value="属性值"/ref="bean标签id值"></property>

	2.有参构造：通过有参构造对成员赋值，······：简单类型注入：<constructor-arg name="属性名称" value="属性值"></constructor-arg>
											    对象类型注入：<property name="属性名称" ref="bean标签id值"></property>	
											   
	3.接口注入：实现方法的参数赋给成员，Spring不支持

	p名称空间注入：在beans标签xmlns属性，添加命名空间并修改后缀为p：xmlns:p = "http://www.springframework.org/schema/p"
				   在bean标签中可直接对_简单数据类型_成员赋值<bean id = "person" class = "cn.itcast.damin.Person" p:pname = "Bob"></bean>

	复杂属性注入：内容写在<property>标签中

		1.Array和List：					2.Map：										3.properties
			<list>						 <map>											<props>					        
				<value> xxx </value>		<entry key = "xx" value="xx"></entry>			<prop key = "xx"> xx </prop>
				······						······											····
			</list>						 </map>											</props>

=============================================================================================================================================

Spring的bean管理（注解）

	1.想要使用注解操作，需要使用一个jar包：aop，在Spring核心配置文件中还需要引入content约束路径

	2.开启注解扫描

			<context:component-scan base-package="包，多个包可用逗号隔开或减少层数来扩大范围"></context:component-scan>//扫描所有
			<context:annotation-config></context:annotation-config>//单独使用这个标签为只扫描属性

	4.创建对象的四个注解

		1.@Component	在类前写 @Component(value="user")	相当于配置<bean id = "user" class = "完整类路径"/>
		2.@Controller	Web层	3.@Service	Service层	4.@Repository	Dao层，目前这四个注解功能一致，提供出来是为了后续版本增强
		另外Scope注解和bean标签的scope属性一样

	5.注入属性的两个注解，在属性前写 @AutoWired (根据类去找对象) 或 @Resource(name = "userDao")(指定对象)，不需要提供set方法，会由底层实现

	6.注解和配置文件可以混合使用，例如在创建对象时使用配置文件，注入属性时使用注解

=============================================================================================================================================

AOP

	AOP：Aspect Oriented Programing 面向切面编程，扩展功能时不修改源代码，采取横向抽取机制，取代了传统的纵向继承体系

		1.纵向继承体系：将增强代码写在另一个类的方法中，通过继承直接调用父类方法来实现

		2.横向抽取机制底层使用动态代理来实现，有两种情况

			1.jdk动态代理：针对有接口的情况，使用动态代理创建与接口实现类平级的代理对象，由代理对象实现方法并增强

			2.cglib动态代理：针对无接口的情况，创建子类代理对象，由代理对象调用父类方法并增强

	术语

		1.JoinPoint      连接点：    可以被增强的方法
		2.Pointcut       切入点：    实际被增强的方法
		3.Adivice     通知/增强：    增强的代码

			1.前置通知：	在方法之前执行
			2.后置通知：	在方法之后执行
			3.异常通知：	出现异常时执行
			4.最终通知：	在后置通知之后执行
			5.环绕通知：	在方法之前和之后执行，优先级分别低于前置通知和后置通知
			6.Introduction  引介：	动态添加属性或方法，是一种特殊的通知																****

		4.Aspect	      切面：     增强使用给切入点的过程
		5.Target	  目标对象：     切入点所在类
		6.Weaving		  织入：     增强使用给目标对象的过程
		7.Proxy			  代理：	 织入后产生的代理类

	Aspectj：一个基于Java语言的AOP框架，经常与Spring一起被使用来进行AOP操作，Spring2.0之后新增了对Aspectj切点表达式的支持

		1.Aspectj实现AOP有两种方式：配置文件和注解

		2.准备工作：1.所需jar包：aopalliance、aspectjweaver、aop、sapects		2.配置Spring核心配置文件：还需要引入aop约束路径
	
=============================================================================================================================================

Aspectj操作

	1.使用表达式配置切入点：execution(<访问修饰符>?<返回类型><方法名>(<参数>)<异常>)，这是execution函数
			
		1.execution(* cn.itcast.aop.Book.add(..))	Book类中的add方法
		2.execution(* cn.itcast.aop.Book.*(..))		Book类中的所有方法
		3.execution(* *.*(..))						所有方法
		4.execution(* save*(..))					所有名称以save开头的方法

	2.实例	
	
		<bean id = "book" class = "cn.itcast.aop.Book"></bean>
		<bean id = "myBook" class = "cn.itcast.aop.MyBook"></bean>//配置对象
		<aop:config>//定义切入点 和 配置切面
			<aop:pointcut id = "pointcut" expression = "execution(* cn.itcast.aop.Book.*(..))"/>
			<aop:aspect ref = "myBook">
				<aop:before method = "before" point-ref = "pointcut"/>
			<aop:aspect>//将MyBook类中的before方法采用前置增强的方式，增强给Book类中的所有方法，可配置多个、多种增强
		</aop:config>

		对于环绕通知：增强(方法)中添加ProceedingJoinPoint类型参数，先后增强代码间通过参数的proceed()方法隔开

		如果使用注解的方式，也需要先创建对象，在配置中开启aop操作：<aop:aspectj-autoproxy></aop:aspectj-autoproxy>，对action中的方法无效

		在增强的类上添加注解 @Aspect，在增强的方法上使用注解配置 @Before(value = "execution(* cn.itcast.aop.Book.*(..))")

==============================================================================================================================================

jdbcTemplate

	1.Spring对不同的持久化层技术都就行了封装并提供了接口实现，需要导入jar包：jdbc、tx、数据库驱动jar包，！注意，需要自己手动创建表

	3.操作：			DriverManagerDataSource dataSource = new DriverManagerDataSource();
						dataSource.setDriverClassName("com.mysql.jdbc.Driver");
						dataSource.setUrl("jdbc:mysql:///mydb");
						dataSource.setUsername("root");
						dataSource.setPassword("123");
						JdbcTemplate jdbcTempalte = new JdbcTemplate(dataSource);
					
		1.增删改		int rows = jdbcTemplate.update(sql语句,参数···);

		2.查询：Dbutils有泛型接口ResultSetHander，jdbcTemplate也有泛型接口RowMapper，但没有提供其实现类，需要自己写类实现接口来数据封装

			1.返回记录数	所需类型 row = jdbcTemplate.queryForObject(sql,所需类型.Class);

			2.返回对象		T t = jdbcTemplate.queryForObject(sql,RowMapper接口的实现类对象,参数);

			3.返回集合		List<T> list = jdbcTemplate.query(sql,RowMapper接口的实现类对象,参数); 会反复调用实现的方法

	4.配置c3p0连接池

		1.jar包：c3p0、mchange-commons

		2.<bean id = "dataSource" class = "com.mchange.v2.c3p0.ComboPooledDataSource">			
		      <property name = "driverClass" value = "com.mysql.jdbc.Driver"></property>
			  <property name = "jdbcUrl" value = "jdbc:mysql:///mydb"></property>
			  <property name = "user" value = "root"></property>
			  <property name = "password" value = "123"></property>
		  </bean>															//在JdbcTemplate对象中需要注入这个dataSource

=============================================================================================================================================

Spring的事务管理

	1.两种方式

		1.编程式事务管理：写代码实现

		2.声明式事务管理: 配置实现

			1.基于xml配置文件实现				
				
				1.创建Spring核心配置文件
				
				2.配置事务管理器，Spring针对不同的dao框架为PlatformTransactionManager接口提供了不同的实现类

																	//使用Spring JDBC或iBatis时使用这个实现类
					<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
						<property name="dataSource" ref="dataSource"></property>//注入dataSourc
					</bean>

				3.配置事务增强

					<tx:advice id="tAadvice" transaction-manager="transactionManager">
						<tx:attributes>//事务操作
							<tx:method name="account*" propagation="REQUIRED"/>//匹配名称以account开头的所有方法，另一个参数为隔离级别
						</tx:attributes>
					</tx:advice>

				4.配置切入点和切面

					<aop:config>//定义切入点 和 配置切面
						<aop:pointcut id="pointcut" expression="execution(* cn.itcast.service.OrdersService.*(..))"/>
						<aop:advisor advice-ref="txAdvice" pointcut-ref="pointcut"/>//注意事务使用的是advisor
					</aop:config>
			
			2.基于注解实现

				1.配置事务管理器

				2.开启事务注解： <tx:annotation-driven transaction-manager = "transactionManager"/>

				3.在使用事务的方法所在类上添加注解 @Transactional 针对本类所有方法

=============================================================================================================================================

Spring整合Web项目

	问题：之前在每次访问action时，都会加载一次spring核心配置文件，即创建一次所有对象，但这个过程只需要执行一次即可。

	原理：在服务器启动时完成加载配置文件和创建对象

		不再在action中写加载核心配置文件的代码，在服务器启动时，会为每个项目创建一个ServletContext对象，使用监听器监听到其被创建时
		就执行加载配置文件和创建对象的操作：ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		再把创建出来的对象放到ServletContext域对象中，需要使用就从该域对象中获取

	注意！这个过程Spring已经帮忙封装了，所以只需要到web.xml配置监听器和导入jar包：web 即可

		<context-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:Spring核心配置文件路径</param-value>
		</context-param>//如果不配置这一部分，默认去找/WEB-INF/applicationContext.xml
		<listener>
			<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		</listener>

=============================================================================================================================================

log4j：能够显示程序运行过程中的详细信息，比如加载过哪些配置文件，创建过哪些对象，使用log4j需要先进行配置

		1.导入log4j的jar包	2.将log4j.properties配置文件复制到src下，该文件前两部分是关于格式的配置，最后部分关于级别，debug比info更加详细

============================================================================================================================================