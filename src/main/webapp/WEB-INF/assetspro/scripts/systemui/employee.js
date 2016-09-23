var baseUrl = $("#baseUrl").val();
var funcID = "R0002";
var pageIndex = 1;
var pageSzie = 15;

// 操作类型变量 A 新增 E 修改 C 审核 V 查看
var opType = "";

// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间

// 修改按钮注册
function fnListeningActModi() {
	$(document).ready(function() {
		$('#ActModi').click(function() {
			// 新增标示
			opType = "B";
			// 开启编辑控件可用
			fnEdtCtrEnable();

		});
	});
}

// 注册监听保存按钮
function fnListeningBtnSave() {
	$(document).ready(function() {
		$('#BtnSave').click(function() {
			fnClickBtnSave();
		});

	});
}

/*
 * 明细选项卡显示
 */
function fnNavTabCtrShow() {

	$("#listData").css("display", "none");
	$("#detailData").css("display", "inline");

}

// 明细选项卡隐藏
function fnNavTabCtrHide() {
	$("#listData").css("display", "inline");
	$("#detailData").css("display", "none");
}

// 禁用界面控件
function fnEdtCtrDisable() {
	$("#edtEmployNo").attr("disabled", "disabled");
	$("#edtEmployName").attr("disabled", "disabled");
	$("#edtRoleID").attr("disabled", "disabled");
	$("#edtRoleIDBtnRefresh").attr("disabled", "disabled");

	// 保存按钮初始化禁用
	$("#BtnSave").css("display", "none");

}

/*
 * 启用界面控件
 */
function fnEdtCtrEnable() {

	$("#edtRoleID").removeAttr("disabled");
	$("#edtRoleIDBtnRefresh").removeAttr("disabled");
	// 保存按钮启用
	$("#BtnSave").css("display", "inline");
}

/*
 * 清空界面控件
 */
function fnEdtCtrClearValue() {
	$("#edtEmployNo").val("");
	$("#edtEmployName").val("");

}

/*
 * 根据单据状态开关单据操作按钮
 */

function fnEdtCtrEnableOrDisable(strFunctionStatus) {

	// strFunctionStatus "" 新增状态 "N":未审核 "Y" 已审核 "D" 作废

	$("#ActModi").css("display", "inline");
}

// 显示查询窗口
function fnBtnClickShowQuery() {

	if ($("#QueryDiv").css("display") == 'none') {
		$("#QueryDiv").css("display", "inline");
	} else {
		$("#QueryDiv").css("display", "none");
	}

}

// 执行查询并关闭查询窗口
function fnBtnClickSetQuery() {
	$("#QueryDiv").css("display", "none");
	pageIndex = 1;


	fnQueryData("", "", "", "");

}

// 查询数据
function fnQueryData(BrandName, StoreName, ChannelName, Paymentkey) {
	fnLoadingShowAndClose(true);
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/systemui/queryEmployeeData/',
		data : {
		
			BrandName : BrandName,
			StoreName : StoreName,
			ChannelName : ChannelName,
			PageIndex : pageIndex,
			PageSize : pageSzie,
			Paymentkey : Paymentkey
		},
		success : function(data) {
			// 明细数据信息
			var listData = data.listData;
			// 分页信息;
			var pageData = data.pageData;

			fnListTableData(listData, pageData);
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}

function fnListTableData(listData, pageData) {
	// 绑定明细列表信息
	var len = listData.length;
	var strHtml = "";
	var strColName = "";
	var strColNameValue = "";
	for (var i = 0; i < len; i++) {
		if (i == 0) {
			strColName += "<thead><tr>";
			strColName += "<th scope='col' style=\"text-align: center;\">行号 </th>";
			for (obj in listData[i]) {

				strColName += "<th scope='col' style=\"text-align: center;padding-left: 80px;padding-right: 80px;\">"
						+ obj + "</th>";

			}
			strColName += "</tr></thead>";
			break;
		}
	}
	strColNameValue += "<tbody>";
	for (var i = 0; i < listData.length; i++) {
		strColNameValue += "<tr>";
		if (parseInt(pageData[0].totalPages) != 0) {
			strColNameValue += "<td>" + ((pageIndex - 1) * pageSzie + i + 1)
					+ "</td>"; // 明细行号列
		}
		$
				.each(
						listData[i],
						function(key, value) {
							if (value != "") {
								if (key == "用户编码") {
									var valueAry = value.split("|=|");
									strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtGetEditData('"
											+ valueAry[0]
											+ "')\">"
											+ valueAry[1] + "</a></td>";
								} else {
									strColNameValue += "<td>" + value + "</td>";
								}
							}

						});
		strColNameValue += "</tr>";
	}

	strColNameValue += "</tbody>";

	strHtml += strColName;
	strHtml += strColNameValue;
	document.getElementById('listTableData').innerHTML = "";
	$('#listTableData').append(strHtml);

	var strPageHtml = "";
	strPageHtml += "<ul class=\"pagination pull-right\" id=\"dataPager\">";
	strPageHtml += "</ul>";
	document.getElementById('dataPagerDiv').innerHTML = "";
	$('#dataPagerDiv').append(strPageHtml);

	// 绑定分页信息;
	$('#dataPager').bootpag({
		paginationClass : 'pagination pagination-sm',
		next : '<i class="fa fa-angle-right"></i>',
		prev : '<i class="fa fa-angle-left"></i>',
		total : parseInt(pageData[0].totalPages),
		page : pageIndex,
		maxVisible : 6,
		leaps : false,
		firstLastUse : false,
		first : '<i class="fa fa-angle-double-left"></i>',
		last : '<i class="fa fa-angle-double-right"></i>'
	}).on('page', function(event, num) {
		pageIndex = num;
	

		fnQueryData("", "", "", "");
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {
	/*
	 * dteTimeStart = ""; // 时间开始时间 dteTimeEnd = ""; // 时间结束时间
	 */
	// 品牌
	

}


/*
 * 显示明细窗口 PrimaryKey 为查询主键
 */
function fnEdtGetEditData(PrimaryKey) {
	fnLoadingShowAndClose(true);
	fnEdtCtrClearValue();
	fnEdtCtrDisable();

	$.ajax({
		type : "POST",
		url : baseUrl + '/systemui/queryEmployeeCodeData/',
		dataType : "json",
		data : {
			employeeCode : PrimaryKey,
		},
		success : function(data) {

			var employeeCodeData = data.oneData;
			var employeeRoleData = data.roleData;
			if (employeeCodeData.length != 0) {
				$("#edtEmployNo").val(employeeCodeData[0].employNo);
				$("#edtEmployName").val(employeeCodeData[0].employName);
				$("#edtEmployeeCode").val(employeeCodeData[0].employeeCode);

				var roleData = "";
				for (var i = 0; i < employeeRoleData.length; i++) {
					if (i < employeeRoleData.length - 1) {
						roleData += "\"" + employeeRoleData[i].roleID + "\""
								+ ",";
					} else {
						roleData += "\"" + employeeRoleData[i].roleID + "\""
					}
				}
				var roleDataJson = '[' + roleData + ']';
				roleDataJson = $.parseJSON(roleDataJson);
				$("#edtRoleID").select2("val", roleDataJson);
			}

			// 明细选项卡显示
			fnNavTabCtrShow();
			fnLoadingShowAndClose(false);
		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}

// 必输项校验
function fnEdtCtrRequiredElement() {
	var strRequiredElement = "";

	if ($('#edtRoleID').select2('val') == "") {
		strRequiredElement = "用户所属角色权限不能为空";
		return strRequiredElement;
	}
	return strRequiredElement;
}

// 保存按钮点击执行方法
function fnClickBtnSave() {
	var strRequired = fnEdtCtrRequiredElement();
	if (strRequired != "") {
		fnAlertMessage("提示信息", strRequired);
		return;
	}

	var strInfoData = "{";

	strInfoData += '"Table1": [{';
	strInfoData += '"employeeCode": "' + $('#edtEmployeeCode').val() + '"';
	strInfoData += ',"roleID": "' + $('#edtRoleID').select2('val') + '"';
	strInfoData += '}]';

	strInfoData += '}';
	var requestData = {
		sendData : strInfoData
	}

	$.ajax({
				type : "POST",
				url : baseUrl + '/systemui/saveEmployeeRoleData',
				dataType : "json",
				data : requestData, // 传输整个form
				success : function(data) {
					var restultStatus = fnAnalysisResultStatus(data, "Y", "");
					if (restultStatus) {
						fnEdtCtrDisable();
						fnQueryData("", "", "", "");
						fnNavTabCtrHide();

					}
				},
				error : function(errMsg, errTextType, errorThrown) {
					fnLoadingShowAndClose(false);
					if (errTextType == "parsererror") {
						fnAlertMessage("错误信息", "返回信息:" + errMsg.responseText
								+ "未正确解析!");
					} else {
						fnAlertMessage("错误信息", errMsg.responseText);
					}
				}
			});

}

/*
 * 初始化窗体控件
 */
function fnInitForm() {

	// 下拉查询信息
	// SelectColName = brandName,channelName,saleModeName
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/role',
		dataType : "json",
		success : function(data) {

			$("#edtRoleID").select2("val", "");
			$("#edtRoleID").select2({
				multiple : true,
				data : data.role
			});

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}

var employee = function() {

	var handleDatePickers = function() {
		if (!jQuery().daterangepicker) {
			return;
		}
		$('#defaultrange').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment().subtract('days', 29),
					endDate : moment(),
					minDate : '01/01/2012',
					maxDate : '12/31/2018',
					locale : {
						applyLabel : '确认',
						cancelLabel : '取消',
						fromLabel : '从',
						toLabel : '至',
						customRangeLabel : '自定义范围',
						daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
						monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',
								'七月', '八月', '九月', '十月', '十一月', '十二月' ],
						firstDay : 1
					}
				},
				function(start, end) {
					$('#defaultrange input').val(
							start.format('YYYY-MM-DD') + ' ~ '
									+ end.format('YYYY-MM-DD'));
					dteTimeStart = start.format('YYYY-MM-DD');
					dteTimeEnd = end.format('YYYY-MM-DD');
				});

		$('#defaultrange input').val(
				moment().subtract('days', 29).format('YYYY-MM-DD') + ' ~ '
						+ moment().format('YYYY-MM-DD'));

		dteTimeStart = moment().subtract('days', 29).format('YYYY-MM-DD');
		dteTimeEnd = moment().format('YYYY-MM-DD');

	}

	return {

		init : function() {
			handleDatePickers();

			/*
			 * 监听按钮事件
			 */

			// 修改
			fnListeningActModi();

			// 保存
			fnListeningBtnSave();

			fnGetMenuFunction(baseUrl, funcID);
			fnInitForm();
			fnQueryData("", "", "", "");
			$("#ActQuery").css("display", "none");
		}
	};

}();
