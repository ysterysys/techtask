<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<head>
<spring:url value="/resources/core/css/spinner.css" var="spinnerCss" />
<link href="${spinnerCss}" rel="stylesheet" />

<spring:url value="/resources/core/js/jquery.js" var="jqueryJs" />
<script src="${jqueryJs}"></script>
</head>
<jsp:include page="../fragments/header.jsp" />
<script>
	jQuery(document).ready(function($) {

		$("#upload").click(function() {
			$("div.spanner").addClass("show");
			$("div.overlay").addClass("show");
		});

	});
	var failure;
	var totalFileLength, totalUploaded, fileCount, filesUploaded;

	//Will be called when upload is completed
	function onUploadComplete(e) {
		if (e.currentTarget.status == 400) {
			failure++;
		}
		totalUploaded += document.getElementById('files').files[filesUploaded].size;
		filesUploaded++;

		if (filesUploaded < fileCount) {
			uploadNext();
		} else {

			$("div.spanner").removeClass("show");
			$("div.overlay").removeClass("show");

			if (failure > 0) {
				alert('Error , files uploaded with error will not be processed!');
			} else
				alert('Finished uploading file(s)');
		}
	}

	//Will be called when user select the files in file control
	function onFileSelect(e) {
		var files = e.target.files; // FileList object
		var output = [];
		fileCount = files.length;
		totalFileLength = 0;
		for (var i = 0; i < fileCount; i++) {
			var file = files[i];

			totalFileLength += file.size;
		}
		//	document.getElementById('selectedFiles').innerHTML = output.join('');

	}

	//This will continueously update the progress bar
	function onUploadProgress(e) {

		if (e.lengthComputable) {
			var percentComplete = parseInt((e.loaded + totalUploaded) * 100
					/ totalFileLength);

		} else {

		}
	}

	//the Ouchhh !! moments will be captured here
	function onUploadFailed(e) {
		alert("Error uploading file");
	}

	//Pick the next file in queue and upload it to remote server
	function uploadNext() {
		var xhr = new XMLHttpRequest();
		var fd = new FormData();
		var file = document.getElementById('files').files[filesUploaded];
		fd.append("multipartFile", file);
		xhr.upload.addEventListener("progress", onUploadProgress, false);
		xhr.addEventListener("load", onUploadComplete, false);
		xhr.addEventListener("error", onUploadFailed, false);
		xhr.open("POST", "emp/savefile");

		xhr.send(fd);
	}

	//Let's begin the upload process
	function startUpload() {
		$("div.spanner").addClass("show");
		$("div.overlay").addClass("show");
		failure = 0;
		totalUploaded = filesUploaded = 0;
		uploadNext();
	}

	//Event listeners for button clicks
	window.onload = function() {
		document.getElementById('files').addEventListener('change',
				onFileSelect, false);
		document.getElementById('uploadButton').addEventListener('click',
				startUpload, false);
	}
</script>
<body>

	<div class="wrapper"></div>
	<div class="overlay"></div>
	<div class="spanner">
		<div class="loader"></div>
		<p>Uploading file, please be patient.</p>
	</div>

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

			<input type="file" id="files" multiple style="margin-bottom: 20px" />

			<output id="selectedFiles"></output>
			<input id="uploadButton" type="button" value="Upload"
				style="margin-top: 20px" />


		</form:form>
	</div>



	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>