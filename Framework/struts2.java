Struts2����

	1.Ӧ����Web�㣬����Struts1��WebWork�Ļ����Ϸ�չ��ȫ�¿�ܣ��汾��2.3.24��Web����һ��������ܣ�SpringMVC

	2.���Խ�������⣺��Web�׶���Ҫд�ܶ�Servlet���ṩ��ͬ�Ĺ��ܣ�֮��ͨ��BaseServlet����ˣ�Strutsԭ����֮����
					  ��Struts2�з�װ�Ĺ������������������󣬸��ݲ�ͬ������ִ��ͬһ��Action���еĲ�ͬ����

=============================================================================================================================================

�����

	1.��������apps\struts2-blank.war\WEB-INF\lib����jar��

	2.����Action���struts2���������ļ���Servlet��web.xml�����ã���Action����struts2���������ļ�������
		
	3.��Web.xml�����ù�����

		<filter>
			<filter-name>struts2</filter-name>
			<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
		</filter>	
		<filter-mapping>
			<filter-name>struts2</filter-name>
			<url-pattern>/*</url-pattern>																						
		</filter-mapping>																								*/
		
		�������ڷ���������ʱ������ִ��init�������ڸù������У��÷�������Ҫ���˼��������ļ��Ĳ����������Լ������ĺ�struts2�Դ���

=============================================================================================================================================

Struts2�Ļ���ִ�й���

	ͨ����������ȡ����·�� -> ʹ��dom4j����struts.xml�е����ݣ�ͨ������·���ҵ���Ӧ��action��ǩ

 -> ͨ��action��ǩ�е�classֵ��ͨ��������÷����õ�����ֵ -> ��ͨ������ֵ��struts.xml���ҵ���Ӧ�ĵ�result��ǩ -> ��Դ��ת

=============================================================================================================================================

Struts2���������ļ�

	<?xml version="1.0" encoding="UTF-8"?>									
	<!DOCTYPE struts PUBLIC								
		"-//Apache Software Foundation//DTD Struts Configuration 2.3//EN"
		"http://struts.apache.org/dtds/struts-2.3.dtd">
	<struts>
		<package name="����" extends="struts-default" namespace="����·����һ����">												
			<action name="����·���ڶ�����" class="��·��" method="Ĭ��ִ�еķ��������ƣ�Ĭ��ֵ��execute">
				<result name="ƥ�䷽���ķ���ֵ">��ת��Դ�ķ���·��</result>
			</action>
		</package>
	</struts>

	1.λ����src�£�����Ϊstruts.xml��ʹ�õ���dtdԼ��

	2.package��ǩ�����ڴ����еİ������ֲ�ͬ��action��extends = "struts-default" д���������֮�������action����

	3.namespace������ֵ��action��name����ֵ���ɷ���·����Ĭ����"/"����ʹ�ó��������������Դʱ.action����ʡ��

	4.���package��ǩ��name����ֵ�����ظ���һ��package��ǩ�ж��action��ǩ��name����ֵҲ�����ظ�

	5.result��ǩ�е�name����ֵ����ƥ���Ӧ��Action��ķ�������ֵ��ƥ��ʧ�ܷ���404������һ��type������������ת�����ض���
	  type����ֵ�У�1.Ĭ��ֵdispatcherת�� 2.redirect   
					�����Action�࣬��������ֵ����ת��ԴΪAction���Ӧ��action��ǩ��name����ֵ��3.redirectAction 4.chain ת�������ڻ�������
	  Action���еķ�������ֵ������String���ͣ���Ҳ����û�з���ֵ(void��return "none")����ʱ�Ͳ���Ҫ����result��ǩ��

	6.������ǩ��package��ͬ����ǩ<constant>��ǩ
				���磺<constant name = "struts.i18n.encoding" value="UTF-8"></constant> ������action��ȡ������ʱ����post��������ݱ�������
				���ó����������ַ�ʽ��1.��src�´���struts.properties��������    2.��web.xml��ͨ����ʼ��������������				        ****

=============================================================================================================================================

Action�����ֱ�д��ʽ

	1.������ͨ�࣬��ʵ�ֽӿںͼ̳�

	2.�����࣬ʵ��Action�ӿڣ��ӿ����г���

	3.�����࣬�̳�ActionSupport������ʵ����Action�ӿ�

=============================================================================================================================================

����һ��Action���в�ͬ���������ַ�ʽ

	1.д���action��ǩ����ͬһ��Action�࣬ÿ����ǩ��ͬ��name����ֵ��Ӧ��ͬmethod����ֵ

	2.��action��ǩ��name����ֵ��ʹ��ͨ�������method������{1}��ȡͨ��������ݣ�ͨ�������д���������class��ʹ��{1}��method��ʹ��{2}

	3.��̬���ʣ����ó���<constant name = "struts.enable.DynamicMethodInvocation" value = "true"></constant>������ʱ��·�������"!������"
						
=============================================================================================================================================
			
���ҳ������

	1.ȫ�ֽ��ҳ�棺�����ڶ��Action��ķ�������ֵ��ͬ����Ӧ��result��ǩָ������תҳ��Ҳ��ͬ�����
					��result��ǩ������action��ͬ��<global-results>��ǩ�У��ñ�ǩֻ������package��������Ч
					�ñ�ǩ����д��action��ǩǰ�棬��Ϊdom4j����xml�ļ��Ǵ��ϵ��½�����
	
	2.�ֲ����ҳ�棺������ͨ��result��ǩ���ã�������ȫ�ֽ��ҳ��

=============================================================================================================================================

Action��ȡ������

	1.ActionContext context = ActionContext.getContext();Map<String,Object> map = context.getParameters();//�������Objectʱ��Ҫ���Object[]
																									
	2.ServletActionContext���о�̬������getRequest��getResponse��getServletContext��getPageContext

	3.�ӿ�ע�룺ʵ��ServletRequestAware��ServletResponseAware�ӿڣ�ʵ�ֵķ�����������HttpServletRequest��HttpServletResponse����

=============================================================================================================================================

��װ������

	1.���Է�װ����Action�ж���������ݶ�Ӧ�ĳ�Ա�������ṩ��set���������ܷ�װ����

	2.ģ��������װ����������ʵ�������Զ�Ӧ��Action��ʵ��ModelDriven<T>�ӿڣ�ָ��TΪʵ�������ͣ�����ʵ�����Ա������ʵ�ֵķ����з���

	3.���ʽ��װ����Action��������ʵ���ಢ�ṩ��get/set�������������������name����ֵ����Ϊ��user.username

	4.ע�⣬���ͬʱʹ��ģ��������װ�����Է�װ��ֻ��ִ��ģ��������װ��ģ������ֻ�ܷ�װһ��ʵ���࣬�����ʽ���Է�װ���

	5.��װ���ݵ�List����Action������List<ʵ����>���ṩget/set�����������������name����ֵ����Ϊ���ʽ��ʽ��list[�±�].ʵ��������

	6.��װ���ݵ�Map����Action������Map<String,ʵ����>���ṩget/set�����������������name����ֵ����Ϊ���ʽ��ʽ��map['keyֵ'].ʵ��������

=============================================================================================================================================

OGNL����

	1.��Struts2Ĭ�ϵı��ʽ���ԣ���EL���ʽ���ܸ���ǿ�󡣵���Ҫ��������Struts2��ǩ��ͬʹ��������ֵջ����

	2.��Ȼognl������Struts2һ��ʹ�ã�����������Struts2��һ���֣����Լ���jar����ognl

	3.���Ű���

		1.��jsp������ognl��ǩ�⣺<%@taglib prefix="s" uri="/struts-tags"%>

		2.ognl֧�ֶ��󷽷��ĵ��ã�<s:property value="'liusijie'.length()"/>�������ǩ�Ὣ���ݽ�������ҳ�棬Ҳ����������ȡֵջ�е�����

=============================================================================================================================================

ֵջ

	1.ֵջ��Struts2�����ṩ��һ�ִ洢���ƣ�����������󣬺���ȳ�

	2.Servlet�ǵ�ʵ�������ڵ�һ�α�����ʱ��������Action�Ƕ�ʵ��������ÿ�α�����ʱ���ᱻ����
											   ÿ��Action��������ֻ��һ��ֵջ�������ֵջ��������Action������
	
	3.��ȡֵջ����ķ�ʽ��ActionContext context = ActionContext.getContext();    ValueStack stack = context.getValueStack();

	4.ֵջ�ڲ��ṹ��������Ҫ����

		1.root���ṹ��list���ϣ�һ�㶼�ǲ������root

		2.context���ṹ��map���ϣ��洢���Ƕ�������ã�key�̶���request��session��application��parameters��attr��
		           �洢����request�������á�HttpSession�������á� ServletContext�������á����ݵ���ز�����attr��Ҫ����ȡ�Ĳ���			****

		3.<s:debug>��ǩ������һ�������ӣ��ɲ鿴ֵջ���ڲ��ṹ�ʹ洢ֵ��������root���֣�������context����

=============================================================================================================================================

��ֵջ�з�����

	1.����ֵջ�����set�������������һ��map����

	2.����ֵջ�����push�������������һ������

	3.��Action�������������ṩ��get�������ڷ����и�ֵ���������һ�����󣬴���Action�������У���ʡ�ڴ�ռ�

=============================================================================================================================================

��ֵջ�л�ȡ����

	1.<s:property value="����.����ֵ"/>

	2.<s:iterator value="list����">		//�����ǩ������<c:foreach> ����û��ʹ����ʱ����
	      <s:property value="���������ֵ"/>
	      ������
	  </s:iterator>													
 
	3.<s:iterator value="list����" var="keyֵ(��ʱ����)">	��ʱ�����ᱻ�ŵ�context��map�����У�������root�ռ��˷ѣ������Ч��
	      <s:property value="#keyֵ(��ʱ����).����ֵ"/>		��ȡcontext�е�������Ҫ��#���������ȡrequest�����е����ݾ���Ҫ��#��
		  ������												
	  </s:iterator>											

	4.��ֵջ������ݵ����ݻᱻ����top�����У�push�������������û������ֻ��ֵ����Ҫ������ȡ:<s:property value="[�±�].top"/>������ӵ���ǰ

	5.ʹ��EL���ʽҲ���Ի�ȡֵջ���ݣ��ȴ�������л�ȡֵ���ҵ�ֱ�ӷ��أ�û�ҵ��͵�ֵջ��ȥ�ң��ҵ�����ӵ��������Ȼ�󷵻أ��������ܺܵ�

=============================================================================================================================================

Struts2������

	1.Struts2��װ�ĺܶ๦�ܶ������������棬�кܶ����������������ÿ����Щ��������ִ�У�ִ�е���Ĭ�ϵ�������

	2.Ĭ���������� core jar���е� struts-default.xml �е� <interceptor-stack name = "defaultStack"> ��ǩ�п����ҵ�

	3.�ײ�ԭ��

		1.aop˼�룺��������Action���󴴽�֮��ִ�з���֮ǰ�Զ�ִ�У�ͨ�������ļ�ʵ��(���޸�Դ������ǿ����)��������Ҫִ�ж�Ӧ��������
		
		2.������ģʽ�������ģʽ��һ�֣�������������ƣ��������������ͬһ������ʱ����Ҫ���ι���
	
	4.�����������Ͽ��Թ����������ݣ���������ֻ������Action

=============================================================================================================================================

�Զ���������

	1.�������ṹ���̳�AbstractInterceptor�࣬����ʵ����Interceptor�ӿڣ��ýӿ�����������

		1. void init()     2.void destroy()     3.String intercept(ActionInvocation invocation)

	2.�Զ���������ʱ����̳� MethodFilterInterceptor �࣬������action����ĳ���ķ�������������ʱ����Ҫʹ�÷��䣬ֻ��Ҫ��������

		�̳�����������дһ��������String doInterception(ActionInvocation invocation)�����о�return invocation.invoke();
									���������Action�ķ���һ���������ŷ���ֵ����Ӧ��action��ǩ��result��ǩ��ȥ��ƥ��ֵ������ת

	3.ͨ�������ļ����ý���Action���Զ����������Ĺ�ϵ��Ĭ������Action�е����з������Ҳ���ִ��Ĭ��������

		package��ǩ�£�

			<interceptors>
			
				<interceptor name="loginintercept" class="cn.itcast.web.interceptor.LoginInterceptor">		//����������
					<param name="excludeMethods">login</param>												//�����ò����صķ���
				</interceptor>	
				
				<interceptor-stack name="myStack">	//�Զ���������ջ�������Ϳ������ض��Action������������Ҫ��Action�е���ʹ��������						
					<interceptor-ref name="defaultStack"></interceptor-ref>			//�����Ĭ������������Ȼ�䲻��ִ��
					<interceptor-ref name="loginintercept"></interceptor-ref>
				</interceptor-stack>
					
			</interceptors>
			
			<default-interceptor-ref name="myStack"></default-interceptor-ref>		//�����������ջ����ΪĬ�ϵ�������ջ

		����ʹ�������������������Բ�������������ʱ���ò����صķ�����������ʹ��ʱ���壬��Ϊÿ��Action�еĲ����ط������ܲ�ͬ

			<interceptor-ref name="loginintercept">
				<param name="excludeMethods">��������������ö��Ÿ�������</param>
			</interceptor-ref>
			<interceptor-ref name="defaultStack"></interceptor-ref>

=============================================================================================================================================

Struts2�ı���ǩ
	
	Struts2�ı���ǩ�е����ݻ��Զ���ӱ��(�Զ�����)������������ָ�ʽ������ʹ��<s:form>��ǩ�е����ԣ�theme = "simple"

	��Struts2�еı���ʹ��ognl���ʽ��������%{}�����ʽ���������磺<s:textfield name="username" value="%{#request.req}"></s:textfield>
	
	<s:form>
		<s:textfield label = "�������ֵ����ʾ�������ǰ�棬���Զ���':'" name = "username"></s:textfield>
		<s:password></s:password>
		<s:radio list="{'Ů','��'}"></s:radio>//����д����ʾ������valueֵ��ͬ
		<s:radio list="#{'nv':'Ů','nan':'��'}"></s:radio>//����д����ʾ�У�valueֵΪnan
		<s:checkboxlist list="{'�Է�','˯��','�ô���'}"></s:checkboxlist>//��ʾ���ݺ�valueֵ��ͬ
		<s:select list="{'�Է�','˯��','�ô���'}"></s:select>//��ʾ���ݺ�valueֵ��ͬ
		<s:file></s:file>
		<s:hidden></s:hidden>
		<s:textarea rows = "" cols = ""></s:textarea>
		<s:submit></s:submit>
		<s:reset></s:reset>
	</s:form>															//�����ǩû��nameֵ�ᱨ��

=============================================================================================================================================

��ģ�鿪��

	������˹�ͬ����һ����Ŀʱ��Ҫ����ͬһ��xml�ļ������⣬���������Լ�������ǲ����ļ���<include file="�ļ�·��"></include>