<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="com.project.form.service.EmployeeService"%>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="../fragments/header.jsp" />

<body>
	<script language="javascript">
	function confirmDelete(url){
		if (confirm("Please confirm delete")){
		post(url);
		}
		return false;
	}
</script>
	<div class="container">

		<c:if test="${not empty msg}">
			<div class="alert alert-${css} alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<strong>${msg}</strong>
			</div>
		</c:if>

		<h1>Dash Board</h1>
		</br>
	</div>
	<div class="container">


		<spring:url value="/emp/searchUserBySalary" var="userActionUrl" />

		<form:form class="form-horizontal" method="get"
			modelAttribute="userSearchForm" action="${userActionUrl}">


			<form:hidden path="offset" />

			<spring:bind path="minSalary">
				<label class="col-sm-2 control-label">Salary From:</label>
				<div class="col-sm-10">
					<form:input path="minSalary" type="text" class="form-control "
						id="minSalary" placeholder="minSalary" />

				</div>
			</spring:bind>
			</br>
			</br>
			</br>
			<spring:bind path="maxSalary">
				<label class="col-sm-2 control-label">Salary To:</label>
				<div class="col-sm-10">
					<form:input path="maxSalary" type="text" class="form-control "
						id="maxSalary" placeholder="maxSalary" />

				</div>
			</spring:bind>
			</br>
			</br>
			</br>
<%-- 			<spring:bind path="type"> --%>
<!-- 				<label class="col-sm-2 control-label">Column Name:</label> -->
<!-- 				<div class="col-sm-10"> -->
<%-- 					<form:input path="type" type="text" class="form-control " id="type" --%>
<%-- 						placeholder="type" /> --%>

<!-- 				</div> -->
<%-- 			</spring:bind> --%>


<spring:bind path="type">
				<label class="col-sm-2 control-label">Column</label>
				<div class="col-sm-10">

					<form:select class="form-control" path="type" id="type"
						placeholder="type">

						<form:options items="${columnList}" />

					</form:select>

				</div>
			</spring:bind>
			
			</br>
			</br>
			</br>
		


			<spring:bind path="orderBy">
				<label class="col-sm-2 control-label">Desc/Asc:</label>
				<div class="col-sm-10">

					<form:select class="form-control" path="orderBy" id="orderBy"
						placeholder="orderBy">

						<form:options items="${sortList}" />

					</form:select>

				</div>
			</spring:bind>
			<br>
			<br>
			<br>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn-lg btn-primary pull-right">Search</button>

				</div>
			</div>
		</form:form>


		<h1>Search Users Result</h1>

		<table class="table table-striped">
			<thead>
				<tr>
					<th>#ID</th>
					<th>Name</th>
					<th>Login</th>
					<th>Salary</th>
					<th>Action</th>
				</tr>
			</thead>

			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.id}</td>
					<td>${user.name}</td>
					<td>${user.login}</td>
					<td>${user.salary}</td>


					<td><spring:url value="/emp/${user.id}" var="viewUrl" /> <spring:url
							value="/emp/${user.id}/delete" var="deleteUrl" /> <spring:url
							value="/emp/${user.id}/update" var="updateUrl" />

						<button class="btn btn-info" onclick="location.href='${viewUrl}'"><span class="glyphicon glyphicon-zoom-in"></span> Query</button>
						<button class="btn btn-primary"
							onclick="location.href='${updateUrl}'"><span class="glyphicon glyphicon-pencil"></span> Update</button>
						<button class="btn btn-danger" onclick=confirmDelete('${deleteUrl}')><span class="glyphicon glyphicon-trash"></span> Delete</button></td>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div class="pagination">
			Page :
			<c:if test="${endpage != null }">
				<c:forEach begin="${startpage}" end="${endpage}" var="p">
					<a
						href="<c:url value="/emp/searchUserBySalaryByPage" >
         <c:param name="page" value="${p}"/>
         <c:param name="minSalary" value="${minSalary}"/>
         <c:param name="maxSalary" value="${maxSalary}"/>
         <c:param name="type" value="${type}"/>
         <c:param name="orderBy" value="${orderBy}"/>
         </c:url>">${p}
					</a>

				</c:forEach>
			</c:if>
			<%--  <c:forEach begin="${startpage}" end="${endpage}" var="p"> --%>
			<%--  <spring:url value="/searchUserBySalaryByPage/${p}" var="userPageUrl" /> --%>
			<%--  <button class="btn btn-primary" onclick="location.href='${userPageUrl}'"> ${p}</button> --%>
			<%--  </c:forEach>  --%>

		</div>
		<jsp:include page="../fragments/footer.jsp" />
	</div>
</body>
</html>