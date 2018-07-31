
		private int tp;
		private int tc;
		private int ps = 5;
		private int cp;
		private List<T> list = new ArrayList<T>();
		private Customer customer;
		private String	parameters = "CustomerServlet?method=list";

============================================================================================================================================
	
		PageBean<Customer> pb = new PageBean<Customer>();
		pb.setCp(Integer.parseInt(request.getParameter("cp")));
		pb.setCustomer(CommonUtils.toBean(request.getParameterMap(),Customer.class));
		request.setAttribute("pb",customerService.list(pb));
		return "f:/list.jsp";

============================================================================================================================================
		
		String s3 = "";
		String s4 = pb.getParameters();						
		List<Object> list = new ArrayList<Object>();
		
		String cname = pb.getCustomer().getCname();
		if(cname != null && !cname.trim().isEmpty()){
			s3 += " and cname like ?";
			list.add("%" + cname + "%");
			s4 += "&cname=" + cname;
		}
		
		String gender = pb.getCustomer().getGender();
		if(gender != null && !gender.trim().isEmpty()){
			s3 += " and gender=?";
			list.add(gender);
			s4 += "&gender=" + gender;
		}
		
		String cellphone = pb.getCustomer().getCellphone();
		if(cellphone != null && !cellphone.trim().isEmpty()){
			s3 += " and cellphone like ?";
			list.add("%" + cellphone + "%");
			s4 += "&cellphone=" + cellphone;
		}
		
		String email = pb.getCustomer().getEmail();
		if(email != null && !email.trim().isEmpty()){
			s3 += " and email like ?";
			list.add("%" + email + "%");
			s4 += "&email=" + email;
		}
		
		String s1 = "select count(*) from t_customer where 1=1" + s3;
		String s2 = "select * from t_customer where 1=1" + s3 + " limit ?,?";
		
		try {
			pb.setParameters(s4);
			pb.setTc(((Number)qr.query(s1,new ScalarHandler(),list.toArray())).intValue());
			list.add((pb.getCp() - 1) * pb.getPs());
			list.add(pb.getPs());
			pb.setList(qr.query(s2,new BeanListHandler<Customer>(Customer.class),list.toArray()));
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

============================================================================================================================================

		<body>

			<h3 align="center">客户列表</h3>
			<table border="1" width="70%" align="center">
				<tr>
					<th>客户姓名</th>
					<th>性别</th>
					<th>生日</th>
					<th>手机</th>
					<th>邮箱</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
				<c:forEach items="${requestScope.pb.list }" var="customer">
					<tr>
						<td>${customer.cname }</td>
						<td>${customer.gender }</td>
						<td>${customer.birthday }</td>
						<td>${customer.cellphone }</td>
						<td>${customer.email }</td>
						<td>${customer.description }</td>
						<td><a href="<c:url value='/CustomerServlet?method=edit&cid=${customer.cid }'/>">编辑</a>
							<a href="<c:url value='/CustomerServlet?method=delete&cid=${customer.cid }'/>">删除</a>
						</td>
					</tr>
				</c:forEach>
			</table>

			当前第${requestScope.pb.cp }/共${requestScope.pb.tp }
			<a href="<c:url value='${requestScope.pb.parameters }&cp=1'/>">[首页]</a>
			<c:if test="${requestScope.pb.cp != 1 }">
				<a href="<c:url value='${requestScope.pb.parameters }&cp=${requestScope.pb.cp - 1 }'/>">[上一页]</a>
			</c:if>

			<c:choose>
				<c:when test="${requestScope.pb.tp <= 10}">
					<c:set var="begin" value="1"></c:set>
					<c:set var="end" value="${requestScope.pb.tp}"></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="begin" value="${requestScope.pb.cp - 5}"></c:set>
					<c:set var="end" value="${requestScope.pb.cp + 4}"></c:set>
					<c:if test="${begin < 1}">
						<c:set var="begin" value="1"></c:set>
						<c:set var="end" value="10"></c:set>
					</c:if>
					<c:if test="${end >= requestScope.pb.tp}">
						<c:set var="begin" value="${requestScope.pb.tp - 9}"></c:set>
						<c:set var="end" value="${requestScope.pb.tp}"></c:set>
					</c:if>
				</c:otherwise>
			</c:choose>

			<c:forEach var="i" begin="${begin}" end="${end}">
				<c:choose>
					<c:when test="${i eq requestScope.pb.cp}">
						[${i}]
					</c:when>
					<c:otherwise>
						<a href="<c:url value='${requestScope.pb.parameters }&cp=${i}'/>">[${i}]</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<c:if test="${requestScope.pb.tp != requestScope.pb.cp}">
				<a href="<c:url value='${requestScope.pb.parameters}&cp=${requestScope.pb.cp + 1}'/>">[下一页]</a>
			</c:if>
			<a href="<c:url value='${requestScope.pb.parameters }&cp=${requestScope.pb.tp }'/>">[末页]</a>
			
		</body>

============================================================================================================================================