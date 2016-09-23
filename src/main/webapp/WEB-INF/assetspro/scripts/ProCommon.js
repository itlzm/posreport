//设置菜单信息
function fnGetMenuFunction(baseUrl, menuId) {

	$
			.ajax({
				type : "get",
				url : baseUrl + '/api/menu',
				dataType : "json",
				success : function(data) {

					var menuIdTemp = menuId.substr(0, 3);
					var menuData = data;
					var strHtml = "";
					var strPageTitle = "";
					var strMeunNavigation = "";

					strHtml += "<li class='sidebar-toggler-wrapper'>";
					strHtml += "<div class='sidebar-toggler'></div> ";
					strHtml += "</li>";
					strHtml += "<li class='sidebar-search-wrapper'>";
					strHtml += "</li>";

					for (var i = 0; i < menuData.length; i++) {
						var strPID = menuData[i].PID;
						var strFuncID = menuData[i].FuncID;
						var strTitleText = menuData[i].TitleText;

						if (strPID == "1") {
							if (menuIdTemp == strFuncID) {
								strHtml += "<li class='active open'><a href='javascript:;'><i class='icon-bar-chart'></i>";
								strHtml += "<span class='title'>"
										+ strTitleText
										+ "</span> <span class='selected'></span><span class='arrow open'> </span></a>";
							} else {
								strHtml += "<li><a href='javascript:;'><i class='icon-bar-chart'></i>";
								strHtml += "<span class='title'>"
										+ strTitleText
										+ "</span> <span ></span><span> </span></a>";
							}

							strHtml += "<ul class='sub-menu'>";

							for (var j = 0; j < menuData.length; j++) {
								var strPIDTemp = menuData[j].PID;
								var strFuncIDTemp = menuData[j].FuncID;
								var strTitleTextTemp = menuData[j].TitleText;
								var strMenuWebPageUrlTemp = menuData[j].MenuWebPageUrl;
								var strDescTextTemp = menuData[j].DescText;
								if (strPIDTemp == strFuncID) {
									if (strFuncIDTemp == menuId) {
										strHtml += "<li class='active'><a href='"
												+ baseUrl
												+ strMenuWebPageUrlTemp
												+ "'><i class=''></i>"
												+ strTitleTextTemp
												+ "</a></li>";
										strPageTitle = strTitleTextTemp
												+ " <small>" + strDescTextTemp
												+ "</small>";
										strMeunNavigation = "<li><a href='#'>"
												+ strTitleText
												+ " </a><i class='fa fa-angle-right'></i></li>";
										strMeunNavigation += "<li><a href='#'>"
												+ strTitleTextTemp
												+ " </a></li>";
									} else {
										strHtml += "<li><a href='" + baseUrl
												+ strMenuWebPageUrlTemp
												+ "'><i class=''></i>"
												+ strTitleTextTemp
												+ "</a></li>";
									}
								}
							}
							strHtml += "</ul></li>";
						}
					}

					document.getElementById('menuUl').innerHTML = "";
					$('#menuUl').append(strHtml);

					document.getElementById('menuPageTitle').innerHTML = "";
					$('#menuPageTitle').append(strPageTitle);

					document.getElementById('meunNavigation').innerHTML = "";
					$('#meunNavigation').append(strMeunNavigation);

				},
				error : function(errMsg, errTextType, errorThrown) {

				}
			});

	$
			.ajax({
				type : "get",
				url : baseUrl + '/api/menubutton?funcID=' + menuId,
				dataType : "json",
				async : false,
				success : function(data) {

					var buttonData = data.listData;
					// 列表按钮
					var listButtonHtml = "";
					// 明细按钮
					var detailButtonHtml = "";
					for (var i = 0; i < buttonData.length; i++) {
						var ModalsPageName = buttonData[i].ModalsPageName;
						var EventName = buttonData[i].EventName;
						var DisplayText = buttonData[i].DisplayText;
						var ButtonImage = buttonData[i].ButtonImage;
						var butColor = ButtonImage.split(",")[0];
						var butios = ButtonImage.split(",")[1];
						if (ModalsPageName == "ListPage") {

							if (EventName == "ActExport") {
								listButtonHtml += "<div class='btn-group'><a class='btn "
										+ butColor
										+ "' href='javascript:;'data-toggle='dropdown'><i class='fa fa-share'> </i>&nbsp;";
								listButtonHtml += DisplayText
										+ " <i class='fa fa-angle-down'></i></a>";
								listButtonHtml += "<ul class='dropdown-menu pull-right'>";
								// listButtonHtml += "<li><a
								// href='javascript:;'><i class='fa
								// fa-file-pdf-o'></i>导出图表到PDF </a></li>";
								listButtonHtml += "<li><a href='javascript:;' onclick=\"JavaScript:fnExportExcel()\" ><i class='fa fa-file-excel-o'></i>导出明细到Excel </a></li>";
								//listButtonHtml += "<li><a href='javascript:;' onclick=\"JavaScript:fnExportExcelImg()\" ><i class='fa fa-file-excel-o'></i>导出图标到Excel </a></li>";
								// listButtonHtml += "<li
								// class='divider'></li>";
								// listButtonHtml += "<li><a
								// href='javascript:;'><i class='fa
								// fa-file-pdf-o'></i>完整导出到PDF </a></li>";
								listButtonHtml += "</ul></div>";

							} else {

								listButtonHtml += "<a href='javascript:;' class='btn "
										+ butColor
										+ "' id='"
										+ EventName
										+ "'>";
								listButtonHtml += "<i class='fa " + butios
										+ "'></i> &nbsp;" + DisplayText
										+ " </a>";
							}
						} else if (ModalsPageName == "DetailPage") {

							if (EventName == "ActExport") {
								detailButtonHtml += "<div class='btn-group'><a class='btn "
										+ butColor
										+ "' href='javascript:;'data-toggle='dropdown'><i class='fa fa-share'> </i>";
								detailButtonHtml += DisplayText
										+ " <i class='fa fa-angle-down'></i></a>";
								detailButtonHtml += "<ul class='dropdown-menu pull-right'>";
								// detailButtonHtml += "<li><a
								// href='javascript:;'><i class='fa
								// fa-file-pdf-o'></i>导出图表到PDF </a></li>";
								detailButtonHtml += "<li><a href='javascript:;'><i class='fa fa-file-excel-o'></i>导出明细到Excel </a></li>";
								// detailButtonHtml += "<li
								// class='divider'></li>";
								// detailButtonHtml += "<li><a
								// href='javascript:;'><i class='fa
								// fa-file-pdf-o'></i>完整导出到PDF </a></li>";
								detailButtonHtml += "</ul></div>";

							} else {
								detailButtonHtml += "<a href='javascript:;' class='btn "
										+ butColor
										+ "' id='"
										+ EventName
										+ "'>";
								detailButtonHtml += "<i class='fa " + butios
										+ "'></i> " + DisplayText + " </a>";
							}

						}
					}
					document.getElementById('listButs').innerHTML = "";
					$('#listButs').append(listButtonHtml);

					detailButtonHtml += "<a href='javascript:;' class='btn blue' id='BtnReturn'";
					detailButtonHtml += " onclick='JavaScript:fnNavTabCtrHide();'> <i class='fa fa-reply'></i> 返回";
					detailButtonHtml += "</a> ";

					document.getElementById('detailButs').innerHTML = "";
					$('#detailButs').append(detailButtonHtml);

					var userList = data.userList;
					var userName = userList[0].employName;

					document.getElementById('userName').innerHTML = "";
					$('#userName').append(userName);

					var userdropdownmenuHtml = "<li  onclick='JavaScript:fnCloseRep();'><a href='JavaScript:void(0)' ><i class='fa fa-sign-out'></i>关闭报表 </a></li>";

					document.getElementById('userdropdownmenu').innerHTML = "";
					$('#userdropdownmenu').append(userdropdownmenuHtml);

					// $("#userdropdownmenu").css("display", "none");

				},
				error : function(errMsg, errTextType, errorThrown) {

				}
			});

}

function fnCloseRep() {
	if (confirm("您确定要关闭报表吗？")) {
		var userAgent = navigator.userAgent;
		if (userAgent.indexOf("Firefox") != -1
				|| userAgent.indexOf("Presto") != -1) {
			window.location.replace("about:blank");
		} else {
			window.opener = null;
			window.open("", "_self");
			window.location.replace("about:blank");
			window.close();
		}
	} else {
	}
}

// #region
// ======显示loading========================================================================
// 显示loading
function fnLoadingShowAndClose(isShow) {
	if (isShow) {
		// Metronic.blockUI({ boxed: true });
		Metronic.startPageLoading({
			animate : true
		});

	} else {
		// 关闭Loading
		// window.setTimeout(function () {
		// Metronic.unblockUI();
		Metronic.stopPageLoading();
		// }, 1000);
	}
}
// #endregion

// #region
// ======消息提醒公共方法===================================================================
// msgTitle 弹框标题 msgContent 弹框内容 msgBtnLabel 按钮内容
function fnAlertMessage(msgTitle, msgContent) {
	bootbox.dialog({
		message : msgContent,
		title : msgTitle,
		buttons : {
			success : {
				label : "确认",
				className : "green",
				callback : function() {

				}
			}

		}
	});
}
// #endregion

// 处理Json返回值错误信息
// resultStatusData Json返回数据
// isSueccesTips 在成功时是否提示信息
// SuccessTipsText 需要提示的信息 前提 isSuccessTips='Y'
function fnAnalysisResultStatus(resultStatusData, isSuccessTips,
		SuccessTipsText) {
	var retStatus = resultStatusData.ResultStatus[0].Status;
	var retMess = resultStatusData.ResultStatus[0].retMess;
	if (retStatus == "N") {
		fnAlertMessage("错误信息", retMess);
		return false;
	} else if (retStatus == "Y") {
		if (retMess == "" || retMess == null) {
			if (SuccessTipsText == "") {
				SuccessTipsText = "操作执行成功!";
			}
		} else {
			if (SuccessTipsText == "") {
				SuccessTipsText = retMess;
			}
		}
		if (isSuccessTips == "Y") {
			fnAlertMessage("提示信息", SuccessTipsText);
		}
		return true;
	} else if (retStatus == "R") {
		return false;
	}
}

// #region ======获取浏览器URL的参数列表 URL
// QueryString============================================
// 获取浏览器URL的参数列表 URL QueryString
function fnGetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
// #endregion

function isfloat(oNum) {

	var stroNum = "" + oNum + "";
	if (oNum == "0" || oNum == 0 || oNum == "--") {
		return true;
	}

	if (!oNum)
		return false;

	var strP = /^\d+(\.\d+)?$/;

	stroNum = stroNum.replace("%", "");
	stroNum = stroNum.replace("-", "");
	
	if (!strP.test(stroNum))
		return false;

	try {

		if (parseFloat(stroNum) != stroNum)
			return false;

	} catch (ex) {

		return false;

	}

	return true;

}

function fomatFloat(src, pos) {
	return Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
}

function toDecimal2(x) {
	var f = parseFloat(x);
	if (isNaN(f)) {
		return false;
	}
	var f = Math.round(x * 100) / 100;
	var s = f.toString();
	var rs = s.indexOf('.');
	if (rs < 0) {
		rs = s.length;
		s += '.';
	}
	while (s.length <= rs + 2) {
		s += '0';
	}
	return s;
}

function Map() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	}

	var put = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	}

	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	}

	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if (v.key === key) {
				continue;
			}
			this.arr.unshift(v);
		}
	}

	var size = function() {
		return this.arr.length;
	}

	var isEmpty = function() {
		return this.arr.length <= 0;
	}
	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.size = size;
	this.isEmpty = isEmpty;
}



var SelectStoreTree = "";

function fnBindQueryStoreViewData(areaData) {

	SelectStoreTree = parent.fnGetSelectStoreTree();

	document.getElementById('DivRoleStore').innerHTML = "";
	$('#DivRoleStore').append("<div id=\"setStoreTreeView\"></div>");

	var areaTreeViewData = fnStoreTreeViewBindData(areaData, "1");
	$('#setStoreTreeView').jstree({
		'plugins' : [ "wholerow", "checkbox", "types" ],
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

	}).bind('click.jstree', function(event) {
		fnGetStoreValue(areaData);
	});

}

// 品牌ID
var strBranCode = "";
// 品牌名称
var strBranName = "";

function fnGetStoreValue(areaData) {
	var strStoreTreeView = $("#setStoreTreeView").jstree("get_selected", true);
	var strTableAreaStore = "";
	var strTableArea = "";
	var jsnStoreTreeView = eval(strStoreTreeView);
	var aryAreaCode = [];
	var aryAreaStore = [];
	var strSelectIDAreaName = "";
	var strSelectIDStoreName = "";
	for (var i = 0; i < jsnStoreTreeView.length; i++) {

		var strSelectID = jsnStoreTreeView[i].id;
		var strSelectIDName = jsnStoreTreeView[i].text;
		var strSelectParents = jsnStoreTreeView[i].parents;
		var strSelectParentsText = jsnStoreTreeView[i].parents;
		var arySelectID = strSelectID.split("|");
		// 是否是末级
		if (arySelectID[1] == "Y") {
			strSelectIDStoreName += strSelectIDName;

			strTableAreaStore += '{';
			strTableAreaStore += '"storeCode": "' + arySelectID[0] + '"';
			strTableAreaStore += ',"areaCode": "' + arySelectID[2] + '"';
			strTableAreaStore += '},';

			aryAreaStore.push(arySelectID[0]);

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
						
						if(strFuncID.indexOf("=") > 0){
							aryAreaCode.push(strFuncID.split("|")[0]);
						}else{
						
							aryAreaCode.push(strFuncID);
						}
					}
				}
			}
		} else {
			strSelectIDAreaName += strSelectIDName;
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
	var strStoreTreeText = ""
	var strStoreTreeTextTemp = "";
	for (var i = 0; i < areaData.length; i++) {
		var strPID = areaData[i].PID;
		var strName = areaData[i].NAME;
		var strID = areaData[i].ID;
		for (var m = 0; m < aryAreaStore.length; m++) {
			var NodeID = aryAreaStore[m];
			if (NodeID == strID) {
				strStoreTreeTextTemp += strName + ",";
			}

		}
	}

	if (strStoreTreeTextTemp != "") {
		strStoreTreeTextTemp = strStoreTreeTextTemp.substr(0,
				strStoreTreeTextTemp.length - 1);
		strStoreTreeText = "[" + strStoreTreeTextTemp + "]";
	}

	var strStoreTreeVal = "";
	for (var m = 0; m < aryAreaStore.length; m++) {
		strStoreTreeVal += aryAreaStore[m] + "|";
	}

	if (strStoreTreeVal != "") {
		strStoreTreeVal = strStoreTreeVal.substr(0, strStoreTreeVal.length - 1);
	}

	fnBranCodeAndNameText(areaData, "1", aryAreaCode);

	var NodeNAMEs = fnStoreTreeText(areaData, "1", aryAreaCode);
	
	parent.fnGetQueryStoreTree(strBranCode, strBranName, NodeNAMEs,
			strStoreTreeText, strStoreTreeVal);

}

// 获取第一级别品牌
function fnStoreTreeText(TreeViewData, PID, aryAreaCode) {
	var strStoreTreeText = ""
	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var NAME = TreeViewData[i].NAME;
		var strName = NAME.split('|')[1];
		var strID = TreeViewData[i].ID.split('|')[1];
		if (strPID == PID) {

			for (var m = 0; m < aryAreaCode.length; m++) {
				var NodeID = aryAreaCode[m];
				if (NodeID == strID) {
					strStoreTreeText += "["
					strStoreTreeText += strName;
					strStoreTreeText += fnStoreTreeTextChildren(TreeViewData,
							strID, aryAreaCode);
					strStoreTreeText += "]";
				}
			}
		}

	}

	return strStoreTreeText;
}

// 获取品牌id及名称
function fnBranCodeAndNameText(TreeViewData, PID, aryAreaCode) {
	strBranCode = "";
	strBranName = "";

	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		
	
		if (strPID == PID) {
			var NAME = TreeViewData[i].NAME;
			var strName = NAME.split('=')[1];
			var strBID =  NAME.split('=')[0];
			var strID= TreeViewData[i].ID.split('=')[1];
			
			for (var m = 0; m < aryAreaCode.length; m++) {
				var NodeID = aryAreaCode[m];
				if (NodeID == strID) {
					strBranCode += strBID + "|";
					strBranName += strName + ",";
				}
			}
		}

	}

	if (strBranName != "") {
		strBranName = strBranName.substr(0, strBranName.length - 1);
		strBranCode = strBranCode.substr(0, strBranCode.length - 1);
		strBranName = "[" + strBranName + "]";
	}
}

// 递归查询下级区域
function fnStoreTreeTextChildren(TreeViewData, PID, aryAreaCode) {

	var treeChildrenData = "";

	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var strName = TreeViewData[i].NAME;
		var strID = TreeViewData[i].ID;
		var ISLAST = TreeViewData[i].ISLAST;
		var ISCHECK = TreeViewData[i].ISCHECK;
		if (strPID == PID) {
			for (var m = 0; m < aryAreaCode.length; m++) {
				var NodeID = aryAreaCode[m];
				if (NodeID == strID) {
					treeChildrenData += "-" + strName;
					treeChildrenData += fnStoreTreeTextChildren(TreeViewData,
							strID, aryAreaCode);
				}
			}
		}

	}
	return treeChildrenData;
}

function fnStoreTreeViewBindData(TreeViewData, PID) {
	var treeData = "[";
	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var NAME  = TreeViewData[i].NAME;
		var strName = NAME.split("=")[1];
		var strID = TreeViewData[i].ID.split("=")[1];
		if (strPID == PID) {
			if (i < TreeViewData.length - 1) {
				treeData += "{";
				treeData += " \"id\" : \"" + strID + "\",";
				treeData += " \"text\" : \"" + strName + "\",";
				treeData += " \"state\" : { ";
				treeData += "		\"opened\" : true";
				treeData += "		}";
				treeData += fnStoreTreeViewBindDataChildren(TreeViewData, strID);
				treeData += "	},";
			} else {
				treeData += "{";
				treeData += " \"id\" : \"" + strID + "\",";
				treeData += " \"text\" : \"" + strName + "\",";
				treeData += " \"state\" : { ";
				treeData += "		\"opened\" : true";
				treeData += "		}";
				treeData += fnStoreTreeViewBindDataChildren(TreeViewData, strID);
				treeData += "	}";
			}
		}

	}
	treeData += "	]";
	return treeData;
}

function fnStoreTreeViewBindDataChildren(TreeViewData, PID) {

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
					treeChildrenData += fnStoreTreeViewBindDataChildren(
							TreeViewData, strID);
					treeChildrenData += "	},";
				} else {
					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "|" + ISLAST
							+ "|" + strPID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true,";

					var arySelectStoreTree = SelectStoreTree.split("|");
					var strISCHECK = "";
					for (var k = 0; k < arySelectStoreTree.length; k++) {
						if (strID == arySelectStoreTree[k]) {
							strISCHECK = "Y";
							break;
						}
					}

					if (strISCHECK == "Y") {
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

					treeChildrenData += fnStoreTreeViewBindDataChildren(
							TreeViewData, strID);
					treeChildrenData += "	}";
				} else {

					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "|" + ISLAST
							+ "|" + strPID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true,";

					var arySelectStoreTree = SelectStoreTree.split("|");
					var strISCHECK = "";
					for (var k = 0; k < arySelectStoreTree.length; k++) {
						if (strID == arySelectStoreTree[k]) {
							strISCHECK = "Y";
							break;
						}
					}

					if (strISCHECK == "Y") {
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
 * 设置门店类型
 * */
function fnSetStoreType(){
	var strHtml = "";
	strHtml +="<option value='ALL'>全部</option>";
	strHtml +="<option value='1'>加盟店</option>";
	strHtml +="<option value='2'>直营店</option>";
	
	document.getElementById('setStoreType').innerHTML = "";
	$('#setStoreType').append(strHtml);
}



// 界面选择的品牌产品分类树
var SelectCategoryTree = "";

// 绑定后台返回的品牌产品分类数据
function fnBindQueryCategoryViewData(categoryData) {
	
	// 得到调用页面选择的分类树
	SelectCategoryTree = parent.fnGetSelectCategoryTree();

	document.getElementById('DivCategory').innerHTML = "";
	$('#DivCategory').append("<div id=\"setCategoryTreeView\"></div>");

	var categoryTreeViewData = fnCategoryTreeViewBindData(categoryData, "1");
	$('#setCategoryTreeView').jstree({
		'plugins' : [ "wholerow", "checkbox", "types" ],
		'core' : {
			"themes" : {
				"responsive" : false
			},
			'data' : eval(categoryTreeViewData)
		},
		"types" : {
			"default" : {
				"icon" : "fa fa-folder icon-state-warning icon-lg"
			},
			"file" : {
				"icon" : "fa fa-file icon-state-warning icon-lg"
			}
		}

	}).bind('click.jstree', function(event) {
		fnGetCategoryValue(categoryData);
	});

}

// 绑定分类树
function fnCategoryTreeViewBindData(TreeViewData, PID) {
	var treeData = "[";
	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var NAME  = TreeViewData[i].NAME;
		var strName = NAME.split("=")[1];
		var strID = TreeViewData[i].ID.split("=")[1];
		if (strPID == PID) {
			if (i < TreeViewData.length - 1) {
				treeData += "{";
				treeData += " \"id\" : \"" + strID + "\",";
				treeData += " \"text\" : \"" + strName + "\",";
				treeData += " \"state\" : { ";
				treeData += "		\"opened\" : true";
				treeData += "		}";
				treeData += fnCategoryTreeViewBindDataChildren(TreeViewData, strID);
				treeData += "	},";
			} else {
				treeData += "{";
				treeData += " \"id\" : \"" + strID + "\",";
				treeData += " \"text\" : \"" + strName + "\",";
				treeData += " \"state\" : { ";
				treeData += "		\"opened\" : true";
				treeData += "		}";
				treeData += fnCategoryTreeViewBindDataChildren(TreeViewData, strID);
				treeData += "	}";
			}
		}

	}
	treeData += "	]";
	return treeData;
}

// 递归绑定分类树下级
function fnCategoryTreeViewBindDataChildren(TreeViewData, PID) {

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
					treeChildrenData += fnCategoryTreeViewBindDataChildren(
							TreeViewData, strID);
					treeChildrenData += "	},";
				} else {
					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "|" + ISLAST
							+ "|" + strPID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true,";

					var arySelectCategoryTree = SelectCategoryTree.split("|");
					var strISCHECK = "";
					for (var k = 0; k < arySelectCategoryTree.length; k++) {
						if (strID == arySelectCategoryTree[k]) {
							strISCHECK = "Y";
							break;
						}
					}

					if (strISCHECK == "Y") {
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

					treeChildrenData += fnCategoryTreeViewBindDataChildren(
							TreeViewData, strID);
					treeChildrenData += "	}";
				} else {

					treeChildrenData += "{";
					treeChildrenData += " \"id\" : \"" + strID + "|" + ISLAST
							+ "|" + strPID + "\",";
					treeChildrenData += " \"text\" : \"" + strName + "\",";
					treeChildrenData += " \"state\" : { ";
					treeChildrenData += "		\"opened\" : true,";

					var arySelectCategoryTree = SelectCategoryTree.split("|");
					var strISCHECK = "";
					for (var k = 0; k < arySelectCategoryTree.length; k++) {
						if (strID == arySelectCategoryTree[k]) {
							strISCHECK = "Y";
							break;
						}
					}

					if (strISCHECK == "Y") {
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

// 第一级分类ID
var strFirstCategoryCode = "";
// 第一级分类名称
var strFirstCategoryName = "";

function fnGetCategoryValue(categoryData) {
	var strCategoryTreeView = $("#setCategoryTreeView").jstree("get_selected", true);
	var jsnTreeView = eval(strCategoryTreeView);
	var strTableLastCategory = "";
	var strTableCategory = "";
	var aryCategoryCode = [];
	var aryLastCategoryCode = [];
	var strSelectIDCategoryName = "";
	var strSelectIDLastCategoryName = "";
	
	for (var i = 0; i < jsnTreeView.length; i++) {

		var strSelectID = jsnTreeView[i].id;
		var strSelectIDName = jsnTreeView[i].text;
		var strSelectParents = jsnTreeView[i].parents;
		var strSelectParentsText = jsnTreeView[i].parents;
		var arySelectID = strSelectID.split("|");
		// 是否是末级
		if (arySelectID[1] == "Y") {
			// 得到末级分类的名称
			strSelectIDLastCategoryName += strSelectIDName;
			// 组成分类树表json
			strTableLastCategory += '{';
			strTableLastCategory += '"lastCategoryCode": "' + arySelectID[0] + '"';
			strTableLastCategory += ',"categoryCode": "' + arySelectID[2] + '"';
			strTableLastCategory += '},';
			// 添加到末级分类选择数组
			aryLastCategoryCode.push(arySelectID[0]);
			
			// 上级分类
			strSelectParents = "" + strSelectParents + "";
			var arySelectParents = strSelectParents.split(",");
			
			// 循环该末级分类的上级分类，加入已选中分类上级分类数组
			for (var j = 0; j < arySelectParents.length; j++) {
				if (arySelectParents[j] != "#") {
					var strID = arySelectParents[j].split("|")[0];
					var isCont = 0;
					for (var k = 0; k < aryCategoryCode.length; k++) {
						if (strID == aryCategoryCode[k]) {
							isCont++;
						}
					}
					if (isCont == 0) {
						if(strID.indexOf("=") > 0){
							aryCategoryCode.push(strID.split("|")[0]);
						}else{
							aryCategoryCode.push(strID);
						}
					}
				}
			}
		} else {
			// 得到选中的分类名称
			strSelectIDCategoryName += strSelectIDName;
		}
	}
	
	// 上级分类json表
	for (var m = 0; m < aryCategoryCode.length; m++) {
		strTableCategory += "";
		strTableCategory += '{';
		strTableCategory += '"categoryCode": "' + aryCategoryCode[m] + '"';
		if (m == aryCategoryCode.length - 1) {
			strTableCategory += '}';
		} else {
			strTableCategory += '},';
		}
	}
	
	strTableLastCategory = strTableLastCategory.substring(0,
			strTableLastCategory.length - 1);
	var strLastTreeText = ""
	var strLastTreeTextTemp = "";
	for (var i = 0; i < categoryData.length; i++) {
		var strPID = categoryData[i].PID;
		var strName = categoryData[i].NAME;
		var strID = categoryData[i].ID;
		
		for (var m = 0; m < aryLastCategoryCode.length; m++) {
			var NodeID = aryLastCategoryCode[m];
			if (NodeID == strID) {
				strLastTreeTextTemp += strName + ",";
			}
		}
	}
	
	if (strLastTreeTextTemp != "") {
		strLastTreeTextTemp = strLastTreeTextTemp.substr(0,
				strLastTreeTextTemp.length - 1);
		strLastTreeText = "[" + strLastTreeTextTemp + "]";
	}

	var strLastTreeVal = "";
	for (var m = 0; m < aryLastCategoryCode.length; m++) {
		strLastTreeVal += aryLastCategoryCode[m] + "|";
	}

	if (strLastTreeVal != "") {
		strLastTreeVal = strLastTreeVal.substr(0, strLastTreeVal.length - 1);
	}

	fnFirstCodeAndNameText(categoryData, "1", aryCategoryCode);

	var NodeNAMEs = fnCategoryTreeText(categoryData, "1", aryCategoryCode);
	
	parent.fnGetQueryCategoryTree(strFirstCategoryCode, strFirstCategoryName, NodeNAMEs,
			strLastTreeText, strLastTreeVal);
	
}

function fnFirstCodeAndNameText(TreeViewData, PID, aryCategoryCode) {
	strFirstCategoryCode = "";
	strFirstCategoryName = "";

	for (var i = 0; i < TreeViewData.length; i++) {
		var strPID = TreeViewData[i].PID;
		if (strPID == PID) {
			var NAME = TreeViewData[i].NAME;
			var strName = NAME.split('=')[1];
			var strBID =  NAME.split('=')[0];
			var strID= TreeViewData[i].ID.split('=')[1];
			
			for (var m = 0; m < aryCategoryCode.length; m++) {
				var NodeID = aryCategoryCode[m];
				if (NodeID == strID) {
					strFirstCategoryCode += strBID + "|";
					strFirstCategoryName += strName + ",";
				}
			}
		}
	}

	if (strFirstCategoryName != "") {
		strFirstCategoryName = strFirstCategoryName.substr(0, strFirstCategoryName.length - 1);
		strFirstCategoryCode = strFirstCategoryCode.substr(0, strFirstCategoryCode.length - 1);
		strFirstCategoryName = "[" + strFirstCategoryName + "]";
	}
}

//获取第一级别分类名称
function fnCategoryTreeText(TreeViewData, PID, aryCategoryCode) {
	var strCategoryTreeText = ""
	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var NAME = TreeViewData[i].NAME;
		var strName = NAME.split('|')[1];
		var strID = TreeViewData[i].ID.split('|')[1];
		if (strPID == PID) {

			for (var m = 0; m < aryCategoryCode.length; m++) {
				var NodeID = aryCategoryCode[m];
				if (NodeID == strID) {
					strCategoryTreeText += "["
						strCategoryTreeText += strName;
					strCategoryTreeText += fnCategoryTreeTextChildren(TreeViewData,
							strID, aryCategoryCode);
					strCategoryTreeText += "]";
				}
			}
		}

	}

	return strCategoryTreeText;
}

//递归查询下级分类名称
function fnCategoryTreeTextChildren(TreeViewData, PID, aryCategoryCode) {

	var treeChildrenData = "";

	for (var i = 0; i < TreeViewData.length; i++) {

		var strPID = TreeViewData[i].PID;
		var strName = TreeViewData[i].NAME;
		var strID = TreeViewData[i].ID;
		var ISLAST = TreeViewData[i].ISLAST;
		var ISCHECK = TreeViewData[i].ISCHECK;
		if (strPID == PID) {
			for (var m = 0; m < aryCategoryCode.length; m++) {
				var NodeID = aryCategoryCode[m];
				if (NodeID == strID) {
					treeChildrenData += "-" + strName;
					treeChildrenData += fnCategoryTreeTextChildren(TreeViewData,
							strID, aryCategoryCode);
				}
			}
		}

	}
	return treeChildrenData;
}
