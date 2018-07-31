	
JavaEE����ܹ��ع�

	��ͼ�� - web��	   - ʹ��struts2���
	ҵ��� - service�� - ʹ��spring���
	�־ò� - dao��	   - ʹ��hibernate���

	MVC˼�룬ģ����ͼ������������Java���еķֲ�˼��

==============================================================================================================================================

Hibrenate����

	��ܣ�����ܹ�������ʵ��һ���ֹ��ܣ������ʹ������дһ���ִ���Ϳ���ʵ��һЩ����

	1.��Դ��������ܣ�Ӧ����dao�㣬�汾��hibernate5.x

	2.ʹ��orm˼�루object relational mapping �����ϵӳ�䣩�����ݿ����crud����
		
			1.ʹ�������ļ���ʵ�����������ݿ���Ӧ��ʵ�������Ժͱ��ֶζ�Ӧ//javabean����ȷ�Ľз�Ϊʵ����
			2.����Ҫֱ�Ӳ������ݿ�����ǲ������Ӧ��ʵ�������

	3.�ײ�������jdbc������������˷�װ��������Ҫдjdbc�����sql���
	
============================================================================================================================================

�hibernate����

	1.������lib\required ��jar����lib\jpa ��jar������־jar��

	2.����ʵ���࣬hibernateҪ��ʵ��������һ������Ψһ��ֵ����Ӧ���е�������hibernate���Դ������������ݿ���Ҫ�ֶ�����

	3.ʹ�������ļ�ʵ��ʵ���������ݿ���ӳ���ϵ

		1.����ӳ�������ļ����ļ����ƺ�λ��û�й̶�Ҫ�󣬵�һ�㽨����ʵ�������һ������Ϊ ʵ������.hbm.xml

		2.����dtdԼ��������ӳ���ϵ
															
			public class User {						
				private int uid;					
				private String username;				
				private String password;				
				private String address;				
				set/get������������
			}		
					
			<?xml version="1.0" encoding="UTF-8"?>
			<!DOCTYPE hibernate-mapping PUBLIC
			"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
			"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
			<hibernate-mapping>
				<class name="cn.itcast.domin.user.User" table="t_user">		//��·�������
					<id name="uid" column="uid">							//�������������ֶ����ƣ����ʡ��column����nameֵ��ͬ
						<generator class="native"></generator>				//������������
					</id>
					<property name="username" column="username"></property>	//���Ժ��ֶζ�Ӧ��type����ָ���ֶ����ͣ�ʡ��Ĭ�϶�Ӧ��������
					<property name="password" column="password"></property>
					<property name="address" column="address"></property>		
				</class>														
			</hibernate-mapping>


	4.����hibernate���������ļ���Ҳ��ʹ��dtdԼ�����������ʹ��hibernate��ܣ���ô���ļ�������srcĿ¼�£����Ʊ���Ϊhibernate.cfg.xml

		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE hibernate-configuration PUBLIC
			"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
			"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
		<hibernate-configuration>
			<session-factory>
				//��һ���֣��������ݿ����Ϣ
				<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
				<property name="hibernate.connection.url">jdbc:mysql:///mydb</property>						//localhost:3306����ʡ��
				<property name="hibernate.connection.username">root</property>
				<property name="hibernate.connection.password">123</property>
				//�ڶ����֣�����hibernate��Ϣ���ⲿ���ǿ�ѡ��
				<property name="hibernate.show_sql">true</property>											//����̨�Ƿ����SQL���		
				<property name="hibernate.format_sql">true</property>										//�Ƿ��ʽ��SQL���
				<property name="hibernate.hbm2ddl.auto">update</property>									//�б���£��ޱ��Զ�����
				<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>			//��hibernateʶ�����ݿⷽ��
				//�������֣�����ӳ�������ļ���hibernate���������У�ֻ����غ��������ļ���������Ҫ�������������ļ�
				<mapping resource="cn/itcast/domin/user/User.hbm.xml"/>						
			</session-factory>
		</hibernate-configuration>			

============================================================================================================================================

Hibernate����API

	1.Configuration

		��ʵ����ʹ��hibernateҲ����ָ�����������ļ���λ�õģ���ʱ����Ҫ�ֶ�ȥ����ӳ���ļ������ҿ����ڴ���������ӳ�������ļ������������ļ���
		
			Configuration config = new Configuration().configure("/config/hibernate.cfg.xml");
			configuration.addResource("cn/itcast/domain/Customer.hbm.xml");

	2.SessionFactory

		�����ö���Ĺ��̻ᴴ�����ܺ���Դ�����Խ���һ����Ŀֻ��һ���ö���ͨ��������ľ�̬������������ʱ����������ֻ����һ��

	3.Session

		������̶߳���(ֻ���Լ�ʹ��)������������jdbc�е�Connection�������䷽������ʵ��crud�������ײ����ThreadLocal

			1.��ӣ�save(����)����id�޹أ�id�ɱ��Զ����ɣ�����ö����ǲ�ѯ�����Ķ�����ô���ø÷���ִ�е����޸Ĳ���
								�÷�������һ��Serializableֵ�������ɵ�idֵ����ֵΪnull˵�����ʧ��										****								

			2.�޸ģ�update(����)�����ݶ����idֵ��ѯ�����������޸ģ��Ὣ��������з�id������ֵ�ڶ�Ӧ�ı��ֶ��н����޸�

			3.ɾ����delete(����)�����ݶ����idֵ��ѯ�������ɾ����Ӧ�ı��¼��ɾ��������������Ȼ����
							      ע�⣺����ǲ�ѯ�����Ķ��󣬾����޸���idֵ��ɾ����Ҳ����ԭid��Ӧ�ı��¼

			4.��ѯ��get(ʵ����Class,idֵ)�����ض���

			5.saveOrUpdate()������ʵ����ӣ���id����Ҳ��ʵ���޸ģ���id��

		Ϊ�˱�֤Session�ǵ��̶߳��󣬿��Խ�Session�뱾���̰߳���ʵ��
		
			1.�ں��������ļ���������� <property name="hibernate.current_session_context_class">thread</property>
		
			2.ͨ��SessionFactory��getCurrentSession()������ȡ

			3.��ʱ�Ͳ���Ҫ�ֶ��ر�����뱾���̰߳󶨵�Session��

	4.Transaction

		���������beginTransaction��commit��rollback�������������ĸ����ԣ�ԭ���ԡ�һ���ԡ������ߡ��־���
		��hibernate���������ļ���Ҳ�ǿ�����������ĸ��뼶���

============================================================================================================================================

������

	public class HibernateUtil {

		private static SessionFactory sessionFactory;
		
		static{
			try {
				Configuration configuration=new Configuration();
				configuration.configure();
				sessionFactory=configuration.buildSessionFactory();
			} catch (ExceptionInInitializerError e) {
				throw new ExceptionInInitializerError("�����ļ�����");
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

��������								

		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction transaction = null;
		try{
			Configuration configuration=new Configuration();
			configuration.configure();								// 1.����hibernate���������ļ���Configuration������
			sessionFactory = configuration.buildSessionFactory();	// 2.��ȡ�ļ�������SessionFactory����������̿��Դ�����
			session = sessionFactory.openSession();					// 3.����Session����
			transaction = session.beginTransaction();				// 4.��������hibernateҪ���ֶ���������
			������������													// 5.crud����
			transaction.commit();									// 6.�ύ����
		}catch(Exception e){
			transaction.rollback();
			e.printStackTrace();									// 7.�ع�����
		}finally{
			if(session != null)session.close();
			if(sessionFactory != null)sessionFactory.close();		// 8.�ر���Դ����Web��Ŀ�У�SessionFactory�ǲ���Ҫ�رյ�
		}

============================================================================================================================================

ʵ����ı�д����

	1.����˽�в��ṩget/set����

	2.��������ΪΨһֵ����Ӧ���ݿ���������һ��Ϊid

	3.���Խ���ʹ�ð�װ�࣬��Ϊ��һ��null

============================================================================================================================================

ʵ��������״̬

	1.˲ʱ̬������û��idֵ��Ҳ��Session����û�й���

	2.�й�̬��������idֵ������Session����û�й���

	3.�־�̬��������idֵ��ҲȥSession�����й���

============================================================================================================================================

������������

	1.increment��ÿ������1��������long��int��short����

	2.identity�����������ݿ�֧������������������

	3.sequence�����������ݿ�֧������

	4.native�����ݱ������ݿ��Զ�ѡ��������������

	5.uuid���Զ�����uuid�ַ���ֵ

============================================================================================================================================

����ϵ

	1.һ�Զ�ӳ������
		
		1.��������ʵ���࣬�����Կͻ�����ϵ��Ϊ�����ͻ�Ϊһ�����෽����һ��

		2.��ʵ����֮�以��������ڿͻ��и���Set<LinkMan> setLinkMan��Ա���Բ���ֵ���ṩget/set������ע��Ҫʹ��Set����
								����ϵ���и���Customer customer��Ա���Ժ�get/set����

		3.��������ӳ�������ļ�������һ�Զ��ϵ

			Customer.hbm.xml��  <set name="setLinkMan">
									<key column="�⽡����"></key>
									<one-to-many class="�෽����·��"/>
								</set>
																		
			LinkMan.hbm.xml��	<many-to-one name="customer" class="һ������·��" column="�������Ӧ"></many-to-one>
		

		4.�ں��������ļ�������������ӳ�������ļ�
		
	2.һ�Զ༶������

		1.�������棺���һ���ͻ�ʱ��Ϊ����Ӷ����ϵ��

			�����ͻ�����ϵ�ˣ��ڿͻ��������ϵ�ˣ������Ҫ���棬���ڿͻ���Set��ǩ��������� cascade="save-update"����ֻ�豣��ͻ���

		2.����ɾ�����ڿͻ���Set��ǩ�е�cascade�������deleteֵ��cascade="save-update,delete"��ɾ����ѯ�����ͻ���������ϵ��Ҳ�ᱻɾ��
					���̣���ѯ�ͻ� -> ���ݿͻ�id����ϵ�������ѯ��ϵ�� -> �Ѳ�ѯ������ϵ�˵��������ΪNULL -> ɾ����ϵ�� -> ɾ���ͻ�
					������� cascade="delete" ��ô��ֻ��ɾ���ͻ����������ϵ�������ΪNULL����
					�������ͨ����ѯ�õ��Ŀͻ�������������id�ж�Ӧ����ϵ��ƥ�䣬ɾ���ÿͻ��������ϵ��Ҳ���ᱻɾ����ֻ�������ΪNULL

		3.�޸Ĳ���������id��ѯ���ͻ�����ϵ�ˣ�һ�����ó�Ա���ɡ�

	3.��ʵ����ʵ����֮��Ĺ�ϵ�ǲ���Ҫ���˶��������õģ����Ǹ�������������ֻ��Ҫͨ���෽��ѯһ������ôһ���Ͳ���Ҫ����ӳ���ϵ��

================================================================================

	3.��Զ�ӳ������

		1.����ʵ���࣬�������û��ͽ�ɫΪ��

		2.��ʵ����֮�以����������û��ͽ�ɫ�ж�����Set��Ա���Բ���ֵ���ṩget/set����

		3.��������ӳ���ļ������ö�Զ��ϵ
													  
			User.hbm.xml��  <set name="setRole" table="�����ű������">
								<key column="�ڵ����ű��е��������"></key>
								<many-to-many class="������Role" column="roleid"></many-to-many>
							</set>				 //��ɫ���ȫ·��  ��ɫ�ڵ����ű��е����
																	
			Role.hbm.xml��	<set name="setUser" table="���϶�Ӧ">
								<key column="���϶�Ӧ"></key>
								<many-to-many class="������User" column="userid"></many-to-many>
							</set>		

		4.�ں��������ļ�����������ӳ�������ļ�

	4.��Զ༶������

		1.�������棺���û���Set��ǩ��������� cascade="save-update"���ڴ�����ֻ������һ���ĳ�Ա����ֻ�豣�漴��

		2.����ɾ�������û���Set��ǩ�е�cascade�������deleteֵ���ڴ�����ֻ��ɾ���û����󣬹����Ľ�ɫ����Ҳ�ᱻɾ����
				    ����ע�⣡�������һ����ɫ���ܻ����������û���Ҳ�ᱻɾ����
					
	5.ά�������ű�

		1.�û��ͽ�ɫ��Ĺ�ϵ��ͨ�������ű���ά����

			1.���û�(���ⷽ)��ӽ�ɫ:session.get(User.class,n).getSetRole().add(session.get(Role.class,n));�ٱ����û�

			2.���û�(���ⷽ)�Ƴ���ɫ��session.get(User.class,n).getSetRole().remove(session.get(Role.class,n));�ٱ����û�

============================================================================================================================================

��ѯ����

		1.Query��		Query query=session.createQuery("hql���");
						List<User> list=query.list();								

		2.Criteria��	Criteria criteria=session.createCriteria(ʵ����Class);		//����Ҫд��䣬ֱ�ӵ��÷���
						List<User> list=criteria.list();

		3.SQLQuery��	SQLQuery sqlQuery=session.createSQLQuery("��ͨsql���");
						List<Object[]> list=sqlQuery.list();						//���ص�List����ÿ���ֶ�������

						sqlQuery.addEntity(ʵ����Class);							//��Ҫ���ض�����Ҫ�ֶ����ʵ����
						List<User> list=sqlQuery.list();

============================================================================================================================================

Hibernate�Ĳ�ѯ��ʽ

	1.���󵼺���ѯ�������ڲ�ѯĳ���ͻ��е�������ϵ�ˣ�ͨ���õ���ѯ�û���Set������ʵ��

	2.OID��ѯ������get����

	3.hql��ѯ
			
		1.ʹ��Query��hibernate�ṩ��hql��ѯ����(hibernate query language)��������ͨsql������ƣ�����������ʵ������������

		2.���õ�hql���

			1.��ѯ���У�Query query=session.createQuery("from "); -> List<User> list=query.list();

			2.������ѯ��from ʵ���� where ���� = ? and ���� like ?	->	query.setParameter(�±�,ֵ);

			3.�����ѯ��order by ���� asc/desc��ascΪĬ�Ͽɲ�д
										
			4.��ҳ��ѯ��query.setFirstResult(��ʼλ��);	query.setMaxResults(��¼��);	//�±��0��ʼ

			5.ͶӰ��ѯ�����ǲ�ѯָ���У�select ���� from ʵ���࣬��������Object�������ѯ����Object���飬select���治֧��д"*"

			6.�ۼ�������count(Long)��sum(Long)��avg(Double)��max(Integer)��min��д��select�� -> Object object = query.uniqueResult();

	4.QBC��ѯ

		1.ʹ��Criteria������Ҫд��ѯ��䣬ֱ��ͨ������ʵ�ֲ�ѯ��Ҳ�ǲ���ʵ������������

		3.qbc��ѯ

			1.��ѯ���У�Criteria criteria=session.createCriteria(Class); -> List<User> list=criteria.list();

			2.������ѯ����Criteria�����add����������������ֵ�������д���Restrictions���ƥ�䷽��
						
				Restrictions.eq("��������",ֵ);	 =��	allEq(Map<String,?>)	ƥ��������ֵ				
							 gt	>�� ge >=�� lt <�� le<=�� between�� like�� in��������������
 
			3.�����ѯ����Criteria�����addOrder������ʹ��Order�������������asc��desc�������������д�����������

			4.��ҳ��ѯ��setFirstResult(��ʼλ��); setMaxResults(��¼��);

			5.ͳ�Ʋ�ѯ����Criteria�����setProjection�����д���Projections��ķ�����ʹ��Criteria�����uniqueResult�����õ�Object����

			6.���߲�ѯ������dao��Ҳ��ƴ�ӳ���ѯ����																						****

				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Class);		//����Ҫʹ��session
				Criteria criteria = detachedCriteria.getExecutableCriteria(Session����);	//ִ�в���Ҫsession

	5.����sql��ѯ��ʹ��SQLQuery����ʹ����ͨsql����ѯ

============================================================================================================================================

HQL����ѯ
	
	1.������	Query query = session.createQuery("from Customer c inner join c.setLinkMan");
				List list = query.list();    //list����ÿ���ֶ���Object������ʽ

	2.�������ӣ�left outer join��������ʽ

	3.��������

	4.���������ӣ��������ӵײ�ʵ��һ������join�����fetch��������ʽ
		
	5.�����������ӣ�������ʽ��û��������������

============================================================================================================================================

Hibernate����

	���棺���ݿⱾ�����ļ�ϵͳ��ͨ�����������ļ�Ч�ʵͣ������ݴ����ڴ��п���ֱ�Ӷ�ȡ���ݣ�Ч�������

	hibernate������Ǹÿ���ṩ�Ż���ʽ��һ��

		hibernateһ�����棺1.Ĭ�ϴ�

						   2.ʹ�÷�Χ�ǴӴ���Session���󵽹رն�����Դ

						   3.�洢�����ݱ����ǳ־�̬���ݣ��洢����ֵ�����Ƕ���

						   4.ִ�й��̣�Session���󱻴����󣬻���һ��һ�����棬��ѯ����ʱ���ȵ�һ�������в�ѯ�����û�У��ٵ����ݿ��ѯ��
									   ��ѯ�������ݻᱻ�ŵ�һ�������У������һ�������в�ѯ���������Щֵ��װ��һ���µĶ��󷵻�
									   ���β�ѯͬһ�����ݣ�ֻ���ڵ�һ�β�ѯʱ�����ѯ��䣬��Ϊ��ѯ�������ݿ�

						   5.�־�̬�Զ��������ݿ⣬�޸ĳ־�̬���������ֵ����ִ���޸Ĳ��������ύ����ʱ���Զ�ִ��
		
								ԭ���־�̬�������ݻ���󣬻��ڿ�����(����)Ҳ��һ�ݣ��޸����ݺ󣬻����е����ݻ��޸ģ����������е����ݲ���
									  ������ύ����ʱ������������ݽ��бȽϣ�ֻҪ����ͬ���Զ�ִ�и��²���


		hibernate�������棺1.Ĭ�Ϲرգ���Ҫ���ÿ���
						   2.ʹ�÷�Χ�ǴӴ���SessionFactory���󵽹رն�����Դ
						   3.Ŀǰ�Ѿ���redis�������

============================================================================================================================================

Hibernate��������

	1.������ѯ��get����һ�����þͷ��Ͳ�ѯ���

	2.�ӳٲ�ѯ��load���������������Ͳ�ѯ��䣬���صĶ�������ֻ��idֵ��ֻ�е��õ��������������ʱ���Żᷢ�Ͳ�ѯ����ѯ

		1.�༶���ӳ٣�����id��ѯ����ʵ������󣬵���load�����������Ϸ������

		2.���������ӳ٣���ѯĳ���ͻ���������ϵ�˵Ĺ����Ƿ���Ҫ�ӳ٣�������̾ͳ�Ϊ���������ӳ�

			Ĭ���ڵõ��ͻ��ĳ�Ա����ʱ����������䣬��Ҫ���ϵ�����ʱ�ŷ������
			�����ڿͻ���Set��ǩ����fetch��lazy�������ı䣬����������Ĭ��ֵ��select��true����Ҫͨ���޸�lazy�������ı��ӳ�

				lazy���ԣ�1.false�����ӳ٣�һ����ֱ�ӷ��������
						  2.true�� �ӳ٣�Ĭ��
						  3.extra�������ӳ٣�����ֵΪtrueʱ���ƣ����Ƿ��͵Ĳ�ѯ���������Բ�ѯ����Ҫʲô��ʲô

============================================================================================================================================

����ץȡ

	��ѯ���пͻ���������ϵ����Ҫʹ������ѭ�������ͺܶ���䣬Ч�ʵͣ�
	�ڿͻ���Set��ǩ������batch-size���ԣ�����ֵΪ�������У�ֵԽ���͵����Խ�٣�Ч��Խ��