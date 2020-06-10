<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="container">
	<hr>
	<footer>
		<p>&copy; project 2020</p>
	</footer>
</div>


<spring:url value="/resources/core/js/hello.js" var="coreJs" />

<script src="${coreJs}"></script>


