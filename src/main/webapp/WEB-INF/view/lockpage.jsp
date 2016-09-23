<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>云POS报表平台--系统提示</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta content="" name="description" />
<meta content="" name="author" />

<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/font-awesome/css/font-awesome.min.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/simple-line-icons/simple-line-icons.min.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap/css/bootstrap.min.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/uniform/css/uniform.default.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/select2/select2.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />


<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/admin/pages/css/lock2.css"
	rel="stylesheet" type="text/css" />

<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/css/components.css?v=<%=application.getInitParameter("verno")%>"
	id="style_components" rel="stylesheet" type="text/css" />
<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/css/plugins.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/css/layout.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<link id="style_color"
	href="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/css/themes/darkblue.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<link
	href="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/css/custom.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />

<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
	<div class="page-lock">

		<div class="page-body">
			<center>
				<span style="color: white;font-size: 16pt;">登录已超时,请返回业务系统重新进入</span>
			</center>
		</div>
	</div>
	<input id="baseUrl" type="hidden"
		value="<%=application.getInitParameter("baseUrl")%>" />
	<!--[if lt IE 9]>
<script src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/respond.min.js?v=<%=application.getInitParameter("verno")%>"></script>
<script src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/excanvas.min.js?v=<%=application.getInitParameter("verno")%>"></script> 
<![endif]-->
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-migrate.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap/js/bootstrap.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery.blockui.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/uniform/jquery.uniform.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery.cokie.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-validation/js/jquery.validate.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/backstretch/jquery.backstretch.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/select2/select2.min.js?v=<%=application.getInitParameter("verno")%>"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/scripts/metronic.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/scripts/layout.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/scripts/demo.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/admin/pages/scripts/login-soft.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
	<script>
		jQuery(document).ready(function() {
			Metronic.init(); // init metronic core components
			Layout.init(); // init current layout
			Login.init();
			Demo.init();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>