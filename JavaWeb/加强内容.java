规范：1.泛型编译阶段的语法，在编译阶段规定集合中的类型，不能是基本数据类型
	  2.泛型擦除:带有泛型的源文件编译生成的class文件中将不再带有泛形信息，以此使程序运行效率不受到影响
	  3.泛型可以统一类型，减少强制类型转换，缺点也是类型太统一
	  4.泛型一般使用在集合中，因为元素放入集合中就失去了原有类型
	  5.泛型可以同时定义多个，泛型可以是随意写字母，但尽量体现意义和注意大小写问题


泛型类：具有一个或多个类型变量的类，称之为泛型类！在创建泛型类实例时，需要为其类型变量赋值，否则会有一个警告！在类名后给出类型变量！
		泛型类中的静态方法不能使用类定义的类型变量


泛型方法：具有一个或多个类型变量的方法，称之为泛型方法！泛型方法不一定非要在泛型类中！泛型方法与泛型类没什么关系！在返回值前给出类型变量！


泛型的使用：1.成员变量		2.方法的返回值或参数		3.局部变量


泛型的继承和实现：当子类不是泛型类时，需要父类指定类型常量，这时父类方法中所有的泛型变量就变成了这个指定类型常量
				  当子类是泛型类时，父类可以指定类型常量，也可以指定类型变量


代码：	String[] strings = new String[10];						// 集合和数组都可以指定类型
		List<String> list = new ArrayList<String>();

		Object[] objects = new String[10];
		objects[0] = new Integer(100);							// 编译通过，但运行时报错，出现ArrayStoreException

		List<Object> list = new ArrayList<String>();			// 编译无法通过，创建泛型对象时两端泛型变量必须相同
		list.add(new Integer(100));								// 如果编译通过，那么由于泛型擦除，就能够运行
		
		public static void print(List<Object> list){}
		print(new ArrayList<String>());							// 编译无法通过，因为这类似于：List<Object> list = new ArrayList<String>();

		public void print(List<Object> list){}
		public void print(List<String> list){}					// 尝试重载，但编译无法通过，因为泛型擦除后这两个方法又变成同一个方法了！

		public static void print(List<? extends Object> list){}	// 这时就出现了通配符
		print(new ArrayList<String>())；						// 编译通过！

		public static void print(List<? super Integer> list){}
		print(new ArrayList<Number>());							// 编译通过！	

		List<?> list = new ArrayList<String>();					// 编译通过！但注意！通配符在使用"new"创建泛型对象时不能使用
	
	
泛型的通配符


	通配符的优点：使用在方法的参数上，使调用方法时传参更灵活

	通配符分类及缺点：	<? extends XXX>：无界统配，参数和返回值为泛型的方法不能再使用，可以简写为<?>	
						<? extends XXX>：子类限定，参数为泛型的方法不能再使用
						<? super XXX>  ：父类限定，返回值为泛型的方法不能再使用


使用泛型通配：	class Clazz<T>{
	
					public void fun1(Collection<? extends T> collection){}
					public void fun2(Collection<? super T> collection){}
					
					public static void main(String[] args) {

						Clazz<Number> clazz = new Clazz<Number>();
						clazz.fun1(new ArrayList<Integer>());
						clazz.fun2(new ArrayList<Object>());
					}
				}

=============================================================================================================================================

注解其实就是代码里的特殊标记，注释是给人看的，注解则是给程序看的！注解用于替代配置文件，一般由框架来读取使用！

Annotation是所有注解的根类，它是一个接口

定义注解类和通过反射读取注解一般都是框架开发者的工作，而我们就只是使用注解

注解的作用目标：类、成员变量、构造器、方法、参数、局部变量、包


定义注解：	@interface MyAnnotation{
				int age();					//注解属性的类型可以是：8种基本数据类型、String、Enum、Class、注解，以及以上的一维数组类型
				String name();				//定义和获取属性时需要加"()"，System.out.print(myAnnotation.name());
				T color();						
				Class clazz() default Demo.class;	//指定属性默认值
				MyAnnotation2 annotation();
				String[] names();
			}

使用注解：@MyAnnotation(age = 1, name = "Jon", color = T.RED, annotation = @MyAnnotation2(money = 100), names = {"Jok","Ros"})
			
		  使用注解时，必须对注解的所有属性赋值。如果属性有默认值，可以不赋值，但如果赋值则是覆盖效果
		  使用注解时，如果只给名为value的属性赋值时，可以省略"value="，这时名为"value"的注解属性的特权
		  使用注解时，如果数组类型的属性值只有一个时，可以省略"{}"


注解的目标限定：可以限定注解的作用目标，需要在定义注解时给注解添加@Target注解，它只有一个value属性，类似是枚举类型。

		@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})//这个注解现在只能使用在类上、方法上、成员变量上
		@interface MyAnnotation {······	}


注解的保留策略：源代码文件（SOURCE）：注解只在源代码中存在
				字节码文件 （CLASS）：注解还能在.class文件中存在，但JVM在加载类时，会忽略注解！
				JVM		 （RUNTIME）：JVM在加载类时不会忽略注解！
  
		@Retention(RetentionPolicy.RUNTIME)
		@interface MyAnno1 {···}

=============================================================================================================================================

反射泛型

	abstract class A<T> {

		private Class clabb;

		public A() {

			Class clazz = this.getClass();
			Type type = clazz.getGenericSuperclass();
			ParameterizedType pType = (ParameterizedType) type;
			Type[] types = pType.getActualTypeArguments();
			clabb = (Class)types[0];
			
			//简写：clabb = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}

	class B extends A<String> {}


反射注解时需要注解的保留策略必须是RUNTIME
反射注解的作用目标有Class、AccessibleObject及其子类Method、Constructor、Field，它们有方法：

	Annotation getAnnotation(Class)，返回目标上指定类型的注解！
	Annotation[] getAnnotations()，返回目标上所有注解！					//这是Class和AccessibleObject才有的方法
	Annotation[] getDeclaredAnnotations()								//getAnnotations()和getDeclaredAnnotations()的区别是什么        ****

=============================================================================================================================================

Servlet3.0要求：MyEclipse10.0或以上版本！发布到Tomcat7.0或以上版本！

创建JavaEE6.0项目！删除web.xml！

Servlet3.0特性：

	1.注解替代配置文件：Servlet：	@WebServlet(urlPatterns="/AServlet", 
										initParams={
											@WebInitParam(name="p1", value="v1"),
											@WebInitParam(name="p2", value="v2")
										},
										loadOnStartup=1
									)

									替代：		<servlet>
												  <servlet-name>AServlet</servlet-name>
												  <servlet-class>cn.itcast.web.servlet.AServlet</servlet-class>
												  <init-param>
													<param-name>p1</param-name>
													<param-value>v1</param-value>
												  </init-param>
												  <init-param>
													<param-name>p2</param-name>
													<param-value>v2</param-value>
												  </init-param>
												  <load-on-startup>1<load-on-startup>
												</servlet>
												<servlet-mapping>
												  <servlet-name>AServlet</servlet-name>
												  <url-pattern>/AServlet</url-pattern>
												</servlet-mapping>


						类似的还有：@WebFilter(urlPatterns="/*")、@WebListener


	2.异步处理：在服务器没有结束响应之前，浏览器是看不到响应内容的！只有响应结束时，浏览器才能显示结果！
				但异步处理可以使浏览器在服务器开始响应时，就可以看到响应内容，不用等待服务器响应结束！

		实现步骤：1.开启异步支持：@WebServlet(urlPatterns="/AServlet", asyncSupported=true)

				  2.设置服务器响应内容的类型：response.setContentType("text/html;charset=utf-8");如果IE不能正常输出，说明响应体大小不足512B
				    
				  3.得到AsyncContext：AsyncContext acyncContext = request.startAsync(request, response);

				  4.启动异步线程：  acyncContext.start(new Runnable() {
										public void run() {
											...
											acyncContext.complete();		//通知Tomcat异步线程已经执行结束，否则会服务器不能够及时结束响应
										}
									});


	3.对上传的支持：1.表单要求不变， 2.request.getParameter()方法可以使用了！ 3.使用Servlet3.0提供的上传接口，其封装了commons-fileupload
					
	　　上传的步骤：默认Servlet是不支持使用上传组件：需要给Servlet添加一个注解: @MultipartConfig
					request.getPart("对应上传表单项的name属性值")，得到Part对象
					Part：> String getContentType()：获取上传文件的MIME类型
					　　　> String getName()：获取上传表单项的name属性值
					　　　> String getHeader(String header)：获取指定头的值，上传文件名称就需要从Content-Disposition头值中截取出来
					　　　> long getSize()：获取上传文件的大小
					　　　> InputStream getInputStream()：获取上传文件的内容
					　　　> void write(String fileName)：把上传文件保存到指定路径下

=============================================================================================================================================

动态代理：Object proxyObject = Proxy.newProxyInstance(ClassLoader classLoader,Class[] interfaces,InvocationHandler invocationHandler);
			
		  该方法能够，动态创建 实现interfaces数组中所有接口的 实现类对象！需要参数：类加载器、实现的接口、调用处理器

	InvocationHandler接口只有一个方法：public Object invoke(Object proxy, Method method, Object[] args);

							该方法在调用代理对象所实现接口中的方法时被调用。需要参数：代理对象、目标方法、实参

			
三步理解：	public class Demo {
	
				@Test
				public void fun() {
					
					ClassLoader classLoader = this.getClass().getClassLoader();
					InvocationHandler invocationHandler = new InvocationHandler() {
						public Object invoke(Object proxy, Method method, Object[] args)
								throws Throwable {
							System.out.println("你好，动态代理！");
							return "String";
						}
					};
					
					Object o = Proxy.newProxyInstance(classLoader, new Class[]{A.class, B.class}, invocationHandler);
					
					A a = (A) o;									// 编译成功
					B b = (B) o;									// 编译成功
					System.out.println(a.aaa("hello", 100));		// String
				}
			}

			interface A {
				public Object aaa(String s, int i);
			}

			interface B {}

	======================================================

			public class Demo {
	
				@Test
				public void fun() {
					
					Waiter manWaiter = new ManWaiter();
					ClassLoader classLoader = this.getClass().getClassLoader();
					Class[] interfaces = {Waiter.class};
					InvocationHandler invocationHandler = new WaiterInvocationHandler(manWaiter);
					
					// 得到代理对象，就是在目标对象的基础上进行了增强的对象！
					Waiter waiterProxy = (Waiter)Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
					waiterProxy.serve();	// 您好！ 服务中... 再见！
				}
			}

			class WaiterInvocationHandler implements InvocationHandler {
				
				private Waiter waiter;													// 目标对象，就是被增强的对象
				
				public WaiterInvocationHandler(Waiter waiter) {
					this.waiter = waiter;
				}
				
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					System.out.println("您好！");
					this.waiter.serve();
					System.out.println("再见！");
					return null;
				}
			}

			interface Waiter {
				public void serve();
			}

			class ManWaiter implements Waiter {
				public void serve() {
					System.out.println("服务中...");
				}
			}

	======================================================
		
		public class ProxyFactory {															// 专门用来生成代理对象的代理工厂
	
			private Object targetObject;													// 目标对象
			private BeforeAdvice beforeAdvice;												// 前置增强
			private AfterAdvice afterAdvice;												// 后置增强
			
			public Object createProxy() {													// 生成代理对象的方法
				
				ClassLoader classLoader = this.getClass().getClassLoader();
				Class[] interfaces = targetObject.getClass().getInterfaces();
				InvocationHandler invocationHandler = new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] params)
							throws Throwable {
						
						if(beforeAdvice != null) {
							beforeAdvice.before();
						}
						
						Object result = method.invoke(targetObject, params);				// 执行目标对象的目标方法
						
						if(afterAdvice != null) {
							afterAdvice.after();
						}
						
						return result;
					}
				};
				
				return  Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
			}
			
			···																				//目标对象和增强的get/set方法
			
			@Test
			public void fun() {
				
				ProxyFactory factory = new ProxyFactory();									// 创建工厂
				factory.setTargetObject(new ManWaiter());									// 设置目标对象
				factory.setBeforeAdvice(new BeforeAdvice() {								// 设置前置增强
					public void before() {
						System.out.println("您好！");
					}
				});
				
				factory.setAfterAdvice(new AfterAdvice() {									// 设置后置增强
					public void after() {
						System.out.println("再见！");
					}
				});
				
				Waiter waiter = (Waiter)factory.createProxy();
				waiter.serve();																// 您好！ 服务中... 再见！
			}
		}

		interface AfterAdvice {
			public void after();
		}

		interface BeforeAdvice {
			public void before();
		}

		interface Waiter {
			public void serve();
		}

		class ManWaiter implements Waiter {
			public void serve() {
				System.out.println("服务中...");
			}
		}

=============================================================================================================================================

类加载器可以把.class文件加载到JVM的方法区中，变成一个Class对象！


类加载器的分类：引导：类库
				扩展：扩展jar包-\lib\ext
				系统：写的类和导入的jar包，也就是classpath，MyEclipse在编译时会临时生成一个calsspath


类加载器的委托机制：先由引导加载，如果没找到，再由扩展加载，如果还没找到，最后由系统加载，如果最后还没找到，那么抛出ClassNotFoundException

					如果但一个类被引导加载了，那么类中的内容也由它加载，扩展和系统也一样

自定义类加载器:当一个类被加载时，它的ClassLoader调用findLoadClass()方法先到JVM的方法区中查看类是否已被加载，如果已被加载，
			   就不重复加载并直接返回Class对象。如果该类没被加载过，就开始调用上级委托机制：getParent().loadClass();
			   如果该方法返回null，就需要自己出手了，这时loadClass()方法会调用到本类的findClass()方法来加载类
			   所以自定义类加载器字需要继承ClassLoader并重写findClass()方法。


Tomcat提供了两种类加载器：服务器类加载器：${CATALINA_HOME}\lib，服务器类加载器，它负责加载这个下面的类！
						  应用类加载器：${CONTEXT_HOME}\WEB-INF\lib和classes，应用类加载器，它负责加载这两个路径下的类！先加载classes

	引导 -> 扩展 -> 系统 -> 服务器类加载器 -> 应用类加载器			服务器类加载器和应用类加载器的特性：先加载，再委托！

=============================================================================================================================================