�淶��1.���ͱ���׶ε��﷨���ڱ���׶ι涨�����е����ͣ������ǻ�����������
	  2.���Ͳ���:���з��͵�Դ�ļ��������ɵ�class�ļ��н����ٴ��з�����Ϣ���Դ�ʹ��������Ч�ʲ��ܵ�Ӱ��
	  3.���Ϳ���ͳһ���ͣ�����ǿ������ת����ȱ��Ҳ������̫ͳһ
	  4.����һ��ʹ���ڼ����У���ΪԪ�ط��뼯���о�ʧȥ��ԭ������
	  5.���Ϳ���ͬʱ�����������Ϳ���������д��ĸ�����������������ע���Сд����


�����ࣺ����һ���������ͱ������࣬��֮Ϊ�����࣡�ڴ���������ʵ��ʱ����ҪΪ�����ͱ�����ֵ���������һ�����棡��������������ͱ�����
		�������еľ�̬��������ʹ���ඨ������ͱ���


���ͷ���������һ���������ͱ����ķ�������֮Ϊ���ͷ��������ͷ�����һ����Ҫ�ڷ������У����ͷ����뷺����ûʲô��ϵ���ڷ���ֵǰ�������ͱ�����


���͵�ʹ�ã�1.��Ա����		2.�����ķ���ֵ�����		3.�ֲ�����


���͵ļ̳к�ʵ�֣������಻�Ƿ�����ʱ����Ҫ����ָ�����ͳ�������ʱ���෽�������еķ��ͱ����ͱ�������ָ�����ͳ���
				  �������Ƿ�����ʱ���������ָ�����ͳ�����Ҳ����ָ�����ͱ���


���룺	String[] strings = new String[10];						// ���Ϻ����鶼����ָ������
		List<String> list = new ArrayList<String>();

		Object[] objects = new String[10];
		objects[0] = new Integer(100);							// ����ͨ����������ʱ��������ArrayStoreException

		List<Object> list = new ArrayList<String>();			// �����޷�ͨ�����������Ͷ���ʱ���˷��ͱ���������ͬ
		list.add(new Integer(100));								// �������ͨ������ô���ڷ��Ͳ��������ܹ�����
		
		public static void print(List<Object> list){}
		print(new ArrayList<String>());							// �����޷�ͨ������Ϊ�������ڣ�List<Object> list = new ArrayList<String>();

		public void print(List<Object> list){}
		public void print(List<String> list){}					// �������أ��������޷�ͨ������Ϊ���Ͳ����������������ֱ��ͬһ�������ˣ�

		public static void print(List<? extends Object> list){}	// ��ʱ�ͳ�����ͨ���
		print(new ArrayList<String>())��						// ����ͨ����

		public static void print(List<? super Integer> list){}
		print(new ArrayList<Number>());							// ����ͨ����	

		List<?> list = new ArrayList<String>();					// ����ͨ������ע�⣡ͨ�����ʹ��"new"�������Ͷ���ʱ����ʹ��
	
	
���͵�ͨ���


	ͨ������ŵ㣺ʹ���ڷ����Ĳ����ϣ�ʹ���÷���ʱ���θ����

	ͨ������༰ȱ�㣺	<? extends XXX>���޽�ͳ�䣬�����ͷ���ֵΪ���͵ķ���������ʹ�ã����Լ�дΪ<?>	
						<? extends XXX>�������޶�������Ϊ���͵ķ���������ʹ��
						<? super XXX>  �������޶�������ֵΪ���͵ķ���������ʹ��


ʹ�÷���ͨ�䣺	class Clazz<T>{
	
					public void fun1(Collection<? extends T> collection){}
					public void fun2(Collection<? super T> collection){}
					
					public static void main(String[] args) {

						Clazz<Number> clazz = new Clazz<Number>();
						clazz.fun1(new ArrayList<Integer>());
						clazz.fun2(new ArrayList<Object>());
					}
				}

=============================================================================================================================================

ע����ʵ���Ǵ�����������ǣ�ע���Ǹ��˿��ģ�ע�����Ǹ����򿴵ģ�ע��������������ļ���һ���ɿ������ȡʹ�ã�

Annotation������ע��ĸ��࣬����һ���ӿ�

����ע�����ͨ�������ȡע��һ�㶼�ǿ�ܿ����ߵĹ����������Ǿ�ֻ��ʹ��ע��

ע�������Ŀ�꣺�ࡢ��Ա���������������������������ֲ���������


����ע�⣺	@interface MyAnnotation{
				int age();					//ע�����Ե����Ϳ����ǣ�8�ֻ����������͡�String��Enum��Class��ע�⣬�Լ����ϵ�һά��������
				String name();				//����ͻ�ȡ����ʱ��Ҫ��"()"��System.out.print(myAnnotation.name());
				T color();						
				Class clazz() default Demo.class;	//ָ������Ĭ��ֵ
				MyAnnotation2 annotation();
				String[] names();
			}

ʹ��ע�⣺@MyAnnotation(age = 1, name = "Jon", color = T.RED, annotation = @MyAnnotation2(money = 100), names = {"Jok","Ros"})
			
		  ʹ��ע��ʱ�������ע����������Ը�ֵ�����������Ĭ��ֵ�����Բ���ֵ���������ֵ���Ǹ���Ч��
		  ʹ��ע��ʱ�����ֻ����Ϊvalue�����Ը�ֵʱ������ʡ��"value="����ʱ��Ϊ"value"��ע�����Ե���Ȩ
		  ʹ��ע��ʱ������������͵�����ֵֻ��һ��ʱ������ʡ��"{}"


ע���Ŀ���޶��������޶�ע�������Ŀ�꣬��Ҫ�ڶ���ע��ʱ��ע�����@Targetע�⣬��ֻ��һ��value���ԣ�������ö�����͡�

		@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})//���ע������ֻ��ʹ�������ϡ������ϡ���Ա������
		@interface MyAnnotation {������������	}


ע��ı������ԣ�Դ�����ļ���SOURCE����ע��ֻ��Դ�����д���
				�ֽ����ļ� ��CLASS����ע�⻹����.class�ļ��д��ڣ���JVM�ڼ�����ʱ�������ע�⣡
				JVM		 ��RUNTIME����JVM�ڼ�����ʱ�������ע�⣡
  
		@Retention(RetentionPolicy.RUNTIME)
		@interface MyAnno1 {������}

=============================================================================================================================================

���䷺��

	abstract class A<T> {

		private Class clabb;

		public A() {

			Class clazz = this.getClass();
			Type type = clazz.getGenericSuperclass();
			ParameterizedType pType = (ParameterizedType) type;
			Type[] types = pType.getActualTypeArguments();
			clabb = (Class)types[0];
			
			//��д��clabb = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}

	class B extends A<String> {}


����ע��ʱ��Ҫע��ı������Ա�����RUNTIME
����ע�������Ŀ����Class��AccessibleObject��������Method��Constructor��Field�������з�����

	Annotation getAnnotation(Class)������Ŀ����ָ�����͵�ע�⣡
	Annotation[] getAnnotations()������Ŀ��������ע�⣡					//����Class��AccessibleObject���еķ���
	Annotation[] getDeclaredAnnotations()								//getAnnotations()��getDeclaredAnnotations()��������ʲô        ****

=============================================================================================================================================

Servlet3.0Ҫ��MyEclipse10.0�����ϰ汾��������Tomcat7.0�����ϰ汾��

����JavaEE6.0��Ŀ��ɾ��web.xml��

Servlet3.0���ԣ�

	1.ע����������ļ���Servlet��	@WebServlet(urlPatterns="/AServlet", 
										initParams={
											@WebInitParam(name="p1", value="v1"),
											@WebInitParam(name="p2", value="v2")
										},
										loadOnStartup=1
									)

									�����		<servlet>
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


						���ƵĻ��У�@WebFilter(urlPatterns="/*")��@WebListener


	2.�첽�����ڷ�����û�н�����Ӧ֮ǰ��������ǿ�������Ӧ���ݵģ�ֻ����Ӧ����ʱ�������������ʾ�����
				���첽�������ʹ������ڷ�������ʼ��Ӧʱ���Ϳ��Կ�����Ӧ���ݣ����õȴ���������Ӧ������

		ʵ�ֲ��裺1.�����첽֧�֣�@WebServlet(urlPatterns="/AServlet", asyncSupported=true)

				  2.���÷�������Ӧ���ݵ����ͣ�response.setContentType("text/html;charset=utf-8");���IE�������������˵����Ӧ���С����512B
				    
				  3.�õ�AsyncContext��AsyncContext acyncContext = request.startAsync(request, response);

				  4.�����첽�̣߳�  acyncContext.start(new Runnable() {
										public void run() {
											...
											acyncContext.complete();		//֪ͨTomcat�첽�߳��Ѿ�ִ�н������������������ܹ���ʱ������Ӧ
										}
									});


	3.���ϴ���֧�֣�1.��Ҫ�󲻱䣬 2.request.getParameter()��������ʹ���ˣ� 3.ʹ��Servlet3.0�ṩ���ϴ��ӿڣ����װ��commons-fileupload
					
	�����ϴ��Ĳ��裺Ĭ��Servlet�ǲ�֧��ʹ���ϴ��������Ҫ��Servlet���һ��ע��: @MultipartConfig
					request.getPart("��Ӧ�ϴ������name����ֵ")���õ�Part����
					Part��> String getContentType()����ȡ�ϴ��ļ���MIME����
					������> String getName()����ȡ�ϴ������name����ֵ
					������> String getHeader(String header)����ȡָ��ͷ��ֵ���ϴ��ļ����ƾ���Ҫ��Content-Dispositionͷֵ�н�ȡ����
					������> long getSize()����ȡ�ϴ��ļ��Ĵ�С
					������> InputStream getInputStream()����ȡ�ϴ��ļ�������
					������> void write(String fileName)�����ϴ��ļ����浽ָ��·����

=============================================================================================================================================

��̬����Object proxyObject = Proxy.newProxyInstance(ClassLoader classLoader,Class[] interfaces,InvocationHandler invocationHandler);
			
		  �÷����ܹ�����̬���� ʵ��interfaces���������нӿڵ� ʵ���������Ҫ���������������ʵ�ֵĽӿڡ����ô�����

	InvocationHandler�ӿ�ֻ��һ��������public Object invoke(Object proxy, Method method, Object[] args);

							�÷����ڵ��ô��������ʵ�ֽӿ��еķ���ʱ�����á���Ҫ�������������Ŀ�귽����ʵ��

			
������⣺	public class Demo {
	
				@Test
				public void fun() {
					
					ClassLoader classLoader = this.getClass().getClassLoader();
					InvocationHandler invocationHandler = new InvocationHandler() {
						public Object invoke(Object proxy, Method method, Object[] args)
								throws Throwable {
							System.out.println("��ã���̬����");
							return "String";
						}
					};
					
					Object o = Proxy.newProxyInstance(classLoader, new Class[]{A.class, B.class}, invocationHandler);
					
					A a = (A) o;									// ����ɹ�
					B b = (B) o;									// ����ɹ�
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
					
					// �õ�������󣬾�����Ŀ�����Ļ����Ͻ�������ǿ�Ķ���
					Waiter waiterProxy = (Waiter)Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
					waiterProxy.serve();	// ���ã� ������... �ټ���
				}
			}

			class WaiterInvocationHandler implements InvocationHandler {
				
				private Waiter waiter;													// Ŀ����󣬾��Ǳ���ǿ�Ķ���
				
				public WaiterInvocationHandler(Waiter waiter) {
					this.waiter = waiter;
				}
				
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					System.out.println("���ã�");
					this.waiter.serve();
					System.out.println("�ټ���");
					return null;
				}
			}

			interface Waiter {
				public void serve();
			}

			class ManWaiter implements Waiter {
				public void serve() {
					System.out.println("������...");
				}
			}

	======================================================
		
		public class ProxyFactory {															// ר���������ɴ������Ĵ�����
	
			private Object targetObject;													// Ŀ�����
			private BeforeAdvice beforeAdvice;												// ǰ����ǿ
			private AfterAdvice afterAdvice;												// ������ǿ
			
			public Object createProxy() {													// ���ɴ������ķ���
				
				ClassLoader classLoader = this.getClass().getClassLoader();
				Class[] interfaces = targetObject.getClass().getInterfaces();
				InvocationHandler invocationHandler = new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] params)
							throws Throwable {
						
						if(beforeAdvice != null) {
							beforeAdvice.before();
						}
						
						Object result = method.invoke(targetObject, params);				// ִ��Ŀ������Ŀ�귽��
						
						if(afterAdvice != null) {
							afterAdvice.after();
						}
						
						return result;
					}
				};
				
				return  Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
			}
			
			������																				//Ŀ��������ǿ��get/set����
			
			@Test
			public void fun() {
				
				ProxyFactory factory = new ProxyFactory();									// ��������
				factory.setTargetObject(new ManWaiter());									// ����Ŀ�����
				factory.setBeforeAdvice(new BeforeAdvice() {								// ����ǰ����ǿ
					public void before() {
						System.out.println("���ã�");
					}
				});
				
				factory.setAfterAdvice(new AfterAdvice() {									// ���ú�����ǿ
					public void after() {
						System.out.println("�ټ���");
					}
				});
				
				Waiter waiter = (Waiter)factory.createProxy();
				waiter.serve();																// ���ã� ������... �ټ���
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
				System.out.println("������...");
			}
		}

=============================================================================================================================================

����������԰�.class�ļ����ص�JVM�ķ������У����һ��Class����


��������ķ��ࣺ���������
				��չ����չjar��-\lib\ext
				ϵͳ��д����͵����jar����Ҳ����classpath��MyEclipse�ڱ���ʱ����ʱ����һ��calsspath


���������ί�л��ƣ������������أ����û�ҵ���������չ���أ������û�ҵ��������ϵͳ���أ�������û�ҵ�����ô�׳�ClassNotFoundException

					�����һ���౻���������ˣ���ô���е�����Ҳ�������أ���չ��ϵͳҲһ��

�Զ����������:��һ���౻����ʱ������ClassLoader����findLoadClass()�����ȵ�JVM�ķ������в鿴���Ƿ��ѱ����أ�����ѱ����أ�
			   �Ͳ��ظ����ز�ֱ�ӷ���Class�����������û�����ع����Ϳ�ʼ�����ϼ�ί�л��ƣ�getParent().loadClass();
			   ����÷�������null������Ҫ�Լ������ˣ���ʱloadClass()��������õ������findClass()������������
			   �����Զ��������������Ҫ�̳�ClassLoader����дfindClass()������


Tomcat�ṩ������������������������������${CATALINA_HOME}\lib�����������������������������������࣡
						  Ӧ�����������${CONTEXT_HOME}\WEB-INF\lib��classes��Ӧ��������������������������·���µ��࣡�ȼ���classes

	���� -> ��չ -> ϵͳ -> ������������� -> Ӧ���������			���������������Ӧ��������������ԣ��ȼ��أ���ί�У�

=============================================================================================================================================