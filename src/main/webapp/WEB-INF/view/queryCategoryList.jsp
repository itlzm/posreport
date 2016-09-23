<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html>
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

<body
	class="page-header-fixed page-quick-sidebar-over-content page-sidebar-fixed">
	<!-- BEGIN HEADER -->

	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">

		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<div class="page-content">
				<div class="row ">
					<div class="form-body" style="min-height: 200px; min-width: 630px;margin-top: -20px; margin-left: -5px;">

						<div class="row">

							<div class="col-md-12">
								<div class="col-md-12" id="DivCategory">
									<div id="setCategoryTreeView"></div>
								</div>

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

	<script>
		jQuery(document).ready(function() {
			Metronic.init();
			Layout.init();

			var baseUrl = $("#baseUrl").val();
			var strSelectBrandCode = parent.fnGetSelectBrandCode();
			
			$.ajax({
				type : "get",
				url : baseUrl + '/common/dict/brand/getCategoryList?SelectBrandCode='+strSelectBrandCode,
				dataType : "json",
				
				success : function(data) {
					fnBindQueryCategoryViewData(data.CategoryName);
				},
				error : function(errMsg, errTextType, errorThrown) {

				}
			});

		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>