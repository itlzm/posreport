var baseUrl = $("#baseUrl").val();
var funcID = "R0204";
var pageIndex = 1;
var pageSzie = 15;

var initdteTimeStart;
var initdteTimeEnd;

// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间

// 显示查询窗口
function fnBtnClickShowQuery() {

	if ($("#QueryDiv").css("display") == 'none') {
		$("#QueryDiv").css("display", "inline");
	} else {
		$("#QueryDiv").css("display", "none");
	}

}

// 查询按钮注册
function fnListeningActQuery() {
	$(document).ready(function() {
		$('#ActQuery').click(function() {
			// 开启编辑控件可用
			fnBtnClickShowQuery();
		});
	});
}

// 执行查询并关闭查询窗口
function fnBtnClickSetQuery() {
	$("#QueryDiv").css("display", "none");
	pageIndex = 1;

	var BrandCode = "";
	var StoreCode = "";
	var ChannelCode = $("#setChannelName").select2("val");
	var favourableName = $("#setfavourableName").select2("val");

	fnQueryData(BrandCode, StoreCode, ChannelCode, favourableName);
}

var listDataSum;

// 查询数据
function fnQueryData(BrandCode, StoreCode, ChannelCode, favourableName) {
	fnLoadingShowAndClose(true);
	StoreCode = $("#StoreListVal").val();
	BrandCode = $("#BranCode").val();
	if (ChannelCode == "ALL") {
		ChannelCode = "";
	}

	if (favourableName == "ALL") {
		favourableName = "";
	}
	
	navAry = Array();
	navAry[0] = "ALLClick$查询结果$";

	var strHtml = "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick(0)\">"
			+ "查询结果" + "</a>>>";
	document.getElementById('DataNav').innerHTML = "";
	$('#DataNav').append(strHtml);
	
	
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/activity/queryRepActivityRevenueBrand/',
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd,
			BrandCode : BrandCode,
			StoreCode : StoreCode,
			ChannelCode : ChannelCode,
			PageIndex : pageIndex,
			PageSize : pageSzie,
			favourableName : favourableName
		},
		success : function(data) {

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

	
	var ChannelCode = $("#setChannelName").select2("val");
	var favourableName = $("#setfavourableName").select2("val");
	
	if (ChannelCode == "ALL") {
		ChannelCode = "";
	}
	
	if(favourableName=="ALL"){
		favourableName ="";
	}
	
	
	if (areaCode != "") {

		// 查询区域数据;
		fnLoadingShowAndClose(true);
		$.ajax({
			type : "POST",
			dataType : "json",
			url : baseUrl + '/activity/queryRepActivityRevenueBrandChildren/',
			data : {
				TimeStart : dteTimeStart,
				TimeEnd : dteTimeEnd,
				BrandCode : brandCode,
				BrandName : brandName,
				StoreCode : $("#StoreListVal").val(),
				ChannelCode : ChannelCode,
				PageIndex : pageIndex,
				PageSize : pageSzie,
				AreaCode : areaCode,
				favourableName : favourableName
			},
			success : function(data) {

			
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

	var ChannelCode = $("#setChannelName").select2("val");
	var favourableName = $("#setfavourableName").select2("val");
	
	if (ChannelCode == "ALL") {
		ChannelCode = "";
	}
	
	if(favourableName=="ALL"){
		favourableName ="";
	}
	
	if (areaCode != "") {

		if (isStore == "N") {

			// 查询区域数据;
			fnLoadingShowAndClose(true);
			$.ajax({
				type : "POST",
				dataType : "json",
				url : baseUrl + '/activity/queryRepActivityRevenueBrandChildren/',
				data : {
					TimeStart : dteTimeStart,
					TimeEnd : dteTimeEnd,
					BrandCode : brandCode,
					BrandName : brandName,
					StoreCode :  $("#StoreListVal").val(),
					ChannelCode : ChannelCode,
					PageIndex : pageIndex,
					PageSize : pageSzie,
					AreaCode : areaCode,
					favourableName:favourableName
				},
				success : function(data) {
					
				
					
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
				url : baseUrl + '/activity/queryRepActivityRevenue/',
				data : {
					TimeStart : dteTimeStart,
					TimeEnd : dteTimeEnd,
					BrandCode : brandCode,
					BrandName : brandName,
					StoreCode : $("#StoreListVal").val(),
					ChannelCode : ChannelCode,
					PageIndex : pageIndex,
					PageSize : pageSzie,
					AreaCode : areaCode,
					AreaName : areaName,
					favourableName:favourableName
				},
				success : function(data) {
					
				
					
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
				} else if (obj == "门店" || obj == "销售渠道" || obj == "优惠名称" || obj == "活动名称" || obj == "品牌" || obj == "所属品牌") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 80px;padding-right: 80px;'>"
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
				
				var valueTemp=""+value+"";
				if(valueTemp.indexOf(".") > -1 && valueTemp.indexOf("%") <= -1){
					strColNameValue += "<td align='right'>" +parseFloat(value).toFixed(2)
					+ "</td>";
				}else{
					strColNameValue += "<td align='right'>" +value
					+ "</td>";
				}
				
				
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
				}else if (key == "区域") {

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
				}else{
				
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

				var valueTemp=""+value+"";
				if(valueTemp.indexOf(".") > -1){
					strColNameValue += "<td align='right'>" +parseFloat(value).toFixed(2)
					+ "</td>";
				}else{
					strColNameValue += "<td align='right'>" +value
					+ "</td>";
				}
			} else {
			strColNameValue += "<td>" + value + "</td>";
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
		var ChannelName = $("#setChannelName").select2("val");
		var favourableName = $("#setfavourableName").select2("val");
		fnQueryData(BrandName, StoreName, ChannelName, favourableName);
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
				startDate : moment().subtract('days', 7),
				endDate : moment(),
				minDate : '01/01/2012',
				maxDate : '12/31/2099',
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
				fnfavourableName();
			});

	$('#defaultrange input').val(
			moment().subtract('days', 7).format('YYYY-MM-DD') + ' ~ '
					+ moment().format('YYYY-MM-DD'));

	$("#setStoreType").val("ALL");

	fnInitForm();

	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);
	var strChannelNameTemp = '[{"id":"ALL","text":"全部渠道"}]';

	$("#setChannelName").select2({
		data : eval(strChannelNameTemp)
	});
	// 渠道
	$("#setChannelName").select2("val", "ALL");

	// 优惠活动下拉
	$("#setfavourableName").select2("val", "ALL");

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

	fnfavourableName();

}

/*
 * 优惠活动下拉
 */
function fnfavourableName() {

	// 优惠活动下拉
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/favourableName',
		dataType : "json",
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd
		},
		success : function(data) {

			var strfavourableName = JSON.stringify(eval(data.favourableName));
			strfavourableName = strfavourableName.substring(1,
					strfavourableName.length);
			var strfavourableNameTemp = '[{"id":"ALL","text":"全部优惠活动"},'
					+ strfavourableName;

			$("#setfavourableName").select2({
				data : eval(strfavourableNameTemp)
			});
			$("#setfavourableName").select2("val", "ALL");

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

	if (strBranCode != "") {
		// 下拉查询信息
		// SelectColName = brandName,channelName,saleModeName
		$.ajax({
			type : "get",
			url : baseUrl + '/common/dict/brand/change/' + strBranCode,
			dataType : "json",
			success : function(data) {

				var strChannelName = JSON.stringify(eval(data.channelName));

				var strChannelNameTemp = "";
				if (typeof (strChannelName) != "undefined") {
					strChannelName = strChannelName.substring(1,
							strChannelName.length);
					strChannelNameTemp = '[{"id":"ALL","text":"全部渠道"},'
							+ strChannelName;
				} else {
					strChannelNameTemp = '[{"id":"ALL","text":"全部渠道"}]'

				}

				$("#setChannelName").select2({
					data : eval(strChannelNameTemp)
				});
				$("#setChannelName").select2("val", "ALL");
			},
			error : function(errMsg, errTextType, errorThrown) {

			}
		});
	} else {

		var strChannelNameTemp = '[{"id":"ALL","text":"全部渠道"}]';

		$("#setChannelName").select2({
			data : eval(strChannelNameTemp)
		});

		$("#setChannelName").select2("val", "ALL");
	}

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
	var ChannelName = $("#setChannelName").select2("val");
	var favourableName = $("#setfavourableName").select2("val");
	if (ChannelName == "ALL") {
		ChannelName = "";
	}

	if (favourableName == "ALL") {
		favourableName = "";
	}

	var queryText = encodeURI(encodeURI("查询日期:+" + dteTimeStart + "~"
			+ dteTimeEnd + " \n门店类型:"
			+ $("#setStoreType").find("option:selected").text() + " \n品牌范围:"
			+ $("#setBrandName").val() + " \n门店范围:"
			+ $("#setStoreListText").val() + "\n优惠活动:"
			+ $("#setfavourableName").select2('data').text));

	var rPam = "?TimeStart=" + dteTimeStart + "&TimeEnd=" + dteTimeEnd
			+ "&BrandCode=" + BrandName + "&StoreCode=" + StoreCode
			+ "&favourableName=" + encodeURI(encodeURI(favourableName))
			+ "&ChannelCode=" + ChannelName + "&queryText=" + queryText;
	var exportIframe = document.getElementById("exportIframe");
	exportIframe.src = baseUrl
			+ '/activity/queryRepActivityRevenueExportExcel/' + rPam;

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

var repactivityrevenue = function() {

	var handleDatePickers = function() {
		if (!jQuery().daterangepicker) {
			return;
		}
		$('#defaultrange').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment().subtract('days', 7),
					endDate : moment(),
					minDate : '01/01/2012',
					maxDate : '12/31/2099',
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
					fnfavourableName();
				});

		$('#defaultrange input').val(
				moment().subtract('days', 7).format('YYYY-MM-DD') + ' ~ '
						+ moment().format('YYYY-MM-DD'));

		dteTimeStart = moment().subtract('days', 7).format('YYYY-MM-DD');
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
				fnQueryData("", "", "", "");
			}, 1000);

			$("#setBrandName").attr("disabled", "disabled");
			$("#setStoreListText").attr("disabled", "disabled");
			$("#setBrandName").val("全部品牌");
			$("#setStoreListText").val("全部门店");

			initdteTimeStart = dteTimeStart;
			initdteTimeEnd = dteTimeEnd;

			var strChannelNameTemp = '[{"id":"ALL","text":"全部渠道"}]';

			$("#setChannelName").select2({
				data : eval(strChannelNameTemp)
			});

			$("#setChannelName").select2("val", "ALL");

		}
	};

}();
