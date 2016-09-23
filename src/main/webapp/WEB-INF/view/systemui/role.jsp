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
<title>云POS报表</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta content="" name="description" />
<meta content="" name="author" />
<!-- BEGIN GLOBAL MANDATORY STYLES -->
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
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css?v=<%=application.getInitParameter("verno")%>"
	rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-select/bootstrap-select.min.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/select2/select2.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-multi-select/css/multi-select.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/clockface/css/clockface.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-datepicker/css/datepicker3.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-timepicker/css/bootstrap-timepicker.min.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-colorpicker/css/colorpicker.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css?v=<%=application.getInitParameter("verno")%>" />
<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css?v=<%=application.getInitParameter("verno")%>" />
<!-- END PAGE LEVEL STYLES -->
<!-- BEGIN THEME STYLES -->

<link rel="stylesheet" type="text/css"
	href="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jstree/dist/themes/default/style.min.css" />
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
<!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
<!-- DOC: Apply "page-sidebar-closed" class to the body and "page-sidebar-menu-closed" class to the sidebar menu element to hide the sidebar by default -->
<!-- DOC: Apply "page-sidebar-hide" class to the body to make the sidebar completely hidden on toggle -->
<!-- DOC: Apply "page-sidebar-closed-hide-logo" class to the body element to make the logo hidden on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-hide" class to body element to completely hide the sidebar on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-fixed" class to have fixed sidebar -->
<!-- DOC: Apply "page-footer-fixed" class to the body element to have fixed footer -->
<!-- DOC: Apply "page-sidebar-reversed" class to put the sidebar on the right side -->
<!-- DOC: Apply "page-full-width" class to the body element to have full width page without the sidebar menu -->
<body
	class="page-header-fixed page-quick-sidebar-over-content page-sidebar-fixed">
	<!-- BEGIN HEADER -->
	<div class="page-header -i navbar navbar-fixed-top">
		<!-- BEGIN HEADER INNER -->
		<div class="page-header-inner">
			<!-- BEGIN LOGO -->
			<div class="page-logo">
				<a href="index.html"> <!--<img src="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/img/logo.png" alt="logo" class="logo-default"/>-->
				</a>
				<div class="menu-toggler sidebar-toggler hide">
					<!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
				</div>
			</div>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="menu-toggler responsive-toggler"
				data-toggle="collapse" data-target=".navbar-collapse"></a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<!-- BEGIN NOTIFICATION DROPDOWN -->
					<!-- END NOTIFICATION DROPDOWN -->
					<!-- BEGIN INBOX DROPDOWN -->
					<!-- END INBOX DROPDOWN -->
					<!-- BEGIN TODO DROPDOWN -->
					<!-- END TODO DROPDOWN -->
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
					<li class="dropdown dropdown-user"><a href="javascript:;"
						class="dropdown-toggle" data-toggle="dropdown"
						data-hover="dropdown" data-close-others="true"> <img alt=""
							class="img-circle"
							src="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/img/avatar3_small.jpg" />
							<span class="username username-hide-on-mobile" id = "userName"></span><i
							class="fa fa-angle-down"> </i></a>
						<ul class="dropdown-menu dropdown-menu-default" id="userdropdownmenu">
							
						</ul></li>
				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END HEADER INNER -->
	</div>
	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar-wrapper">
			<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
			<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
			<div class="page-sidebar navbar-collapse collapse">
				<!-- BEGIN SIDEBAR MENU -->
				<!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
				<!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
				<!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
				<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
				<!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
				<!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
				<ul class="page-sidebar-menu " data-keep-expanded="false"
					data-auto-scroll="true" data-slide-speed="200" id="menuUl">
				</ul>
				<!-- END SIDEBAR MENU -->
			</div>
		</div>
		<!-- END SIDEBAR -->
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<div class="page-content">
				<!-- BEGIN PAGE HEADER-->
				<h3 class="page-title" id="menuPageTitle"></h3>
				<div class="page-bar">
					<ul class="page-breadcrumb" id="meunNavigation">

					</ul>
				</div>
				<!-- END PAGE HEADER-->
				<!-- BEGIN PAGE CONTENT-->

				<div class="row ">
					<div class="col-md-12 " id="listData">
						<!-- BEGIN Portlet PORTLET-->
						<div class="portlet box blue">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-cogs"></i>角色权限设置
								</div>
								<div class="actions" id="listButs">
									

								</div>
							</div>
							<div class="portlet-body form">
								<form action="#" class="horizontal-form">
									<div class="form-body">

										<div class="row" style="min-height: 200px;">
											<div class="col-md-12 ">
												<div class="table-scrollable">

													<table
														class="table table-striped table-bordered table-advance table-hover"
														id="listTableData">
													</table>
												</div>
												<div class="margin-top-20">

													<div id="pagerContent" class="pageBotInfo"></div>

													<div id="dataPagerDiv" style="margin-top: -45px;">
														<ul class="pagination pull-right" id="dataPager">

														</ul>
													</div>

												</div>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
					<div class="col-md-12" id="detailData" style="display: none;">
						<div class="portlet box blue">
							<div class="portlet-title">
								<div class="caption">
									<i class="fa fa-cogs"></i>角色权限设置
								</div>
								<div class="actions" id="detailButs">
								
									
								</div>
							</div>
							<div class="portlet-body form">
								<form action="#" class="form-horizontal">
									<div class="form-body" style="min-height: 200px;">
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3">角色名称 </label>
													<div class="col-md-9">
														<input type="text" class="form-control"
															placeholder="未输入角色名称" id="edtRoleName" />
													</div>
												</div>

											</div>
										</div>
										<div class="row">
											<div class="col-md-6">
												<div class="portlet box blue">
													<div class="portlet-title">
														<div class="caption">
															<i class="fa fa-indent"></i>业务功能授权
														</div>
													</div>
													<div class="portlet-body form">
														<div class="form-body" id="DivRoleTreeView"
															style="height: 450px; overflow-x: hidden; overflow-y: auto;">
															<div id="edtRoleTreeView"></div>
														</div>
													</div>
												</div>
											</div>
											<div class="col-md-6">
												<div class="portlet box blue">
													<div class="portlet-title">
														<div class="caption">
															<i class="fa fa-indent"></i>门店数据授权
														</div>
													</div>
													<div class="portlet-body form">
														<div class="form-body" id="DivRoleStore"
															style="height: 450px; overflow-x: hidden; overflow-y: auto;">
															<div id="edtStoreTreeView"></div>
														</div>
													</div>
												</div>
											</div>
										</div>

									</div>
									<div class="form-actions right">
										<button type="button" class="btn blue" id="BtnSave">
											<i class="fa fa-save"></i>&nbsp保存
										</button>
										<button type="button" class="btn default" id="BtnReturn"
											onclick="JavaScript:fnNavTabCtrHide()">
											<i class="fa fa-reply"></i>&nbsp返回
										</button>
									</div>
								</form>
							</div>
						</div>
					</div>

				</div>
			</div>
			<!-- END CONTENT -->
			<!-- BEGIN QUICK SIDEBAR -->
			<!-- END QUICK SIDEBAR -->
		</div>
	</div>
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="page-footer">
		<div class="page-footer-inner">2016 &copy; Demo design by
			JieshiTech.</div>
		<div class="scroll-to-top">
			<i class="icon-arrow-up"></i>
		</div>
	</div>
	<input type="hidden" id="baseUrl"
		value="<%=application.getInitParameter("baseUrl")%>" />

	<input type="hidden" id="edtRoleID" value="" />
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom,
    this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
    <script src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/respond.min.js?v=<%=application.getInitParameter("verno")%>"></script> <script src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/excanvas.min.js?v=<%=application.getInitParameter("verno")%>"></script>
    <![endif]-->
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-migrate.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui.min.js?v=<%=application.getInitParameter("verno")%> before bootstrap.min.js?v=<%=application.getInitParameter("verno")%> to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-ui/jquery-ui.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap/js/bootstrap.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery.blockui.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery.cokie.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/uniform/jquery.uniform.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootbox/bootbox.min.js"
		type="text/javascript"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-select/bootstrap-select.min.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/select2/select2.min.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-multi-select/js/jquery.multi-select.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/clockface/js/clockface.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-daterangepicker/moment.min.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-daterangepicker/daterangepicker.js?v=<%=application.getInitParameter("verno")%>"
		charset="utf-8"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-colorpicker/js/bootstrap-colorpicker.js?v=<%=application.getInitParameter("verno")%>"></script>
	<script type="text/javascript"
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js?v=<%=application.getInitParameter("verno")%>"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jstree/dist/jstree.min.js?v=<%=application.getInitParameter("verno")%>""></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/scripts/metronic.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/scripts/layout.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/scripts/quick-sidebar.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/admin/layout/scripts/demo.js?v=<%=application.getInitParameter("verno")%>"
		type="text/javascript"></script>

	<script
		src="<%=application.getInitParameter("baseUrl")%>/assets/global/plugins/jquery-bootpag1.0.7/jquery.bootpag.js?v=<%=application.getInitParameter("verno")%>"
		charset="utf-8" type="text/javascript"></script>

	<script
		src="<%=application.getInitParameter("baseUrl")%>/assetspro/scripts/ProCommon.js?v=<%=application.getInitParameter("verno")%>"
		charset="utf-8" type="text/javascript"></script>
	<script
		src="<%=application.getInitParameter("baseUrl")%>/assetspro/scripts/systemui/role.js?v=<%=application.getInitParameter("verno")%>"
		charset="utf-8" type="text/javascript"></script>
	<script>
		jQuery(document).ready(function() {
			Metronic.init();
			Layout.init();
			role.init();

		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>