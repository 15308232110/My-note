Struts2概述

	1.应用在Web层，是在Struts1和WebWork的基础上发展的全新框架，版本：2.3.24，Web层另一个常见框架：SpringMVC

	2.可以解决的问题：在Web阶段需要写很多Servlet来提供不同的功能，之后通过BaseServlet解决了，Struts原理与之类似
					  在Struts2中封装的过滤器可以拦下了请求，根据不同的请求，执行同一个Action类中的不同方法

=============================================================================================================================================

搭建环境

	1.导包：到apps\struts2-blank.war\WEB-INF\lib中找jar包

	2.创建Action类和struts2核心配置文件，Servlet在web.xml中配置，而Action是在struts2核心配置文件中配置
		
	3.在Web.xml中配置过滤器

		<filter>
			<filter-name>struts2</filter-name>
			<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
		</filter>	
		<filter-mapping>
			<filter-name>struts2</filter-name>
			<url-pattern>/*</url-pattern>																						
		</filter-mapping>																								*/
		
		过滤器在服务器启动时创建，执行init方法，在该过滤器中，该方法中主要做了加载配置文件的操作，包括自己创建的和struts2自带的

=============================================================================================================================================

Struts2的基本执行过程

	通过过滤器获取请求路径 -> 使用dom4j解析struts.xml中的内容，通过请求路径找到对应的action标签

 -> 通过action标签中的class值，通过反射调用方法得到返回值 -> 再通过返回值到struts.xml中找到对应的的result标签 -> 资源跳转

=============================================================================================================================================

Struts2核心配置文件

	<?xml version="1.0" encoding="UTF-8"?>									
	<!DOCTYPE struts PUBLIC								
		"-//Apache Software Foundation//DTD Struts Configuration 2.3//EN"
		"http://struts.apache.org/dtds/struts-2.3.dtd">
	<struts>
		<package name="包名" extends="struts-default" namespace="访问路径第一部分">												
			<action name="访问路径第二部分" class="类路径" method="默认执行的方法的名称，默认值是execute">
				<result name="匹配方法的返回值">跳转资源的访问路径</result>
			</action>
		</package>
	</struts>

	1.位置在src下，名称为struts.xml，使用的是dtd约束

	2.package标签类似于代码中的包，区分不同的action，extends = "struts-default" 写了这段配置之后，类才有action功能

	3.namespace的属性值与action的name属性值构成访问路径，默认是"/"，在使用常用浏览器访问资源时.action可以省略

	4.多个package标签的name属性值不能重复，一个package标签中多个action标签的name属性值也不能重复

	5.result标签中的name属性值用来匹配对应的Action类的方法返回值，匹配失败返回404。还有一个type属性用来配置转发或重定向
	  type属性值有：1.默认值dispatcher转发 2.redirect   
					而针对Action类，还有两个值，跳转资源为Action类对应的action标签的name属性值：3.redirectAction 4.chain 转发，存在缓存问题
	  Action类中的方法返回值必须是String类型，但也可以没有返回值(void或return "none")，这时就不需要配置result标签了

	6.常量标签：package的同级标签<constant>标签
				例如：<constant name = "struts.i18n.encoding" value="UTF-8"></constant> 可以在action获取表单数据时处理post请求的数据编码问题
				配置常量还有两种方式：1.在src下创建struts.properties进行配置    2.在web.xml中通过初始化参数进行配置				        ****

=============================================================================================================================================

Action的三种编写方式

	1.创建普通类，不实现接口和继承

	2.创建类，实现Action接口，接口中有常量

	3.创建类，继承ActionSupport，该类实现了Action接口

=============================================================================================================================================

访问一个Action类中不同方法的三种方式

	1.写多个action标签访问同一个Action类，每个标签不同的name属性值对应不同method属性值

	2.在action标签的name属性值中使用通配符，在method中是用{1}获取通配符的内容，通配符可以写多个，例如class中使用{1}，method中使用{2}

	3.动态访问：配置常量<constant name = "struts.enable.DynamicMethodInvocation" value = "true"></constant>，访问时在路径后加上"!方法名"
						
=============================================================================================================================================
			
结果页面配置

	1.全局结果页面：适用于多个Action类的方法返回值相同，对应的result标签指定的跳转页面也相同的情况
					将result标签配置在action的同级<global-results>标签中，该标签只对所在package的内容生效
					该标签必须写在action标签前面，因为dom4j解析xml文件是从上到下解析的
	
	2.局部结果页面：就是普通的result标签配置，优先于全局结果页面

=============================================================================================================================================

Action获取表单数据

	1.ActionContext context = ActionContext.getContext();Map<String,Object> map = context.getParameters();//接收这个Object时需要变成Object[]
																									
	2.ServletActionContext类有静态方法：getRequest、getResponse、getServletContext、getPageContext

	3.接口注入：实现ServletRequestAware、ServletResponseAware接口，实现的方法参数中有HttpServletRequest和HttpServletResponse对象

=============================================================================================================================================

封装表单数据

	1.属性封装：在Action中定义与表单数据对应的成员变量并提供其set方法，不能封装对象

	2.模型驱动封装：表单数据与实体类属性对应，Action类实现ModelDriven<T>接口，指定T为实体类类型，创建实体类成员对象，在实现的方法中返回

	3.表达式封装：在Action类中声明实体类并提供其get/set方法，并将表单输入项的name属性值设置为：user.username

	4.注意，如果同时使用模型驱动封装和属性封装，只会执行模型驱动封装。模型驱动只能封装一个实体类，而表达式可以封装多个

	5.封装数据到List：在Action中声明List<实体类>并提供get/set方法，将表单输入项的name属性值设置为表达式形式：list[下标].实体类属性

	6.封装数据到Map：在Action中声明Map<String,实体类>并提供get/set方法，将表单输入项的name属性值设置为表达式形式：map['key值'].实体类属性

=============================================================================================================================================

OGNL概述

	1.是Struts2默认的表达式语言，比EL表达式功能更加强大。但主要功能是与Struts2标签共同使用来操作值栈数据

	2.虽然ognl经常和Struts2一起使用，但它并不是Struts2的一部分，有自己的jar包：ognl

	3.入门案例

		1.在jsp中引入ognl标签库：<%@taglib prefix="s" uri="/struts-tags"%>

		2.ognl支持对象方法的调用：<s:property value="'liusijie'.length()"/>，这个标签会将内容结果输出到页面，也可以用来获取值栈中的数据

=============================================================================================================================================

值栈

	1.值栈是Struts2本身提供的一种存储机制，类似于域对象，后进先出

	2.Servlet是单实例对象，在第一次被访问时创建，而Action是多实例对象，在每次被访问时都会被创建
											   每个Action对象都有且只有一个值栈对象，这个值栈对象又有Action的引用
	
	3.获取值栈对象的方式：ActionContext context = ActionContext.getContext();    ValueStack stack = context.getValueStack();

	4.值栈内部结构中两个主要部分

		1.root：结构是list集合，一般都是操作这个root

		2.context：结构是map集合，存储的是对象的引用，key固定有request、session、application、parameters和attr，
		           存储的是request对象引用、HttpSession对象引用、 ServletContext对象引用、传递的相关参数，attr主要做获取的操作			****

		3.<s:debug>标签：这是一个超链接，可查看值栈的内部结构和存储值，上面是root部分，下面是context部分

=============================================================================================================================================

向值栈中放数据

	1.调用值栈对象的set方法，存入的是一个map集合

	2.调用值栈对象的push方法，存入的是一个对象

	3.在Action中声明变量并提供其get方法，在方法中赋值，存入的是一个对象，存在Action的引用中，节省内存空间

=============================================================================================================================================

从值栈中获取数据

	1.<s:property value="对象.属性值"/>

	2.<s:iterator value="list集合">		//这个标签类似于<c:foreach> 这里没有使用临时变量
	      <s:property value="对象的属性值"/>
	      ···
	  </s:iterator>													
 
	3.<s:iterator value="list集合" var="key值(临时变量)">	临时变量会被放到context的map集合中，避免了root空间浪费，提高了效率
	      <s:property value="#key值(临时变量).属性值"/>		获取context中的数据需要加#符，比如获取request对象中的数据就需要加#符
		  ···												
	  </s:iterator>											

	4.向值栈存的数据的数据会被放在top数组中，push方法存入的数据没有名称只有值，需要这样获取:<s:property value="[下标].top"/>，后添加的在前

	5.使用EL表达式也可以获取值栈数据，先从域对象中获取值，找到直接返回，没找到就到值栈中去找，找到就添加到域对象中然后返回，所以性能很低

=============================================================================================================================================

Struts2拦截器

	1.Struts2封装的很多功能都在拦截器里面，有很多的拦截器，但不是每次这些拦截器都执行，执行的是默认的拦截器

	2.默认拦截器在 core jar包中的 struts-default.xml 中的 <interceptor-stack name = "defaultStack"> 标签中可以找到

	3.底层原理

		1.aop思想：拦截器在Action对象创建之后，执行方法之前自动执行，通过配置文件实现(不修改源代码增强功能)，根据需要执行对应的拦截器
		
		2.责任链模式：是设计模式的一种，与过滤链很相似，多个过滤器过滤同一个请求时，需要依次过滤
	
	4.过滤器理论上可以过滤任意内容，而拦截器只能拦截Action

=============================================================================================================================================

自定义拦截器

	1.拦截器结构：继承AbstractInterceptor类，该类实现了Interceptor接口，该接口有三个方法

		1. void init()     2.void destroy()     3.String intercept(ActionInvocation invocation)

	2.自定义拦截器时建议继承 MethodFilterInterceptor 类，这样对action里面某个的方法不进行拦截时不需要使用反射，只需要进行配置

		继承这个类必须重写一个方法：String doInterception(ActionInvocation invocation)：放行就return invocation.invoke();
									这个方法和Action的方法一样，会拿着返回值到对应的action标签的result标签中去找匹配值进行跳转

	3.通过配置文件配置建立Action与自定义拦截器的关系，默认拦截Action中的所有方法，且不再执行默认拦截器

		package标签下：

			<interceptors>
			
				<interceptor name="loginintercept" class="cn.itcast.web.interceptor.LoginInterceptor">		//声明拦截器
					<param name="excludeMethods">login</param>												//并配置不拦截的方法
				</interceptor>	
				
				<interceptor-stack name="myStack">	//自定义拦截器栈，这样就可以拦截多个Action，不配置则需要在Action中单独使用拦截器						
					<interceptor-ref name="defaultStack"></interceptor-ref>			//添加上默认拦截器，不然其不会执行
					<interceptor-ref name="loginintercept"></interceptor-ref>
				</interceptor-stack>
					
			</interceptors>
			
			<default-interceptor-ref name="myStack"></default-interceptor-ref>		//将这个拦截器栈定义为默认的拦截器栈

		单独使用声明的拦截器，可以不在声明拦截器时配置不拦截的方法，而是在使用时定义，因为每个Action中的不拦截方法可能不同

			<interceptor-ref name="loginintercept">
				<param name="excludeMethods">方法，多个方法用逗号隔开即可</param>
			</interceptor-ref>
			<interceptor-ref name="defaultStack"></interceptor-ref>

=============================================================================================================================================

Struts2的表单标签
	
	Struts2的表单标签中的内容会自动添加表格(自动换行)，如果不想这种格式就需在使用<s:form>标签中的属性：theme = "simple"

	在Struts2中的表单中使用ognl表达式，必须用%{}将表达式括起来，如：<s:textfield name="username" value="%{#request.req}"></s:textfield>
	
	<s:form>
		<s:textfield label = "这个属性值会显示在输入框前面，会自动加':'" name = "username"></s:textfield>
		<s:password></s:password>
		<s:radio list="{'女','男'}"></s:radio>//这样写，显示内容与value值相同
		<s:radio list="#{'nv':'女','nan':'男'}"></s:radio>//这样写，显示男，value值为nan
		<s:checkboxlist list="{'吃饭','睡觉','敲代码'}"></s:checkboxlist>//显示内容和value值相同
		<s:select list="{'吃饭','睡觉','敲代码'}"></s:select>//显示内容和value值相同
		<s:file></s:file>
		<s:hidden></s:hidden>
		<s:textarea rows = "" cols = ""></s:textarea>
		<s:submit></s:submit>
		<s:reset></s:reset>
	</s:form>															//个别标签没有name值会报错

=============================================================================================================================================

分模块开发

	解决多人共同开发一个项目时需要操作同一个xml文件的问题，就是引入自己负责的那部分文件：<include file="文件路径"></include>