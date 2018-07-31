常见软件系统体系结构有BS和CS

	B/S(Browser/Server)浏览器/服务器:优点是只需要编写服务器端程序，但安全性差
	C/S(Client/Server)客户端/服务器，优点是安全性高，但还需编写客户端程序和安装客户端，并且服务器更新时客户端也需要更新

============================================================================================================================================

Web资源

	动态资源:有变量，需要转换成静态资源后再响应给客户端，Java涉及JSP、Servlet这两种动态资源，其他还有ASP、PHP等
	静态资源:浏览器能看懂并直接响应，例如html、css、javascript
	访问Web资源，协议名://主机名:端口号/路径


JavaWeb应用程序的标准目录结构

	/WEB-INF(该目录下的内容无法直接访问到)
		/classes(可无)
		/WEB-INF/lib
		/WEB-INF/web.xml
	/index.jsp(可无)

============================================================================================================================================

Web服务器

	接收请求时可以把动态资源转换成静态资源再响应，对于JavaWeb程序需要Web服务器才可以运行

	Tomcat(Apache):不支持JavaEE，支持Servlet规则(JSP/Servlet容器)，当前应用最广的JavaWeb服务器
	GlassFish(Orcale):不支持JavaEE，，支持Servlet规则，JavaWeb服务器，应用不广
	JBoss(Redhat红帽):支持JavaEE(EJB容器，企业级的JavaBean)，应用较广
	Resin(Caucho):支持JavaEE，越来越广
	Weblogic(Orcale):支持JavaEE，适合大型项目，收费
	Websphere(IBM):支持JavaEE，适合大型项目，收费


Tomcat	

	由Apache提供的开源免费的Web服务器，Tomcat7支持Servlet3.0，而Tomcat6只支持Servlet2.5
	下载地址为http://tomcat.apache.org，安装版一台电脑上只能安装一个，而解压版无需安装，解压即可用，解压多少份都可以
	默认端口为8080，可以在conf目录下的server.xml文件中找到<Connector prot"8080"······/>修改端口号
	启动:bin目录下双击startup.bat，启动需要JDK，若启动即关闭，可能JAVA_HOME环境变量有问题或重复启动Tomcat
	关闭:bin目录下双击shutdown.bat，这种关闭方式较正确


Tomcat目录结构

	bin:存放二进制可执行文件，包含启动和关闭文件

	conf:存放配置文件，该目录下有四个重要文件(目前只了解到三个)

		server.xml:配置整个服务器信息

		web.xml:部署描述符文件，这个文件中注册了很多MIME(文档类型)，用来说明文档的内容是什么类型
				该文件的内容写在了所有项目的web.xml中
				其中有一个名为default的<servlet>元素，它的优先级最低，没人处理的请求最后就由它处理，它显示404，说明资源不存在
				其中有一个名为jsp的<servlet>元素匹配所有的jsp文件，它就负责将动态资源转换成静态资源，所以JSP不用在web.xml中配置
				其中<session-timeout>30</session-timeout>这表示session的过期时间为30分钟

		context.xml:对所有应用的统一配置。在<Context>中配置属性reloadable="true"，可以改啥都不用重启Tomcat，只适用于开发阶段

	lib:存放jar包
	logs:存放日志文件
	temp:存放临时文件，这些文件在停止Tomcat就自动删除
	webapps:存放项目，其中ROOT是一个特殊的项目，在地址栏中没有给出项目目录时，默认的就是ROOT项目
	work:存放由Tomcat生成的文件


理解server.xml

	<Server>根元素，整个服务器的配置信息
	  <Servier>整个服务
	    <Connector>连接，n
	    <Engine>引擎，Service组件的核心
	      <Host>虚拟主机，n，和主机相同，也有主机名和项目目录
		    <Context>项目，n
	用户来到大酒店(Server)的服务部门(Servier)，酒店发现是http/1.1协议，而且还是8080端口，所以就派处理对应请求的服务员(Connector)接待
	服务员把菜单(请求)交给后厨(Engine)，因为要一盘水煮鱼，所以由川菜区(Host)负责，大老王师傅(Context)做水煮鱼最地道，所以由它完成


配置外部应用(项目不在主机项目目录中)的两种方式

	1.在server.xml中的<Host>元素中添加<Context path="新的项目名" docBase="项目路径">
	2.在conf/Catalina/localhost目录下创建 项目名.xml文件 内容为<Context docBase="项目路径">


映射虚拟主机

	1.修改端口号为80
	2.在C:\WINDOWS\system32\drivers\etc\hosts中绑定关系127.0.0.1	新域名
	3.在server.xml中复制新建一个Host，修改：name="绑定的新域名" appBase="主机项目目录路径"
	4.在主机项目目录下创建Root项目，这时访问绑定的新域名就是访问ROOT项目的主页

============================================================================================================================================

HTTP协议

	HTTP(hypertext transport protocol)超文本传输协议，即通信格式规则，规定了客户端(浏览器)的请求协议和服务器响应协议
	
	1.请求协议

		请求首行(请求方式 请求路径 协议/版本)
		请求头信息(有些头可以对应多个值，可以多行显示也可以在一行显示)
		空行
		请求正文(请求体)(GET请求没有请求体)

	2.响应协议

		响应首行(协议/版本 状态码 状态码的解析)
		响应头信息
		空行
		响应正文(响应体)	


请求头

		1.Host:主机名
		2.User-Agent:浏览器和OS相关的信息
		3.Accept:可以接收的资源类型
		4.Accept-Language:支持的语言
		5.Accept-Encoding:压缩格式
		6.Accept-Charset:编码
		7.Content-Length:请求正文长度
		8.Connection:keep-alive：链接方式，头值为保持一段链接时间，不再是连接即断开，默认为3000ms，作用最能体现在访问框架页页面时。
		9.Content-Type:application/x-www-form-urlencoded:表示表单中的数据会自动使用url来编码 
		10.Referer:访问来源，可以统计访问量和防盗链
		11.Cookie:键值对形式


响应头

		1.Server:版本信息
		2.Content-Type:响应的资源类型，文本类型需要指出编码格式
		3.Content-Length:响应正文字节数
		4.Set-Cookie:响应给客户端的Cookie，多行显示
		5.Date:响应时间，这可能会有8小时的时区差
		6.Refresh:定时跳转
		7.Cache-Control:no-cache:不缓存，HTTP1.1版本
		8.Pragma:no-cache:不缓存，HTTP1.0版本
		9.Expires:-1:过期时间


响应码

	1. 200	请求成功
	2. 404	请求资源不存在
	3. 500	请求资源找到了，但服务器内部出现了错误
	4. 302	重定向，服务器要求浏览器重发请求，服务器会发送一个Location响应头指定新的请求地址
	5. 304	请求资源没有改变，第一次请求html页面时，服务器响应200并缓存页面，提供Last-Modified响应头保存页面的最后修改时间
		    第二次请求时，浏览器带着If-Modified-Since请求头将这个时间还给服务器，服务器将页面当前的最后修改时间与之比较
		    如果相同，响应304且不会响应正文内容，只显示缓存内容，注意！动态资源一般不让浏览器做缓存处理

============================================================================================================================================

Servlet

	服务器端的响应程序，可以处理客户端请求的动态资源，JavaWeb三大组件之一(Servlet、Listener和Filter)

	Servlet可以获取请求数据、处理请求和响应，每个Servlet处理请求的方式是不同的

	Servlet类是单例的，线程不安全效率高，为了Servlet相对更安全，就不要在Servlet中创建成员->可以创建无状态成员->可以创建有状态只读的成员

	Servlet在第一次被访问或服务器启动时通过反射机制创建，所以Servlet必须有无参构造器，并且Servlet的方法由服务器来调用
		在servlet标签中配置<load-on-startup>，内容为非负整数即可，这可以使Servlet在服务器在启动时就被创建，数越小就越早被创建

============================================================================================================================================

定义Servlet

	1)继承HttpServlet(也可以实现Servlet或继承GenericServlet)

	2)在web.xml中部署Servlet，访问时会通过访问路径找到servlet-name，再找到类路径

		  <servlet>
			<servlet-name>XXX</servlet-name>
			<servlet-class>类路径</servlet-class>
		  </servlet>
		  <servlet-mapping>
			<servlet-name>与上对应</servlet-name>
			<url-pattern>Servlet路径</url-pattern>
		  </servlet-mapping>
	
	3)<url-pattern>可以有多个，且支持通配符"*"，"*"只能出现一次且只能出现在路径两端，如果不使用通配符就必须以"/"开头
	  有路径匹配("*"在后)和扩展名匹配("*"在前)，在访问资源时，匹配的越少的优先级越高


Servlet接口中的5个方法(生命周期方法)

	void init(ServletConfig)						创建Servlet对象之后马上被调用，只执行一次
	void service(ServletRequest,ServletResponse)  每次处理请求时都会被调用
	void destory()									Servlet被销毁之前，也就是关闭服务器时被调用，只执行一次
	ServletConfig getServletConfig()				可以获取Servlet的配置信息
	String getServletInfo()							可以获取Servlet的信息


GenericServlet抽象类

	1.该类实现了Servlet和ServletConfig接口，只有抽象方法service()

	2.该类将init(ServletConfig)方法中的参数赋给一个成员，然后调用了一个init()无参方法，这时初始化Servlet就应该覆盖这个方法
	  

HttpServlet抽象类(GenericServlet子类)

	1.该类没有任何抽象方法，实现了void service(ServletRequest,ServletResponse)方法：
		把ServletRequest强转成HttpServletRequest
		把ServletResponse强转成HttpServletResponse
		调用本类添加的void service(HttpServletRequest,HttpServletResponse)方法

	2.添加了void service(HttpServletRequest,HttpServletResponse)方法:
		调用HttpServletRequest对象的getMethod()方法获取请求方式
		如果请求方式为GET，调用本类添加的doGet(HttpServletRequest,HttpServletResponse)方法
		如果请求方式为POST，调用本类添加的doPost(HttpServletRequest,HttpServletResponse)方法
	  
	3.doGet和doPost方法，内容都是响应405，表示错误，所以继承该类需要重写这两个方法

============================================================================================================================================

ServletConfig接口

	1.ServletConfig对象封装了在web.xml中对应的servlet标签中的配置信息，其在服务器调用Servlet的void init(ServletConfig)方法时会传递给Servlet

	2.ServletConfig有4个方法

		String getServletName()					获取<servlet-name>的值
		ServletContext getServletContext()		获取ServletContext对象
		String getInitParameter(String name)	通过名称获取指定初始化参数的值
		Enumeration getInitParameterNames()		获取所有初始化参数的名称

	3.配置servlet>初始化参数，可配多个			<servlet>
													<init-param>
														<param-name>p1</param-name>
														<param-value>v1</param-value>
													</init-param>
													······
												</servlet>	

============================================================================================================================================

ServletContext

	1.一个项目只有一个ServletContext对象，所以可共享数据，在Tomcat启动时创建，关闭时才被销毁，很多类都有getServletContext()方法可获取该对象

	2.ServletContext可以存取数据、读取web.xml中的应用初始化参数以及应用资源的相关信息

	3.域对象的方法

		ServletContext是JavaWeb四大域对象(ServletContext、PageContext、ServletRequest和HttpSession)之一，域对象内部都有一个Map来存储数据

			1.void setAttribute(String name, Object value)
			2.Object getAttribute(String name)
			3.void removeAttribute(String name)
			4.Enumeration getAttributeNames()			      获取所有域属性的名称

	4.一个Servlet只能获取自己的初始化参数，但ServletContext可以获取应用的初始化参数，应用的初始化参数也可以有多个

		<web-app>
			<context-param>
				<param-name>p1</param-name>
				<param-value>v1</param-value>  	
			</context-param>
			······
		</web-app>

		String getInitParameter(String name)		通过名称获取指定应用初始化参数的值
		Enumeration getInitParameterNames()			获取所有应用初始化参数的名称
	
	5.读取应用资源的相关信息

		String getRealPath(String path)					获取资源的带盘符的路径
		InputStream getResourceAsStream(String path)	获取资源的输入流
		Set<String> getResourcePaths(String path)		获取指定目录下一层的所有资源路径

	6.获取类路径资源

		1.类路径对JavaWeb项目而言，就是/WEB-INF/classes中的资源和/WEB-INF/lib下每个jar包

		2.src下的.java文件会出现在/WEB-INF/classes中变成.class文件，其他类型文件会原封不动地copy进来。项目中只有WebRoot下的文件会发布给用户

		  1.ClassLoader相对的是classes路径，得到该类需要先得到Class(ClassLoader获取资源时不能以"/"开头)
		  2.Class类以"/"开头相对classes路径，否则就相对当前所在路径
		 
		3.commons-io.jar包中的IOUtils类有一个静态方法 String toString(InputStream input)，可以以字符串形式返回流中内容

	7.案例:统计访问量

============================================================================================================================================

服务器处理请求流程：服务器每次收到请求时，都会开辟一个新的线程，服务器会创建request对象并把请求数据封装到其中，其就是请求数据的载体
															    服务器还会创建response对象，它与客户端连接在一起，用来完成响应


response:其类型为HttpServletResponse(与HTTP协议相关的类型，ServletResponse是与协议无关的类型)

	1.状态码	response.sendError(int sc)				发送错误状态码
　　　　		response.sendError(int sc, String msg)	发送错误状态码，还可以带一个错误信息
　　　　		response.setStatus(int sc)				发送成功状态码

	2.响应头	setHeader(String name,String value)		适用于单值的响应头
	　　　　	addHeader(String name,String value)		可多次调用来添加头值，适用于多值的响应头
				······									常见的是String类型的值，还有其他类型的值的对应方法

	3.响应体，两个流，不能同时使用		PrintWriter pw = response.getWriter();					用来向客户端发送字符数据
　　　　　　							ServletOutputStream out = response.getOutputStream;		用来向客户端发送字节数据
										通常使用commons-io.jar包中的IOUtils类的静态方法toByteArray()先将图片内容封装为数组再响应到页面
　　　　　	

request

	1.方法

		request.getRemoteAddr()							获取客户端IP
　　　　request.getMethod()								获取请求方式
　　	String getHeader(String name)					获取请求头，适用于单值请求头
　　　　int getIntHeader(String name)					适用于单值int类型的请求头
　　　　long getDateHeader(String name)					适用于单值毫秒类型的请求头
　　　　Enumeration<String> getHeaders(String name)		适用于多值请求头
　　　　String getScheme()								获取协议
　　　　String getServerName()							获取服务器名
　　　　String getServerPort()							获取服务器端口
　　　　String getContextPath()							获取项目名
　　　　String getServletPath()							获取Servlet路径
　　　　String getQueryString()							获取请求参数
　　　　String getRequestURI()							获取请求URI，等于项目名加Servlet路径
　　　　String getRequestURL()							获取请求URL，等于不包含参数的整个请求路径
		String getParameter(String name)				获取指定名称的请求参数值，适用于单值请求参数
　　　　String[] getParameterValues(String name)		适用于多值请求参数
　　　　Enumeration<String> getParameterNames()			获取所有请求参数名称
　　　　Map<String,String[]> getParameterMap()			获取所有请求参数，参数值为数组


　　2.请求转发和请求包含：当一个请求需要两个或多个Servlet协作才能完成，就需要使用转发和包含

	　　RequestDispatche getRequestDispatcher(Servlet路径)		通过路径获取转发器对象
	　　-> forward(request,response)							转发：所有Servlet都可以设置响应头，但只能由最后一个Servlet完成响应体
																	  否则无法显示或抛异常(前面的Servlet响应数据过大时)
	　　-> include(request,response)							包含：所有Servlet都可以设置响应头和响应体


	3.请求转发和重定向的区别

		请求转发是一个请求一次响应(可以共享request数据、效率高)，而重定向是两次请求两次响应(地址栏发生变化)
　　　　	请求转发只能转发到本项目其他Servlet，而重定向还能定向到其他项目
　　　　	路径以"/"开头时，请求转发(和请求包含)相对的是项目路径，而重定向相对的是当前主机路径(表单和超链接也是如此)，需要手动添加项目名
　　
	
	4.request域

　　　　Servlet中三大域对象:request、session、application(ServletContext)，都有三个方法:

	　　　　void setAttribute(String name, Object value)
	　　　　Object getAttribute(String name)
			void removeAttribute(String name)

============================================================================================================================================

编码

	1. 响应编码

　		服务器的默认编码是ISO-8859-1，浏览器默认编码为GBK
		可以设置响应编码方式：setCharaceterEncoding();
　　	设置响应头ContentType可以告诉客户端服务器的编码方式，并且会自动将该编码设为响应编码，便捷方式：setContentType(String string)
　　	
	2. 请求编码

		发送请求时，如果在地址栏中直接给出参数，则默认是GBK编码，但如果在页面中提交表单或点击超链接，请求编码和当前页面编码相同

　　	GET请求和POST请求的编码处理方式是不同的

　　		1.GET：在server.xml文件中的端口号为8080的Connector标签中添加URIEncoding属性可以设置默认编码，但是不允许使用该方法
																										 一般是采用反编来处理
			2.POST：通过在获取参数之前需要调用setCharacterEncoding()方法来指定请求编码的方式来处理数据 


URL编码

	1.URL是用来在客户端与服务器之间传递参数用的一种方式，但它不是一种字符编码方式，但它是在字符编码之上进行另一次转换
	  一般请求(GET)中没有URL编码，可能丢失数据，而表单(包括GET请求方式的表单)会自动使用URL编码，服务器也就会识别之并使用它来解码
	2.URL在客户端与服务端传递非英文的数据时可以将其转换成网络适合的方式，方便传输
	3.URL编码需要先使用指定的字符编码将字符串解码后，会先得到byte[]，然后把小于0的字节+256，再转换成16进制，再在前面添加一个"%"，最终生成
　　4.URL编码:URLEncoder.encode(String string,"字符编码")
　　5.URL解码:URLDecoder.decode(String string,"字符编码")
	6.在超链接中的非英文参数就需要使用URL进行编码和解码操作

============================================================================================================================================

JSP的作用

	Servlet是动态资源，可以编程，但不适合设置html响应体，需要大量的response.getWriter().print()来输出html标签
	HTML则不用为输出html标签而发愁，但它是静态页面，不能包含动态信息
	JSP(java server pages)就集合了前两者的优点，在原有html的基础上添加了java脚本，构成JSP页面
	它可以作为请求发起页面和请求结束页面，例如显示表单、超链接以及显示数据，而Servlet常常作为请求中处理数据的环节


JSP的组成

	JSP = HTML + JAVA脚本 + JSP标签(指令)
	JSP中有9大内置对象，它们无需被创建即可使用

	JSP有3种脚本，脚本中可以写JAVA注释
		
		<%...%>:JAVA代码片段，JAVA方法内能放什么，它就可以放什么！但是它并不是一个方法！
		<%=...%>:JAVA表达式，用于输出，response.getWriter().print()里面能放什么，它就可以放什么！
		<%!...%>:JAVA声明，JAVA类体中能放什么，它就可以放什么！但它已经过时了！尽量不要使用！


JSP原理：JSP其实是一种特殊的Servlet，它也实现了Servlet接口，当JSP页面第一次被访问时，服务器会把.jsp编译成.java文件，再编译成.class 
		 然后创建该类对象， 最后调用它的service()方法完成响应，而第二次被访问时，就是直接调用service()方法

		 在tomcat的work目录下可以找到JSP对应的.java源代码和.class字节码文件，查看其对应的.java文件可以发现
		 <%···%>脚本中的内容会原样输出，HTML代码会写在out.write()中，<%=···%>脚本中的内容会写到out.print()中
		 并且在类中有一个_jspservice方法声明了一些内置对象，然后在紧跟的try语句块中对其赋值和执行JSP页面中的内容


JSP注释：JSP注释：<%--...--%>，当服务器把.jsp编译成.java文件时已经忽略了这部分注释
		 HTML注释：<!--...-->，HTML注释不会被忽略，因为它属于页面的一部分内容，只是页面中不显示而已
		 注意！<!--<%=a%>-->这部分内容在页面源码中能看见，因为它本质就是HTML注释！
   
============================================================================================================================================

Http协议与Cookie

	Cookie是HTTP协议制定的！是由服务器保存到浏览器的一个键值对，键值对可以有多个，多行显示！Set-Cookie:xxx=xxx
	在浏览器下一次请求服务器时会将上次得到的Cookie归还给服务器，一行显示！Cookies:xxx=xxx;xxx=xxx
	
	Http协议为保证不给浏览器太大压力而规定:		1.一个Cookie最大4KB
												2.一个服务器最多向1个浏览器保存20个Cookie
												3.一个浏览器最多可以保存300个Cookie
												4.Cookie是不能跨浏览器的
												5.Cookie中不能存在中文

	Cookie的用途是可以使服务器能够跟踪客户端的状态：例如保存购物和、保存上次登录名


JavaWeb中使用Cookie：原始方式：response发送Set-Cookie响应头，request获取Cookie请求头
					 便捷方式：repsonse.addCookie()，request.getCookies()


Cookie的属性

	maxAge:Cookie可保存的最大时长，可通过setMaxAge(int num)方法设置秒数：

		maxAge>0:浏览器会把Cookie保存到客户机硬盘上，有效时长为maxAge的值决定。
		maxAge<0:Cookie只在浏览器内存中存在(默认)，一旦关闭浏览器，Cookie就死亡。
		maxAge=0:直接删除这个Cookie！

	Cookie的path: Cookie的路径在服务器创建Cookie时被设置，并不是指Cookie在客户端的保存路径！而是指当前访问页面路径的父路径(默认)！
				  Cookie的路径决定Cookie是否需要被归还。访问资源的路径包含哪些Cookie的路径，就归还哪些Cookie。JSESSIONID的路径是项目路径

============================================================================================================================================

HttpSession：是由JavaWeb提供，用来跟踪会话，是服务器端对象，保存在服务器端！底层依赖Cookie或URL重写


作用：共享数据

	会话范围:某个用户开启浏览器后首次访问服务器到关闭浏览器
	服务器会为每个客户端创建一个session对象，它就好比客户在服务器端的账户，它们被服务器保存到一个Map中，这个Map被称之为session缓存！
    得到session对象:request.getSession();但是session是JSP九大内置对象之一，所以在JSP中session不用创建就可以直接使用。


案例:用户登录

    login.jsp:		登录页面，能够显示错误信息，并可以从Cookie中获取登录名称并显示到登录名输入项中
    LoginServlet:	先编码设置，再校验登录信息，错误即保存信息到request并转发回登录页面，反之则添加Cookies并保存信息到session再重定向到主页
    index.jsp:		校验是否登录，如果登录，显示用户信息，反之则保存错误信息到request并转发到登录页面

  
HttpSession原理

	只有在第一次获取session时，即调用request.getSession()方法，才会创建session！在这个方法中会去获取Cookie中的JSESSIONID，
	如果sessionId不存在或存在或没有查找到session对象，就创建新的session并将其sessionId保存到Cookie中
	如果sessionId存在并且查找到了session对象，就不会再创建session对象了									  
	最后返回session


	如果创建新的session，浏览器会得到一个包含sessionId的Cookie，只在浏览器内存中存在。下次再执行request.getSession()方法时，
	可以通过Cookie中的sessionId找到该session对象。

	JSP会自动去使用session，因为其为JSP的内置对象，在访问页面时就将其赋值了，但Servlet必须调用request.getSession()才能使用session。

	request.getSession(true)、request.getSession()，这两个方法效果相同，
	但request.getSession(false)方法只有通过sessionId找到session对象时会直接返回对象，其它情况并不会创建session对象，而是返回null


HttpSession其他方法

	String getId()					获取sessionId，每个session都有个16进制32位长的字符串作为ID，UUID类就可以生成这样一个随机不重复的字符串
	int getMaxInactiveInterval()	获取session最大不活动时间(秒)，默认为30分钟。当session在30分钟内没有使用，会被Tomcat从session池中移除
									最大不活动时间可以在web.xml中配置:
																		<web-app>
																			 <session-config>
																				<session-timeout>30</session-timeout>
																			</session-config>
																		</web-app>
	void invalidate()				使session失效
	boolean isNew()					判断session是否为新


URL重写

    其实如果Cookie不存在(比如客户端禁用了Cookie)，服务器就会到请求参数中去找sessionId，进而查找session对象
	这时就可以使用URL重写来替代Cookie：在所有超链接、表单中都添加一个请求参数：";JSESSIONID=<%=session.getId()%>"
	response.encodeURL(String url)方法会对URL智能重写:当请求没有归还cookie时，该方法默认添加JSESSIONID参数，否则不添加！


Session序列化

	在Session中保存数据后，数据可以再从Sessoion中取出来。但是在重启服务器之后还可以获取数据，这就是因为在关闭服务器后，
	在Tomcat的work目录下的该项目目录中，会临时生成一个SESSIONS.ser序列化文件，保存了session中可序列化数据，在下一次启动服务器时，
	该文件又重新被服务器从硬盘读回到内存。在conf目录下的context.xml文件中配置<Manager pathname=""/>可以避免这种情况发生。


Session的钝化和活化

	当Session个数猛增会导致服务器内存不足，服务器可将长时间未使用的Session保存到硬盘中，一个session一个文件，文件名为sessionId.session
	当用户再次使用Session时又重新读取文件到内存中，但该文件并不会从硬盘上丢失。这个过程就是Session的钝化和活化
	这是在不关闭服务器的情况下完成的。针对的也是可序列化数据，如果数据不能被序列化，就无法被活化
	需要配置：<Manager className="org.apache.catalina.session.PersistentManager" maxIdleSwap="1">
				  <Store className="org.apache.catalina.session.FileStore" directory="文件目录"/>	
			  </Manager>//配置Session的最大不活动时间和保存目录，时间单位为min

============================================================================================================================================

JSP三大指令

	一个jsp页面中，可以有0~N个指令的定义！

	1. @page：页面指令<%@page 属性1="属性值" 属性2="属性值"...%>

		pageEncoding：指定当前JSP页面的编码，在服务器要把.jsp编译成.java时需要使用pageEncoding！
		contentType：类似于添加一个Content-Type响应头！
		这两个属性只提供一个，那么另一个的默认值为设置那一个。但如果两个属性都没有被提供，那么默认为ISO-8859-1 

		import：导包！可以出现多次，也可以将多次合并为一次，中间用","隔开

		errorPage：指定出错后的转发页面，当前页面如果抛出异常，然后转发，响应200
		isErrorPage：指定当前页面是否为处理错误的页面！如果该属性值为true时，转发到该页面会响应500并且该页面可以使用内置对象exception!
		
			也可以在web.xml中配置错误信息：

				<error-page>
					<error-code>404</error-code>
					<location>/errorPage1.jsp</location>
				</error-page>
				<error-page>
					<error-code>500</error-code>
					<location>/errorPage1.jsp</location>
				</error-page>
				<error-page>
					<exception-type>java.lang.RuntimeException</exception-type>      这种方式范围大，优先级低
					<location>/errorPage2.jsp</location>
				</error-page>

		autoFlush：指定jsp的输出流缓冲区移除时，是否自动刷新！默认为true，如果为false，那么在缓冲区满时抛出异常！
		buffer：指定缓冲区大小，默认为KB

		isELIgnored：是否忽略EL表达式，默认值为false，不忽略，即支持！
		language：指定当前JSP编译后的语言类型，默认值为JAVA
		isThreadSafe：当前的JSP是否支持并发访问！默认为false，不要设置为true
		session：当前页面是否支持session，如果为false，那么当前页面就没有session这个内置对象！
		extends：让JSP生成的Servlet去继承该属性指定的类
		info：信息


	2.@include 静态包含<%@include file="xxxx.jsp"%>

		它是在.jsp编译成.java文件时合并生成一个.java(就是Servlet)文件(注意！避免代码重复)，然后再生成一个.class！所以不能跟变量(还没运行)！
		RequestDispatcher的include()是一个方法，包含和被包含的是两个Servlet，即两个.class！他们只是在运行时把响应的内容在合并了！
		作用：分解页面再组合，这样一个页面中不变的部分，就是一个独立JSP，而我们只需要处理变化的页面，实现代码重用


	3.@taglib 导入标签库<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		prefix：定义标签库在本页面中使用的前缀！例如：<c:url value="/AServlet"/>			uri: 指定标签库的位置！

============================================================================================================================================

九个内置对象(无需创建即可使用)

	out(JspWriter)：					JSP输出流，等同与response.getWriter()，用来向客户端发送文本数据
	config(ServletConfig)：				对应ServletConfig
	page(当前JSP类型)：					当前JSP页面对象，引用类型是Object类型：Object page=this
	pageContext(PageContext)：			页面上下文对象，一个顶九个
	exception(Throwable)：				只有在错误页面中可以使用该对象
	request(HttpServletRequest)：		即HttpServletRequest类的对象
	response(HttpServletResponse)：		即HttpServletResponse类的对象
	application(ServletContext)：		即ServletContext对象，整个应用程序就这一个对象
	session(HttpSession)：				即HttpSession对象，只有在设置了<%@page session="false"%>的JSP页面不能使用该对象

	pageContext！
		
		Servlet中有三大域，而JSP中有四大域，它就是最后一个域对象！可以获取其他8个内置对象，还可以代理其它域，例如：
		pageContext.setAttribute("xxx", "XXX", PageContext.SESSION_SCOPE);
		
		application(ServletContext)：	整个应用程序
		session：						整个会话，一个会话中只有一个用户
		request：						一个请求链
		pageContext：					一个JSP页面！这个域是在当前JSP页面和当前JSP页面中使用的标签之间共享数据！

		全域查找：pageContext.findAttribute("xxx");从小到大，依次查找！

============================================================================================================================================

JSP动作标签

　　JSP动作标签，与html提供的标签有本质的区别。它是由服务器来解释执行！与JAVA代码一样，都是在服务器端执行的！
    <jsp:forward>：就是请求转发！只是在JSP页面中使用！这样使用：<jsp:forward page="xxx.jsp"/>
    <jsp:include>：就是请求包含！只是在JSP页面中使用！
    <jsp:param>  ：它用来作为forward和include的子标签！用来给转发或包含的页面传递参数！

============================================================================================================================================

JavaBean规范：1. 必须要有无参构造器
			  2. 必须为成员提供get/set方法，但是只提供一个也是可以的
			  3. 属性：有get/set方法，属性名称由get/set方法去除get/set再将首字母小写得来，boolean类型属性的读方法还可以以is开头


内省依赖反射来操作JavaBean，但是比反射方便一些： 1.通过内省类得到BeanInfo:Introspector.getBeanInfo
												 2.通过BeanInfo得到属性描述符对象集合：PropertyDescriptor[] getPropertyDescriptors()
												 3.通过单个属性描述符对象得到set/get方法，就可以反射了


commons-beanutils小工具内部就是依赖内省！所需jar包：commons-beanutils、commons-logging
	
	先反射得到对象：Object bean = Class.forName("xx.xx.xx.xxx").newInstance()，有了对象就可以调用方法对其属性赋值了

	BeanUtils.setProperty(Object bean, String propertyName, String propertyValue)
	BeanUtils.getProperty(Object bean, String propertyName)，返回String类型
	BeanUtils.populate(Object bean, Map map)返回JavaBean对象，将Map中的数据封装到JavaBean中，要求Map的key与Bean的属性相同！
	CommontUtils.toBean(Map map, Class class)返回JavaBean对象，这个方法就不需要创建对象，内部通过反射自动创建对象。


JSP中与JavaBean相关的标签

	<jsp:useBean>：<jsp:useBean id="user" class="xx.xx.xx.User" scope="session"/> 在session域中查找名为user的JavaBean，如果不存在，创建之
	<jsp:setProperty>：<jsp:setProperty property="username" name="user" value="admin"/> 设置名为user的JavaBean的username属性值为admin
	<jsp:getProperty>：<jsp:getProperty property="username" name="user"/> 获取名为user的JavaBean的名为username属性值

============================================================================================================================================

EL表达式是JSP内置的表达式语言！-${}
	
	自JSP2.0开始，规定使用EL表达式和动态标签来替代JAVA脚本！EL替代的就是<%= ... %>，也就是说，EL只能做输出！

	EL有11个内置对象！其中10个是Map！通过"."跟上key值指定查询内容。特殊的是pageContext，它是PageContext类型，1个项9个

		pageScope：			${pageScope.xxx}等同与pageContext.getAttribute("xxx")；如果不存在，输出空字符串，而不是null。
		requestScope：···
		sessionScoep：···
		applicationScope：  以上四中都是指定域查找，还有全域查找: ${xxx}

		param：				对应参数，Map<String,String>类型，其中key参数名，value是参数值，适用于单值的参数。
		paramValues：		对应参数，Map<String,String[]>类型，其中key参数名，value是参数值，适用于多值的参数。
		header：			对应请求头，Map<String,String>类型，其中key表示头名称，value是头值，适用于单值请求头
		headerValues：		对应请求头，Map<String,String[]>类型，其中key表示头名称，value是头值，适用于多值请求头
		initParam：			对应<context-param>内的参数，Map<String,String>类型，其中key表示参数名，value表示参数值，适用于单值的参数
		cookie：			对应Cookie，Map<String,Cookie>类型，其中key是cookie的name，value是cookie对象。注意加.value
		pageContext：		它是PageContext类型！可以获取其他的内容，包括session的id：{pageContext.session.id}
			  
		注意!对于多值，如果不指定下标，则返回数组对象。如果指定查询的数据带有'-'号，可以使用"['']"进行处理，例如:${header['User-Agent']}

============================================================================================================================================

EL函数库(由JSTL提供的)

	先得导入标签库：<%@ tablib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

	String toUpperCase(String input)：把参数转换成大写
	String toLowerCase(String input)：把参数转换成小写
	int indexOf(String input, String substring)：从大串，找出小串第一次出现的位置
	boolean contains(String input, String substring)：查看大串中是否包含小串
	boolean containsIgnoreCase(String input, String substring)：忽略大小写后是否包含
	boolean startsWith(String input, String substring)：是否以小串为前缀
	boolean endsWith(String input, String substring)：是否以小串为后缀
	String substring(String input, int beginIndex, int endIndex)：截取子串，有头没尾
	String substringAfter(String input, String substring)：获取大串中，小串所在位置后面的字符串
	substringBefore(String input, String substring)：获取大串中，小串所在位置前面的字符串
	String escapeXml(String input)：把input中“<”、">"、"&"、"'"、"""，进行转义
	String trim(String input)：去除前后空格
	String replace(String input, String substringBefore, String substringAfter)：替换
	String[] split(String input, String delimiters)：分割字符串，得到字符串数组
	int length(Object obj)：可以获取字符串、数组、各种集合的长度！
	String join(String array[], String separator)：将数组以指定字符串联合成字符串！

	在JSP页面中的写法为：${fn:endWith("hello","he")}···

============================================================================================================================================

自定义EL函数库

	写一个java类，类中可以定义0~N个静态有返回值的方法。再创建一个tld文件，该文件一般都放在WEB-INF目录下，最后导入自定义函数库

	<?xml version="1.0" encoding="UTF-8" ?>

	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd"
	  version="2.0">
	    
	  <description>itcast</description>
	  <display-name>itcast-function</display-name>
	  <tlib-version>1.0</tlib-version>
	  <short-name>it</short-name>
	  <uri>http://www.itcast.cn/el/functions</uri>
	  
	  <description>描述</description>	
	  <function>
	    <name>方法名</name>
	    <function-class>类路径</function-class>
	    <function-signature>返回值 方法名(参数)</function-signature>//返回值和参数都要带包名
		<example>示例</example>		示例和描述标签可以不写
	  </function>//可以配置多个方法
	</taglib>

============================================================================================================================================

JSTL的概述

	JSTL是apache对EL表达式的扩展，也就是说JSTL依赖EL，JSTL是标签语言！它不和JSP内置的标签相同，需要我们自己导包，以及导入标签库
	如果使用MyEclipse开发工具，把项目发到Tomcat时，它会自动导入这个JSTL的jar包

	JSTL四大库：core核心库、fmt格式化库、sql过时了、xml过时了

		core库 -> c标签<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

		<c:out value="输出内容" default="当输出内容为null时，默认为该值" escapeXml="默认值为true，表示转义">

		<c:set var="变量名" value="变量值" scope="域，默认为page，可选值：page、request、session、application">

		<c:remove var="变量名" scope="如果不指定scope，表示删除所有域中的该名称的变量！">

		<c:url var="添加这个属性后，value值会保存到域中，而不是输出到页面" value="以"/"开头的路径，会自动添加项目名" scope="与var一起使用"/>
			<c:param name="参数名" value="参数值">
		</c:url>

		<c:if test="布尔类型">当test为true时，执行这里的内容！</c:if>

		<c:choose>
			<c:when test="布尔类型">当test为true时，执行这里的内容！</c:when>
			······
			<c:otherwise>当所有c:when标签的test全为false时，执行这里的内容！</c:otherwise>
		</c:choose>
		
		<c:forEach var="循环变量" begin="开始值" end="结束值" step="步长，默认为1">
			循环内容
		</c:forEach>

		<c:forEach items="数组或集合" var="临时变量" varStatus="vs，创建循环状态变量">
			创建循环状态变量的属性：count当前对象、index元素下标、count遍历数量、first是否是第一个、last是否是最后一个
		</c:forEach>

		fmt库 -> fmt标签<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

		 <ft:formatDate value="Date类型变量" partten="日期模板，例如：yyyy-MM-dd HH:mm:ss"/>
		 <fmt:formatNumber value="变量" pattern="0.00">四舍五入保留小数点后2位，不足补0！
		 <fmt:formatNumber value="变量" pattern="#.##">不补位！

============================================================================================================================================

自定义标签

	1.提供标签处理类，实现SimpleTag接口，其中有方法：

		void doTag()：每次执行标签时都会调用这个方法，在这个方法中写标签内容，如果throw new SkipPageException()就不执行本页面剩余内容！
		JspTag getParent()：返回父标签（非生命周期方法），不设置
		void setParent(JspTag)：设置父标签，也不设置
		void setJspBody(JspFragment)：设置标签体，得到标签体
		void setJspContext(JspContext)：设置JSP上下文对象，PageContext是它的子类，得到JspContext

　　	其中doTag()会在其他三个set方法之后被tomcat调用。  
  
		SimpleTagSuppot实现了SimpleTag接口，保存了传递的数据并提供了get方法，它只需重写doTag()方法，但该类未强转JspContext

	2. 创建并配置tld文件，一般都放到WEB-INF之下！

		<?xml version="1.0" encoding="UTF-8" ?>
		<taglib xmlns="http://java.sun.com/xml/ns/javaee"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd"
			version="2.1">
		  <tlib-version>1.0</tlib-version>
		  <short-name>itcast</short-name>
		  <uri>http://www.itcast.cn/tags/it-1.0</uri>
		  <tag>
			<name>标签的名称</name>
			<tag-class>标签处理类</tag-class>
			<body-content>标签体的类型</body-content>
			<attribute>
				<name>属性名</name>
				<required>true</required>指定该属性是否是必须的
				<rtexprvalue>true</rtexprvalue>指定属性值是否可以使用EL表达式
			</attribute>
		  </tag>//可以配置多个标签
		</taglib>

	3. 导入自定义标签


标签体的类型

	empty：无标签体！
	scriptless：只能是EL表达式或其它标签！在doTag()方法中写入getJspBody().invoke(getJspContext().getout()/*可以简写为null*/)可以执行标签体
	还有两个类型，基本没用


标签属性

	在标签处理类中添加属性并至少提供set方法！这个set方法会在doTag()方法之前被tomcat执行！然后doTag()中使用属性，最后在tld文件中配置标签属性
 
============================================================================================================================================

MVC
	
  MVC模式（Model-View-Controller）是软件工程中的一种软件架构模式，把软件系统分：模型（Model）、视图（View）和控制器（Controller）。
  MVC可对程序的后期维护和扩展提供了方便，并且为程序某些部分的重用提供了方便。而且MVC也使程序简化，更加直观。
  注意，MVC不是Java的东西，几乎现在所有B/S结构的软件都采用了MVC设计模式。

JavaWeb与MVC

	JSP Model1第一代
		服务器端只有JSP页面，所有的操作甚至访问数据库的API都在JSP页面中完成。所有的东西都耦合在一起，对后期的维护和扩展不利。
	JSP Model1第二代
		把业务逻辑的内容放到了JavaBean中，而JSP页面负责显示以及请求调度的工作。
	JSP Model2
		适合多人合作开发大型的Web项目，各司其职，互不干涉，有利于开发中的分工，有利于组件的重用。
		  JSP：视图层，用来与用户打交道。负责接收用来的数据，以及显示数据给用户
		  Servlet：控制层，负责找到合适的模型对象来处理业务逻辑，转发到合适的视图
		  JavaBean：模型层，完成具体的业务工作
		

JavaWeb三层框架

我们常说的三层框架是由JavaWeb提出的，也就是说这是JavaWeb独有的！Web层依赖业务层！而业务层依赖数据层！

       WEB层  --> 与Web相关的内容(Servlet、JSP、Servlet相关API:request、responset···)
  业务逻辑层  --> 业务对象(Service)，将零散的基本操作聚合成功能(可重用)，它只关心业务逻辑，不包含Web相关内容，甚至可以应用到非Web环境中
  数据访问层  --> DAO，Date Access Object数据访问对象，可以对数据库进行零散的基本操作，例如增删改查(所有对数据库的操作不能跳出到Dao之外)

  还会涉及到实体类(JavaBean)  --> 与表单和数据库对应 

   			|					服务器
			|		  Web层				    
			|		   |				      
			|       Servlet◢────────────◣Service--业务层
			|        ◥|      				  ▲		
			|      ╱  |                      |
			|    ╱	   |					  |
			|  请求	   |					  |				
			|╱        |					  |
		   ╱		   |					  |
		 ╱	|	       ▼					  ▼
	浏览器◢|───响应──JSP			 数据层--Dao◢───────◣DB--数据库
			|
			|

============================================================================================================================================

监听器是一个接口，需要注册在事件源上，监听器中的方法在指定事件发生时被调用。

JavaWeb中的监听器

	事件源：JavaWeb三大域	


	事件对象：1. ServletContextEvent：			ServletContext getServletContext();
			  2. HttpSeessionEvent：			HttpSession getSession();
			  3. ServletRequestEvent：			ServletRequest getServletRequest();
												ServletContext getServletContext();
				 ······

	监听器：	

		生命周期监听器：ServletContextListener			void contextInitialized(ServletContextEvent sce);
														void contextDestroyed(ServletContextEvent sce);

				
						HttpSessionListener				void sessionCreated(HttpSessionEvent se);
														void sessionDestroyed(HttpSessionEvent se);
				
						ServletRequestListener			void requestInitialized(ServletRequestEvent sre);
													v	oid requestDestroyed(ServletRequestEvent sre);
				

		属性监听器：ServletContextAttributeListener		void attributeAdded(ServletContextAttributeEvent event);	添加属性时
														void attributeReplaced(ServletContextAttributeEvent event);	替换属性时
														void attributeRemoved(ServletContextAttributeEvent event);	移除属性时

					HttpSessioniAttributeListener		void attributeAdded(HttpSessionBindingEvent event);	
														void attributeReplaced(HttpSessionBindingEvent event);
														void attributeRemoved(HttpSessionBindingEvent event);

				
					ServletRequestAttributeListener		void attributeAdded(ServletRequestAttributeEvent srae);	
														void attributeReplaced(ServletRequestAttributeEvent srae);
														void attributeRemoved(ServletRequestAttributeEvent srae);
	

	编写监听器：1. 写一个监听器类实现某个监听器接口
				2. 注册监听器，在web.xml中配置部署：<listener>
														<listener-class>类路径</listener-class>>
													</listener>


	感知监听器：有两个！都与HttpSession相关，但是由JavaBean实现接口，并不是添加到三大域上，这两个监听器都不需要在web.xml中部署！

		HttpSessionBindingListener			void valueBound(HttpSessionBindingEvent event);			把JavaBean对象添加到Session中时被调用
											void valueUnbound(HttpSessionBindingEvent event);		从Session中移除JavaBean对象时被调用

		HttpSessionActivationListener		void sessionWillPassivate(HttpSessionEvent se);			JavaBean对象被钝化时被调用
											void sessionDidActivate(HttpSessionEvent se);			JavaBean对象被活化时被调用

============================================================================================================================================

国际化：页面会根据不同的语言来显示不同的语言内容，因此需要把与语言相关的所有内容都写成变量

	写两个配置文件放在src目录下，分别存放不同的语言信息，名称为基本名称_Locale部分.properties，两个资源名称不同的部分就是Locale部分
	该部分内容是该类中的静态常量，例如常见的zh_CN和en_US

	应用程序使用ResourceBundle类的Locale对象(语言环境)来识别浏览器默认使用的语言类型，决定加载哪一个配置文件信息，

		 <%ResourceBundle rb = ResourceBundle.getBundle("配置文件的基础名称",request.getLocale()); %>
		 <h1><%=rb.getString("login") %></h1>						//通过由客户端的浏览器提供的Locale创建ResourceBundle
			 <form action="XXXX" method="post">	
				 <%=rb.getString("username") %>：<input type="text" name="username"/><br/>
				 <%=rb.getString("password") %>：<input type="password" name="password"/><br/>
				 <input type="submit" value="<%=rb.getString("login") %>"/>	
			 </form>

============================================================================================================================================

Filter：过滤器会在一组资源之前执行，有拦截能力。与Servlet一样，Filter也是单例的。

编写过滤器：1.写一个过滤器类类去实现Filter接口
			2.在web.xml中配置部署，内容与Servlet的配置基本相同:<filter>
																  <filter-name></filter-name>
																  <filter-class></filter-class>
																</servlet>
																<filter-mapping>			//filter-mapping的先后顺序决定了拦截器的执行属性
																  <filter-name></filter-name>					
																  <url-pattern></url-pattern>
																  <servlet-name></servlet-name>	 //拦截指定的Servlet
																</filter-mapping>		这里面的内容有些可配可不配，有些也可以配多个


Filter接口的生命周期方法：
			
		void init(FilterConfig)				Filter会在服务器启动时就创建，创建之后，马上执行，FilterConfig与ServletConfig相似

		void destory()						Filter在服务器关闭之前被销毁，销毁之前执行！

		void doFilter(ServletRequest,ServletResponse,FilterChain)		每次过滤时都会执行


FilterChain类的doFilter(ServletRequest, ServletResponse)				放行！执行访问资源或是下一个过滤器！执行结束之后接着往下剩余内容


过滤器的四种拦截方式，在<fitler-mapping>标签中配置，支持多拦截：1. <dispatcher>REQUEST</dispatcher>//默认，若给出其他的，这个就没了
																2. <dispatcher>FORWARD</dispatcher>
																3. <dispatcher>INCLUDE</dispatcher>
																4. <dispatcher>ERROR</dispatcher>


过滤器的应用场景：1.执行目标资源之前做预处理工作，例如设置编码，这种试通常都会放行，只是在目标资源执行之前做一些准备工作； 
				  2.通过条件判断是否放行，例如校验当前用户是否已经登录，或者用户IP是否已经被禁用；
				  3.在目标资源执行后，做一些后续的特殊处理工作，例如把目标资源输出的数据进行处理；

============================================================================================================================================

上传对表单限制：1.method="post"    2.enctype="multipart/form-data"	3.表单中需要添加文件表单项：<input type="file" name="xxx" />


上传对Servlet限制：enctype="multipart/form-data"使getParametere("xxx")系列方法返回null，所以不能使用BaseServlet。
																				        可以使用request.getInputStream()方法得到整个请求体
														

多部件表单的体：1.一个表单项一个部件。
				2.每个部件都包含请求头和空行，以及请求体。
				3.普通表单项：有一个Content-Disposition头，包含name="xxxx"，表示表单项名称，体就是表单项的值
				4. 文件表单项：比不同表单项多一个filename="xxx"和一个Content-Type头，表示上传文件的名称和类型，体则是上传文件的内容


上传三步：需要使用jar包commons-fileupload，它依赖commons-io，这个小组件可以解析request封装的上传数据并封装到一个FileItem中

		  1. DiskFileItemFactory factory = new DiskFileItemFactory();		创建工厂
		  2. ServletFileUpload sfu = new ServletFileUpload(factory);		创建解析器：
          3. List<FileItem> fileItemList = sfu.parseRequest(request);		使用解析器来解析request，得到FileItem集合：

		  

FileItem接口中的方法：boolean isFormField()：				返回true为普通表单项，false为文件表单项
					  String getFieldName()：				返回当前表单项的名称
					  String getString(String charset)：	返回表单项的值
					  String getName()：					返回上传的文件名称
					  long getSize()：						返回上传文件字节数
					  InputStream getInputStream()：		返回上传文件对应的输入流
					  void write(File destFile)：			把上传的文件内容保存到指定的文件中
					  String getContentType();				返回文件类型


上传的细节：1.文件的保存路径不能让浏览器直接访问到，如果为了方便项目使用，可以保存到WEB—INF下

			2.有的浏览器上传的文件名是绝对路径，需要切割字符串

			3.需要调用request.setCharacterEncoding(String)或servletFileUpload.setHeaderEncoding(String)方法设置编码

			  防止文件名称或普通表单项乱码，后者的优先级高

			4.文件名称重复问题，需要使用uuid作前缀

			5.一个目录下不能存放太多文件，需要打散：1.首字符打散	2.时间打散	3.哈希打散，两层目录

			6.文件大小限制：servletFileUpload.setFileSizeMax(Long)可以限制单个文件大小
							servletFileUpload.setSizeMax(Long)可以限制整个请求数据大小
							这两个方法必须在解析开始之前调用！如果上传内容超出大小限制，会在解析时分别抛出异常：
							FileUploadBase.FileSizeLimitExceededException 和 FileUploadBase.SizeLimitExceededException

			7.缓存大小与临时目录：当文件过大时，需要先将文件先保存到硬盘，最后cope到目录中去，所以需要设置缓存大小和临时目录
								  缓存大小默认为10KB	              new DiskFileItemFactory(Long, new File("F:/temp"));


示例代码：  request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(50*1024, new File("F:/temp"));
			
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			servletFileUpload.setFileSizeMax(512*1024);
			servletFileUpload.setSizeMax(1024*1024);
			
			try {
				List<FileItem> list = servletFileUpload.parseRequest(request);
				
				FileItem fileItem = list.get(1);
				
				String fileItemName = fileItem.getName();
				if(fileItemName.contains("\\"))fileItemName = fileItemName.substring(fileItemName.lastIndexOf("\\")+1);
				String fileName = CommonUtils.uuid() + "_" + fileItemName;
				
				String root = getServletContext().getRealPath("/WEB-INF/files");
				String hex = Integer.toHexString(fileName.hashCode());
				File parentFile = new File(root,hex.charAt(0) + "/" + hex.charAt(1));
				
				parentFile.mkdirs();
				fileItem.write(new File(parentFile,fileName));
				
			} catch (Exception e) {
				
				if(e instanceof FileUploadBase.FileSizeLimitExceededException){
					request.setAttribute("msg","上传文件不能超过512M!");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
				}
				
				if(e instanceof FileUploadBase.SizeLimitExceededException){
					request.setAttribute("msg","上传数据不能超过1G!");
					request.getRequestDispatcher("/index.jsp").forward(request, response);
				}
			}

============================================================================================================================================

下载就是向客户端响应字节数据！要求两个头一个流：Content-Type文件的MIME类型
												Content-Disposition打开方式：默认为inline，即在浏览器窗口中打开，也有下载按钮可点击
																			 attachment;filename=xxx，即在下载框中打开，文件名称就是xxx
												一个指定下载文件的输入流


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		String file = "e:/南京.mp3";
		response.setHeader("Content-Type",getServletContext().getMimeType(file));		//通过文件名称获取MIME类型
		String filename = filenameEncoding("南京.mp3", request);
		response.setHeader("Content-Disposition","attachment;filename=" + filename);
		FileInputStream fileInputStream = new FileInputStream(file);
		IOUtils.copy(fileInputStream, response.getOutputStream());
	}
	
	public static String filenameEncoding(String filename, HttpServletRequest request) throws IOException {		
		String agent = request.getHeader("User-Agent");									// 根据浏览器的不同，采取不同的编码方式
		if (agent.contains("Firefox")) {
			BASE64Encoder base64Encoder = new BASE64Encoder();
			filename = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
		} else if(agent.contains("MSIE"))filename = URLEncoder.encode(filename, "utf-8");
		  else filename = URLEncoder.encode(filename, "utf-8");
		return filename;
	}

============================================================================================================================================

发邮件是从客户端把邮件发送到邮件服务器（可看做邮局），收邮件是把邮件服务器的邮件下载到客户端，邮件与HTTP相同，收发邮件也是需要有传输协议

	SMTP：（Simple Mail Transfer Protocol，简单邮件传输协议）发邮件协议；负责发邮件的请求
	POP3：（Post Office Protocol Version 3，邮局协议第3版）收邮件协议；负责收邮件的请求
	IMAP：（Internet Message Access Protocol，因特网消息访问协议）收发邮件协议

	smtp服务器的端口号为25，服务器名称为：smtp.xxx.xxx
	pop3服务器的端口号为110，服务器名称为：pop3.xxx.xxx


JavaMail：需要jar包：mail.jar、actvition.jar

	普通邮件：	public void sendMail() throws Exception{
				
					Properties properties = new Properties();											//得到Session，它类似于连接对象
					properties.setProperty("mail.host", "smtp.163.com");
					properties.setProperty("mail.smtp.auth", "true");									//开启认证
					Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication("18030832926","liusijieq1230");			//登录信息
						}
					});
					
																										//创建邮件对象，它可以设置邮件内容
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress("18030832926@163.com"));
					message.setRecipients(RecipientType.TO, "15308232110@163.com");	
					message.setRecipients(RecipientType.CC, "15308232110@163.com");						//抄送
					message.setRecipients(RecipientType.BCC, "15308232110@163.com");					//密送
					
					message.setSubject("测试邮件");
					message.setContent("这是一封测试邮件", "text/html;charset=utf-8");					//文本内容需要处理编码
					
					//发邮件
					Transport.send(message);
				}

	带附件的邮件：	public void sendMail() throws Exception{
		
						Properties properties = new Properties();
						properties.setProperty("mail.host", "smtp.163.com");
						properties.setProperty("mail.smtp.auth", "true");
						Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication("18030832926","liusijieq1230");
							}
						});
						
						MimeMessage message = new MimeMessage(session);
						message.setFrom(new InternetAddress("18030832926@163.com"));
						message.setRecipients(RecipientType.TO, "15308232110@163.com");
						message.setSubject("测试邮件");
						
						//当发送包含附件的邮件时，邮件体就为多部件形式！MimeMultipart就是用来装载多个主体部件的集合！
						MimeMultipart list = new MimeMultipart();
						
						MimeBodyPart mimeBodyPart1 = new MimeBodyPart();										//多个主体部件
						MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
						MimeBodyPart mimeBodyPart3 = new MimeBodyPart();
						
						mimeBodyPart1.setContent("这是一封包含附件的测试邮件", "text/html;charset=utf-8");
						
						mimeBodyPart2.attachFile(new File("F:/003.jpg"));										//附件内容，也需要处理编码
						mimeBodyPart2.setFileName(MimeUtility.encodeText("图片.jpg"));
						mimeBodyPart3.attachFile(new File("F:/南京.mp3"));
						mimeBodyPart3.setFileName(MimeUtility.encodeText("歌曲.mp3"));
						
						list.addBodyPart(mimeBodyPart1);														//装载主体部件
						list.addBodyPart(mimeBodyPart2);
						list.addBodyPart(mimeBodyPart3);
						
						message.setContent(list);																//将其设置为邮件内容
						
						Transport.send(message);
					}


itcast-tools小工具发送邮件：public void sendMail() throws Exception{
		
								Session session = MailUtils.createSession("smtp.163.com","18030832926", "liusijieq1230");
								
								Mail mail = new Mail("18030832926@163.com","15308232110@163.com,···","测试邮件","这是一封测试邮件");
								mail.addAttach(new AttachBean(new File("F:/003.jpg"), "图片.jpg"));
								mail.addAttach(new AttachBean(new File("F:/南京.mp3"), "歌曲.mp3"));
								
								MailUtils.send(session, mail);
							}

============================================================================================================================================