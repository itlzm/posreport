var baseUrl = $("#baseUrl").val();
var funcID = "R0301";
var pageIndex = 1;
var pageSzie = 15;

var initdteTimeStart;
var initdteTimeEnd;

// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间

var myChart1;
var myChart2;
var myChart3;

var option1;

// 查询按钮注册
function fnListeningActQuery() {
	$(document).ready(function() {
		$('#ActQuery').click(function() {
			// 开启编辑控件可用
			fnBtnClickShowQuery();
		});
	});
}

// Step:3 conifg ECharts's path, link to echarts.js from current page.
// Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
require.config({
	paths : {
		echarts : baseUrl + '/assets/global/plugins/echarts-2.2.7/js'
	}
});

// Step:4 require echarts and use it in the callback.
// Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
require([ 'echarts', 'echarts/chart/bar', 'echarts/chart/line',
		'echarts/chart/pie', 'echarts/chart/funnel' ], function(ec) {
	myChart1 = ec.init(document.getElementById('chart-1'));

	myChart2 = ec.init(document.getElementById('chart-2'), 'macarons');

});

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

	var BrandName = "";
	var StoreName = "";
	var ChannelName = "";

	fnQueryData(BrandName, StoreName, ChannelName);

}

var listDataSum;

// 查询数据
function fnQueryData(BrandCode, StoreCode, ChannelCode) {
	fnLoadingShowAndClose(true);
	StoreCode = $("#StoreListVal").val();

	BrandCode = $("#BranCode").val();

	navAry = Array();
	navAry[0] = "ALLClick$查询结果$";

	var strHtml = "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick(0)\">"
			+ "查询结果" + "</a>>>";
	document.getElementById('DataNav').innerHTML = "";
	$('#DataNav').append(strHtml);
	
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/business/queryRepTrendBrand/',
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd,
			BrandCode : BrandCode,
			StoreCode : StoreCode,
			ChannelCode : ChannelCode,
			PageIndex : pageIndex,
			PageSize : pageSzie
		},
		success : function(data) {

			// 第一张图形化
			var dataTXHOne = data.dataTXHOne;
			fnDataTXHOne(dataTXHOne);

			// 第2-1张图形化 第2-2张图形化
			var dataTXHTwo1 = data.dataTXHTwo1;
			var dataTXHTwo2 = data.dataTXHTwo2;
			fnDataTXHTwo(dataTXHTwo1, dataTXHTwo2);

			// 明细数据信息
			var listData = data.listData;
			// 分页信息;
			var pageData = data.pageData;

			// 合计行
			var listDataSumTemp = data.listDataSum;

			if (pageIndex == 1) {
				listDataSum = listDataSumTemp;
			}
			fnListTableData(listData, pageData, listDataSum);
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}



var navAry = Array();

var mapNav = new Map();
function fnNavDataClick(index) {
	var navAryL = navAry.length;
	var navAryTemp = new Array();

	for (var i = 0; i < index; i++) {
		navAryTemp[i] = navAry[i];
	}

	var strAll = navAry[index];
	var strPrmAry = strAll.split("$");
	var stype = strPrmAry[0];
	var sName = strPrmAry[1];
	var sPrm = strPrmAry[2];

	navAry = navAryTemp;

	if (stype == "BrandClick") {
		fnEdtBrandClick(sPrm);
	} else if (stype == "AreaClick") {
		fnEdtAreaClick(sPrm);
	} else if (stype == "ALLClick") {
		fnBtnClickSetQuery();
	}
}

// 第一级别品牌点击
function fnEdtBrandClick(strPrm) {

	navID = 1;
	var strPrmAry = strPrm.split("|=|");
	var brandCode = strPrmAry[0];
	var brandName = strPrmAry[1];
	var areaCode = strPrmAry[2];

	var navAryL = navAry.length;
	navAry[navAryL] = "BrandClick$" + brandName + "$" + strPrm;
	var strHtml = "";
	for (var i = 0; i < navAry.length; i++) {
		var alltemp = navAry[i];
		var showName = alltemp.split("$")[1];
		strHtml += "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick("
				+ (i) + ")\">" + showName + "</a>>>";

		document.getElementById('DataNav').innerHTML = "";
		$('#DataNav').append(strHtml);
	}

	if (areaCode != "") {

		// 查询区域数据;
		fnLoadingShowAndClose(true);
		$.ajax({
			type : "POST",
			dataType : "json",
			url : baseUrl + '/business/queryRepTrendBrandChildren/',
			data : {
				TimeStart : dteTimeStart,
				TimeEnd : dteTimeEnd,
				BrandCode : brandCode,
				BrandName : brandName,
				StoreCode : $("#StoreListVal").val(),
				ChannelName : "",
				PageIndex : pageIndex,
				PageSize : pageSzie,
				AreaCode : areaCode
			},
			success : function(data) {
				// 第一张图形化
				var dataTXHOne = data.dataTXHOne;
				fnDataTXHOne(dataTXHOne);

				// 第2-1张图形化 第2-2张图形化
				var dataTXHTwo1 = data.dataTXHTwo1;
				var dataTXHTwo2 = data.dataTXHTwo2;
				fnDataTXHTwo(dataTXHTwo1, dataTXHTwo2);
				
				// 明细数据信息
				var listData = data.listData;
				// 分页信息;
				var pageData = data.pageData;

				// 合计行
				var listDataSumTemp = data.listDataSum;

				if (pageIndex == 1) {
					listDataSum = listDataSumTemp;
				}
				fnListTableData(listData, pageData, listDataSum);
				fnLoadingShowAndClose(false);
			},
			error : function(e) {
				fnLoadingShowAndClose(false);
			}
		});
	} else {

		// 直接查询门店数据

	}
}




//第二级别区域点击
function fnEdtAreaClick(strPrm) {

	var strPrmAry = strPrm.split("|=|");

	var brandCode = strPrmAry[0];
	var brandName = strPrmAry[1];
	var areaCode = strPrmAry[2];
	var areaName = strPrmAry[3];
	var isStore = strPrmAry[4];

	var navAryL = navAry.length;
	navAry[navAryL] = "AreaClick$" + areaName + "$" + strPrm;
	var strHtml = "";
	for (var i = 0; i < navAry.length; i++) {
		var alltemp = navAry[i];
		var showName = alltemp.split("$")[1];
		strHtml += "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick("
				+ (i) + ")\">" + showName + "</a>>>";
	}

	document.getElementById('DataNav').innerHTML = "";
	$('#DataNav').append(strHtml);

	
	if (areaCode != "") {

		if (isStore == "N") {

			// 查询区域数据;
			fnLoadingShowAndClose(true);
			$.ajax({
				type : "POST",
				dataType : "json",
				url : baseUrl + '/business/queryRepTrendBrandChildren/',
				data : {
					TimeStart : dteTimeStart,
					TimeEnd : dteTimeEnd,
					BrandCode : brandCode,
					BrandName : brandName,
					StoreCode :  $("#StoreListVal").val(),
					ChannelName : "",
					PageIndex : pageIndex,
					PageSize : pageSzie,
					AreaCode : areaCode
				},
				success : function(data) {
					// 第一张图形化
					var dataTXHOne = data.dataTXHOne;
					fnDataTXHOne(dataTXHOne);

					// 第2-1张图形化 第2-2张图形化
					var dataTXHTwo1 = data.dataTXHTwo1;
					var dataTXHTwo2 = data.dataTXHTwo2;
					fnDataTXHTwo(dataTXHTwo1, dataTXHTwo2);
					
					// 明细数据信息
					var listData = data.listData;
					// 分页信息;
					var pageData = data.pageData;

					// 合计行
					var listDataSumTemp = data.listDataSum;

					if (pageIndex == 1) {
						listDataSum = listDataSumTemp;
					}
					fnListTableData(listData, pageData, listDataSum);
					fnLoadingShowAndClose(false);
				},
				error : function(e) {
					fnLoadingShowAndClose(false);
				}
			});
		} else {

			// 查询区域数据;
			fnLoadingShowAndClose(true);
			$.ajax({
				type : "POST",
				dataType : "json",
				url : baseUrl + '/business/queryRepTrend/',
				data : {
					TimeStart : dteTimeStart,
					TimeEnd : dteTimeEnd,
					BrandCode : brandCode,
					BrandName : brandName,
					StoreCode : $("#StoreListVal").val(),
					ChannelName : "",
					PageIndex : pageIndex,
					PageSize : pageSzie,
					AreaCode : areaCode,
					AreaName : areaName
				},
				success : function(data) {
				
					// 第一张图形化
					var dataTXHOne = data.dataTXHOne;
					fnDataTXHOne(dataTXHOne);

					// 第2-1张图形化 第2-2张图形化
					var dataTXHTwo1 = data.dataTXHTwo1;
					var dataTXHTwo2 = data.dataTXHTwo2;
					fnDataTXHTwo(dataTXHTwo1, dataTXHTwo2);
					
					// 明细数据信息
					var listData = data.listData;
					// 分页信息;
					var pageData = data.pageData;

					// 合计行
					var listDataSumTemp = data.listDataSum;

					if (pageIndex == 1) {
						listDataSum = listDataSumTemp;
					}
					fnListTableData(listData, pageData, listDataSum);
					fnLoadingShowAndClose(false);
				},
				error : function(e) {
					fnLoadingShowAndClose(false);
				}
			});
		}

	} else {

		// 直接查询门店数据

	}
}



// 第一个报表图形化
function fnDataTXHOne(dataTXHOne) {

	if (dataTXHOne.length == 0) {
		myChart1.clear();
		myChart1.setOption(option1);
		return;
	}

	// 获取月份
	var len = dataTXHOne.length;
	var strMonthName = "";
	var strMonthNameValue = "";
	for (var i = 0; i < len; i++) {
		if (i == 0) {
			for (obj in dataTXHOne[i]) {
				strMonthName += "'" + obj + "'" + ",";
			}
		}
		break;
	}

	if (strMonthName != "") {
		strMonthName = strMonthName.substr(0, strMonthName.length - 1);
	}

	strMonthName = "[" + strMonthName + "]";

	// 获取值
	strMonthNameValue = "";
	for (var i = 0; i < dataTXHOne.length; i++) {
		$.each(dataTXHOne[i], function(key, value) {
			strMonthNameValue += "" + value + ",";
		});
	}

	if (strMonthNameValue != "") {
		strMonthNameValue = strMonthNameValue.substr(0,
				strMonthNameValue.length - 1);
	}

	strMonthNameValue = "[" + strMonthNameValue + "]";

	var option1Data = {
		title : {
			text : '',
			subtext : ''
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '实际收入' ]
		},
		toolbox : {
			show : false,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'line', 'bar' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : eval(strMonthName)
		} ],
		yAxis : [ {
			type : 'value',
			axisLabel : {
				formatter : '{value} 元'
			}
		} ],
		series : [

		{
			name : '实际收入',
			type : 'bar',
			data : eval(strMonthNameValue),
			markPoint : {
				data : [ {
					type : 'max',
					name : '最大值'
				}, {
					type : 'min',
					name : '最小值'
				} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		} ]
	};

	myChart1.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart1.hideLoading();
	myChart1.clear();
	myChart1.setOption(option1Data);

}

// 第二张图形化报表
function fnDataTXHTwo(dataTXHTwo1, dataTXHTwo2) {
	if (dataTXHTwo1.length == 0) {
		myChart2.clear();
		myChart2.setOption(option1);
		return;
	}

	// 获取月份
	var len = dataTXHTwo1.length;
	var strMonthName = "";
	// 日单量
	var strTDLValue = "";
	for (var i = 0; i < len; i++) {
		if (i == 0) {
			for (obj in dataTXHTwo1[i]) {
				strMonthName += "'" + obj + "'" + ",";
			}
		}
		break;
	}

	if (strMonthName != "") {
		strMonthName = strMonthName.substr(0, strMonthName.length - 1);
	}

	strMonthName = "[" + strMonthName + "]";

	// 获取日单量
	strTDLValue = "";
	for (var i = 0; i < dataTXHTwo1.length; i++) {
		$.each(dataTXHTwo1[i], function(key, value) {
			strTDLValue += "" + value + ",";
		});
	}

	if (strTDLValue != "") {
		strTDLValue = strTDLValue.substr(0, strTDLValue.length - 1);
	}

	strTDLValue = "[" + strTDLValue + "]";

	// 客单价值
	var strKDJValue = "";
	for (var i = 0; i < dataTXHTwo2.length; i++) {
		$.each(dataTXHTwo2[i], function(key, value) {
			strKDJValue += "" + value + ",";
		});
	}

	if (strKDJValue != "") {
		strKDJValue = strKDJValue.substr(0, strKDJValue.length - 1);
	}

	strKDJValue = "[" + strKDJValue + "]";

	var option2data = {
		tooltip : {
			trigger : 'axis'
		},
		toolbox : {
			show : false,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'bar', 'line' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		legend : {
			data : [ '日单量', '日均客单价' ]
		},
		xAxis : [ {
			type : 'category',
			data : eval(strMonthName)
		} ],
		yAxis : [ {
			type : 'value',
			name : '日单量',
			axisLabel : {
				formatter : '{value} 单'
			}
		}, {
			type : 'value',
			name : '日均客单价',
			axisLabel : {
				formatter : '{value} 元'
			}
		} ],
		series : [

		{
			name : '日单量',
			type : 'bar',
			data : eval(strTDLValue)
		}, {
			name : '日均客单价',
			type : 'line',
			yAxisIndex : 1,
			data : eval(strKDJValue)
		} ]
	};

	myChart2.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart2.hideLoading();
	myChart2.clear();
	myChart2.setOption(option2data);

}

function fnListTableData(listData, pageData, listDataSum) {
	// 绑定明细列表信息
	var len = listDataSum.length;
	var strHtml = "";
	var strColName = "";
	var strColNameValue = "";
	for (var i = 0; i < len; i++) {
		if (i == 0) {
			strColName += "<thead><tr>";
			strColName += "<th scope='col' style=\"text-align: center;\">行号 </th>";
			for (obj in listDataSum[i]) {

				if (obj == "日期" || obj == "区域" || obj == "所属区域") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "门店" || obj == "品牌" || obj == "所属品牌") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 80px;padding-right: 80px;'>"
							+ obj + "</th>";
				} else if (obj == "省份" || obj == "城市" || obj == "地区") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 20px;padding-right: 20px;'>"
							+ obj + "</th>";
				} else {
					strColName += "<th scope='col' style=\"text-align: center;\">"
							+ obj + "</th>";
				}
			}
			strColName += "</tr></thead>";
			break;
		}
	}
	strColNameValue += "<tbody>";
	for (var i = 0; i < listData.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td>" + ((pageIndex - 1) * pageSzie + i + 1)
				+ "</td>"; // 明细行号列
		$.each(listData[i], function(key, value) {

			if (isfloat(value)) {

				strColNameValue += "<td align='right'>" + value + "</td>";
			} else {
				if (key == "品牌") {

					if (value != "") {
						var valueAry = value.split("|=|");

						strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtBrandClick('"
								+ value
								+ "')\">"
								+ valueAry[1]
								+ "</a></td>";

					} else {
						strColNameValue += "<td>" + value
								+ "</td>";
					}
				} else if (key == "区域") {

					if (value != "") {
						var valueAry = value.split("|=|");

						strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtAreaClick('"
								+ value
								+ "')\">"
								+ valueAry[3]
								+ "</a></td>";
					} else {
						strColNameValue += "<td>" + value
								+ "</td>";
					}
				} else {

					strColNameValue += "<td>" + value + "</td>";
				}
			}

		});
		strColNameValue += "</tr>";
	}

	for (var i = 0; i < listDataSum.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td></td>"; // 明细行号列
		$.each(listDataSum[i], function(key, value) {
			if (isfloat(value)) {
				strColNameValue += "<td align='right'>" + value + "</td>";
			} else {
				strColNameValue += "<td >" + value + "</td>";
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
		var BrandName = "";
		var StoreName = "";
		var ChannelName = "";
		var favourableName = "";
		fnQueryData(BrandName, StoreName, ChannelName);
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {

	dteTimeStart = initdteTimeStart;
	dteTimeEnd = initdteTimeEnd;

	$('#defaultrange').daterangepicker(
			{
				opens : (Metronic.isRTL() ? 'left' : 'right'),
				format : 'MM/DD/YYYY',
				separator : ' to ',
				startDate : moment(),
				endDate : moment(),
				minDate : '01/01/2012',
				maxDate : '12/31/2099',
				dateLimit : {
					days : 0
				},
				locale : {
					applyLabel : '确认',
					cancelLabel : '取消',
					fromLabel : '从',
					toLabel : '至',
					customRangeLabel : '自定义范围',
					daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
					monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月',
							'八月', '九月', '十月', '十一月', '十二月' ],
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
			moment().format('YYYY-MM-DD') + ' ~ '
					+ moment().format('YYYY-MM-DD'));

	$("#setStoreType").val("ALL");

	fnInitForm();

	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);

	$("#BranCode").val("");
}




var aryStoreList = "";
function fnInitForm() {

	var strStoreType = $("#setStoreType").val();

	// 获取授权的所有门店信息
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/brand/getStoreList?StoreType='
				+ strStoreType,
		dataType : "json",
		async : false,
		success : function(data) {

			var strStore = "";
			var storeName = data.storeName;
			for (var i = 0; i < storeName.length; i++) {

				var ISLAST = storeName[i].ISLAST;
				var ID = storeName[i].ID;
				if (ISLAST == "Y") {
					strStore += ID + "|";
				}
			}

			if (strStore != "") {
				strStore = strStore.substr(0, strStore.length - 1);
			}
			$("#StoreListVal").val(strStore);
			aryStoreList = strStore;

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}

function fnStoreTree() {
	var asyncSettings = {
		type : 'iframe',
		closeable : false,
		padding : false,
		cache : false,
		url : baseUrl + '/queryStoreList'
	};
	$('#setStoreList').webuiPopover('destroy').webuiPopover(
			$.extend({}, asyncSettings));
}

fnStoreTree();

function fnGetQueryStoreTree(strBranCode, strBranName, treeText,
		strStoreTreeText, treeVal) {
	$("#BranCode").val(strBranCode);
	$("#setBrandName").val(strBranName);
	$("#setStoreListText").val(strStoreTreeText);
	$("#StoreListVal").val(treeVal);
}

function fnGetSelectStoreTree() {
	return $("#StoreListVal").val();
}

// 获取选择的门店类型
function fnGetSelectStoreType() {
	return $("#setStoreType").val();
}

/*
 * 导出Excel文件
 */
function fnExportExcel() {

	var BrandName = "";
	var StoreCode = $("#StoreListVal").val();
	var ChannelName = "";
	var queryText = encodeURI(encodeURI("查询日期:+" + dteTimeStart + "~"
			+ dteTimeEnd + " \n门店类型:"
			+ $("#setStoreType").find("option:selected").text() + " \n品牌范围:"
			+ $("#setBrandName").val() + " \n门店范围:"
			+ $("#setStoreListText").val()));

	var rPam = "?TimeStart=" + dteTimeStart + "&TimeEnd=" + dteTimeEnd
			+ "&BrandName=" + BrandName + "&StoreCode=" + StoreCode
			+ "&ChannelName=" + ChannelName + "&queryText=" + queryText;
	var exportIframe = document.getElementById("exportIframe");
	exportIframe.src = baseUrl + '/business/queryRepTrendExportExcel/' + rPam;

}

/*
 * 设置门店类型值改变事件
 */
function fnSetStoreTypeChange() {
	fnInitForm();
	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);
	$("#BranCode").val("");
}

var reptrend = function() {

	var handleDatePickers = function() {
		if (!jQuery().daterangepicker) {
			return;
		}
		$('#defaultrange').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment(),
					endDate : moment(),
					minDate : '01/01/2012',
					maxDate : '12/31/2099',
					dateLimit : {
						days : 0
					},
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
				moment().format('YYYY-MM-DD') + ' ~ '
						+ moment().format('YYYY-MM-DD'));

		dteTimeStart = moment().format('YYYY-MM-DD');
		dteTimeEnd = moment().format('YYYY-MM-DD');

	}

	return {

		init : function() {
			handleDatePickers();
			fnSetStoreType();
			fnGetMenuFunction(baseUrl, funcID);
			fnListeningActQuery();
			fnInitForm();
	

			setTimeout(function() {
				fnQueryData("", "", "");
			}, 1000);

			$("#setBrandName").attr("disabled", "disabled");
			$("#setStoreListText").attr("disabled", "disabled");
			$("#setBrandName").val("全部品牌");
			$("#setStoreListText").val("全部门店");

			initdteTimeStart = dteTimeStart;
			initdteTimeEnd = dteTimeEnd;

		}
	};

}();
