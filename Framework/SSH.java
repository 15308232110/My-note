SVN概述

	1.通常软件开发由多人协作开发，SVN可以对代码文件、配置文件、文档等进行版本控制，解决以下问题：

		1.备份多个版本，占用磁盘空间大				2.代码冲突，引发BUG				3.项目版本发布困难

		4.无法追溯问题代码的修改人和修改时间		5.无法恢复至以前正确版本		6.无法进行权限控制
		

	2.svn体系结构：svn服务器和svn客户端，所以安装也需要安装这两部分，安装服务器时注意选择标准版
			
	3.基本操作

		1.Server：
			
			1.建仓库：Repositories -> Create New Repository

			2.建用户：User -> Create New User

			3.向仓库添加用户：目标仓库 -> Properties -> Add -> OK -> Permissions

		2.Windows：

			1.让文件夹与SVN同步：进入选中文件 -> SVN CheckOut -> URL of repository(可在Server中的选定仓库右键赋值) -> OK

			2.添加文件：选中文件 -> TortoiseSVN -> Add

			3.提交文件：选中文件 -> SVN Commit

			3.获取文件：选中文件 -> SVN Update
		

		问题：

			1.文件状态图标不显示：TortoiseSVN -> Settings -> Icon Overlays -> Shell -> 找到Server安装程序修复

			2.取消保存用户名和密码：TortoiseSVN -> Settings -> Save Date -> Authentication data -> Clear all

			3.版本冲突问题：删除多余文件 -> 将<<<<<<<.mine ~ =======之间自己修改的部分备份一份 -> 删除原文件 -> 获取最新文件再将备份添加进去

		3.MyEclipse:

			1.安装SVN插件：

				在MyEclipse的dropins目录中创建svn目录，把这两个目录文件拷贝到这个目录中去，重启MyEclipse

				检验：Window -> Show View -> Other -> SVN ->查看是否有内容

			2.MyEclipse操作：

				1.添加文件：选定文件 -> Team -> Share Project -> SVN -> Next -> 仓库路径 -> 默认

				2.提交文件：选定文件 -> Team -> 提交

				3.获取文件：Window -> Show View -> Other -> SVN -> SVN资源库 -> 选中文件 -> 在视图菜单添加文件 —>
							
							检出为 注意是否修改Context root

							完成之后如果有红色感叹号警告，说明jar包有问题，Build Path - > Configure Build Path 解决

				4.查看版本：选定文件 -> Team -> 显示资源历史记录
			
	4.SVN官方版本命令(小黑屏)

		1.检验安装是否成功：svnadmin --version

		2.创建仓库：svnadmin create 文件夹路径

		3.启动服务器：svnserve -d -r 仓库文件夹路径(光标会一直闪)，官方版本有服务，并且需要手动启动服务器

		4.官方版本的svn路径与压缩版本不同，为：svn://ip地址:3690

============================================================================================================================================

struts文件上传：

	表单要求：1.post请求	2.enctype要设置    3.有type为file的inpt标签
	
	1.配置常量

		<constant name="struts.multipart.maxSize" value="配置最大上传大小，单位K"></constant>	//不配置就默认为2M

	2.接收上传文件
	
		提供两个成员和set/get方法：File 和 String， File的属性名与表单name值相同，而String的属性名是在前者之后加上"FileName"

		最后coye到本地路径，FileUtils.copyFile(上传文件,new File("本地路径"));//这是apache提供的工具类
	
	3.如果超过限制大小，默认会返回"input"，需要在struts.xml中配置对应的跳转资源，使用Struts2标签<s:actionerror/>可以显示错误信息        ****

上面超过限制大小时，出现问题，我的解决方式：
																
	1.配置常量为-1：<constant name="struts.multipart.maxSize" value="-1"></constant>

	2.对文件大小进行判断，超过大小就返回一个字符串，对应一个result标签

============================================================================================================================================

让与本地线程绑定的 session 延迟关闭

	<filter>
  		<filter-name>openSessionInViewFilter</filter-name>
  		<filter-class>org.springframework.orm.hibernate5.support.OpenSessionInViewFilter</filter-class>
  	</filter>
  	
  	<filter-mapping>
  		<filter-name>openSessionInViewFilter</filter-name>
  		<url-pattern>/*</url-pattern>
  	</filter-mapping>
  	<filter>	*/
	
	在jsp显示 linkman.customer.custName 时，会查询两次数据库，第二次查询时与本地线程绑定的 session 就已经关闭了，
	所以需要配置这个拦截器使这个session在action操作结束之后才关闭。这段配置写在struts过滤器之前才会有效果

============================================================================================================================================

BaseDao的抽取：对简单的增删改查操作进行封装，在service和dao层就无需写这些方法了，简化代码！

		public interface BaseDao<T> {
			public void add(T t);
			public void update(T t);
			public void delete(T t);
			public T findById(int id);
			public List<T> findAll();
		}

		public class BaseDaoImp<T> extends HibernateDaoSupport implements BaseDao<T> {
			
			private Class clazz;
			
			public BaseDaoImp(){
				Class claxx=this.getClass();
				ParameterizedType parameterizedType=(ParameterizedType)claxx.getGenericSuperclass();	//BaseDaoImp<Customer,···>
				Type[] types=parameterizedType.getActualTypeArguments();								//Customer···
				clazz=(Class)types[0];																	//Customer
			}
			
			public void add(T t){
				getHibernateTemplate().save(t);
			}

			public void update(T t) {
				getHibernateTemplate().update(t);
			}

			public void delete(T t) {
				getHibernateTemplate().delete(t);
			}

			public T findById(int id) {
				return (T) getHibernateTemplate().get(clazz, id);
			}

			public List<T> findAll() {
				return (List<T>)getHibernateTemplate().find("from "+clazz.getSimpleName());
			}
		}

		public interface CustomerDao extends BaseDao<Customer>{···}								//接口继承接口
			
		public class CustomerDaoImp extends BaseDaoImp<Customer> implements CustomerDao {···}	//实现类继承实现类，同时实现对应接口

============================================================================================================================================

多条件组合查询的两种方式：

	1.使用hibernate模板里面find方法实现

			String hql = "from Customer where 1=1 ";

			List<Object> list = new ArrayList<Object>();

			if(customer.getCustName()!=null && !"".equals(customer.getCustName().trim())) {
				hql += " and custName=?";
				list.add(customer.getCustName());
			}
			············

			return (List<Customer>) this.getHibernateTemplate().find(hql, list.toArray());


	2.使用离线对象

			DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);

			if(customer.getCustName()!=null && !"".equals(customer.getCustName())) {
				criteria.add(Restrictions.eq("custName", customer.getCustName()));
			}
			············
			
			return (List<Customer>) this.getHibernateTemplate().findByCriteria(criteria);

============================================================================================================================================

修改删除联系人问题

	hibernate是双向维护外键，所以一方非id的属性时，多方所属一方会变为NULL，所以一般需要在一方的set标签中添加inverser="true"，放弃外键维护
				  但这时又有一个问题，在删除客户时，就不再会先将联系人的所属客户设置为NULL，所以抛出异常，
				  因此还需要配置与cascade="delete"，这样就既能随意修改客户非id的属性时，也能级联删除									****

============================================================================================================================================

多对多配置：使用hibernate直接配置多对多，缺陷是第三张表只有两个字段，所以采用两个一对多的方式来实现

============================================================================================================================================

多表指定查询：对于复杂的查询，建议使用底层sql语句来查询，所以需要使用SQLQuery

		Session session = this.getSessionFactory().getCurrentSession();
		
		SQLQuery sqlQuery = session.createSQLQuery("SELECT COUNT(*) AS num,custSource FROM t_customer GROUP BY custSource");
		
		sqlQuery.setResultTransformer(Transformers.aliasToBean(HashMap.class));//创建对应的实体类太麻烦，两个字段可以像这样采用结构转换

		List list = sqlQuery.list();

		注意！这个例子中，在jsp页面获取数据时，需要写num和custSource！

============================================================================================================================================


SSH三大框架整合

	1.struts2和spring整合：action对象的创建交给spring来管理，jar包：struts2-spring-plugin 
						   注意！action标签中的class属性值写spring配置的bean的id值，否则action会被创建两次！action是多实例的(scope)！

	2.spring和hibernate整合：数据库交给spring配置，用spring来创建SessionFactory对象。spring框架对hibernate框架进行了封装，jar包：spring-orm

		<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
			<property name="dataSource" ref="dataSource"></property>//因为数据库由spring来配置，所以需要注入dataSource
			<property name="configLocations" value="classpath:hibernate.cfg.xml"></property>//指定hibernate核心配置文件
		</bean>								
																  //使用Hibernate时使用这个实现类
		<bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">//配置事务管理器
			<property name="sessionFactory" ref="sessionFactory"></property>//这里需要注入sessionFactory，它也包含dataSource
		</bean>

		<bean id="hibernateTemplate" class="org.springframework.orm.hibernate5.HibernateTemplate">//创建hibernateTemplate对象
				<property name="sessionFactory" ref="sessionFactory"></property>//这个类有参构造需要sessionfactory，也提供了set方法和成员
			</bean>
		</beans>

		HibernateTemplate的常用方法

			 Serializable save(Object entity) ：					添加
			 void update(Object entity) ：							修改
			 void delete(Object entity) ：							删除
			 <T> T get(Class<T> entityClass, Serializable id) ：	根据id查询
			 <T> T load(Class<T> entityClass, Serializable id)：	根据id查询
			 List<T> find(String queryString, Object... values) ：	查询，第一个参数是hql语句，所以该方法不能实现分页



		也可以不写hibernate核心配置文件，把hibernate核心配置文件中的内容放在Spring核心配置文件中

			<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
				<property name="dataSource" ref="dataSource"></property>
				<property name="hibernateProperties">
					<props>
						<prop key="hibernate.show_sql">true</prop>
						<prop key="hibernate.format_sql">true</prop>
						<prop key="hibernate.hbm2ddl.auto">update</prop>
						<prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
						···
					</props>
				</property>
				<property name="mappingResources">
					<list>
						<value>cn/itcast/entity/User.hbm.xml</value>
						···
					</list>
				</property>
			</bean>

	4.在Dao中注入HibernateTemplate的简化做法：

		Dao层的类继承HibernateDaoSupport，这个类中已经注入了HibernateTemplate，所以在Spring核心配置文件中无需创建HibernateTemplate对象了
		在Spring核心配置文件中的Dao对象也就无需注入HibernateTemplate了，而需要注入SessionFactory对象
		注意！这个类中已经绑定了本地Session，不能再在Hibernate核心配置文件中绑定本地线程！
		getHibernateTemplate()可以得到HibernateTemplate对象，但是注意！只能在方法中才能得到！										    ****

=============================================================================================================================================

Spring中分模块开发，引入文件需要使用：<import resource="classpath:文件路径"> classpath:可省略

============================================================================================================================================

配置文件没有提示问题的解决方法

	1.检查是否有网络

	2.引入本地约束文件：Window -> preferences -> Files and Editors -> XML -> XML File -> XML Catalog -> Add -> Catalog Entry -> 重启

		1.dtd约束：	     Key type设置为URI，Key为网络路径，Location为文件路径
		2.schema约束：	 Location为文件路径，Key type设置为Schema location，Key为网络路径

============================================================================================================================================