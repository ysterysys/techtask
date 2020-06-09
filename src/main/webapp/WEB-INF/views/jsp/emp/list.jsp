<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>

<jsp:include page="../fragments/header.jsp" />

<body>

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

		<h1>Upload CSVs</h1>

		<spring:url value="/emp/savefile" var="userUploadActionUrl" />

		<form:form action="${userUploadActionUrl}" method="post"
			acceptcharset="UTF-8" enctype="multipart/form-data" id="form1">  
Select File: <input type="file" name="file" multiple/>
			</br>
			<input type="submit" value="Upload File" />
		</form:form>
	</div>



	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>