<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>云POS报表平台--系统登录</title>
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
	href="<%=application.getInitParameter("baseUrl")%>/assets/admin/pages/css/login-soft.css?v=<%=application.getInitParameter("verno")%>"
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
<link rel="shortcut icon" href="favicon.ico" />
</head>
<body class="login">
	<!-- BEGIN LOGO -->
	<div class="logo"></div>
	<!-- END LOGO -->
	<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
	<div class="menu-toggler sidebar-toggler"></div>
	<!-- END SIDEBAR TOGGLER BUTTON -->
	<!-- BEGIN LOGIN -->
	<div class="content">
		<!-- BEGIN LOGIN FORM -->
		<form class="login-form" method="post">
			<h3 class="form-title">云POS报表平台</h3>
			<div class="alert alert-danger display-hide">
				<button class="close" data-close="alert"></button>
				<span> 云POS报表平台 </span>
			</div>
			<div class="form-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9">选择商户</label>
				<div class="input-icon">
					<input type="hidden" class="form-control select2m"
						name="companyCode" id="companyCode">
				</div>
			</div>
			<div class="form-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9">用户名</label>
				<div class="input-icon">
					<i class="fa fa-user"></i> <input
						class="form-control placeholder-no-fix" type="text"
						autocomplete="off" placeholder="用户名" name="username" id="username" />
				</div>
			</div>
			<div class="form-group" style="display: none;">
				<label class="control-label visible-ie8 visible-ie9">密码</label>
				<div class="input-icon">
					<i class="fa fa-lock"></i> <input
						class="form-control placeholder-no-fix" type="password"
						autocomplete="off" placeholder="用户密码" name="password"
						id="password" value="123456" />
				</div>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn blue pull-right">
					登录 <i class="m-icon-swapright m-icon-white"></i>
				</button>
			</div>
		</form>
	</div>
	<!-- END LOGIN -->
	<!-- BEGIN COPYRIGHT -->
	<div class="copyright"></div>
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