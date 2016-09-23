var baseUrl = $("#baseUrl").val();
var funcID = "R0001";
var pageIndex = 1;
var pageSzie = 15;

// 操作类型变量 A 新增 E 修改 C 审核 V 查看
var opType = "";

// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间

/*
 * 注册监听新增按钮
 */
function fnListeningActNew() {
	$(document).ready(function() {
		$('#ActNew').click(function() {
			// 新增标示
			opType = "A";
			fnEdtCtrEnableOrDisable("");
			// 清除界面数据
			fnEdtCtrClearValue();
			// 开启编辑控件可用
			fnEdtCtrEnable();
			// 获取授权功能列表
			fnEdtGetRoleFuncTree("N");
			// 明细选项卡显示
			fnNavTabCtrShow();
		});
	});
}

// 修改按钮注册
function fnListeningActModi() {
	$(document).ready(function() {
		$('#ActModi').click(function() {
			// 新增标示
			opType = "B";
			fnEdtTreeCtrClearValue();
			// 开启编辑控件可用
			fnEdtCtrEnable();
			// 获取授权功能列表
			fnEdtGetRoleFuncTree("N");
		});
	});
}

// 注册监听删除按钮
function fnListeningActDelete() {
	$(document).ready(function() {
		$('#ActDelete').click(function() {
			fnClickActDelete();
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
	$("#edtRoleID").attr("disabled", "disabled");
	$("#edtRoleName").attr("disabled", "disabled");

	// 保存按钮初始化禁用
	$("#BtnSave").css("display", "none");

}

/*
 * 启用界面控件
 */
function fnEdtCtrEnable() {
	$("#edtRoleID").removeAttr("disabled");
	$("#edtRoleName").removeAttr("disabled");

	// 保存按钮启用
	$("#BtnSave").css("display", "inline");
}

/*
 * 清空界面控件
 */
function fnEdtCtrClearValue() {
	$("#edtRoleID").val("");
	$("#edtRoleName").val("");

	fnEdtTreeCtrClearValue();

}

function fnEdtTreeCtrClearValue() {
	document.getElementById('DivRoleTreeView').innerHTML = "";
	$('#DivRoleTreeView').append("<div id=\"edtRoleTreeView\"></div>");

	document.getElementById('DivRoleStore').innerHTML = "";
	$('#DivRoleStore').append("<div id=\"edtStoreTreeView\"></div>");
}

/*
 * 根据单据状态开关单据操作按钮
 */

function fnEdtCtrEnableOrDisable(strFunctionStatus) {

	// strFunctionStatus "" 新增状态 "N":未审核 "Y" 已审核 "D" 作废

	$("#ActModi").css("display", "inline");
}

function fnTreeViewBindData(TreeViewData, PID) {
	var treeData = "[";
	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var strName = TreeViewData[i].NAME;
		var strID = TreeViewData[i].ID;
		if (strPID == PID) {
			if (i < TreeViewData.length - 1) {
				treeData += "{";
				treeData += " \"id\" : \"" + strID + "\",";
				treeData += " \"text\" : \"" + strName + "\",";
				treeData += " \"state\" : { ";
				treeData += "		\"opened\" : true";
				treeData += "		}";
				treeData += fnTreeViewBindDataChildren(TreeViewData, strID);
				treeData += "	},";
			} else {
				treeData += "{";
				treeData += " \"id\" : \"" + strID + "\",";
				treeData += " \"text\" : \"" + strName + "\",";
				treeData += " \"state\" : { ";
				treeData += "		\"opened\" : true";
				treeData += "		}";
				treeData += fnTreeViewBindDataChildren(TreeViewData, strID);
				treeData += "	}";
			}
		}

	}
	treeData += "	]";
	return treeData;
}

function fnTreeViewBindDataChildren(TreeViewData, PID) {

	var treeChildrenData = ",\"children\":[";

	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var strName = TreeViewData[i].NAME;
		var strID = TreeViewData[i].ID;
		var ISLAST = TreeViewData[i].ISLAST;
		var ISCHECK = TreeViewData[i].ISCHECK;
		if (strPID == PID) {
			if (i < TreeViewData.length - 1) {

				if (ISLAST == "N") {
					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true";
					treeChildrenData += "		}";
					treeChildrenData += fnTreeViewBindDataChildren(
							TreeViewData, strID);
					treeChildrenData += "	},";
				} else {
					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "|" + ISLAST
							+ "|" + strPID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true,";
					if (ISCHECK == "Y") {
						treeChildrenData += "	\"selected\" : true";
					} else {
						treeChildrenData += "	\"selected\" : false";
					}

					treeChildrenData += "		},";
					treeChildrenData += " \"icon\":\"fa fa-gears icon-danger\"";
					treeChildrenData += "	},";
				}

			} else {

				if (ISLAST == "N") {
					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true";
					treeChildrenData += "		}";

					treeChildrenData += fnTreeViewBindDataChildren(
							TreeViewData, strID);
					treeChildrenData += "	}";
				} else {

					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "|" + ISLAST
							+ "|" + strPID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true,";
					if (ISCHECK == "Y") {
						treeChildrenData += "	\"selected\" : true";
					} else {
						treeChildrenData += "	\"selected\" : false";
					}
					treeChildrenData += "		},";
					treeChildrenData += " \"icon\":\"fa fa-gears icon-danger\"";
					treeChildrenData += "	}";
				}

			}
		}
	}
	treeChildrenData += "]";
	return treeChildrenData

}

/*
 * 获取授权功能列表
 */
function fnEdtGetRoleFuncTree(isCheckBox) {

	var roleID = $("#edtRoleID").val();
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/systemui/getFunButTreeData/',
		data : {
			roleID : roleID,
			isEdit : isCheckBox
		},
		success : function(data) {

			var roleData = data.roleData;

			if (roleData.length != 0) {
				$("#edtRoleName").val(roleData[0].roleName);
			}

			var treeData = data.menuData;
			var treeViewData = fnTreeViewBindData(treeData, "1");
			var TreeViewplugins = "";
			if (isCheckBox == "N") {
				TreeViewplugins = " [ \"wholerow\", \"checkbox\", \"types\" ]";
			} else {
				TreeViewplugins = " [ \"wholerow\", \"types\" ]";
			}

			$('#edtRoleTreeView').jstree({
				'plugins' : eval(TreeViewplugins),
				'core' : {
					"themes" : {
						"responsive" : false
					},
					'data' : eval(treeViewData)
				},
				"types" : {
					"default" : {
						"icon" : "fa fa-folder icon-state-warning icon-lg"
					},
					"file" : {
						"icon" : "fa fa-file icon-state-warning icon-lg"
					}
				}
			});

			var areaData = data.areaData;

			var areaTreeViewData = fnTreeViewBindData(areaData, "1");
			$('#edtStoreTreeView').jstree({
				'plugins' : eval(TreeViewplugins),
				'core' : {
					"themes" : {
						"responsive" : false
					},
					'data' : eval(areaTreeViewData)
				},
				"types" : {
					"default" : {
						"icon" : "fa fa-folder icon-state-warning icon-lg"
					},
					"file" : {
						"icon" : "fa fa-file icon-state-warning icon-lg"
					}
				}
			});

		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

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
		url : baseUrl + '/systemui/queryRoleData/',
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
								var valueAry = value.split("|=|");

								strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtGetEditData('"
										+ valueAry[0]
										+ "')\">"
										+ valueAry[1]
										+ "</a></td>";
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
	$("#setBrandName").select2("val", "");

	// 门店
	$('#setStoreName').select2("val", "");
	// 渠道
	$("#setChannelName").select2("val", "");

	// 支付方式
	$("#setPaymentkey").select2("val", "");

}

// 注册下拉框值改变事件
function fnListeningCtrSelectChange() {
	// 所有品牌
	$("#setBrandName").on("change", function(e) {
		fnGetChannelNameByBrandName()
	});

}

// 根据选择的品牌,查询该品牌下所有的渠道
function fnGetChannelNameByBrandName() {
	var strBrandName = $("#setBrandName").select2("val"); // 品牌
	if (Object.prototype.toString.call(strBrandName) == "[object Object]") {
		strBrandName = "";
	}
	// 清空渠道下拉
	$("#setChannelName").select2("val", "");
	$("#setStoreName").select2("val", "");
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/brand/change/' + strBrandName,
		dataType : "json",
		success : function(data) {

			var strChannelName = JSON.stringify(eval(data.channelName));
			var strChannelNameTemp = "";
			if (typeof (strChannelName) != "undefined") {
				strChannelName = strChannelName.substring(1,
						strChannelName.length);
				strChannelNameTemp = '[{"id":"","text":"全部渠道"},'
						+ strChannelName;
			} else {
				strChannelNameTemp = '[{"id":"","text":"全部渠道"}]'

			}

			$("#setChannelName").select2("val", "");
			$("#setChannelName").select2({
				data : eval(strChannelNameTemp)
			});

			var strStoreName = JSON.stringify(eval(data.storeName));
			var strStoreNameTemp = "";
			if (typeof (strStoreName) != "undefined") {
				strStoreName = strStoreName.substring(1, strStoreName.length);
				strStoreNameTemp = '[{"id":"","text":"全部门店"},' + strStoreName;
			} else {
				strStoreNameTemp = '[{"id":"","text":"全部门店"}]';
			}

			$("#setStoreName").select2("val", "");
			$("#setStoreName").select2({
				data : eval(strStoreNameTemp)
			});

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}

/*
 * 显示明细窗口 PrimaryKey 为查询主键
 */
function fnEdtGetEditData(PrimaryKey) {
	fnLoadingShowAndClose(true);
	fnEdtCtrClearValue();
	fnEdtCtrDisable();
	$("#edtRoleID").val(PrimaryKey);
	fnEdtGetRoleFuncTree("Y");
	// 明细选项卡显示
	fnNavTabCtrShow();
	fnLoadingShowAndClose(false);
}

// 必输项校验
function fnEdtCtrRequiredElement() {
	var strRequiredElement = "";

	if ($("#edtRoleName").val() == "") {
		strRequiredElement = "角色名称不能为空";
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

	var strRoleTreeView = $("#edtRoleTreeView").jstree("get_selected", true);
	if (strRoleTreeView == "" || strRoleTreeView == null) {
		fnAlertMessage("提示信息", "授权业务功能不能为空");
		return;
	}

	var strStoreTreeView = $("#edtStoreTreeView").jstree("get_selected", true);
	if (strStoreTreeView == "" || strStoreTreeView == null) {
		fnAlertMessage("提示信息", "门店数据授权不能为空");
		return;
	}

	var strTableButton = "";
	var strTableFunction = "";
	var jsnTreeView = eval(strRoleTreeView);
	var aryFunctionID = [];
	for (var i = 0; i < jsnTreeView.length; i++) {

		var strSelectID = jsnTreeView[i].id;
		var strSelectParents = jsnTreeView[i].parents;
		var arySelectID = strSelectID.split("|");

		// 是否是末级
		if (arySelectID[1] == "Y") {

			strTableButton += '{';
			strTableButton += '"ButtonID": "' + arySelectID[0] + '"';
			strTableButton += ',"FuncID": "' + arySelectID[2] + '"';
			strTableButton += '},';

			strSelectParents = "" + strSelectParents + "";
			var arySelectParents = strSelectParents.split(",");
			for (var j = 0; j < arySelectParents.length; j++) {
				if (arySelectParents[j] != "#") {
					var strFuncID = arySelectParents[j].split("|")[0];
					var isCont = 0;
					for (var k = 0; k < aryFunctionID.length; k++) {
						if (strFuncID == aryFunctionID[k]) {
							isCont++;
						}
					}
					if (isCont == 0) {
						aryFunctionID.push(strFuncID);
					}
				}
			}
		}
	}

	for (var m = 0; m < aryFunctionID.length; m++) {
		strTableFunction += "";
		strTableFunction += '{';
		strTableFunction += '"FuncID": "' + aryFunctionID[m] + '"';
		if (m == aryFunctionID.length - 1) {
			strTableFunction += '}';
		} else {
			strTableFunction += '},';
		}

	}

	strTableButton = strTableButton.substring(0, strTableButton.length - 1);

	var strTableAreaStore = "";
	var strTableArea = "";
	var jsnStoreTreeView = eval(strStoreTreeView);
	var aryAreaCode = [];
	for (var i = 0; i < jsnStoreTreeView.length; i++) {

		var strSelectID = jsnStoreTreeView[i].id;
		var strSelectParents = jsnStoreTreeView[i].parents;
		var arySelectID = strSelectID.split("|");

		// 是否是末级
		if (arySelectID[1] == "Y") {

			strTableAreaStore += '{';
			strTableAreaStore += '"storeCode": "' + arySelectID[0] + '"';
			strTableAreaStore += ',"areaCode": "' + arySelectID[2] + '"';
			strTableAreaStore += '},';

			strSelectParents = "" + strSelectParents + "";
			var arySelectParents = strSelectParents.split(",");
			for (var j = 0; j < arySelectParents.length; j++) {
				if (arySelectParents[j] != "#") {
					var strFuncID = arySelectParents[j].split("|")[0];
					var isCont = 0;
					for (var k = 0; k < aryAreaCode.length; k++) {
						if (strFuncID == aryAreaCode[k]) {
							isCont++;
						}
					}
					if (isCont == 0) {
						aryAreaCode.push(strFuncID);
					}
				}
			}
		}
	}

	for (var m = 0; m < aryAreaCode.length; m++) {
		strTableArea += "";
		strTableArea += '{';
		strTableArea += '"areaCode": "' + aryAreaCode[m] + '"';
		if (m == aryAreaCode.length - 1) {
			strTableArea += '}';
		} else {
			strTableArea += '},';
		}

	}

	strTableAreaStore = strTableAreaStore.substring(0,
			strTableAreaStore.length - 1);

	var strInfoData = "{";

	strInfoData += '"Table1": [{';
	strInfoData += '"RoleID": "' + $('#edtRoleID').val() + '"';
	strInfoData += ',"RoleName": "' + $('#edtRoleName').val() + '"';
	strInfoData += '}]';
	strInfoData += ',"Table2": [';
	strInfoData += strTableFunction;

	strInfoData += ']';
	strInfoData += ',"Table3": [';
	strInfoData += strTableButton;
	strInfoData += ']';

	strInfoData += ',"Table4": [';
	strInfoData += strTableArea;
	strInfoData += ']';

	strInfoData += ',"Table5": [';
	strInfoData += strTableAreaStore;
	strInfoData += ']';

	strInfoData += '}';
	var requestData = {
		sendData : strInfoData
	}

	$
			.ajax({
				type : "POST",
				url : baseUrl + '/systemui/saveRoleData',
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

// 删除按钮点击执行方法
function fnClickActDelete() {
	bootbox.dialog({
		message : "<i class='fa fa-comments'></i> 是否要删除角色权限信息 ["
				+ $("#edtRoleName").val() + ']',
		title : "<h5><i class='fa fa-volume-up'></i> 信息提醒</h5>",
		buttons : {
			success : {
				label : "是(Yes)",
				className : "green",
				callback : function() {
					var getPrimaryKey = $("#edtRoleID").val();
					$.ajax({
						type : "POST",
						url : baseUrl + '/systemui/delRoleData',
						dataType : "json",
						data : {
							roleID : getPrimaryKey
						},
						success : function(data) {
							var restultStatus = fnAnalysisResultStatus(data,
									"Y", "数据删除成功!");
							if (restultStatus) {
								fnEdtCtrDisable();
								fnQueryData("", "", "", "");
								fnNavTabCtrHide();
							}
						},
						error : function(errMsg, errTextType, errorThrown) {
							fnLoadingShowAndClose(false);
							if (errTextType == "parsererror") {
								fnAlertMessage("错误信息", "返回信息:"
										+ errMsg.responseText + "未正确解析!");
							} else {
								fnAlertMessage("错误信息", errMsg.responseText);
							}
						}
					});

				}
			},
			danger : {
				label : "否(No)",
				className : "red",
				callback : function() {

				}
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
		url : baseUrl + '/common/dict/brand',
		dataType : "json",
		success : function(data) {

			var strBrandName = JSON.stringify(eval(data.brandName));
			strBrandName = strBrandName.substring(1, strBrandName.length);
			var strBrandNameTemp = '[{"id":"","text":"全部品牌"},' + strBrandName;

			$("#setBrandName").select2("val", "");
			$("#setBrandName").select2({
				data : eval(strBrandNameTemp)
			});

			/*
			 * var strChannelName = JSON.stringify(eval(data.channelName));
			 * strChannelName = strChannelName.substring(1,
			 * strChannelName.length); var strChannelNameTemp =
			 * '[{"id":"","text":"全部渠道"},' + strChannelName;
			 * 
			 * $("#setChannelName").select2("val", "");
			 * $("#setChannelName").select2({ data: eval(strChannelNameTemp) });
			 */

			var strChannelNameTemp = '[{"id":"","text":"全部渠道"}]';

			$("#setChannelName").select2("val", "");
			$("#setChannelName").select2({
				data : eval(strChannelNameTemp)
			});

			var strStoreNameTemp = '[{"id":"","text":"全部门店"}]';

			$("#setStoreName").select2("val", "");
			$("#setStoreName").select2({
				data : eval(strStoreNameTemp)
			});

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

	// 支付方式
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/paymentkey',
		dataType : "json",
		success : function(data) {

			var strPaymentkey = JSON.stringify(eval(data.paymentkey));
			strPaymentkey = strPaymentkey.substring(1, strPaymentkey.length);
			var strPaymentkeyTemp = '[{"id":"","text":"全部支付方式"},'
					+ strPaymentkey;

			$("#setPaymentkey").select2("val", "");
			$("#setPaymentkey").select2({
				data : eval(strPaymentkeyTemp)
			});

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}

var role = function() {

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
			// 新增
			fnListeningActNew();
			// 修改
			fnListeningActModi();
			// 删除
			fnListeningActDelete();
			// 保存
			fnListeningBtnSave();

			fnGetMenuFunction(baseUrl, funcID);
			fnInitForm();
			fnListeningCtrSelectChange();
			fnQueryData("", "", "", "");
			
			$("#ActQuery").css("display", "none");
		}
	};

}();
